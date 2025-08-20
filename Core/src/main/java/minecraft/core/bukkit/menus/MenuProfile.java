package minecraft.core.bukkit.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.bukkit.menus.profile.*;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.role.Rank;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Menu de perfil do jogador.
 * Permite visualizar informações pessoais, estatísticas, preferências e mais.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuProfile extends PlayerMenu {
  
  // Constantes
  private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"));
  private static final String MENU_TITLE = "Perfil";
  private static final int MENU_ROWS = 4;
  
  // Slots dos itens
  private static final int SLOT_INFORMACOES = 10;
  private static final int SLOT_ESTATISTICAS = 19;
  private static final int SLOT_PREFERENCIAS = 13;
  private static final int SLOT_TITULOS = 14;
  private static final int SLOT_DESAFIOS = 15;
  
  /**
   * Construtor do menu de perfil.
   * 
   * @param profile Perfil do jogador
   */
  public MenuProfile(Profile profile) {
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
    setItem(SLOT_INFORMACOES, createInformacoesItem(profile));
    setItem(SLOT_ESTATISTICAS, createEstatisticasItem());
    setItem(SLOT_PREFERENCIAS, createPreferenciasItem());
    setItem(SLOT_TITULOS, createTitulosItem());
    setItem(SLOT_DESAFIOS, createDesafiosItem());
  }
  
  /**
   * Cria o item de informações pessoais.
   * 
   * @param profile Perfil do jogador
   * @return ItemStack do item
   */
  private ItemStack createInformacoesItem(Profile profile) {
            String rankName = Rank.getRankByName(profile.getDataContainer("account", "rank").getAsString()).getName();
    String cash = StringUtils.formatNumber(profile.getStats("account", "cash"));
    String created = SDF.format(profile.getDataContainer("account", "created").getAsLong());
    String lastLogin = SDF.format(profile.getDataContainer("account", "lastlogin").getAsLong());
    
            String desc = "&fRank: " + rankName + "\n" +
                  "&fCash: &b" + cash + "\n" +
                  " \n" +
                  "&fCadastrado: &7" + created + "\n" +
                  "&fÚltimo acesso: &7" + lastLogin;
    
    return BukkitUtils.putProfileOnSkull(player, BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : nome>&aCredenciais : desc>" + desc));
  }
  
  /**
   * Cria o item de estatísticas.
   * 
   * @return ItemStack do item
   */
  private ItemStack createEstatisticasItem() {
    return BukkitUtils.deserializeItemStack(
        "PAPER : 1 : nome>&aEstatísticas : desc>&7Visualize as suas estatísticas de\n" +
        "&7cada Minigame do nosso servidor.\n" +
        " \n" +
        "&eClique para ver as estatísticas!");
  }
  
  /**
   * Cria o item de preferências.
   * 
   * @return ItemStack do item
   */
  private ItemStack createPreferenciasItem() {
    return BukkitUtils.deserializeItemStack(
        "REDSTONE_COMPARATOR : 1 : nome>&aPreferências : desc>&7Em nosso servidor você pode personalizar\n" +
        "&7sua experiência de jogo por completo.\n" +
        "&7Personalize várias opções únicas como\n" +
        "&7você desejar!\n" +
        " \n" +
        "&8As opções incluem ativar ou desativar as\n" +
        "&8mensagens privadas, os jogadores e outros.\n" +
        " \n" +
        "&eClique para personalizar as opções!");
  }
  
  /**
   * Cria o item de títulos.
   * 
   * @return ItemStack do item
   */
  private ItemStack createTitulosItem() {
    return BukkitUtils.deserializeItemStack(
        "MAP : 1 : esconder>tudo : nome>&aTítulos : desc>&7Esbanje estilo com um título exclusivo\n" +
        "&7que ficará acima da sua cabeça para\n" +
        "&7os outros jogadores.\n" +
        " \n" +
        "&8Lembrando que você não verá o título,\n" +
        "&8apenas os outros jogadores.\n" +
        " \n" +
        "&eClique para selecionar um título!");
  }
  
  /**
   * Cria o item de desafios.
   * 
   * @return ItemStack do item
   */
  private ItemStack createDesafiosItem() {
    return BukkitUtils.deserializeItemStack(
        "GOLD_INGOT : 1 : nome>&aDesafios : desc>&7Em nosso servidor existe um sistema\n" +
        "&7de &6Desafios &7que se consiste em missões\n" +
        "&7de realização única que lhe garante\n" +
        "&7vários prêmios vitalícios.\n" +
        " \n" +
        "&8Os Prêmios variam entre títulos, coins,\n" +
        "&8ícones de prestígio e outros cosméticos.\n" +
        " \n" +
        "&eClique para ver os desafios!");
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
      case SLOT_INFORMACOES:
        EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
        break;
        
      case SLOT_ESTATISTICAS:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuStatistics(profile);
        break;
        
      case SLOT_PREFERENCIAS:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuPreferences(profile);
        break;
        
      case SLOT_TITULOS:
        EnumSound.CLICK.play(player, 0.5F, 2.0F);
        new MenuTitles(profile);
        break;
        
      case SLOT_DESAFIOS:
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
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getInventory())) {
      cancel();
    }
  }
}
