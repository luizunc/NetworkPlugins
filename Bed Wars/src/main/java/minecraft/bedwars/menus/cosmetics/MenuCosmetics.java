package minecraft.bedwars.menus.cosmetics;

import minecraft.core.bukkit.Core;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.object.preview.CagePreview;
import minecraft.bedwars.cosmetics.object.preview.KillEffectPreview;
import minecraft.bedwars.cosmetics.types.Cage;
import minecraft.bedwars.cosmetics.types.DeathCry;
import minecraft.bedwars.cosmetics.types.DeathMessage;
import minecraft.bedwars.cosmetics.types.KillEffect;
import minecraft.bedwars.menus.MenuShop.MenuCosmeticsPage;

import minecraft.core.core.cash.CashManager;
import minecraft.core.core.libraries.menu.PagedPlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.role.Rank;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MenuCosmetics<T extends Cosmetic> extends PagedPlayerMenu {
  
  private Class<T> cosmeticClass;
  private Map<ItemStack, T> cosmetics = new HashMap<>();
  public MenuCosmetics(Profile profile, String name, Class<T> cosmeticClass) {
    super(profile.getPlayer(), "Bed Wars - " + name, (Cosmetic.listByType(cosmeticClass).size() / 7) + 4);
    this.cosmeticClass = cosmeticClass;
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    
    String desc = "§7Para Cosméticos da Partida.";
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar : desc>" + desc), (this.rows * 9) - 5);
    
    List<ItemStack> items = new ArrayList<>();
    List<T> cosmetics = Cosmetic.listByType(cosmeticClass);
    for (T cosmetic : cosmetics) {
      if (cosmetic.getCash() == 0 && cosmetic.getCoins() == 0 && cosmetic.getId() == 0) {
        if (!cosmetic.has(profile)) {
          cosmetic.give(profile);
        }
      }
      ItemStack icon = cosmetic.getIcon(profile);
      items.add(icon);
      this.cosmetics.put(icon, cosmetic);
    }
    
    this.setItems(items);
    cosmetics.clear();
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
            } else if (evt.getSlot() == (this.rows * 9) - 5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmeticsPage(profile);
            } else {
              T cosmetic = this.cosmetics.get(item);
              if (cosmetic != null) {
                if (evt.isRightClick()) {
                  if (cosmetic.getType() == CosmeticType.DEATH_CRY) {
                    ((DeathCry) cosmetic).getSound().play(this.player, ((DeathCry) cosmetic).getVolume(), ((DeathCry) cosmetic).getSpeed());
                    return;
                  } else if (cosmetic.getType() == CosmeticType.KILL_EFFECT) {
                    if (!AbstractPreview.canDoKillEffect()) {
                      if (player.hasPermission("bedwars.cmd.bedwars")) {
                        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                        player.sendMessage("§cSete as localizações da previsualização utilizando /pl preview killeffect");
                      }
                      return;
                    }
                    
                    new KillEffectPreview(profile, (KillEffect) cosmetic);
                    player.closeInventory();
                    return;
                  } else if (cosmetic.getType() == CosmeticType.DEATH_MESSAGE) {
                    StringBuilder message = new StringBuilder("\n §eMensagens que poderão ser exibidas ao abater seu oponente: \n");
                    for (String msg : ((DeathMessage) cosmetic).getMessages()) {
                      message.append("\n §8▪ ").append(StringUtils.formatColors(msg.replace("{name}", "§7Jogador").replace("{killer}", Rank.getColored(player.getName()))));
                    }
                    message.append("\n \n");
                    player.sendMessage(message.toString());
                    
                    return;
                  } else if (cosmetic.getType() == CosmeticType.CAGE) {
                    if (!AbstractPreview.canDoCage()) {
                      if (player.hasPermission("bedwars.cmd.bedwars")) {
                        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                        player.sendMessage("§cSete as localizações da previsualização utilizando /pl preview cage");
                      }
                      return;
                    }
                    
                    new CagePreview(profile, (Cage) cosmetic);
                    player.closeInventory();
                    return;
                  }
                  
                  
                }
                
                if (!cosmetic.has(profile)) {
                  if (!cosmetic.canBuy(this.player) || (profile.getCoins("bedwars") < cosmetic.getCoins() && (CashManager.CASH && profile
                      .getStats("account", "cash") < cosmetic.getCash()))) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    return;
                  }
                  
                  EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                  if (!CashManager.CASH || cosmetic.getCash() == 0) {
                    new MenuBuyCosmetic<>(profile, this.name.replace("Bed Wars - ", ""), cosmetic, this.cosmeticClass);
                  } else {
                    new MenuBuyCashCosmetic<>(profile, this.name.replace("Bed Wars - ", ""), cosmetic, this.cosmeticClass);
                  }
                  return;
                }
                
                if (!cosmetic.canBuy(this.player)) {
                  EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                  this.player.sendMessage("§cVocê não possui permissão suficiente para continuar.");
                  return;
                }
                
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                if (cosmetic.isSelected(profile)) {
                  profile.getAbstractContainer("bedwars", "selected", SelectedContainer.class).setSelected(cosmetic.getType(), 0);
                } else {
                  profile.getAbstractContainer("bedwars", "selected", SelectedContainer.class).setSelected(cosmetic);
                }
                
                new MenuCosmetics<>(profile, this.name.replace("Bed Wars - ", ""), this.cosmeticClass);
              }
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
    this.cosmeticClass = null;
    this.cosmetics.clear();
    this.cosmetics = null;
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
}
