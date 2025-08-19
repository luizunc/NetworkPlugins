package minecraft.core.bukkit.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.UpdatablePlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Menu de servidores do jogador.
 * Permite navegar entre diferentes servidores do network.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuServers extends UpdatablePlayerMenu {
  
  // Constantes
  private static final int UPDATE_INTERVAL = 20; // 1 segundo
  private static final String ALREADY_CONNECTED_MESSAGE = "§cVocê já está conectado a este servidor.";
  
  /**
   * Construtor do menu de servidores.
   * 
   * @param profile Perfil do jogador
   */
  public MenuServers(Profile profile) {
    super(profile.getPlayer(), ServerItem.CONFIG.getString("title"), ServerItem.CONFIG.getInt("rows"));
    
    setupMenu();
  }
  
  /**
   * Configura o menu inicial.
   */
  private void setupMenu() {
    update();
    register(Core.getInstance(), UPDATE_INTERVAL);
    open();
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
    
    handleServerClick(evt.getSlot(), profile);
  }
  
  /**
   * Manipula o clique em servidores específicos.
   * 
   * @param slot Slot clicado
   * @param profile Perfil do jogador
   */
  private void handleServerClick(int slot, Profile profile) {
    if (ServerItem.DISABLED_SLOTS.contains(slot)) {
      player.sendMessage(ALREADY_CONNECTED_MESSAGE);
      return;
    }
    
    ServerItem.listServers().stream()
        .filter(serverItem -> serverItem.getSlot() == slot)
        .findFirst()
        .ifPresent(serverItem -> serverItem.connect(profile));
  }
  
  /**
   * Atualiza os itens do menu.
   */
  @Override
  public void update() {
    for (ServerItem serverItem : ServerItem.listServers()) {
      String iconWithPlayers = serverItem.getIcon().replace(
          "{players}", 
          StringUtils.formatNumber(ServerItem.getServerCount(serverItem))
      );
      
      setItem(serverItem.getSlot(), BukkitUtils.deserializeItemStack(iconWithPlayers));
    }
  }
  
  /**
   * Cancela o registro de eventos.
   */
  public void cancel() {
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
