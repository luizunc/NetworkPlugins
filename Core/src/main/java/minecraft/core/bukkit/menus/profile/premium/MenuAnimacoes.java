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
 * Menu de animações de chegada do jogador.
 * Permite selecionar e gerenciar animações de chegada disponíveis.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuAnimacoes extends UpdatablePlayerMenu {
  
  // Constantes
  private static final String MENU_TITLE = "Animações de Chegada";
  private static final int MENU_ROWS = 5;
  private static final int UPDATE_INTERVAL = 20; // 1 segundo
  private static final int SLOT_VOLTAR = 40;
  
  // Slots disponíveis para animações
  private static final int[] ANIMATION_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
  
  // Mapa de itens para animações
  private Map<ItemStack, String> animations = new HashMap<>();
  private Profile profile;
  
  /**
   * Construtor do menu de animações de chegada.
   * 
   * @param profile Perfil do jogador
   */
  public MenuAnimacoes(Profile profile) {
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
    
         // Lista de animações disponíveis (ID -> Nome)
     String[] availableAnimations = {
         "Aquaman",
         "Chefão",
         "Estrela",
         "Thor"
     };
    
         for (int i = 0; i < availableAnimations.length; i++) {
       String animation = availableAnimations[i];
       int animationId = i + 1; // IDs começam em 1
       ItemStack item = createAnimationItem(animation, animationId, profile);
       animations.put(item, String.valueOf(animationId));
       
       if (hasAnimation(profile, animationId)) {
         items.add(item);
       } else {
         sub.add(item);
       }
     }
    
    items.addAll(sub);
    
    // Adiciona os itens aos slots disponíveis
    int slotIndex = 0;
    for (ItemStack item : items) {
      if (slotIndex < ANIMATION_SLOTS.length) {
        setItem(ANIMATION_SLOTS[slotIndex], item);
        slotIndex++;
      }
    }
    
    // Limpa as listas
    sub.clear();
    items.clear();
  }
  
     /**
    * Cria um item para uma animação específica.
    * 
    * @param animation Animação de chegada
    * @param animationId ID da animação
    * @param profile Perfil do jogador
    * @return ItemStack do item
    */
   private ItemStack createAnimationItem(String animation, int animationId, Profile profile) {
     boolean hasAnimation = hasAnimation(profile, animationId);
     boolean isSelected = isSelectedAnimation(profile, animationId);
    
         String itemName;
     String material;
     
     String actionText;
     if (!hasAnimation) {
       itemName = "&c" + animation;
       actionText = "&cVocê não possui esta animação.";
       material = "STAINED_GLASS_PANE";
     } else if (isSelected) {
       itemName = "&a" + animation;
       actionText = "&eClique para remover!";
       material = "STAINED_GLASS:5"; // Vidro verde lima
     } else {
       itemName = "&6" + animation;
       actionText = "&eClique para selecionar!";
       material = "STAINED_GLASS"; // Vidro branco
     }
     
     String description = "\n" +
                        (hasAnimation ? "&7Uma animação de chegada especial\n&7que será exibida quando você\n&7entrar no servidor." : "&7Rank Iron+ necessário para usar.") + 
                        "\n \n" + actionText;
    
    return BukkitUtils.deserializeItemStack(
        material + " : 1 : nome>" + itemName + " : desc>" + description
    );
  }
  
     /**
    * Verifica se o jogador possui a animação.
    * 
    * @param profile Perfil do jogador
    * @param animationId ID da animação a verificar
    * @return true se possui a animação
    */
   private boolean hasAnimation(Profile profile, int animationId) {
     // Verifica se o jogador tem rank Iron ou superior
     return hasIronOrHigherRank(profile);
   }
  
     /**
    * Verifica se a animação está selecionada.
    * 
    * @param profile Perfil do jogador
    * @param animationId ID da animação a verificar
    * @return true se está selecionada
    */
   private boolean isSelectedAnimation(Profile profile, int animationId) {
     // Verifica se a animação está salva no perfil do jogador
     String selectedAnimation = profile.getDataContainer("account", "entryanimation").getAsString();
     return String.valueOf(animationId).equals(selectedAnimation) && !selectedAnimation.equals("[]");
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
        handleAnimationClick(item, profile);
        break;
    }
  }
  
  /**
   * Manipula o clique em animações.
   * 
   * @param item Item clicado
   * @param profile Perfil do jogador
   */
     private void handleAnimationClick(ItemStack item, Profile profile) {
     String animationIdStr = animations.get(item);
     if (animationIdStr == null) {
       return;
     }
     
     int animationId = Integer.parseInt(animationIdStr);
     String animationName = getAnimationName(animationId);
     
     if (!hasAnimation(profile, animationId)) {
       EnumSound.ENDERMAN_TELEPORT.play(player, 0.5F, 1.0F);
       player.sendMessage("§cVocê precisa ter rank Iron+ para usar animações de chegada!");
       return;
     }
     
     EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
     
     boolean isSelected = isSelectedAnimation(profile, animationId);
     if (isSelected) {
       // Deseleciona a animação
       deselectAnimation(profile, animationId);
     } else {
       // Seleciona a animação
       selectAnimation(profile, animationId);
     }
     
     new MenuAnimacoes(profile);
   }
  
     /**
    * Seleciona uma animação de chegada.
    * 
    * @param profile Perfil do jogador
    * @param animationId ID da animação a selecionar
    */
   private void selectAnimation(Profile profile, int animationId) {
     // Salva a animação selecionada no perfil do jogador
     profile.getDataContainer("account", "entryanimation").set(String.valueOf(animationId));
     profile.save();
   }
  
     /**
    * Deseleciona uma animação de chegada.
    * 
    * @param profile Perfil do jogador
    * @param animationId ID da animação a deselecionar
    */
   private void deselectAnimation(Profile profile, int animationId) {
     // Remove a animação selecionada do perfil do jogador
     profile.getDataContainer("account", "entryanimation").set("[]");
     profile.save();
   }
  
     /**
    * Obtém o nome da animação pelo ID.
    * 
    * @param animationId ID da animação
    * @return Nome da animação
    */
   private String getAnimationName(int animationId) {
     String[] availableAnimations = {
         "Aquaman",
         "Chefão",
         "Estrela",
         "Thor"
     };
     
     if (animationId > 0 && animationId <= availableAnimations.length) {
       return availableAnimations[animationId - 1];
     }
     return "Animação Desconhecida";
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
    animations.clear();
    animations = null;
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
