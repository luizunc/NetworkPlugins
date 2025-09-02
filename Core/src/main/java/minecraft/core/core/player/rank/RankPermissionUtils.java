package minecraft.core.core.player.rank;

import minecraft.core.Manager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Utilitário para verificação de permissões hierárquicas de ranks.
 * Permite que ranks superiores herdem automaticamente as permissões dos ranks inferiores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class RankPermissionUtils {

    /**
     * Verifica se um jogador tem um rank específico ou superior.
     * 
     * @param player Jogador a ser verificado
     * @param rankPermission Permissão do rank (ex: "rank.iron")
     * @return true se o jogador tem o rank ou superior
     */
    public static boolean hasRankOrHigher(Player player, String rankPermission) {
        if (player == null || rankPermission == null) {
            return false;
        }
        
        // Se a permissão estiver vazia, significa que é um rank padrão (como Membro)
        // que deve estar sempre disponível para todos
        if (rankPermission.trim().isEmpty()) {
            return true;
        }
        
        return Rank.hasRankOrHigher(player, rankPermission);
    }
    
    /**
     * Verifica se um sender tem um rank específico ou superior.
     * 
     * @param sender Sender do comando
     * @param rankPermission Permissão do rank (ex: "rank.iron")
     * @return true se o sender tem o rank ou superior
     */
    public static boolean hasRankOrHigher(CommandSender sender, String rankPermission) {
        if (sender == null || rankPermission == null) {
            return false;
        }
        
        // Se a permissão estiver vazia, significa que é um rank padrão (como Membro)
        // que deve estar sempre disponível para todos
        if (rankPermission.trim().isEmpty()) {
            return true;
        }
        
        if (sender instanceof Player) {
            return hasRankOrHigher((Player) sender, rankPermission);
        }
        
        // Para console, sempre retorna true
        return true;
    }
    
    /**
     * Verifica se um jogador tem rank Iron ou superior.
     * 
     * @param player Jogador a ser verificado
     * @return true se o jogador tem rank Iron ou superior
     */
    public static boolean hasIronOrHigher(Player player) {
        return hasRankOrHigher(player, "rank.iron");
    }
    
    /**
     * Verifica se um sender tem rank Iron ou superior.
     * 
     * @param sender Sender do comando
     * @return true se o sender tem rank Iron ou superior
     */
    public static boolean hasIronOrHigher(CommandSender sender) {
        return hasRankOrHigher(sender, "rank.iron");
    }
    
    /**
     * Verifica se um jogador tem rank Gold ou superior.
     * 
     * @param player Jogador a ser verificado
     * @return true se o jogador tem rank Gold ou superior
     */
    public static boolean hasGoldOrHigher(Player player) {
        return hasRankOrHigher(player, "rank.gold");
    }
    
    /**
     * Verifica se um sender tem rank Gold ou superior.
     * 
     * @param sender Sender do comando
     * @return true se o sender tem rank Gold ou superior
     */
    public static boolean hasGoldOrHigher(CommandSender sender) {
        return hasRankOrHigher(sender, "rank.gold");
    }
    
    /**
     * Verifica se um jogador tem rank Emerald ou superior.
     * 
     * @param player Jogador a ser verificado
     * @return true se o jogador tem rank Emerald ou superior
     */
    public static boolean hasEmeraldOrHigher(Player player) {
        return hasRankOrHigher(player, "rank.emerald");
    }
    
    /**
     * Verifica se um sender tem rank Emerald ou superior.
     * 
     * @param sender Sender do comando
     * @return true se o sender tem rank Emerald ou superior
     */
    public static boolean hasEmeraldOrHigher(CommandSender sender) {
        return hasRankOrHigher(sender, "rank.emerald");
    }
    
    /**
     * Verifica se um jogador tem rank Admin ou superior.
     * 
     * @param player Jogador a ser verificado
     * @return true se o jogador tem rank Admin ou superior
     */
    public static boolean hasAdminOrHigher(Player player) {
        return hasRankOrHigher(player, "rank.admin");
    }
    
    /**
     * Verifica se um sender tem rank Admin ou superior.
     * 
     * @param sender Sender do comando
     * @return true se o sender tem rank Admin ou superior
     */
    public static boolean hasAdminOrHigher(CommandSender sender) {
        return hasRankOrHigher(sender, "rank.admin");
    }
    
    /**
     * Verifica se um jogador tem rank Mod ou superior.
     * 
     * @param player Jogador a ser verificado
     * @return true se o jogador tem rank Mod ou superior
     */
    public static boolean hasModOrHigher(Player player) {
        return hasRankOrHigher(player, "rank.mod");
    }
    
    /**
     * Verifica se um sender tem rank Mod ou superior.
     * 
     * @param sender Sender do comando
     * @return true se o sender tem rank Mod ou superior
     */
    public static boolean hasModOrHigher(CommandSender sender) {
        return hasRankOrHigher(sender, "rank.mod");
    }
} 