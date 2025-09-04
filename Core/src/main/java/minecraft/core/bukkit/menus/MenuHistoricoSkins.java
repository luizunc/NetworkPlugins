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
 * Mostra as últimas 10 skins utilizadas pelo jogador.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuHistoricoSkins extends PlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Histórico de Skins";
  private static final int MENU_ROWS = 4;
  
  // Slots dos itens
  private static final int SLOT_VOLTAR = 31; // Quarta linha, quinta coluna (slot 31) - Centralizado
  
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
    
    // Se histórico está vazio e há skin atual, adiciona ao histórico
    if (history.isEmpty()) {
      String currentSkin = skinsContainer.getSkin();
      String currentValue = skinsContainer.getValue();
      String currentSignature = skinsContainer.getSignature();
      
      if (currentSkin != null && currentValue != null && currentSignature != null) {
        skinsContainer.setSkinWithHistory(currentSkin, currentValue, currentSignature);
        profile.saveSync();
        // Recarrega o histórico após adicionar
        history = skinsContainer.getHistoryList();
      }
    }
    
    if (history.isEmpty()) {
      // Se não há skins no histórico, mostra mensagem
      setItem(22, createNoSkinsItem());
      return;
    }
    
    // Mostra as últimas 10 skins do histórico
    int maxSkins = Math.min(history.size(), 10);
    
    for (int i = 0; i < maxSkins; i++) {
      SkinsContainer.SkinHistoryEntry skin = history.get(i);
      int slot = getSlotForSkin(i);
      setItem(slot, createSkinItem(skin));
    }
    
    // Adiciona informações sobre o histórico
    setItem(35, createHistoryInfoItem(history.size()));
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
   * Distribui as skins em 2 linhas com até 5 skins por linha.
   * 
   * @param index Índice da skin
   * @return Slot no inventário
   */
  private int getSlotForSkin(int index) {
    if (index < 5) {
      // Primeira linha: slots 11, 12, 13, 14, 15
      return 11 + index;
    } else {
      // Segunda linha: slots 20, 21, 22, 23, 24
      return 20 + (index - 5);
    }
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
      desc = "&a&lSKIN ATUAL\n" +
             "&7Utilizada em: &f" + dateStr + "\n" +
             " \n" +
             "&7Esta é sua skin atual!";
    }
    
    // Cria o skull com a texture da skin usando o value
    return BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : skin>" + skinEntry.value + " : nome>&a" + skinEntry.name + " : desc>" + desc);
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
    SkinsContainer skinsContainer = profile.getSkinsContainer();
    List<SkinsContainer.SkinHistoryEntry> history = skinsContainer.getHistoryList();
    
    // Converte slot para índice da skin
    int skinIndex = getSkinIndexFromSlot(slot);
    
    if (skinIndex >= 0 && skinIndex < history.size() && skinIndex < 10) {
      SkinsContainer.SkinHistoryEntry skinEntry = history.get(skinIndex);
      
      // Aplica a skin selecionada
      applySkinFromHistory(skinEntry);
    }
  }
  
  /**
   * Converte um slot para o índice da skin correspondente.
   * 
   * @param slot Slot clicado
   * @return Índice da skin ou -1 se inválido
   */
  private int getSkinIndexFromSlot(int slot) {
    // Primeira linha: slots 11-15 (índices 0-4)
    if (slot >= 11 && slot <= 15) {
      return slot - 11;
    }
    // Segunda linha: slots 20-24 (índices 5-9)
    if (slot >= 20 && slot <= 24) {
      return slot - 20 + 5;
    }
    return -1;
  }
  
  /**
   * Aplica uma skin do histórico.
   * 
   * @param skinEntry Entrada da skin no histórico
   */
  private void applySkinFromHistory(SkinsContainer.SkinHistoryEntry skinEntry) {
    // Verifica se já está usando esta skin
    SkinsContainer skinsContainer = profile.getSkinsContainer();
    String currentSkin = skinsContainer.getSkin();
    
    if (skinEntry.name.equals(currentSkin)) {
      player.sendMessage("§cVocê já está utilizando essa skin!");
      return;
    }
    
    // Aplica a skin usando o sistema de update
    boolean success = minecraft.core.bukkit.listeners.UpdateSkin.updateSkin(
        player, skinEntry.name, skinEntry.value, skinEntry.signature);
    
    if (success) {
      player.sendMessage("§aSkin §f" + skinEntry.name + " §aaplicada com sucesso!");
      player.closeInventory();
    } else {
      player.sendMessage("§cErro ao aplicar a skin. Tente novamente!");
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