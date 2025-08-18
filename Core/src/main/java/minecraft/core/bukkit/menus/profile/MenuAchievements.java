package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.bukkit.achievements.types.BedWarsAchievement;
// import minecraft.core.bukkit.achievements.types.MurderAchievement; // Removido - Murder não é mais suportado
import minecraft.core.bukkit.achievements.types.SkyWarsAchievement;
// import minecraft.core.bukkit.achievements.types.TheBridgeAchievement; // Removido - The Bridge não é mais suportado
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.bukkit.menus.profile.achievements.MenuAchievementsList;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuAchievements extends PlayerMenu {
  
  public MenuAchievements(Profile profile) {
    super(profile.getPlayer(), "Desafios", 4);
    
    List<SkyWarsAchievement> skywars = Achievement.listAchievements(SkyWarsAchievement.class);
    long max = skywars.size();
    long completed = skywars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    skywars.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack("GRASS : 1 : nome>&aSky Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));
    
    // The Bridge e Murder removidos do sistema
    
    List<BedWarsAchievement> bedwars = Achievement.listAchievements(BedWarsAchievement.class);
    max = bedwars.size();
    completed = bedwars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    bedwars.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack("BED : 1 : nome>&aBed Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));
    
    
    this.setItem(31, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    
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
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Sky Wars", SkyWarsAchievement.class);
            // The Bridge e Murder removidos do sistema
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Bed Wars", BedWarsAchievement.class);
            } else if (evt.getSlot() == 31) {
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
