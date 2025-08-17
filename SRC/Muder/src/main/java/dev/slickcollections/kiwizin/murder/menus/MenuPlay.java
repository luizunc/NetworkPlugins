package dev.slickcollections.kiwizin.murder.menus;

import dev.slickcollections.kiwizin.libraries.menu.UpdatablePlayerMenu;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPlay extends UpdatablePlayerMenu {

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
            if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              Murder game = Murder.findRandom(this.mode);
              if (game != null) {
                this.player.sendMessage(Language.lobby$npc$play$connect);
                game.join(profile);
              }
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuMapSelector(profile, this.mode);
            }
          }
        }
      }
    }
  }

  private MurderMode mode;

  public MenuPlay(Profile profile, MurderMode mode) {
    super(profile.getPlayer(), "Modo " + mode.getName(), 3);
    this.mode = mode;

    this.update();
    this.register(Main.getInstance(), 20);
    this.open();
  }

  @Override
  public void update() {
    int players = this.mode.getSize();
    int waiting = Murder.getWaiting(this.mode);
    int playing = Murder.getPlaying(this.mode);

    if (this.mode == MurderMode.CLASSIC) {
      this.setItem(12, BukkitUtils.deserializeItemStack(
        "ENDER_PEARL : 1 : nome>&aModo Clássico : desc>&71 Detetive\n&71 Assassino\n&714 Inocentes\n \n&fEm espera: &7" + StringUtils
          .formatNumber(waiting) + "\n&fJogando: &7" + StringUtils.formatNumber(playing) + "\n \n&eClique para jogar!"));
    } else {
      this.setItem(12, BukkitUtils.deserializeItemStack(
        "ENDER_PEARL : 1 : nome>&aModo Assassinos : desc>&716 Assassinos\n \n&fEm espera: &7" + StringUtils
          .formatNumber(waiting) + "\n&fJogando: &7" + StringUtils.formatNumber(playing) + "\n \n&eClique para jogar!"));
    }

    this.setItem(14, BukkitUtils.deserializeItemStack("MAP : 1 : nome>&aSelecione um Mapa : desc>&eClique para jogar em um mapa específico."));
  }

  public void cancel() {
    super.cancel();
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
