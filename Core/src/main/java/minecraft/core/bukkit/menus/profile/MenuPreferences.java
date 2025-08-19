package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.core.database.data.container.PreferencesContainer;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.BloodAndGore;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.enums.PrivateMessages;
import minecraft.core.core.player.enums.ProtectionLobby;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Menu de preferências do jogador.
 * Permite configurar visibilidade de jogadores, mensagens privadas e outras opções.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuPreferences extends PlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Preferências";
  private static final int MENU_ROWS = 5;
  
  // Slots dos itens
  private static final int SLOT_JOGADORES_INFO = 11;
  private static final int SLOT_JOGADORES_TOGGLE = 20;
  private static final int SLOT_MSG_PRIVADAS_INFO = 12;
  private static final int SLOT_MSG_PRIVADAS_TOGGLE = 21;
  private static final int SLOT_VIOLENCIA_INFO = 14;
  private static final int SLOT_VIOLENCIA_TOGGLE = 23;
  private static final int SLOT_PROTECAO_INFO = 15;
  private static final int SLOT_PROTECAO_TOGGLE = 24;
  private static final int SLOT_VOLTAR = 40;
  
  /**
   * Construtor do menu de preferências.
   * 
   * @param profile Perfil do jogador
   */
  public MenuPreferences(Profile profile) {
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
    PreferencesContainer pc = profile.getPreferencesContainer();
    
    // Jogadores
    setItem(SLOT_JOGADORES_INFO, createJogadoresInfoItem());
    setItem(SLOT_JOGADORES_TOGGLE, createToggleItem(pc.getPlayerVisibility()));
    
    // Mensagens Privadas
    setItem(SLOT_MSG_PRIVADAS_INFO, createMsgPrivadasInfoItem());
    setItem(SLOT_MSG_PRIVADAS_TOGGLE, createToggleItem(pc.getPrivateMessages()));
    
    // Violência
    setItem(SLOT_VIOLENCIA_INFO, createViolenciaInfoItem());
    setItem(SLOT_VIOLENCIA_TOGGLE, createToggleItem(pc.getBloodAndGore()));
    
    // Proteção Lobby
    setItem(SLOT_PROTECAO_INFO, createProtecaoInfoItem());
    setItem(SLOT_PROTECAO_TOGGLE, createToggleItem(pc.getProtectionLobby()));
    
    // Voltar
    setItem(SLOT_VOLTAR, createVoltarItem());
  }
  
  /**
   * Cria o item de informação sobre jogadores.
   * 
   * @return ItemStack do item
   */
  private ItemStack createJogadoresInfoItem() {
    return BukkitUtils.deserializeItemStack(
        "347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n" +
        "&7jogadores no lobby.");
  }
  
  /**
   * Cria o item de informação sobre mensagens privadas.
   * 
   * @return ItemStack do item
   */
  private ItemStack createMsgPrivadasInfoItem() {
    return BukkitUtils.deserializeItemStack(
        "PAPER : 1 : nome>&aMensagens privadas : desc>&7Ative ou desative as mensagens\n" +
        "&7enviadas através do tell.");
  }
  
  /**
   * Cria o item de informação sobre violência.
   * 
   * @return ItemStack do item
   */
  private ItemStack createViolenciaInfoItem() {
    return BukkitUtils.deserializeItemStack(
        "REDSTONE : 1 : nome>&aViolência : desc>&7Ative ou desative as partículas\n" +
        "&7de sangue no PvP.");
  }
  
  /**
   * Cria o item de informação sobre proteção no lobby.
   * 
   * @return ItemStack do item
   */
  private ItemStack createProtecaoInfoItem() {
    return BukkitUtils.deserializeItemStack(
        "NETHER_STAR : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido de\n" +
        "&7confirmação ao utilizar /lobby.");
  }
  
  /**
   * Cria um item de toggle genérico.
   * 
   * @param enumValue Enum com informações do toggle
   * @return ItemStack do item
   */
  private ItemStack createToggleItem(Enum<?> enumValue) {
    String displayName = "";
    String inkSack = "";
    
    if (enumValue instanceof PlayerVisibility) {
      PlayerVisibility pv = (PlayerVisibility) enumValue;
      displayName = pv.getName();
      inkSack = pv.getInkSack();
    } else if (enumValue instanceof PrivateMessages) {
      PrivateMessages pm = (PrivateMessages) enumValue;
      displayName = pm.getName();
      inkSack = pm.getInkSack();
    } else if (enumValue instanceof BloodAndGore) {
      BloodAndGore bg = (BloodAndGore) enumValue;
      displayName = bg.getName();
      inkSack = bg.getInkSack();
    } else if (enumValue instanceof ProtectionLobby) {
      ProtectionLobby pl = (ProtectionLobby) enumValue;
      displayName = pl.getName();
      inkSack = pl.getInkSack();
    }
    
    return BukkitUtils.deserializeItemStack(
        "INK_SACK:" + inkSack + " : 1 : nome>" + displayName + " : desc>&fEstado: &7" + 
        StringUtils.stripColors(displayName) + "\n" +
        " \n" +
        "&eClique para modificar!");
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
      case SLOT_JOGADORES_INFO:
      case SLOT_MSG_PRIVADAS_INFO:
      case SLOT_VIOLENCIA_INFO:
      case SLOT_PROTECAO_INFO:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        break;
        
      case SLOT_JOGADORES_TOGGLE:
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        profile.getPreferencesContainer().changePlayerVisibility();
        if (!profile.playingGame()) {
          profile.refreshPlayers();
        }
        new MenuPreferences(profile);
        break;
        
      case SLOT_MSG_PRIVADAS_TOGGLE:
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        profile.getPreferencesContainer().changePrivateMessages();
        new MenuPreferences(profile);
        break;
        
      case SLOT_VIOLENCIA_TOGGLE:
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        profile.getPreferencesContainer().changeBloodAndGore();
        new MenuPreferences(profile);
        break;
        
      case SLOT_PROTECAO_TOGGLE:
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        profile.getPreferencesContainer().changeProtectionLobby();
        new MenuPreferences(profile);
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
