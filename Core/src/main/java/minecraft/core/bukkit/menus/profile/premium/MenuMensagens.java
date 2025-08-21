package minecraft.core.bukkit.menus.profile.premium;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.UpdatablePlayerMenu;
import minecraft.core.bukkit.menus.profile.AreaPremium;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu de mensagens de chegada do jogador.
 * Permite selecionar e gerenciar mensagens de chegada disponíveis.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuMensagens extends UpdatablePlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Mensagens de Chegada";
  private static final int MENU_ROWS = 5;
  private static final int UPDATE_INTERVAL = 20; // 1 segundo
  private static final int SLOT_VOLTAR = 40;
  
  // Slots disponíveis para mensagens
  private static final int[] MESSAGE_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
  
  // Mapa de itens para mensagens
  private Map<ItemStack, String> messages = new HashMap<>();
  private Profile profile;
  
  /**
   * Construtor do menu de mensagens de chegada.
   * 
   * @param profile Perfil do jogador
   */
  public MenuMensagens(Profile profile) {
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
    
         // Lista de mensagens disponíveis (ID -> Nome)
     String[] availableMessages = {
         "§6entrou no lobby!",
         "§aentrou juntamente com brr brr patapim",
         "§4entrou pronto para batalhar",
         "§2§kentrou no lobby",
         "§despalhou doces no lobby",
         "§6tudo deles e nada nosso"
     };
     
     // Lista de títulos personalizados para cada mensagem
     String[] messageTitles = {
         "Padrão",
         "Braintrot",
         "Gladiador",
         "Glitch",
         "Doce",
         "Regresso"
     };
    
         for (int i = 0; i < availableMessages.length; i++) {
       String message = availableMessages[i];
       String title = messageTitles[i];
       int messageId = i + 1; // IDs começam em 1
       ItemStack item = createMessageItem(message, title, messageId, profile);
       messages.put(item, String.valueOf(messageId));
       
       if (hasMessage(profile, messageId)) {
         items.add(item);
       } else {
         sub.add(item);
       }
     }
    
    items.addAll(sub);
    
    // Adiciona os itens aos slots disponíveis
    int slotIndex = 0;
    for (ItemStack item : items) {
      if (slotIndex < MESSAGE_SLOTS.length) {
        setItem(MESSAGE_SLOTS[slotIndex], item);
        slotIndex++;
      }
    }
    
    // Limpa as listas
    sub.clear();
    items.clear();
  }
  
     /**
    * Cria um item para uma mensagem específica.
    * 
    * @param message Mensagem de chegada
    * @param title Título personalizado do item
    * @param messageId ID da mensagem
    * @param profile Perfil do jogador
    * @return ItemStack do item
    */
   private ItemStack createMessageItem(String message, String title, int messageId, Profile profile) {
     boolean hasMessage = hasMessage(profile, messageId);
     boolean isSelected = isSelectedMessage(profile, messageId);
    
         String itemName;
     String material;
     
     String actionText;
     if (!hasMessage) {
       itemName = "&c" + title;
       actionText = "&cVocê não possui esta mensagem.";
       material = "STAINED_GLASS_PANE";
     } else if (isSelected) {
       itemName = "&a" + title; // Verde claro quando selecionado
       actionText = "&eClique para remover!";
       material = "STAINED_GLASS:5"; // Vidro verde lima
     } else {
       itemName = "&6" + title; // Dourado quando não selecionado
       actionText = "&eClique para selecionar!";
       material = "STAINED_GLASS"; // Vidro branco
     }
     
     String description = "&fMensagem: " + message + "\n \n" + 
                        (hasMessage ? "&7Uma mensagem de chegada especial\n&7que será exibida quando você\n&7entrar no servidor." : "&7Rank Iron+ necessário para usar.") + 
                        "\n \n" + actionText;
    
    return BukkitUtils.deserializeItemStack(
        material + " : 1 : nome>" + itemName + " : desc>" + description
    );
  }
  
     /**
    * Verifica se o jogador possui a mensagem.
    * 
    * @param profile Perfil do jogador
    * @param messageId ID da mensagem a verificar
    * @return true se possui a mensagem
    */
   private boolean hasMessage(Profile profile, int messageId) {
     // Verifica se o jogador tem rank Iron ou superior
     return hasIronOrHigherRank(profile);
   }
  
     /**
    * Verifica se a mensagem está selecionada.
    * 
    * @param profile Perfil do jogador
    * @param messageId ID da mensagem a verificar
    * @return true se está selecionada
    */
   private boolean isSelectedMessage(Profile profile, int messageId) {
     // Verifica se a mensagem está salva no perfil do jogador
     String selectedMessage = profile.getDataContainer("account", "entrymessage").getAsString();
     return String.valueOf(messageId).equals(selectedMessage) && !selectedMessage.equals("[]");
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
        new AreaPremium(profile);
        break;
        
      default:
        handleMessageClick(item, profile);
        break;
    }
  }
  
  /**
   * Manipula o clique em mensagens.
   * 
   * @param item Item clicado
   * @param profile Perfil do jogador
   */
     private void handleMessageClick(ItemStack item, Profile profile) {
     String messageIdStr = messages.get(item);
     if (messageIdStr == null) {
       return;
     }
     
     int messageId = Integer.parseInt(messageIdStr);
     String messageName = getMessageName(messageId);
     
     if (!hasMessage(profile, messageId)) {
       EnumSound.ENDERMAN_TELEPORT.play(player, 0.5F, 1.0F);
       player.sendMessage("§cVocê precisa ter rank Iron+ para usar mensagens de chegada!");
       return;
     }
     
     EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
     
     boolean isSelected = isSelectedMessage(profile, messageId);
     if (isSelected) {
       // Deseleciona a mensagem
       deselectMessage(profile, messageId);
     } else {
       // Seleciona a mensagem
       selectMessage(profile, messageId);
     }
     
     new MenuMensagens(profile);
   }
  
     /**
    * Seleciona uma mensagem de chegada.
    * 
    * @param profile Perfil do jogador
    * @param messageId ID da mensagem a selecionar
    */
   private void selectMessage(Profile profile, int messageId) {
     // Salva a mensagem selecionada no perfil do jogador
     profile.getDataContainer("account", "entrymessage").set(String.valueOf(messageId));
     profile.save();
   }
  
     /**
    * Deseleciona uma mensagem de chegada.
    * 
    * @param profile Perfil do jogador
    * @param messageId ID da mensagem a deselecionar
    */
   private void deselectMessage(Profile profile, int messageId) {
     // Remove a mensagem selecionada do perfil do jogador
     profile.getDataContainer("account", "entrymessage").set("0");
     profile.save();
   }
  
     /**
    * Obtém o nome da mensagem pelo ID.
    * 
    * @param messageId ID da mensagem
    * @return Nome da mensagem
    */
   private String getMessageName(int messageId) {
     String[] availableMessages = {
             "§6entrou no lobby!",
             "§aentrou juntamente com brr brr patapim",
             "§4entrou pronto para batalhar",
             "§2§kentrou no lobby",
             "§despalhou doces no lobby",
             "§6tudo deles e nada nosso"
     };
     
     if (messageId > 0 && messageId <= availableMessages.length) {
       return availableMessages[messageId - 1];
     }
     return "Mensagem Desconhecida";
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
    
    // Ranks em ordem (do mais baixo para o mais alto)
    String[] rankOrder = {"membro", "iron", "gold", "emerald", "creator", "builder", "staff", "trial", "mod", "admin"};
    
    // Obtém o rank atual do jogador
    minecraft.core.core.player.role.Rank currentRank = minecraft.core.core.player.role.Rank.getPlayerRank(profile.getPlayer(), true);
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
    messages.clear();
    messages = null;
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
