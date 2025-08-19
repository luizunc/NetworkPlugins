package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.bukkit.achievements.types.BedWarsAchievement;
import minecraft.core.bukkit.achievements.types.SkyWarsAchievement;
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

/**
 * Menu de desafios/achievements do jogador.
 * Permite visualizar desafios de Sky Wars, Bed Wars e outros minigames.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuAchievements extends PlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Desafios";
  private static final int MENU_ROWS = 4;
  
  // Slots dos itens
  private static final int SLOT_SKYWARS = 11;
  private static final int SLOT_BEDWARS = 12;
  private static final int SLOT_VOLTAR = 31;
  
  /**
   * Construtor do menu de achievements.
   * 
   * @param profile Perfil do jogador
   */
  public MenuAchievements(Profile profile) {
    super(profile.getPlayer(), MENU_TITLE, MENU_ROWS);
    
    setupItems(profile);
    register(Core.getInstance());
    open();
  }
  
  /**
   * Configura os itens do menu.
   * 
   * @param profile Perfil do jogador
   */
  private void setupItems(Profile profile) {
    setItem(SLOT_SKYWARS, createSkyWarsItem(profile));
    setItem(SLOT_BEDWARS, createBedWarsItem(profile));
    setItem(SLOT_VOLTAR, createVoltarItem());
  }
  
  /**
   * Cria o item de Sky Wars.
   * 
   * @param profile Perfil do jogador
   * @return ItemStack do item
   */
  private ItemStack createSkyWarsItem(Profile profile) {
    List<SkyWarsAchievement> skywars = Achievement.listAchievements(SkyWarsAchievement.class);
    long max = skywars.size();
    long completed = skywars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = getProgressColor(completed, max);
    skywars.clear();
    
    return BukkitUtils.deserializeItemStack(
        "EYE_OF_ENDER : 1 : nome>&aSky Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n" +
        " \n" +
        "&eClique para visualizar!");
  }
  
  /**
   * Cria o item de Bed Wars.
   * 
   * @param profile Perfil do jogador
   * @return ItemStack do item
   */
  private ItemStack createBedWarsItem(Profile profile) {
    List<BedWarsAchievement> bedwars = Achievement.listAchievements(BedWarsAchievement.class);
    long max = bedwars.size();
    long completed = bedwars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = getProgressColor(completed, max);
    bedwars.clear();
    
    return BukkitUtils.deserializeItemStack(
        "BED : 1 : nome>&aBed Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n" +
        " \n" +
        "&eClique para visualizar!");
  }
  
  /**
   * Cria o item de voltar.
   * 
   * @return ItemStack do item
   */
  private ItemStack createVoltarItem() {
    return BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar");
  }
  
  /**
   * Obtém a cor baseada no progresso dos desafios.
   * 
   * @param completed Número de desafios completados
   * @param max Número total de desafios
   * @return Cor formatada
   */
  private String getProgressColor(long completed, long max) {
    if (completed == max) {
      return "&a"; // Verde - todos completados
    } else if (completed > max / 2) {
      return "&7"; // Cinza - mais da metade
    } else {
      return "&c"; // Vermelho - menos da metade
    }
  }
  
  /**
   * Manipula o clique no inventário.
   * 
   * @param evt Evento de clique
   */
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (!evt.getInventory().equals(getInventory()) || !evt.getWhoClicked().equals(player)) {
      return;
    }
    
    evt.setCancelled(true);
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile == null) {
      player.closeInventory();
      return;
    }
    
    if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(getInventory())) {
      return;
    }
    
    ItemStack item = evt.getCurrentItem();
    if (item == null || item.getType() == Material.AIR) {
      return;
    }
    
    handleItemClick(evt.getSlot(), profile);
  }
  
  /**
   * Manipula o clique em itens específicos.
   * 
   * @param slot Slot clicado
   * @param profile Perfil do jogador
   */
  private void handleItemClick(int slot, Profile profile) {
    switch (slot) {
      case SLOT_SKYWARS:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuAchievementsList<>(profile, "Sky Wars", SkyWarsAchievement.class);
        break;
        
      case SLOT_BEDWARS:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuAchievementsList<>(profile, "Bed Wars", BedWarsAchievement.class);
        break;
        
      case SLOT_VOLTAR:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuProfile(profile);
        break;
    }
  }
  
  /**
   * Cancela o registro de eventos.
   */
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  /**
   * Manipula o evento de saída do jogador.
   * 
   * @param evt Evento de saída
   */
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      cancel();
    }
  }
  
  /**
   * Manipula o evento de fechamento do inventário.
   * 
   * @param evt Evento de fechamento
   */
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getInventory())) {
      cancel();
    }
  }
}
