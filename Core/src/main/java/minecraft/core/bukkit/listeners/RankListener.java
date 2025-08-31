package minecraft.core.bukkit.listeners;

import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.rank.RankManager;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener para gerenciar ranks quando jogadores entram e saem do servidor.
 * IMPORTANTE: As permissões sempre vêm da coluna rank do MySQL, as tags são apenas visuais.
 * 
 * @author Luiz
 * @version 1.0
 */
public class RankListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            // Obter o profile do jogador
            Profile profile = Profile.createOrLoadProfile(event.getPlayer().getName());
            
            // SEMPRE aplicar as permissões baseadas na coluna rank do MySQL
            applyRankPermissionsFromDatabase(event.getPlayer(), profile);
            
            // Depois aplicar a tag visual selecionada (se houver) - SEM ALTERAR PERMISSÕES
            applyVisualTagFromDatabase(event.getPlayer(), profile);
            
        } catch (Exception e) {
            // Log do erro, mas não interrompe o login
            e.printStackTrace();
        }
    }

    /**
     * Aplica as permissões baseadas na coluna rank do MySQL.
     * IMPORTANTE: Tags visuais NUNCA afetam permissões.
     * Este método preserva o rank salvo no banco e aplica as permissões corretas.
     */
    private void applyRankPermissionsFromDatabase(Player player, Profile profile) {
        try {
            // Obter o rank salvo na coluna rank do MySQL
            String savedRankName = profile.getDataContainer("account", "rank").getAsString();
            
            if (savedRankName != null && !savedRankName.isEmpty()) {
                // Buscar o rank pelo nome salvo
                Rank savedRank = Rank.getRoleByName(savedRankName);
                if (savedRank != null && !savedRank.isDefault()) {
                    // Aplicar as permissões do rank salvo
                    RankManager.applyRank(player, savedRank);
                }
                // IMPORTANTE: NÃO aplicar rank padrão se o jogador já tem rank salvo
                // O rank padrão é aplicado apenas para novos jogadores
            }
            // Se não tem rank salvo, não fazer nada - o jogador manterá suas permissões atuais
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aplica APENAS a tag visual baseada na coluna tag do MySQL.
     * IMPORTANTE: Este método NUNCA altera permissões ou rank.
     * É usado apenas para aparência visual (chat, scoreboard, etc).
     * As permissões são definidas APENAS pelo método applyRankPermissionsFromDatabase.
     */
    private void applyVisualTagFromDatabase(Player player, Profile profile) {
        try {
            // Obter a tag selecionada APENAS da coluna tag do MySQL
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            
            if (selectedTag != null && !selectedTag.isEmpty()) {
                // Verificar se a tag selecionada é válida
                Rank rank = Rank.getRoleByName(selectedTag);
                if (rank != null) {
                    // IMPORTANTE: Aplicar APENAS a tag visual (SEM ALTERAR PERMISSÕES OU RANK)
                    // Este método NUNCA deve interferir nas permissões
                    RankManager.applyVisualTag(player, rank);
                    
                    // Atualizar o cache de tags (apenas para referência visual)
                    minecraft.core.core.database.cache.TagCache.setCache(player.getName(), selectedTag, player.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Limpar permissões quando o jogador sai
        RankManager.onPlayerQuit(event.getPlayer());
    }
} 