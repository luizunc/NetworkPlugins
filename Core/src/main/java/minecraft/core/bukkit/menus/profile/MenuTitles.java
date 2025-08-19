package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.PagedPlayerMenu;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.core.player.Profile;
import minecraft.core.core.titles.Title;
import minecraft.core.core.titles.TitleManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu de títulos do jogador.
 * Permite selecionar e gerenciar títulos disponíveis.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuTitles extends PagedPlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Títulos";
  private static final int MENU_ROWS = 5;
  private static final int SLOT_PREVIOUS_PAGE = 36;
  private static final int SLOT_NEXT_PAGE = 44;
  private static final int SLOT_VOLTAR = 40;
  
  // Slots disponíveis para títulos
  private static final int[] TITLE_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
  
  // Mapa de itens para títulos
  private Map<ItemStack, Title> titles = new HashMap<>();
  
  /**
   * Construtor do menu de títulos.
   * 
   * @param profile Perfil do jogador
   */
  public MenuTitles(Profile profile) {
    super(profile.getPlayer(), MENU_TITLE, MENU_ROWS);
    
    setupMenu(profile);
  }
  
  /**
   * Configura o menu inicial.
   * 
   * @param profile Perfil do jogador
   */
  private void setupMenu(Profile profile) {
    setupPagination();
    setupItems(profile);
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
   */
  private void setupItems(Profile profile) {
    List<ItemStack> items = new ArrayList<>();
    List<ItemStack> sub = new ArrayList<>();
    
    for (Title title : Title.listTitles()) {
      ItemStack item = title.getIcon(profile);
      titles.put(item, title);
      
      if (title.has(profile)) {
        items.add(item);
      } else {
        sub.add(item);
      }
    }
    
    items.addAll(sub);
    setItems(items);
    
    // Limpa as listas
    sub.clear();
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
    
    handleItemClick(evt.getSlot(), item, profile);
  }
  
  /**
   * Manipula o clique em itens específicos.
   * 
   * @param slot Slot clicado
   * @param item Item clicado
   * @param profile Perfil do jogador
   */
  private void handleItemClick(int slot, ItemStack item, Profile profile) {
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
        new MenuProfile(profile);
        break;
        
      default:
        handleTitleClick(item, profile);
        break;
    }
  }
  
  /**
   * Manipula o clique em títulos.
   * 
   * @param item Item clicado
   * @param profile Perfil do jogador
   */
  private void handleTitleClick(ItemStack item, Profile profile) {
    Title title = titles.get(item);
    if (title == null) {
      return;
    }
    
    if (!title.has(profile)) {
      EnumSound.ENDERMAN_TELEPORT.play(player, 0.5F, 1.0F);
      return;
    }
    
    EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
    
    Title selected = profile.getSelectedContainer().getTitle();
    if (title.equals(selected)) {
      // Deseleciona o título
      profile.getSelectedContainer().setTitle("0");
      TitleManager.deselect(profile);
    } else {
      // Seleciona o título
      profile.getSelectedContainer().setTitle(title.getId());
      TitleManager.select(profile, title);
    }
    
    new MenuTitles(profile);
  }
  
  /**
   * Cancela o registro de eventos.
   */
  public void cancel() {
    titles.clear();
    titles = null;
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
