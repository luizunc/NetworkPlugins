package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.UpdatablePlayerMenu;
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
public class MenuTitles extends UpdatablePlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Títulos";
  private static final int MENU_ROWS = 5;
  private static final int UPDATE_INTERVAL = 20; // 1 segundo
  private static final int SLOT_VOLTAR = 40;
  
  // Slots disponíveis para títulos
  private static final int[] TITLE_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
  
  // Mapa de itens para títulos
  private Map<ItemStack, Title> titles = new HashMap<>();
  private Profile profile;
  
  /**
   * Construtor do menu de títulos.
   * 
   * @param profile Perfil do jogador
   */
  public MenuTitles(Profile profile) {
    super(profile.getPlayer(), MENU_TITLE, MENU_ROWS);
    this.profile = profile;
    
    setupMenu();
  }
  
  /**
   * Configura o menu inicial.
   */
  private void setupMenu() {
    setupItems();
    setItem(SLOT_VOLTAR, createVoltarItem());
    register(Core.getInstance(), UPDATE_INTERVAL);
    open();
  }
  
  /**
   * Configura os itens do menu.
   */
  private void setupItems() {
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
    
    // Adiciona os itens aos slots disponíveis
    int slotIndex = 0;
    for (ItemStack item : items) {
      if (slotIndex < TITLE_SLOTS.length) {
        setItem(TITLE_SLOTS[slotIndex], item);
        slotIndex++;
      }
    }
    
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
    
    // Verifica se o jogador tem rank Iron ou superior
    boolean hasIronPlus = hasIronOrHigherRank(profile);
    
    if (!title.has(profile)) {
      // Se tem rank Iron+ e não possui o título, dá o título
      if (hasIronPlus && !profile.getTitlesContainer().has(title)) {
        title.give(profile);
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        player.sendMessage("§aVocê obteve o título: " + title.getTitle());
        new MenuTitles(profile);
        return;
      }
      
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
   * Verifica se o jogador tem rank Iron ou superior.
   * 
   * @param profile Perfil do jogador
   * @return true se tem rank Iron ou superior
   */
  private boolean hasIronOrHigherRank(Profile profile) {
    if (profile.getPlayer() == null) {
      return false;
    }
    
    // Ranks em ordem (do mais baixo para o mais alto) conforme Rank.java
    String[] rankOrder = {"membro", "apoiador", "iron", "gold", "emerald", "partner", "partner+", "beta", "builder", "helper", "mod", "mod+", "admin"};
    
    // Obtém o rank atual do jogador
            minecraft.core.core.player.rank.Rank currentRank = minecraft.core.core.player.rank.Rank.getRank(profile.getPlayer(), true);
    String currentRankName = minecraft.core.core.utils.StringUtils.stripColors(currentRank.getName()).toLowerCase();
    
    // Encontra a posição do rank atual
    int currentRankIndex = -1;
    for (int i = 0; i < rankOrder.length; i++) {
      if (rankOrder[i].equals(currentRankName)) {
        currentRankIndex = i;
        break;
      }
    }
    
    // Se não encontrou o rank, assume que é membro (mais baixo)
    if (currentRankIndex == -1) {
      currentRankIndex = 0; // membro
    }
    
    // Encontra a posição do rank Iron
    int ironRankIndex = -1;
    for (int i = 0; i < rankOrder.length; i++) {
      if (rankOrder[i].equals("iron")) {
        ironRankIndex = i;
        break;
      }
    }
    
    // Retorna true se o rank atual é igual ou superior ao Iron
    return currentRankIndex >= ironRankIndex;
  }
  
  /**
   * Atualiza os itens do menu.
   */
  @Override
  public void update() {
    setupItems();
  }
  
  /**
   * Cancela o registro de eventos.
   */
  public void cancel() {
    titles.clear();
    titles = null;
    super.cancel();
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
