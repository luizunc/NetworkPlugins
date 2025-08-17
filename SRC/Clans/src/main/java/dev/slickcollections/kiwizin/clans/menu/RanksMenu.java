package dev.slickcollections.kiwizin.clans.menu;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.clans.clan.Clan;
import dev.slickcollections.kiwizin.clans.database.Database;
import dev.slickcollections.kiwizin.libraries.menu.PagedPlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RanksMenu extends PagedPlayerMenu {
  
  public RanksMenu(Profile profile) {
    super(profile.getPlayer(), "Ranking - Clan Coins", 6);
    this.previousPage = 18;
    this.nextPage = 26;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("BOOK : 1 : nome>&aInformações : desc>&7O rank é atualizado de tempos em\n&7tempos. As informações vistas aqui não\n&7são em tempo real."), 49);
    
    List<ItemStack> items = new ArrayList<>();
    List<String[]> list = Database.getInstance().getLeaderBoard("kclans", "coins");
    int index = 1;
    for (String[] strings : list) {
      if (this.parse(strings[1]) < 1) {
        continue;
      }
      Clan clan = Clan.getByTag(strings[0]);
      ItemStack itemStack = BukkitUtils.deserializeItemStack("PAPER : 1 : esconder>tudo : nome>&f&l" + index + "º, &" + (clan.tagPermissionPlus ? "6" : "7")
          + "[" + strings[0] + "] " + clan.getName() + " : desc>&fClan Coins: &7" + strings[1]);
      items.add(itemStack);
      index++;
    }
    
    this.setItems(items);
    items.clear();
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            } else {
              EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
  
  public int parse(String formattedString) {
    return Integer.parseInt(formattedString.replace(".", "").replace(",", ""));
  }
  
}
