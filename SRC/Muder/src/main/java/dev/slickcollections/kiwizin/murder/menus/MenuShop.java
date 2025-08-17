package dev.slickcollections.kiwizin.murder.menus;

import java.util.List;

import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.cosmetics.types.*;
import dev.slickcollections.kiwizin.murder.menus.cosmetics.MenuCosmetics;
import dev.slickcollections.kiwizin.murder.menus.cosmetics.MenuHotbarConfigSelect;
import dev.slickcollections.kiwizin.murder.menus.shop.MenuClassicUpgrades;
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
import dev.slickcollections.kiwizin.murder.cosmetics.Cosmetic;

public class MenuShop extends PlayerMenu {

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
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuClassicUpgrades(profile);
            } else if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Comemorações", WinAnimation.class);
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Aparência da Faca", Knife.class);
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Gritos de Morte", DeathCry.class);
            } else if (evt.getSlot() == 29) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Mensagens de Morte", DeathMessage.class);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuHotbarConfigSelect(profile);
            } else if (evt.getSlot() == 33) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Chapéu", Hat.class);
            }
          }
        }
      }
    }
  }

  public MenuShop(Profile profile) {
    super(profile.getPlayer(), "Loja - Murder", 5);

    List<WinAnimation> animations = Cosmetic.listByType(WinAnimation.class);
    long max = animations.size();
    long owned = animations.stream().filter(animation -> animation.has(profile)).count();
    long percentage = max == 0 ? 100 : (owned * 100) / max;
    String color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    animations.clear();
    this.setItem(10 ,BukkitUtils.deserializeItemStack(
      "DRAGON_EGG : 1 : nome>&aComemorações de Vitória : desc>&7Esbanje estilo nas suas vitórias\n&7com comemorações exclusivas.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));

    this.setItem(12, BukkitUtils.deserializeItemStack(
      "BEACON : 1 : nome>&aMelhorias: &fClássico : desc>&7Nesta seção você poderá adquirir\n&7maiores chances de se tornar um\n&cAssassino &7ou &6Detetive &7na partida\n&7de Murder Clássico.\n \n&eClique para comprar!"));

    List<Knife> knifes = Cosmetic.listByType(Knife.class);
    max = knifes.size();
    owned = knifes.stream().filter(knife -> knife.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    knifes.clear();
    this.setItem(14, BukkitUtils.deserializeItemStack(
      "DIAMOND_SWORD : 1 : esconder>tudo : nome>&aAparência da Faca : desc>&7Altere a aparência\n&7de sua Faca como &cAssassino&7.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));

    List<DeathCry> deathcries = Cosmetic.listByType(DeathCry.class);
    max = deathcries.size();
    owned = deathcries.stream().filter(deathcry -> deathcry.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    deathcries.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack(
      "GHAST_TEAR : 1 : nome>&aGritos de Morte : desc>&7Gritos de mortes são sons que\n&7irão ser reproduzidos toda vez\n&7que você for assassinado.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));

    List<DeathMessage> deathmessages = Cosmetic.listByType(DeathMessage.class);
    max = deathmessages.size();
    owned = deathmessages.stream().filter(cage -> cage.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
    deathmessages.clear();
    this.setItem(29, BukkitUtils.deserializeItemStack(
      "BOOK_AND_QUILL : 1 : nome>&aMensagens de Morte&f: Assassinos : desc>&7Anuncie o abate do seu inimigo de\n&7uma forma estilosa com mensagens de morte.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));

    this.setItem(31, BukkitUtils.deserializeItemStack(
      "IRON_SWORD : 1 : esconder>tudo : nome>&aOrdem dos Itens : desc>&7Configure como você irá receber\n&7o seus itens na partida.\n \n&eClique para configurar!"));

    List<Hat> hats = Cosmetic.listByType(Hat.class);
    max = hats.size();
    owned = hats.stream().filter(hat -> hat.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    hats.clear();
    this.setItem(33, BukkitUtils.deserializeItemStack(
      "SKULL_ITEM:3 : 1 : nome>&aChapéu : desc>&7Esbanje estilo com seu chapéu\n&7até mesmo dentro da partida!\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI1YWY5NjZhMzI2ZjlkOTg0NjZhN2JmODU4MmNhNGRhNjQ1M2RlMjcxYjNiYzllNTlmNTdhOTliNjM1MTFjNiJ9fX0="));

    this.register(Main.getInstance());
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
