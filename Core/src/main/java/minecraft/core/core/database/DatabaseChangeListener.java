package minecraft.core.core.database;

import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listener que monitora mudanças diretas no banco de dados MySQL
 * e aplica automaticamente tags visuais quando detecta mudanças na coluna rank.
 *
 * @author Luiz
 * @version 1.0
 */
public class DatabaseChangeListener {

    private static final Logger LOGGER = Logger.getLogger(DatabaseChangeListener.class.getName());
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);
    private static final long CHECK_INTERVAL = 1000; // 1 segundo

    private final Database database;
    private boolean isRunning = false;

    // Cache para armazenar o estado anterior dos jogadores (nome -> rank:tag)
    private static final java.util.Map<String, String> PREVIOUS_STATE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    
    // Cache para evitar processamento duplicado
    private static final java.util.Map<String, Long> PROCESSED_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    private static final long PROCESSED_CACHE_DURATION = 30000; // 30 segundos

    public DatabaseChangeListener(Database database) {
        this.database = database;
    }

    /**
     * Inicia o monitoramento de mudanças no banco de dados.
     */
    public void startMonitoring() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        LOGGER.info("Iniciando monitoramento de mudanças no banco de dados...");

        EXECUTOR.scheduleWithFixedDelay(this::checkForRankChanges, 0, CHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Para o monitoramento de mudanças no banco de dados.
     */
    public void stopMonitoring() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        LOGGER.info("Parando monitoramento de mudanças no banco de dados...");

        if (!EXECUTOR.isShutdown()) {
            EXECUTOR.shutdown();
        }
    }

    /**
     * Verifica mudanças na coluna rank do banco de dados.
     */
    private void checkForRankChanges() {
        try {
            if (database instanceof MySQLDatabase) {
                checkMySQLRankChanges();
            } else if (database instanceof HikariDatabase) {
                checkHikariRankChanges();
            }
        } catch (Exception e) {
            LOGGER.warning("Erro ao verificar mudanças de rank: " + e.getMessage());
        }
    }

    /**
     * Verifica mudanças de rank no MySQLDatabase.
     */
    private void checkMySQLRankChanges() {
        MySQLDatabase mysqlDb = (MySQLDatabase) database;
        
        try (Connection connection = mysqlDb.getConnection()) {
            // Buscar jogadores com ranks
            String query = "SELECT a.name, a.rank, a.tag FROM account a " +
                          "WHERE a.rank IS NOT NULL AND a.rank != ''";
            
            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    String playerName = rs.getString("name");
                    String dbRank = rs.getString("rank");
                    String dbTag = rs.getString("tag");
                    
                    // Processar apenas se houve mudança real
                    if (hasStateChanged(playerName, dbRank, dbTag)) {
                        // Verificar se já não foi processado recentemente para evitar loops
                        if (!isRecentlyProcessed(playerName, dbRank)) {
                            // Aplicar mudanças automaticamente apenas na primeira detecção
                            applyChangesAutomatically(playerName, dbRank, dbTag);
                        }
                        
                        // Atualizar o estado anterior
                        updatePreviousState(playerName, dbRank, dbTag);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.warning("Erro ao verificar mudanças de rank no MySQL: " + e.getMessage());
        }
    }

    /**
     * Verifica mudanças de rank no HikariDatabase.
     */
    private void checkHikariRankChanges() {
        HikariDatabase hikariDb = (HikariDatabase) database;
        
        try (Connection connection = hikariDb.getConnection()) {
            // Buscar jogadores com ranks
            String query = "SELECT a.name, a.rank, a.tag FROM account a " +
                          "WHERE a.rank IS NOT NULL AND a.rank != ''";
            
            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    String playerName = rs.getString("name");
                    String dbRank = rs.getString("rank");
                    String dbTag = rs.getString("tag");
                    
                    // Processar apenas se houve mudança real
                    if (hasStateChanged(playerName, dbRank, dbTag)) {
                        // Verificar se já não foi processado recentemente para evitar loops
                        if (!isRecentlyProcessed(playerName, dbRank)) {
                            // Aplicar mudanças automaticamente apenas na primeira detecção
                            applyChangesAutomatically(playerName, dbRank, dbTag);
                        }
                        
                        // Atualizar o estado anterior
                        updatePreviousState(playerName, dbRank, dbTag);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.warning("Erro ao verificar mudanças de rank no Hikari: " + e.getMessage());
        }
    }

    /**
     * Verifica se o estado do jogador mudou desde a última verificação.
     */
    private boolean hasStateChanged(String playerName, String dbRank, String dbTag) {
        String currentState = dbRank + ":" + (dbTag != null ? dbTag : "null");
        String previousState = PREVIOUS_STATE_CACHE.get(playerName);
        
        // Se não há estado anterior, é uma mudança (primeira vez)
        if (previousState == null) {
            return true; // Primeira verificação, registrar estado inicial
        }
        
        // Verificar se houve mudança real no estado
        return !currentState.equals(previousState);
    }
    
    /**
     * Atualiza o estado anterior do jogador.
     */
    private void updatePreviousState(String playerName, String dbRank, String dbTag) {
        String newState = dbRank + ":" + (dbTag != null ? dbTag : "null");
        PREVIOUS_STATE_CACHE.put(playerName, newState);
        
        // Limpar cache quando ficar muito grande
        if (PREVIOUS_STATE_CACHE.size() > 2000) {
            // Remove entradas mais antigas (simples cleanup)
            PREVIOUS_STATE_CACHE.clear();
            LOGGER.info("Cache de estado anterior foi limpo devido ao tamanho");
        }
    }
    
    /**
     * Aplica mudanças automaticamente apenas na primeira detecção.
     * Sistema híbrido: aplica para jogadores online, offline recebe na próxima conexão.
     */
    private void applyChangesAutomatically(String playerName, String dbRank, String dbTag) {
        try {
            // Marcar como processado para evitar loops
            markAsProcessed(playerName, dbRank);
            
            String previousState = PREVIOUS_STATE_CACHE.get(playerName);
            
            if (previousState == null) {
                // Primeira verificação - apenas registrar estado inicial, não aplicar
                LOGGER.info("Estado inicial registrado para " + playerName + " - Rank: " + dbRank + ", Tag: " + (dbTag != null ? dbTag : "null"));
                return;
            }
            
            // Mudança real detectada - aplicar automaticamente
            LOGGER.info("Mudança detectada para " + playerName + " - Novo estado: Rank: " + dbRank + ", Tag: " + (dbTag != null ? dbTag : "null") + " (Estado anterior: " + previousState + ")");
            
            // Verificar se o jogador está online
            Player onlinePlayer = Bukkit.getPlayerExact(playerName);
            
            if (onlinePlayer != null) {
                // Jogador online - aplicar mudanças imediatamente
                applyChangesToOnlinePlayer(onlinePlayer, dbRank, dbTag);
                LOGGER.info("Mudanças aplicadas automaticamente para " + playerName + " (online)");
            } else {
                // Jogador offline - aplicar mudanças como o /setrank faz para offline
                applyChangesToOfflinePlayer(playerName, dbRank, dbTag);
                LOGGER.info("Mudanças aplicadas para " + playerName + " (offline) - serão efetivas na próxima conexão");
            }
            
        } catch (Exception e) {
            LOGGER.warning("Erro ao aplicar mudanças automaticamente para " + playerName + ": " + e.getMessage());
        }
    }
    
    /**
     * Aplica mudanças para jogador offline seguindo o padrão do /setrank.
     * Automaticamente seleciona a tag correspondente ao rank.
     */
    private void applyChangesToOfflinePlayer(String playerName, String dbRank, String dbTag) {
        try {
            // Obter o profile do jogador offline
            Profile profile = Profile.createOrLoadProfile(playerName);
            
            // Verificar se houve mudança de rank
            String currentRank = profile.getDataContainer("account", "rank").getAsString();
            if (!dbRank.equals(currentRank)) {
                // Rank mudou - aplicar como o /setrank faz para offline
                Rank newRank = Rank.getRoleByName(dbRank);
                if (newRank != null) {
                    // IMPORTANTE: Aplicar automaticamente a tag correspondente ao rank (igual ao /setrank offline)
                    // Para jogadores offline, salvamos tanto o rank quanto a tag
                    String cleanRankName = minecraft.core.core.utils.StringUtils.stripColors(newRank.getName());
                    profile.getDataContainer("account", "tag").set(cleanRankName);
                    
                    // Atualizar o cache de tags (para jogadores offline, salvamos no profile)
                    minecraft.core.core.database.cache.TagCache.setCache(playerName, cleanRankName, playerName);
                    
                    LOGGER.info("Tag automaticamente selecionada para " + playerName + " (offline): " + cleanRankName + " (baseado no rank)");
                }
            } else {
                // Rank não mudou, mas pode ter mudança apenas na tag
                String currentTag = profile.getDataContainer("account", "tag").getAsString();
                if (!Objects.equals(dbTag, currentTag)) {
                    // Tag mudou - aplicar nova tag (apenas para mudanças específicas de tag)
                    if (dbTag != null && !dbTag.isEmpty()) {
                        Rank tagRank = Rank.getRoleByName(dbTag);
                        if (tagRank != null) {
                            String cleanTagName = minecraft.core.core.utils.StringUtils.stripColors(tagRank.getName());
                            profile.getDataContainer("account", "tag").set(cleanTagName);
                            minecraft.core.core.database.cache.TagCache.setCache(playerName, cleanTagName, playerName);
                            
                            LOGGER.info("Tag visual aplicada para " + playerName + " (offline): " + cleanTagName);
                        }
                    } else {
                        // Tag removida - aplicar tag baseada no rank (como setrank faz)
                        Rank rankForTag = Rank.getRoleByName(dbRank);
                        if (rankForTag != null) {
                            String cleanRankName = minecraft.core.core.utils.StringUtils.stripColors(rankForTag.getName());
                            profile.getDataContainer("account", "tag").set(cleanRankName);
                            minecraft.core.core.database.cache.TagCache.setCache(playerName, cleanRankName, playerName);
                            
                            LOGGER.info("Tag resetada para rank base para " + playerName + " (offline): " + cleanRankName);
                        }
                    }
                }
            }
            
            // Salvar o profile no banco de dados para persistir as mudanças
            profile.save();
            
        } catch (Exception e) {
            LOGGER.warning("Erro ao aplicar mudanças para jogador offline " + playerName + ": " + e.getMessage());
        }
    }
    
    /**
     * Aplica mudanças para jogador online.
     * Atualiza rank (permissões) e automaticamente seleciona a tag correspondente.
     */
    private void applyChangesToOnlinePlayer(Player player, String dbRank, String dbTag) {
        try {
            // Obter o profile do jogador
            Profile profile = Profile.createOrLoadProfile(player.getName());
            
            // 1. Verificar se houve mudança de rank (permissões)
            String currentRank = profile.getDataContainer("account", "rank").getAsString();
            if (!dbRank.equals(currentRank)) {
                // Rank mudou - aplicar novas permissões
                Rank newRank = Rank.getRoleByName(dbRank);
                if (newRank != null) {
                    minecraft.core.core.player.rank.RankManager.applyRank(player, newRank);
                    LOGGER.info("Permissões de rank atualizadas para " + player.getName() + ": " + dbRank);
                    
                    // IMPORTANTE: Automaticamente selecionar a tag correspondente ao rank (igual ao /setrank)
                    applyTagVisualLikeSetRank(player, newRank, profile);
                    
                    // Notificar o jogador sobre mudança de rank
                    player.sendMessage("§aSeu rank foi atualizado para " + newRank.getName() + "§a!");
                }
            } else {
                // Rank não mudou, mas pode ter mudança apenas na tag
                // Verificar se houve mudança de tag visual
                String currentTag = profile.getDataContainer("account", "tag").getAsString();
                if (!Objects.equals(dbTag, currentTag)) {
                    // Tag mudou - aplicar nova tag visual
                    if (dbTag != null && !dbTag.isEmpty()) {
                        Rank tagRank = Rank.getRoleByName(dbTag);
                        if (tagRank != null) {
                            // Aplicar apenas a tag visual (sem alterar permissões)
                            applyTagVisualOnly(player, tagRank, profile);
                        }
                    } else {
                        // Tag removida - aplicar tag baseada no rank (como setrank faz)
                        Rank rankForTag = Rank.getRoleByName(dbRank);
                        if (rankForTag != null) {
                            applyTagVisualLikeSetRank(player, rankForTag, profile);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            LOGGER.warning("Erro ao aplicar mudanças para jogador online " + player.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Aplica a tag visual como o comando /setrank faz (seleciona automaticamente a tag do rank).
     */
    private void applyTagVisualLikeSetRank(Player player, Rank rank, Profile profile) {
        try {
            // IMPORTANTE: Salvar APENAS na coluna tag (igual ao /setrank)
            String cleanTagName = minecraft.core.core.utils.StringUtils.stripColors(rank.getName());
            profile.getDataContainer("account", "tag").set(cleanTagName);
            
            // Atualizar o cache de tags (apenas para referência visual)
            minecraft.core.core.database.cache.TagCache.setCache(player.getName(), cleanTagName, player.getName());
            
            // Aplicar APENAS a tag visual usando TagUtils (sem permissões)
            minecraft.core.core.utils.TagUtils.setTag(player, rank);
            
            // Salvar o profile para persistir a tag selecionada
            profile.save();
            
            LOGGER.info("Tag automaticamente selecionada para " + player.getName() + ": " + cleanTagName + " (baseado no rank)");
            
        } catch (Exception e) {
            LOGGER.warning("Erro ao aplicar tag visual como setrank para " + player.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Aplica apenas a tag visual (quando apenas a tag mudou, não o rank).
     */
    private void applyTagVisualOnly(Player player, Rank tagRank, Profile profile) {
        try {
            // Aplicar apenas a tag visual (sem alterar permissões)
            minecraft.core.core.player.rank.RankManager.applyVisualTag(player, tagRank);
            
            // Atualizar o cache de tags
            minecraft.core.core.database.cache.TagCache.setCache(player.getName(), 
                minecraft.core.core.utils.StringUtils.stripColors(tagRank.getName()), player.getName());
            
            LOGGER.info("Tag visual aplicada para " + player.getName() + ": " + tagRank.getName());
            
        } catch (Exception e) {
            LOGGER.warning("Erro ao aplicar tag visual para " + player.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Verifica se um jogador foi processado recentemente para evitar loops.
     */
    private boolean isRecentlyProcessed(String playerName, String rankName) {
        String key = playerName + ":" + rankName;
        Long lastProcessed = PROCESSED_CACHE.get(key);
        
        if (lastProcessed == null) {
            return false;
        }
        
        long timeSinceLastProcessed = System.currentTimeMillis() - lastProcessed;
        if (timeSinceLastProcessed > PROCESSED_CACHE_DURATION) {
            // Remover entrada expirada
            PROCESSED_CACHE.remove(key);
            return false;
        }
        
        return true;
    }
    
    /**
     * Marca um jogador como processado recentemente.
     */
    private void markAsProcessed(String playerName, String rankName) {
        String key = playerName + ":" + rankName;
        PROCESSED_CACHE.put(key, System.currentTimeMillis());
        
        // Limpar cache expirado periodicamente
        if (PROCESSED_CACHE.size() > 1000) {
            long currentTime = System.currentTimeMillis();
            PROCESSED_CACHE.entrySet().removeIf(entry -> 
                currentTime - entry.getValue() > PROCESSED_CACHE_DURATION);
        }
    }

    /**
     * Verifica se o monitoramento está ativo.
     */
    public boolean isMonitoring() {
        return isRunning;
    }
}