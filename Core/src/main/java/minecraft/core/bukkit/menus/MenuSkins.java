package minecraft.core.bukkit.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.core.database.data.container.SkinsContainer;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


/**
 * Menu de skins do jogador.
 * Permite acessar diferentes funcionalidades relacionadas a skins.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuSkins extends PlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Skins";
  private static final int MENU_ROWS = 4;
  
  // Slots dos itens principais (baseado na imagem)
  private static final int SLOT_BOOKSHELF = 14; // Segunda linha, sexta coluna (slot 14)
  private static final int SLOT_PLAYER_HEAD = 12; // Segunda linha, quarta coluna (slot 12)
  private static final int SLOT_PAINT = 31; // Terceira linha, terceira coluna (slot 21) - Personalização
  private static final int SLOT_BOOK = 32; // Terceira linha, quarta coluna (slot 22) - Histórico
  private static final int SLOT_ARROW = 30; // Quarta linha, terceira coluna (slot 30) - Fechar menu
  
  private final Profile profile;
  
  /**
   * Construtor do menu de skins.
   * 
   * @param player Jogador que abrirá o menu
   * @param profile Perfil do jogador
   */
  public MenuSkins(Player player, Profile profile) {
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
    // Item principal: BOOKSHELF (estante de livros)
    setItem(SLOT_BOOKSHELF, createBookshelfItem());
    
    // Item: Cabeça do jogador (skin atual)
    setItem(SLOT_PLAYER_HEAD, createPlayerHeadItem());
    
    // Item: PAINT (pintura/cores)
    setItem(SLOT_PAINT, createPaintItem());
    
    // Item: BOOK (livro)
    setItem(SLOT_BOOK, createBookItem());
    
    // Item: ARROW (seta para fechar menu)
    setItem(SLOT_ARROW, createArrowItem());
    
    // Adiciona bordas decorativas (opcional)
    setupDecorativeBorders();
  }
  
  /**
   * Cria o item BOOKSHELF (estante de livros).
   * 
   * @return ItemStack do item
   */
  private ItemStack createBookshelfItem() {
    return BukkitUtils.deserializeItemStack(
        "BOOKSHELF : 1 : nome>&aEstante de Skins : desc>&7Acesse nossa biblioteca completa\n" +
        "&7de skins disponíveis no servidor.\n" +
        " \n" +
        "&eClique para explorar!");
  }
  
  /**
   * Cria o item da cabeça do jogador (skin atual).
   * 
   * @return ItemStack do item
   */
  private ItemStack createPlayerHeadItem() {
    SkinsContainer skinsContainer = profile.getSkinsContainer();
    String currentSkin = skinsContainer.getSkin();
    
    String desc;
    if (currentSkin != null && !currentSkin.isEmpty()) {
      Long appliedAt = skinsContainer.getAppliedAt();
      String dateStr;
      
      if (appliedAt != null) {
        // Formata a data de quando foi aplicada
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", new java.util.Locale("pt", "BR"));
        dateStr = sdf.format(new java.util.Date(appliedAt));
      } else {
        dateStr = "Indefinida";
      }
      
      desc = "&fNome: &7" + currentSkin + "\n" +
             "&fAdicionada: &7" + dateStr;
    } else {
      desc = "&7Você ainda não possui uma skin personalizada.\n" +
             "&7Use &e/skin (jogador) &7para aplicar uma skin.";
    }
    
    return BukkitUtils.putProfileOnSkull(player, BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : nome>&aMinha Skin : desc>" + desc));
  }
  
  /**
   * Cria o item PAINT (pintura/cores).
   * 
   * @return ItemStack do item
   */
  private ItemStack createPaintItem() {
    return BukkitUtils.deserializeItemStack(
        "INK_SACK:12 : 1 : nome>&aPersonalização : desc>&7Personalize cores e detalhes\n" +
        "&7da sua skin atual.\n" +
        " \n" +
        "&eClique para customizar!");
  }
  
  /**
   * Cria o item BOOK (livro).
   * 
   * @return ItemStack do item
   */
  private ItemStack createBookItem() {
    return BukkitUtils.deserializeItemStack(
        "BOOK : 1 : nome>&aHistórico de Skins : desc>&7Visualize todas as skins que você\n" +
        "&7já utilizou no servidor.\n" +
        " \n" +
        "&eClique para ver histórico!");
  }
  
  /**
   * Cria o item ARROW (seta para fechar menu).
   * 
   * @return ItemStack do item
   */
  private ItemStack createArrowItem() {
    return BukkitUtils.deserializeItemStack(
        "ARROW : 1 : nome>&cVoltar");
  }
  
  /**
   * Configura bordas decorativas opcionais.
   */
  private void setupDecorativeBorders() {
    // Você pode adicionar bordas decorativas aqui se desejar
    // Por exemplo, vidro colorido nas bordas
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
    switch (slot) {
      case SLOT_BOOKSHELF:
        // TODO: Implementar menu de estante de skins
        player.sendMessage("§aEstante de Skins - Em desenvolvimento!");
        break;
        
      case SLOT_PLAYER_HEAD:
        // TODO: Implementar menu de personalização da skin atual
        break;
        
      case SLOT_PAINT:
        // TODO: Implementar menu de cores e personalização
        player.sendMessage("§aPersonalização de Cores - Em desenvolvimento!");
        break;
        
      case SLOT_BOOK:
        // Abre o menu de histórico de skins
        HandlerList.unregisterAll(this);
        new MenuHistoricoSkins(player, profile);
        break;
        
      case SLOT_ARROW:
        // Fecha o menu
        player.closeInventory();
        break;
        
      default:
        break;
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