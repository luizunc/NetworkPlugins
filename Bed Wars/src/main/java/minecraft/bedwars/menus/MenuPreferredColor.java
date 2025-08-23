package minecraft.bedwars.menus;

import minecraft.bedwars.Main;
import minecraft.core.core.libraries.menu.PlayerMenu;
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

public class MenuPreferredColor extends PlayerMenu {
  
    public MenuPreferredColor(Profile profile) {
    super(profile.getPlayer(), "Cor Preferida", 5);

    // Obter cor preferida atual do banco de dados
    String corAtual = profile.getDataContainer("bedwars", "preferred_color").getAsString();
    if (corAtual == null || corAtual.isEmpty()) {
      corAtual = "0"; // Nenhum por padrão
    }

    // Botão "Nenhum" (símbolo de proibido) - Primeiro
    String descNenhum = "";
    if (corAtual.equals("0")) {
      descNenhum += "&aSelecionada.";
      ItemStack itemNenhum = BukkitUtils.deserializeItemStack(
          "BARRIER : 1 : nome>&cNenhum : desc>\n" + descNenhum);
      BukkitUtils.putGlowEnchantment(itemNenhum);
      this.setItem(11, itemNenhum);
    } else {
      descNenhum += "&eClique para selecionar";
      this.setItem(11, BukkitUtils.deserializeItemStack(
          "BARRIER : 1 : nome>&cNenhum : desc>\n" + descNenhum));
    }

    // Cores dos times na ordem da nova sequência
    String descVermelho = "";
    if (corAtual.equals("1")) {
      descVermelho += "&aSelecionada.";
      ItemStack itemVermelho = BukkitUtils.deserializeItemStack(
          "WOOL:14 : 1 : nome>&cVermelho : desc>\n" + descVermelho);
      BukkitUtils.putGlowEnchantment(itemVermelho);
      this.setItem(12, itemVermelho);
    } else {
      descVermelho += "&eClique para selecionar";
      this.setItem(12, BukkitUtils.deserializeItemStack(
          "WOOL:14 : 1 : nome>&cVermelho : desc>\n" + descVermelho));
    }

    String descAzul = "";
    if (corAtual.equals("2")) {
      descAzul += "&aSelecionada.";
      ItemStack itemAzul = BukkitUtils.deserializeItemStack(
          "WOOL:11 : 1 : nome>&9Azul : desc>\n" + descAzul);
      BukkitUtils.putGlowEnchantment(itemAzul);
      this.setItem(13, itemAzul);
    } else {
      descAzul += "&eClique para selecionar";
      this.setItem(13, BukkitUtils.deserializeItemStack(
          "WOOL:11 : 1 : nome>&9Azul : desc>\n" + descAzul));
    }

    String descVerde = "";
    if (corAtual.equals("3")) {
      descVerde += "&aSelecionada.";
      ItemStack itemVerde = BukkitUtils.deserializeItemStack(
          "WOOL:5 : 1 : nome>&aVerde : desc>\n" + descVerde);
      BukkitUtils.putGlowEnchantment(itemVerde);
      this.setItem(14, itemVerde);
    } else {
      descVerde += "&eClique para selecionar";
      this.setItem(14, BukkitUtils.deserializeItemStack(
          "WOOL:5 : 1 : nome>&aVerde : desc>\n" + descVerde));
    }

    String descAmarelo = "";
    if (corAtual.equals("4")) {
      descAmarelo += "&aSelecionada.";
      ItemStack itemAmarelo = BukkitUtils.deserializeItemStack(
          "WOOL:4 : 1 : nome>&eAmarelo : desc>\n" + descAmarelo);
      BukkitUtils.putGlowEnchantment(itemAmarelo);
      this.setItem(15, itemAmarelo);
    } else {
      descAmarelo += "&eClique para selecionar";
      this.setItem(15, BukkitUtils.deserializeItemStack(
          "WOOL:4 : 1 : nome>&eAmarelo : desc>\n" + descAmarelo));
    }

    String descCiano = "";
    if (corAtual.equals("5")) {
      descCiano += "&aSelecionada.";
      ItemStack itemCiano = BukkitUtils.deserializeItemStack(
          "WOOL:9 : 1 : nome>&bCiano : desc>\n" + descCiano);
      BukkitUtils.putGlowEnchantment(itemCiano);
      this.setItem(21, itemCiano);
    } else {
      descCiano += "&eClique para selecionar";
      this.setItem(21, BukkitUtils.deserializeItemStack(
          "WOOL:9 : 1 : nome>&bCiano : desc>\n" + descCiano));
    }

    String descBranco = "";
    if (corAtual.equals("6")) {
      descBranco += "&aSelecionada.";
      ItemStack itemBranco = BukkitUtils.deserializeItemStack(
          "WOOL:0 : 1 : nome>&fBranco : desc>\n" + descBranco);
      BukkitUtils.putGlowEnchantment(itemBranco);
      this.setItem(22, itemBranco);
    } else {
      descBranco += "&eClique para selecionar";
      this.setItem(22, BukkitUtils.deserializeItemStack(
          "WOOL:0 : 1 : nome>&fBranco : desc>\n" + descBranco));
    }

    String descRosa = "";
    if (corAtual.equals("7")) {
      descRosa += "&aSelecionada.";
      ItemStack itemRosa = BukkitUtils.deserializeItemStack(
          "WOOL:6 : 1 : nome>&dRosa : desc>\n" + descRosa);
      BukkitUtils.putGlowEnchantment(itemRosa);
      this.setItem(23, itemRosa);
    } else {
      descRosa += "&eClique para selecionar";
      this.setItem(23, BukkitUtils.deserializeItemStack(
          "WOOL:6 : 1 : nome>&dRosa : desc>\n" + descRosa));
    }

    String descCinza = "";
    if (corAtual.equals("8")) {
      descCinza += "&aSelecionada.";
      ItemStack itemCinza = BukkitUtils.deserializeItemStack(
          "WOOL:7 : 1 : nome>&7Cinza : desc>\n" + descCinza);
      BukkitUtils.putGlowEnchantment(itemCinza);
      this.setItem(24, itemCinza);
    } else {
      descCinza += "&eClique para selecionar";
      this.setItem(24, BukkitUtils.deserializeItemStack(
          "WOOL:7 : 1 : nome>&7Cinza : desc>\n" + descCinza));
    }

    // Botão Voltar
    this.setItem(40, BukkitUtils.deserializeItemStack(
        "ARROW : 1 : nome>&cVoltar : desc>\n&7Voltar para Cosméticos da Partida"));
    
    this.register(Main.getInstance());
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
            if (evt.getSlot() == 11) {
              // Nenhum
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("0");
              this.player.sendMessage("§cCor preferida removida!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 12) {
              // Vermelho
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("1");
              this.player.sendMessage("§aCor preferida definida como §cVermelho§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 13) {
              // Azul
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("2");
              this.player.sendMessage("§aCor preferida definida como §9Azul§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 14) {
              // Verde Lima
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("3");
              this.player.sendMessage("§aCor preferida definida como §aVerde§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 15) {
              // Amarelo
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("4");
              this.player.sendMessage("§aCor preferida definida como §eAmarelo§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 21) {
              // Ciano
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("5");
              this.player.sendMessage("§aCor preferida definida como §bCiano§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 22) {
              // Branco
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("6");
              this.player.sendMessage("§aCor preferida definida como §fBranco§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 23) {
              // Rosa
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("7");
              this.player.sendMessage("§aCor preferida definida como §dRosa§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 24) {
              // Cinza
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              profile.getDataContainer("bedwars", "preferred_color").set("8");
              this.player.sendMessage("§aCor preferida definida como §7Cinza§a!");
              new MenuPreferredColor(profile);
            } else if (evt.getSlot() == 40) {
              // Voltar
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShop.MenuCosmeticsPage(profile);
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