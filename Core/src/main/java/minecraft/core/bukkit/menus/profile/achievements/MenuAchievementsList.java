package minecraft.core.bukkit.menus.profile.achievements;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.core.libraries.menu.PagedPlayerMenu;
import minecraft.core.bukkit.menus.profile.MenuAchievements;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Menu de lista de achievements/desafios.
 * Permite visualizar todos os achievements de um tipo específico.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuAchievementsList<T extends Achievement> extends PagedPlayerMenu {
  
  // Constantes
  private static final int MENU_ROWS = 5;
  private static final int SLOT_PREVIOUS_PAGE = 36;
  private static final int SLOT_NEXT_PAGE = 44;
  private static final int SLOT_VOLTAR = 40;
  
  // Slots disponíveis para achievements
  private static final int[] ACHIEVEMENT_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
  
  /**
   * Construtor do menu de lista de achievements.
   * 
   * @param profile Perfil do jogador
   * @param name Nome do tipo de achievement
   * @param achievementClass Classe do tipo de achievement
   */
  public MenuAchievementsList(Profile profile, String name, Class<T> achievementClass) {
    super(profile.getPlayer(), "Desafios - " + name, MENU_ROWS);
    
    setupMenu(profile, achievementClass);
  }
  
  /**
   * Configura o menu inicial.
   * 
   * @param profile Perfil do jogador
   * @param achievementClass Classe do tipo de achievement
   */
  private void setupMenu(Profile profile, Class<T> achievementClass) {
    setupPagination();
    setupItems(profile, achievementClass);
    register(Core.getInstance());
    open();
  }
  
  /**
   * Configura a paginação do menu.
   */
  private void setupPagination() {
    previousPage = SLOT_PREVIOUS_PAGE;
    nextPage = SLOT_NEXT_PAGE;
    onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);
    removeSlotsWith(createVoltarItem(), SLOT_VOLTAR);
  }
  
  /**
   * Configura os itens do menu.
   * 
   * @param profile Perfil do jogador
   * @param achievementClass Classe do tipo de achievement
   */
  private void setupItems(Profile profile, Class<T> achievementClass) {
    List<ItemStack> items = new ArrayList<>();
    List<T> achievements = Achievement.listAchievements(achievementClass);
    
    for (T achievement : achievements) {
      items.add(achievement.getIcon(profile));
    }
    
    setItems(items);
    
    // Limpa as listas
    achievements.clear();
    items.clear();
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
   * Manipula o clique no inventário.
   * 
   * @param evt Evento de clique
   */
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (!evt.getInventory().equals(getCurrentInventory()) || !evt.getWhoClicked().equals(player)) {
      return;
    }
    
    evt.setCancelled(true);
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile == null) {
      player.closeInventory();
      return;
    }
    
    if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(getCurrentInventory())) {
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
      case SLOT_PREVIOUS_PAGE:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        openPrevious();
        break;
        
      case SLOT_NEXT_PAGE:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        openNext();
        break;
        
      case SLOT_VOLTAR:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuAchievements(profile);
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
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getCurrentInventory())) {
      cancel();
    }
  }
}
