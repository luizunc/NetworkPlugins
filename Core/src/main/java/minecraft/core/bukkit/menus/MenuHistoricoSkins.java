package minecraft.core.bukkit.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.database.data.container.SkinsContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Menu de histórico de skins do jogador.
 * Mostra as últimas skins utilizadas pelo jogador.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuHistoricoSkins extends PlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Histórico de Skins";
  private static final int MENU_ROWS = 5;
  
  // Slots dos itens
  private static final int SLOT_VOLTAR = 40; // Quinta linha, quinta coluna (slot 40) - Centralizado
  
  private final Profile profile;
  
  /**
   * Construtor do menu de histórico de skins.
   * 
   * @param player Jogador que abrirá o menu
   * @param profile Perfil do jogador
   */
  public MenuHistoricoSkins(Player player, Profile profile) {
    super(player, MENU_TITLE, MENU_ROWS);
    this.profile = profile;
    
    setupItems();
    register(Core.getInstance());
    open();
  }
  
  /**
   * Configura os itens do menu.
   */
  private void setupItems() {
    // Preenche o histórico de skins
    setupHistoricoSkins();
    
    // Item para voltar ao menu anterior
    setItem(SLOT_VOLTAR, createVoltarItem());
  }
  
  /**
   * Configura o histórico de skins.
   */
  private void setupHistoricoSkins() {
    SkinsContainer skinsContainer = profile.getSkinsContainer();
    
    // Obtém a lista de skins do histórico
    List<SkinsContainer.SkinHistoryEntry> history = skinsContainer.getHistoryList();
    
    if (history.isEmpty()) {
      // Se não há skins no histórico, mostra mensagem
      setItem(22, createNoSkinsItem());
      return;
    }
    
    // Mostra as skins do histórico
    int maxSkins = Math.min(history.size(), 36); // Máximo 36 slots disponíveis
    
    for (int i = 0; i < maxSkins; i++) {
      SkinsContainer.SkinHistoryEntry skin = history.get(i);
      int slot = getSlotForSkin(i);
      setItem(slot, createSkinItem(skin));
    }
    
    // Adiciona informações sobre o histórico
    setItem(49, createHistoryInfoItem(history.size()));
  }
  
  /**
   * Cria o item para quando não há skins.
   * 
   * @return ItemStack do item
   */
  private ItemStack createNoSkinsItem() {
    return BukkitUtils.deserializeItemStack(
        "BARRIER : 1 : nome>&cNenhuma Skin Encontrada : desc>&7Você ainda não utilizou\n" +
        "&7nenhuma skin personalizada.\n" +
        " \n" +
        "&eUse &f/skin (jogador) &epara aplicar uma skin!");
  }
  
  /**
   * Calcula o slot para uma skin baseado no índice.
   * 
   * @param index Índice da skin
   * @return Slot no inventário
   */
  private int getSlotForSkin(int index) {
    int row = (index / 7) + 1; // Começa na segunda linha
    int col = (index % 7) + 1; // Começa na segunda coluna
    return row * 9 + col;
  }
  
  /**
   * Cria um item representando uma skin do histórico.
   * 
   * @param skinEntry Entrada do histórico da skin
   * @return ItemStack do item
   */
  private ItemStack createSkinItem(SkinsContainer.SkinHistoryEntry skinEntry) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"));
    String dateStr = sdf.format(skinEntry.timestamp);
    
    String desc = "&7Utilizada em: &f" + dateStr + "\n" +
                  " \n" +
                  "&eClique para aplicar esta skin!";
    
    // Se for a skin atual, destaca
    SkinsContainer skinsContainer = profile.getSkinsContainer();
    String currentSkin = skinsContainer.getSkin();
    if (skinEntry.name.equals(currentSkin)) {
      desc = "&a&lSKIN ATUAL\n" + desc;
    }
    
    return BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : nome>&a" + skinEntry.name + " : desc>" + desc);
  }
  
  /**
   * Cria o item informativo sobre o histórico.
   * 
   * @param totalSkins Total de skins no histórico
   * @return ItemStack do item
   */
  private ItemStack createHistoryInfoItem(int totalSkins) {
    return BukkitUtils.deserializeItemStack(
        "BOOK : 1 : nome>&eHistórico de Skins : desc>&7Total de skins: &f" + totalSkins + "\n" +
        "&7Máximo armazenado: &f10 skins\n" +
        " \n" +
        "&7As skins são ordenadas por data\n" +
        "&7de utilização (mais recente primeiro).");
  }
  

  
  /**
   * Cria o item para voltar ao menu anterior.
   * 
   * @return ItemStack do item
   */
  private ItemStack createVoltarItem() {
    return BukkitUtils.deserializeItemStack(
        "ARROW : 1 : nome>&cVoltar ao Menu de Skins");
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
    
    if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(getInventory())) {
      return;
    }
    
    ItemStack item = evt.getCurrentItem();
    if (item == null || item.getType() == Material.AIR) {
      return;
    }
    
    handleItemClick(evt.getSlot());
  }
  
  /**
   * Manipula o clique em itens específicos.
   * 
   * @param slot Slot clicado
   */
  private void handleItemClick(int slot) {
    if (slot == SLOT_VOLTAR) {
      // Volta ao menu de skins
      HandlerList.unregisterAll(this);
      new MenuSkins(player, profile);
      return;
    }
    
    // Verifica se é um item de skin do histórico
    ItemStack item = getItem(slot);
    if (item != null && item.getType() == Material.SKULL_ITEM && 
        item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
      
      String displayName = item.getItemMeta().getDisplayName();
      if (displayName.startsWith("§a")) {
        String skinName = displayName.substring(2); // Remove a cor §a
        
        // TODO: Implementar aplicação da skin selecionada
        player.sendMessage("§aSkin " + skinName + " selecionada! Em desenvolvimento...");
        player.closeInventory();
      }
    }
  }
  
  /**
   * Manipula o fechamento do inventário.
   * 
   * @param evt Evento de fechamento
   */
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getInventory().equals(getInventory()) && evt.getPlayer().equals(player)) {
      HandlerList.unregisterAll(this);
    }
  }
  
  /**
   * Manipula o jogador saindo do servidor.
   * 
   * @param evt Evento de saída
   */
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      HandlerList.unregisterAll(this);
    }
  }
} 