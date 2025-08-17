package dev.slickcollections.kiwizin.clans.menu;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.clans.clan.Clan;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ShopMenu extends PlayerMenu implements Listener {
  
  public ShopMenu(Player player, Clan clan) {
    super(player, "Loja do Clan", 5);
    
    int index = 0;
    int[] prices = new int[]{2000, 4000, 6000, 8000, 10000};
    for (int slots : new int[]{10, 15, 20, 25, 30}) {
      int price = prices[index];
      String name = index < 3 ? StringUtils.repeat("I", index + 1) : index == 4 ? "V" : "IV";
      
      if (clan.getSlots() / 5 != (index + 1)) {
        if (clan.getSlots() / 5 > (index + 1)) {
          this.setItem(index + 11,
              BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&eLimite de Membros "
                  + name + " : desc>&7Amplia a capacidade do clan para &a" + slots
                  + " &7membros.\n \n&eVocê já possui esta melhoria!"));
        } else {
          this.setItem(index + 11,
              BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&cLimite de Membros "
                  + name + " : desc>&7Amplia a capacidade do clan para &a" + slots
                  + " &7membros.\n \n&cNecessita do nível anterior!"));
        }
      } else {
        if (clan.getCoins() < price) {
          this.setItem(index + 11,
              BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&cLimite de Membros "
                  + name + " : desc>&7Amplia a capacidade do clan para &a" + slots
                  + " &7membros.\n \n&fPreço: &b" + StringUtils.formatNumber(price)
                  + " Clan Coins\n \n&cVocê não tem possui suficiente!"));
        } else {
          this.setItem(index + 11,
              BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&eLimite de Membros "
                  + name + " : desc>&7Amplia a capacidade do clan para &a" + slots
                  + " &7membros.\n \n&fPreço: &b" + StringUtils.formatNumber(price)
                  + " Clan Coins\n \n&eClique para comprar!"));
        }
      }
      index++;
    }
    
    String color = clan.tagPermissionPlus ? "e" : clan.tagPermission ? clan.getCoins() < 1500 ? "c" : "e" : clan.getCoins() < 1000 ? "c" : "e";
    this.setItem(31, BukkitUtils.deserializeItemStack("SIGN : 1 : nome>&" + color + "Sigla no Chat : desc>&7Quando você falar no chat\n&7aparecerá sua sigla.\n \n&fPreço: &b" + StringUtils.formatNumber(clan.tagPermission ? 1500 : 1000) + " Clan Coins" +
        "\n \n" + (clan.tagPermission ? clan.tagPermissionPlus ? "§eVocê já possui esta melhoria" :
        clan.getCoins() < 1500 ? "§cVocê não possui saldo suficiente!" : "§eClique para comprar!" : clan.getCoins() < 1000 ? "§cVocê não tem saldo suficiente" : "§eClique para comprar")));
    
    this.open();
    this.register(Core.getInstance());
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
        Clan clan = Clan.getClan(player);
        if (clan == null) {
          player.closeInventory();
        } else {
          ItemStack item = evt.getCurrentItem();
          
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(evt.getInventory()) && item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 31) {
              if (clan.tagPermission && !clan.tagPermissionPlus) {
                if (clan.getCoins() >= 1500) {
                  if (!clan.getRole(player.getName()).equals("Líder")) {
                    EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                    player.closeInventory();
                    player.sendMessage("§cApenas o líder pode comprar upgrades.");
                    return;
                  }
                  
                  clan.addTagColor(true);
                  clan.removeCoins(1500);
                  player.sendMessage("§aVocê comprou §6{upgrade}".replace("{upgrade}",
                      StringUtils.stripColors(item.getItemMeta().getDisplayName())));
                  EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
                  new ShopMenu(player, clan);
                } else {
                  EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 1.0F);
                }
              } else if (!clan.tagPermission) {
                if (clan.getCoins() >= 1000) {
                  if (!clan.getRole(player.getName()).equals("Líder")) {
                    EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                    player.closeInventory();
                    player.sendMessage("§cApenas o líder pode comprar upgrades.");
                    return;
                  }
                  
                  clan.addTagColor(false);
                  clan.removeCoins(1000);
                  EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
                  player.sendMessage("§aVocê comprou §6{upgrade}".replace("{upgrade}",
                      StringUtils.stripColors(item.getItemMeta().getDisplayName())));
                  new ShopMenu(player, clan);
                } else {
                  EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 1.0F);
                }
              } else {
                EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
              }
            } else if (item.getType() == Material.SKULL_ITEM && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().contains("Limite de Membros")) {
              if (evt.getSlot() - 11 > this.getInventory().getSize()) {
                return;
              }
              int index = evt.getSlot() - 11;
              int[] prices = new int[]{2000, 4000, 6000, 8000, 10000};
              
              if (clan.getSlots() / 5 != (index + 1)) {
                EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
                return;
              }
              
              if (prices[index] <= clan.getCoins()) {
                if (clan.getRole(player.getName()) != null && !clan.getRole(player.getName()).equals("Líder")) {
                  EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                  player.closeInventory();
                  player.sendMessage("§cApenas o líder pode comprar upgrades.");
                  return;
                }
                
                clan.addSlots();
                clan.removeCoins(prices[index]);
                player.sendMessage("§aVocê comprou §6{upgrade}".replace("{upgrade}",
                    StringUtils.stripColors(item.getItemMeta().getDisplayName())));
                EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
                new ShopMenu(player, clan);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 1.0F);
              }
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
    if (evt.getPlayer().equals(player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getInventory())) {
      this.cancel();
    }
  }
}
