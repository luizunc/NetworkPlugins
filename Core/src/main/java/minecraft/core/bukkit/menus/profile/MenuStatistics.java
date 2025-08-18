package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuStatistics extends PlayerMenu {
  
  public MenuStatistics(Profile profile) {
    super(profile.getPlayer(), "Estatísticas", 5);
    
    this.setItem(10, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "GRASS : 1 : nome>&aSky Wars : desc>&eSolo:\n &8▪ &fAbates: &7%Core_SkyWars_1v1kills%\n &8▪ &fMortes: &7%Core_SkyWars_1v1deaths%\n &8▪ &fVitórias: &7%Core_SkyWars_1v1wins%\n &8▪ &fPartidas: &7%Core_SkyWars_1v1games%\n &8▪ &fAssistências: &7%Core_SkyWars_1v1assists%\n " + /*"\n&eDupla:\n &8▪ &fAbates: &7%Core_SkyWars_2v2kills%\n &8▪ &fMortes: &7%Core_SkyWars_2v2deaths%\n &8▪ &fVitórias: &7%Core_SkyWars_2v2wins%\n &8▪ &fPartidas: &7%Core_SkyWars_2v2games%\n &8▪ &fAssistências: &7%Core_SkyWars_2v2assists%\n*/ "\n&eRanked:\n &8▪ &fAbates: &7%Core_SkyWars_rankedkills%\n &8▪ &fMortes: &7%Core_SkyWars_rankeddeaths%\n &8▪ &fVitórias: &7%Core_SkyWars_rankedwins%\n &8▪ &fPartidas: &7%Core_SkyWars_rankedgames%\n &8▪ &fPontos: &7%Core_SkyWars_rankedpoints%\n \n&fCoins: &6%Core_SkyWars_coins%")));
    
    this.setItem(12, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "BED : 1 : nome>&aBed Wars : desc>&eGeral:\n &8▪ &fPartidas: &7%Core_BedWars_games%\n &8▪ &fAbates: &7%Core_BedWars_kills%\n &8▪ &fMortes: &7%Core_BedWars_deaths%\n &8▪ &fAbates Finais: &7%Core_BedWars_finalkills%\n &8▪ &fMortes Finais: &7%Core_BedWars_finaldeaths%\n &8▪ &fVitórias: &7%Core_BedWars_wins%\n &8▪ &fCamas destruídas: &7%Core_BedWars_bedsdestroyeds%\n &8▪ &fCamas perdidas: &7%Core_BedWars_bedslosteds%\n \n&fCoins: &6%Core_BedWars_coins%")));
    
    // The Bridge, Murder e Build Battle removidos do sistema
    
    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 10 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 16 || evt.getSlot() == 22) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
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
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
