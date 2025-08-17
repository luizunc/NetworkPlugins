package dev.slickcollections.kiwizin.bedwars.menus.game;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.bedwars.hook.container.FavoritesContainer;
import dev.slickcollections.kiwizin.bedwars.game.shop.Shop;
import dev.slickcollections.kiwizin.bedwars.game.shop.ShopItem;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuItemShopSelect extends PlayerMenu {
  
  private static final List<Integer> SLOTS = Arrays.asList(
      19, 20, 21, 22, 23, 24,
      25, 28, 29, 30, 31, 32,
      33, 34, 37, 38, 39,
      40, 41, 42, 43);
  private ShopItem item;
  
  public MenuItemShopSelect(Profile ap, ShopItem item, ItemStack stack) {
    super(ap.getPlayer(), "Selecione um slot...", 6);
    this.item = item;
    
    this.setItem(4, stack);
    
    for (int i = 0; i < 9; i++) {
      this.setItem(9 + i, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:7 : 1 : nome>&8↑ &7Item : desc>&8↓ &7Slots"));
    }
    
    FavoritesContainer preferences = ap.getAbstractContainer("kCoreBedWars", "favorites", FavoritesContainer.class);
    for (int slot : SLOTS) {
      if (preferences.hasQuickBuy(slot)) {
        String fav = preferences.getQuickBuy(slot);
        ShopItem favItem = Objects.requireNonNull(Shop.getCategoryById(
            Integer.parseInt(fav.split(":")[0]))).getItem(fav.split(":")[1]);
        ItemStack icon = BukkitUtils.deserializeItemStack(favItem.getIcon().replace("{color}", "§a").replace("{price}", String.valueOf(item.getPrice().getAmount())).replace("{tier}", "I"));
        ItemMeta meta = icon.getItemMeta();
        List<String> lore = meta.getLore();
        lore.clear();
        lore.add("§7Este slot já está sendo");
        lore.add("§7utilizado por algum item!");
        lore.add("");
        lore.add("§7Tente clicar apenas no");
        lore.add("§7slots em §2verde§7.");
        meta.setLore(lore);
        icon.setItemMeta(meta);
        this.setItem(slot, icon);
      } else {
        this.setItem(slot, BukkitUtils.deserializeItemStack(
            "STAINED_GLASS_PANE:13 : 1 : nome>&aUtilizar este slot : desc>§7Ao clicar neste vidro você\n§7irá colocar o item neste slot.\n \n§eClique para utilizar este slot!"));
      }
    }
    
    this.open();
    this.register(Core.getInstance());
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
        ItemStack item = evt.getCurrentItem();
        Profile profile = Profile.getProfile(this.player.getName());
        
        if (profile == null) {
          player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(evt.getInventory())
            && item != null && item.getType() != Material.AIR) {
          if (SLOTS.contains(evt.getSlot())) {
            if (item.getDurability() == 13) {
              EnumSound.ORB_PICKUP.play(this.player, 1.0f, 1.0f);
              FavoritesContainer preferences = profile.getAbstractContainer("kCoreBedWars", "favorites",
                  FavoritesContainer.class);
              
              preferences.setQuickBuy(evt.getSlot(), Shop.getCategoryId(this.item.getCategory()) +
                  ":" + this.item.getName());
              new MenuItemShop(profile, null);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    this.item = null;
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
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}