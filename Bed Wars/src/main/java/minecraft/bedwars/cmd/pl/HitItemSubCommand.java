package minecraft.bedwars.cmd.pl;

import minecraft.bedwars.cmd.SubCommand;
import minecraft.bedwars.listeners.player.ChestHitListener;
import minecraft.core.core.player.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HitItemSubCommand extends SubCommand {
    
    public HitItemSubCommand() {
        super("hititem", "hititem", "Ver qual item você usou para hitar em chests.", false);
    }
    
    @Override
    public void perform(Player player, String[] args) {
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            player.sendMessage("§cErro ao carregar perfil.");
            return;
        }
        
        ItemStack hitItem = ChestHitListener.getPlayerHitItem(player.getUniqueId());
        if (hitItem != null) {
            if (hitItem.getType() == Material.AIR) {
            } else {
                if (hitItem.hasItemMeta() && hitItem.getItemMeta().hasDisplayName()) {
                    player.sendMessage("§aNome do item: §e" + hitItem.getItemMeta().getDisplayName());
                }
            }
        } else {
            player.sendMessage("§cVocê ainda não hitou em nenhum chest ou enderchest.");
        }
    }
} 