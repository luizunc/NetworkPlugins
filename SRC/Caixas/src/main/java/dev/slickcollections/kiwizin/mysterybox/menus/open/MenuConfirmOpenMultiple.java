package dev.slickcollections.kiwizin.mysterybox.menus.open;


import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.lobby.BoxNPC;
import dev.slickcollections.kiwizin.mysterybox.menus.MenuBoxes;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuConfirmOpenMultiple extends PlayerMenu {
  
  private BoxContent box;
  private BoxNPC npc;
  public MenuConfirmOpenMultiple(Player player, BoxContent box, BoxNPC npc) {
    super(player, "Abrir Múltiplas Cápsulas", 3);
    this.box = box;
    this.npc = npc;
    
    this.setItem(12, BukkitUtils.deserializeItemStack(
        "STAINED_GLASS_PANE:5 : 1 : nome>&aConfirmar : desc>&7Confirmar e abrir\n&7as cápsulas mágicas!"));
    this.setItem(14, BukkitUtils.deserializeItemStack(
        "STAINED_GLASS_PANE:14 : 1 : nome>&cCancelar : desc>&7Cancelar e não abrir\n&7as cápsulas mágicas!"));
    
    this.open();
    this.register(Core.getInstance());
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
        ItemStack item = evt.getCurrentItem();
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
          player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(evt.getInventory()) && item != null && item.getType() != Material.AIR) {
          if (evt.getSlot() == 12) {
            npc.openMultiples(profile);
            player.closeInventory();
          } else if (evt.getSlot() == 14) {
            new MenuBoxes(profile, npc);
          }
        }
      }
    }
  }
  
  public void cancel() {
    box = null;
    npc = null;
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
