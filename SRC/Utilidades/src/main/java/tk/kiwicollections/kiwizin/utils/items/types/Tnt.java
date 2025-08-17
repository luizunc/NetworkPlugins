package tk.kiwicollections.kiwizin.utils.items.types;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import tk.kiwicollections.kiwizin.utils.Main;
import tk.kiwicollections.kiwizin.utils.items.object.AbstractItem;
import tk.kiwicollections.kiwizin.utils.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

public class Tnt extends AbstractItem {

    public Tnt() {
        super("Tnt");

        this.register(Main.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String display = item.getItemMeta().getDisplayName();
            if (display.equalsIgnoreCase("§aTnt")) {
                evt.setCancelled(true);
                EnumSound.FIRE_IGNITE.play(player, 0.5F, 1.0F);
                player.getWorld().spawn(player.getEyeLocation().add(0, 1, 0), TNTPrimed.class).setVelocity(player.getEyeLocation().getDirection().multiply(0.5).setY(0.5));
                BukkitUtils.removeItem(player.getInventory(), item.getType(), 1);
            }
        }
    }
}
