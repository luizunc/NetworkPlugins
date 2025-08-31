package minecraft.core.core.player.rank;

import minecraft.core.Manager;
import minecraft.core.bukkit.Core;
import minecraft.core.core.database.cache.TagCache;
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

        // Aplicar novas permissões
        if (!rank.isDefault()) {
            PermissionAttachment attachment = player.addAttachment(Core.getInstance());
            attachment.setPermission(rank.getPermission(), true);
            PERMISSION_ATTACHMENTS.put(player.getUniqueId(), attachment);
        }

        // Atualizar cache de tags
        TagCache.setCache(player.getName(), rank.getName(), player.getName());
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
} 