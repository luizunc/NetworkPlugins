package minecraft.core.core.player.rank;

import minecraft.core.Manager;
import minecraft.core.bukkit.Core;
import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerenciador de ranks que controla permissões e aplicação de ranks.
 * 
 * @author Luiz
 * @version 1.0
 */
public class RankManager {

    private static final Map<UUID, PermissionAttachment> PERMISSION_ATTACHMENTS = new HashMap<>();

    /**
     * Aplica um rank a um jogador, incluindo permissões.
     * IMPORTANTE: Este método aplica apenas as permissões do rank especificado.
     * As permissões sempre vêm da coluna rank do MySQL.
     * 
     * @param player Jogador que receberá o rank
     * @param rank Rank a ser aplicado
     */
    public static void applyRank(Player player, Rank rank) {
        if (player == null || rank == null) {
            return;
        }

        // Remover permissões antigas
        removeOldPermissions(player);

        // Aplicar novas permissões apenas se não for rank padrão
        if (!rank.isDefault()) {
            PermissionAttachment attachment = player.addAttachment(Core.getInstance());
            attachment.setPermission(rank.getPermission(), true);
            PERMISSION_ATTACHMENTS.put(player.getUniqueId(), attachment);
        }

        // IMPORTANTE: NÃO atualizar cache de tags aqui
        // O cache de tags é gerenciado separadamente para tags visuais
    }

    /**
     * Remove o rank de um jogador.
     * 
     * @param player Jogador que terá o rank removido
     */
    public static void removeRank(Player player) {
        if (player == null) {
            return;
        }

        removeOldPermissions(player);
        
        // Definir como membro (rank padrão)
        Rank defaultRank = Rank.getLastRole();
        if (defaultRank != null) {
            TagCache.setCache(player.getName(), defaultRank.getName(), player.getName());
        }
    }

    /**
     * Remove permissões antigas de um jogador.
     * 
     * @param player Jogador
     */
    private static void removeOldPermissions(Player player) {
        PermissionAttachment attachment = PERMISSION_ATTACHMENTS.remove(player.getUniqueId());
        if (attachment != null) {
            attachment.remove();
        }
    }

    /**
     * Limpa todas as permissões quando o jogador sai.
     * 
     * @param player Jogador que saiu
     */
    public static void onPlayerQuit(Player player) {
        if (player != null) {
            removeOldPermissions(player);
        }
    }

    /**
     * Verifica se um jogador tem um rank específico.
     * 
     * @param player Jogador
     * @param rankName Nome do rank
     * @return true se o jogador tem o rank
     */
    public static boolean hasRank(Player player, String rankName) {
        if (player == null || rankName == null) {
            return false;
        }

        Rank rank = Rank.getRoleByName(rankName);
        if (rank == null) {
            return false;
        }

        return rank.has(player);
    }

    /**
     * Obtém o rank atual de um jogador.
     * 
     * @param player Jogador
     * @return Rank atual do jogador
     */
    public static Rank getCurrentRank(Player player) {
        if (player == null) {
            return null;
        }

        return Rank.getRank(player);
    }

    /**
     * Aplica apenas as permissões do rank mais alto do jogador.
     * Este método é usado para garantir que as permissões sempre sejam do rank mais alto.
     * IMPORTANTE: NUNCA altera a coluna rank do banco de dados.
     * 
     * @param player Jogador
     */
    public static void applyHighestRankPermissions(Player player) {
        if (player == null) {
            return;
        }

        // Remover permissões antigas
        removeOldPermissions(player);

        // Obter o rank mais alto baseado nas permissões
        Rank highestRank = Rank.getRank(player, true);
        if (highestRank != null && !highestRank.isDefault()) {
            PermissionAttachment attachment = player.addAttachment(Core.getInstance());
            attachment.setPermission(highestRank.getPermission(), true);
            PERMISSION_ATTACHMENTS.put(player.getUniqueId(), attachment);
            
            // IMPORTANTE: NÃO atualizar a coluna rank do banco de dados
            // A coluna rank deve ser preservada e nunca alterada automaticamente
        }
    }

    /**
     * Garante que as permissões sejam sempre baseadas no rank mais alto do jogador.
     * IMPORTANTE: Este método NUNCA é afetado por tags visuais.
     * As permissões sempre vêm do rank com mais privilégios.
     * 
     * @param player Jogador
     */
    /*
    public static void ensureHighestRankPermissions(Player player) {
        if (player == null) {
            return;
        }

        // Remover permissões antigas
        removeOldPermissions(player);

        // IMPORTANTE: Usar getRealRank para obter o rank baseado APENAS nas permissões
        // NUNCA considerar tags visuais para determinar permissões
        Rank realRank = Rank.getRealRank(player);
        if (realRank != null && !realRank.isDefault()) {
            PermissionAttachment attachment = player.addAttachment(Core.getInstance());
            attachment.setPermission(realRank.getPermission(), true);
            PERMISSION_ATTACHMENTS.put(player.getUniqueId(), attachment);
        }
    }
    */

    /**
     * Aplica APENAS a tag visual de um rank, SEM ALTERAR PERMISSÕES OU RANK.
     * IMPORTANTE: Este método é usado APENAS para aparência visual.
     * As permissões sempre vêm da coluna rank do MySQL e NUNCA são alteradas por tags.
     * 
     * @param player Jogador
     * @param rank Rank para aplicar a tag visual
     */
    public static void applyVisualTag(Player player, Rank rank) {
        if (player == null || rank == null) {
            return;
        }

        // Aplicar APENAS a tag visual usando TagUtils (SEM PERMISSÕES)
        try {
            minecraft.core.core.utils.TagUtils.setTag(player, rank);
            
            // IMPORTANTE: NÃO atualizar cache de permissões
            // Apenas cache visual para referência
            TagCache.setCache(player.getName(), rank.getName(), player.getName());
            
        } catch (Exception e) {
            // Log do erro, mas não interrompe o funcionamento
            e.printStackTrace();
        }
    }
} 