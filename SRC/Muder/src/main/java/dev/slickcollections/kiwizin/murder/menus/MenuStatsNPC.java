package dev.slickcollections.kiwizin.murder.menus;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Maxter
 */
public class MenuStatsNPC extends PlayerMenu {

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
            if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.player.closeInventory();
            }
          }
        }
      }
    }
  }

  public MenuStatsNPC(Profile profile) {
    super(profile.getPlayer(), "Estatísticas - Murder", 5);

    String all = profile.getFormatedStats("kCoreMurder", "clkills", "askills");
    String allKnife = profile.getFormatedStats("kCoreMurder", "clknifekills", "askills");
    String allTKnife = profile.getFormatedStats("kCoreMurder", "clthrownknifekills", "asthrownknifekills");
    String asWins = profile.getFormatedStats("kCoreMurder", "aswins");
    this.setItem(4, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
      "PAPER : 1 : nome>&aTodos os Modos : desc>&eAbates:\n &8▪ &fGeral: &7" + all + "\n &8▪ &fArco: &7%kCore_Murder_classic_bowkills%\n &8▪ &fFaca: &7" + allKnife + "\n &8▪ &fArremesso: &7" + allTKnife + "\n \n&eVitórias:\n &8▪ &fComo Detetive: &7%kCore_Murder_classic_detectivewins%\n &8▪ &fComo Assassino: &7%kCore_Murder_classic_killerwins%\n &8▪ &fModo Assassinos: &7" + asWins + "\n \n&fCoins: &6%kCore_Murder_coins%")));

    this.setItem(21, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
      "PAPER : 1 : nome>&aClássico : desc>&eAbates:\n &8▪ &fGeral: &7%kCore_Murder_classic_kills%\n &8▪ &fArco: &7%kCore_Murder_classic_bowkills%\n &8▪ &fFaca: &7%kCore_Murder_classic_knifekills%\n &8▪ &fArremesso: &7%kCore_Murder_classic_thrownknifekills%\n \n&eVitórias:\n &8▪ &fGeral: &7%kCore_Murder_classic_wins%\n &8▪ &fComo Detetive: &7%kCore_Murder_classic_detectivewins%\n &8▪ &fComo Assassino: &7%kCore_Murder_classic_killerwins%\n \n&eVitória mais rápida:\n &8▪ &fComo Detetive: &7%kCore_Murder_classic_quickestdetective%\n &8▪ &fComo Assassino: &7%kCore_Murder_classic_quickestkiller%")));

    all = profile.getFormatedStats("kCoreMurder", "askills", "asthrownknifekills");
    this.setItem(23, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
      "PAPER : 1 : nome>&aAssassinos : desc>&eAbates:\n &8▪ &fGeral: &7" + all + "\n &8▪ &fFaca: &7%kCore_Murder_assassins_kills%\n &8▪ &fArremesso: &7%kCore_Murder_assassins_thrownknifekills%\n \n&fVitórias: &7%kCore_Murder_assassins_wins%")));

    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cFechar"));

    this.register(Core.getInstance());
    this.open();
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
