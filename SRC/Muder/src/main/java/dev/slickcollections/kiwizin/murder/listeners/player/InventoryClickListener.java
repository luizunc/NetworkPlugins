package dev.slickcollections.kiwizin.murder.listeners.player;

import dev.slickcollections.kiwizin.murder.cmd.mm.BuildCommand;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.getProfile(player.getName());

      if (profile != null) {
        evt.setCancelled(!BuildCommand.hasBuilder(player));
      }
    }
  }
}
