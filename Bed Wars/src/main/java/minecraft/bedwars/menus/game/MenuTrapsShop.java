package minecraft.bedwars.menus.game;

import minecraft.core.bukkit.Core;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.game.improvements.traps.Trap;
import minecraft.bedwars.utils.PlayerUtils;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuTrapsShop extends PlayerMenu {
  
  protected Map<Integer, Trap> TRAPS = new HashMap<>();
  
  public MenuTrapsShop(Profile profile) {
    super(profile.getPlayer(), "Comprar armadilha", 4);
    
    BedWarsTeam team = profile.getGame(BedWars.class).getTeam(profile.getPlayer());
    
    int slot = 10;
    for (Trap trap : Trap.listTraps()) {
      String color = PlayerUtils
          .getCountFromMaterial(player.getInventory(), trap.getMaterial()) < (team.getTraps().size() + 1) ? "&c" : "&a";
      ItemStack icon = BukkitUtils.deserializeItemStack(trap.getIcon().replace("{color}", color));
      ItemMeta meta = icon.getItemMeta();
      List<String> lore = meta.getLore();
      lore.add("");
      lore.add("§7Custo: §b" + (team.getTraps().size() + 1) + " Diamante" + (team.getTraps().size() + 1 > 1 ? "s" : ""));
      lore.add("");
      if ("&c".equals(color)) {
        lore.add("§cVocê não possui Diamantes suficientes!");
      } else {
        lore.add("§eClique para comprar!");
      }
      meta.setLore(lore);
      icon.setItemMeta(meta);
      
      this.setItem(slot, icon);
      this.TRAPS.put(slot++, trap);
    }
    
    this.setItem(31, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"));
    
    this.open();
    this.register(Core.getInstance());
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
        ItemStack item = evt.getCurrentItem();
        Profile profile = Profile.getProfile(this.player.getName());
        
        BedWars game = profile.getGame(BedWars.class);
        BedWarsTeam team = game.getTeam(this.player);
        
        if (team == null) {
          player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(evt.getInventory()) && item != null && item.getType() != Material.AIR) {
          if (evt.getSlot() == 31) {
            EnumSound.CLICK.play(player, 0.5F, 2.0F);
            new MenuUpgradeShop(profile);
          } else {
            Trap trap = TRAPS.get(evt.getSlot());
            if (trap != null) {
              if (team.getTraps().size() > 2) {
                player.sendMessage("§cVocê já possui o máximo de armadilhas na fila!");
                return;
              }
              
              if (PlayerUtils.getCountFromMaterial(player.getInventory(), trap.getMaterial()) < (team.getTraps().size() + 1)) {
                player.sendMessage("§cVocê não possui recursos suficientes para adquirir esta Armadilha!");
                return;
              }
              
              PlayerUtils.removeItem(player.getInventory(), trap.getMaterial(), team.getTraps().size() + 1);
              team.addTrap(trap);
              
              team.listPlayers()
                  .forEach(players -> players.sendMessage("§a" + player.getName() +
                      " comprou §6" + StringUtils.stripColors(item.getItemMeta().getDisplayName())));
              
              new MenuTrapsShop(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    this.TRAPS.clear();
    this.TRAPS = null;
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