package minecraft.core.bukkit.listeners;

import minecraft.core.Manager;
import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.menus.profile.premium.animacoes.EntryAnimationManager;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.PrivateMessages;
import minecraft.core.core.player.enums.ProtectionLobby;
import minecraft.core.core.player.hotbar.HotbarButton;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.reflection.Accessors;
import minecraft.core.core.reflection.acessors.FieldAccessor;
import minecraft.core.core.titles.TitleManager;
import minecraft.core.core.utils.StringUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.WatchdogThread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Classe principal de listeners do sistema Core.
 * Gerencia eventos de jogadores, chat, comandos e interações.
 */
public class Listeners implements Listener {
  
  // Loggers e caches
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("Listeners");
  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
  public static final Set<String> SKINS = new HashSet<>();
  
  // Constantes para reflection
  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());
  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);
  
  // Constantes de tempo
  private static final long LOBBY_PROTECTION_DELAY = 3000L; // 3 segundos
  
  /**
   * Configura os listeners do sistema.
   */
  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
    Bukkit.getPluginManager().registerEvents(new RankListener(), Core.getInstance());
  }
  
  // Eventos de login/logout
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      try {
        Profile.createOrLoadProfile(evt.getName());
      } catch (ProfileLoadException ex) {
        LOGGER.log(Level.SEVERE, "Não foi possível carregar os dados do perfil \"" + evt.getName() + "\": ", ex);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile == null) {
      evt.disallow(PlayerLoginEvent.Result.KICK_OTHER,
          "§cAparentemente o servidor não conseguiu carregar seu Perfil.\n \n" +
          "§cIsso ocorre normalmente quando o servidor ainda está despreparado para receber logins, " +
          "aguarde um pouco e tente novamente.");
      return;
    }
    
    // Aplicar a skin salva
    minecraft.core.core.database.data.container.SkinsContainer container = profile.getSkinsContainer();
    String skinValue = container.getValue();
    String skinSignature = container.getSignature();
    String skinName = container.getSkin();
    
    // Se tem uma skin personalizada salva, aplicar
    if (skinName != null && skinValue != null && skinSignature != null && !skinName.isEmpty() && !skinName.equals("none")) {
      try {
        // Aplicar a skin usando reflection
        Object gameProfile = evt.getPlayer().getClass().getMethod("getProfile").invoke(evt.getPlayer());
        Object properties = gameProfile.getClass().getMethod("getProperties").invoke(gameProfile);
        
        // Limpar propriedades existentes
        properties.getClass().getMethod("clear").invoke(properties);
        
        // Adicionar nova skin
        Object property = Class.forName("com.mojang.authlib.properties.Property").getConstructor(String.class, String.class, String.class)
            .newInstance("textures", skinValue, skinSignature);
        properties.getClass().getMethod("put", Object.class, Object.class).invoke(properties, "textures", property);
        
      } catch (Exception e) {
        Core.getInstance().getLogger().warning("Erro ao aplicar skin para " + evt.getPlayer().getName() + ": " + e.getMessage());
      }
    } else {
      // Jogador não tem skin personalizada
      // Verificar se é jogador pirata e aplicar skin padrão
      applyDefaultSkinForCracked(evt.getPlayer());
    }
    
    profile.setPlayer(evt.getPlayer());
  }
  
  /**
   * Aplica skin padrão para jogadores piratas
   */
  private void applyDefaultSkinForCracked(Player player) {
    try {
      // Verificar se o jogador é original (premium) ou pirata
      if (player.isOnline() && !isPlayerCracked(player)) {
        // Jogador original - manter skin do Minecraft
        return;
      }
      
      // Jogador pirata - aplicar skin padrão
      // DEFINA AQUI A SKIN PADRÃO (substitua pelos valores desejados)
      String defaultSkinValue = "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM4MzM4NjEwOCwKICAicHJvZmlsZUlkIiA6ICIzYTNmNzhkZmExZjQ0OTllYjE5NjlmYzlkOTEwZGYwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb19jcmVyYXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJkMWI3YTE2MWQ4NDdlY2M3MmEzNTk1NzU4MTNlYTExOTJiZWQ4Yjc4ZWMzMjVkYzVhYTM0ZGFlYjg4NmE2OCIKICAgIH0KICB9Cn0=";
      String defaultSkinSignature = "pIJBaRYN5ECaVn2h9VrU6fLgww1vFDXhakwUUKcIJ1NE8eUmz8M9+XR3IsTQkzk/Skh7qC5TeE9Ono8skZ/1fOE6qxoAG+ZQNbMZ3k1VJFA2D2wL9msvIgfxgKLhzfm9UAIx2vxh9QEx2/rFtMcTNmKWkcaig4dXF/060LbghHDd1WaUa0TdnYhuPdvEdKS1Jo5T8K0Bq3FhOcs36z4OKB7n3JF11uXzxh8GpsZf1sKHHjTLZY5woCFzHtK5zAE50aKgNgbixNEzSaAXTUktlbcWS3RSU8mGLu4rzEKEwSv//04UkuRpmNo6TvsAAGA8h+6lptOO5LtVYW5ddaqGGtQqrtVFMnti0hIiXdQsDmDA28x3bqKjoG56UYzbiw4tjuBVGHqZC2FW1Sq7Eah/sZzmEwDmsDuKWn02qSjsv6r3QDBBXAEGJzPcqmk6g9O++tmaFoDPTLVfRJgdSep/QXjG7NtAAI/7T+319ZwLyeKGVqbgsxUwcOEo6qH+w3a6EBdSxbM0gNp3w1u1e3Eb9q02VXPp5Jx0oJaQFeqRmmmY+vN0J0CAEZ0F9x0VK0kROmJndRSmJFtvlF1+pW9zMf6Um6Rfv+6BFh+caiOT8T5flh9+E/3Zyz+mb4Qjm/2gHcBHyawjG1wCjuhqQvR+HWBFmbPWG3HGyrW+tnpHwwc=";
      
      // Aplicar a skin padrão
      Object gameProfile = player.getClass().getMethod("getProfile").invoke(player);
      Object properties = gameProfile.getClass().getMethod("getProperties").invoke(gameProfile);
      
      // Limpar propriedades existentes
      properties.getClass().getMethod("clear").invoke(properties);
      
      // Adicionar skin padrão
      Object property = Class.forName("com.mojang.authlib.properties.Property").getConstructor(String.class, String.class, String.class)
          .newInstance("textures", defaultSkinValue, defaultSkinSignature);
      properties.getClass().getMethod("put", Object.class, Object.class).invoke(properties, "textures", property);
      
      Core.getInstance().getLogger().info("Skin padrão aplicada para jogador pirata: " + player.getName());
      
    } catch (Exception e) {
      Core.getInstance().getLogger().warning("Erro ao aplicar skin padrão para " + player.getName() + ": " + e.getMessage());
    }
  }
  
  /**
   * Verifica se o jogador é pirata (não original)
   */
  private boolean isPlayerCracked(Player player) {
    try {
      // Obtém o GameProfile do jogador
      Object gameProfile = player.getClass().getMethod("getProfile").invoke(player);
      Object uuid = gameProfile.getClass().getMethod("getId").invoke(gameProfile);
      
      // Se o UUID for null ou vazio, é jogador pirata
      if (uuid == null) {
        return true;
      }
      
      // Verifica se o UUID é de jogador offline (pirata)
      // UUIDs de jogadores piratas seguem um padrão específico
      String uuidString = uuid.toString();
      return uuidString.startsWith("00000000-0000-0000");
      
    } catch (Exception e) {
      // Em caso de erro, assume que é pirata para aplicar skin padrão
      return true;
    }
  }



  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      // Aplicar a tag selecionada no tab
      minecraft.core.core.utils.TagUtils.setTag(player, "", "", 0);
      
      // Executa a animação de chegada após 1 segundo
      Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
        EntryAnimationManager.playEntryAnimation(player, profile);
      }, 20L);
    }
  }
  

  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    handlePlayerQuit(evt.getPlayer());
  }
  
  /**
   * Gerencia o logout de um jogador.
   * 
   * @param player Jogador que saiu
   */
  private void handlePlayerQuit(Player player) {
    // Cancela a animação de entrada se estiver ativa
    EntryAnimationManager.cancelPlayerAnimation(player);
    
    Profile profile = Profile.unloadProfile(player.getName());
    if (profile != null) {
      handleProfileCleanup(profile);
    }
    
    cleanupPlayerData(player);
  }
  
  /**
   * Limpa os dados do perfil do jogador.
   * 
   * @param profile Perfil do jogador
   */
  private void handleProfileCleanup(Profile profile) {
    // Remove do jogo se estiver em um
    if (profile.getGame() != null) {
      profile.getGame().leave(profile, profile.getGame());
    }
    
    TitleManager.leaveServer(profile);
    
    // Salva o perfil baseado no estado do servidor
    saveProfileBasedOnServerState(profile);
    
    profile.destroy();
  }
  
  /**
   * Salva o perfil baseado no estado do servidor.
   * 
   * @param profile Perfil a ser salvo
   */
  private void saveProfileBasedOnServerState(Profile profile) {
    boolean isServerStopping = !((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning() || 
                              RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null));
    
    if (isServerStopping) {
      // Servidor parando - salva de forma síncrona
      profile.saveSync();
      Core.getInstance().getLogger().info("O jogador " + profile.getName() + " foi salvo!");
    } else {
      // Servidor rodando - salva de forma assíncrona
      profile.save();
    }
  }
  
  /**
   * Limpa dados temporários do jogador.
   * 
   * @param player Jogador
   */
  private void cleanupPlayerData(Player player) {
    String playerName = player.getName();

    DELAY_PLAYERS.remove(playerName);
    PROTECTION_LOBBY.remove(playerName.toLowerCase());


  }
  
  // Eventos de chat
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    handleChatMessage(evt);
  }
  
  /**
   * Processa mensagens de chat com formatação personalizada.
   * 
   * @param evt Evento de chat
   */
  private void handleChatMessage(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    String format = String.format(evt.getFormat(), player.getName(), evt.getMessage());
    
    String currentName = Manager.getCurrent(player.getName());
            Rank rank = Rank.getSelectedTag(player);
    
            TextComponent component = createChatComponent(format, currentName, rank);
    
    evt.setCancelled(true);
    evt.getRecipients().forEach(recipient -> {
      if (recipient != null) {
        recipient.spigot().sendMessage(component);
      }
    });
  }
  
  /**
   * Cria componente de chat com interatividade.
   * 
   * @param format Formato da mensagem
   * @param playerName Nome do jogador
   * @param role Role do jogador
   * @return TextComponent formatado
   */
      private TextComponent createChatComponent(String format, String playerName, Rank rank) {
    TextComponent component = new TextComponent("");
    
    for (BaseComponent comp : TextComponent.fromLegacyText(format)) {
      comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + playerName + " "));
      comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                  TextComponent.fromLegacyText(StringUtils.getLastColor(rank.getPrefix()) + playerName +
            "\n§fGrupo: " + rank.getName() + "\n \n§eClique para enviar uma mensagem privada.")));
      component.addExtra(comp);
    }
    
    return component;
  }
  
  // Eventos de comandos
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    handleCommandPreprocess(evt);
  }
  
  /**
   * Processa comandos antes da execução.
   * 
   * @param evt Evento de comando
   */
  private void handleCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile == null) {
      return;
    }
    
    String[] args = evt.getMessage().replace("/", "").split(" ");
    if (args.length == 0) {
      return;
    }
    
    String command = args[0];
    
    // Verifica proteção do lobby
    if (isLobbyCommand(command) && hasLobbyProtection(profile)) {
      handleLobbyProtection(evt, player);
    }
    
    // Verifica mensagens privadas
    if (isTellCommand(command, args)) {
      handleTellCommand(evt, player, args[1]);
    }
  }
  
  /**
   * Verifica se é o comando lobby.
   * 
   * @param command Comando executado
   * @return true se for comando lobby
   */
  private boolean isLobbyCommand(String command) {
    return "lobby".equals(command) && COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby");
  }
  
  /**
   * Verifica se o jogador tem proteção de lobby ativada.
   * 
   * @param profile Perfil do jogador
   * @return true se tem proteção ativada
   */
  private boolean hasLobbyProtection(Profile profile) {
    return profile.getPreferencesContainer().getProtectionLobby() == ProtectionLobby.ATIVADO;
  }
  
  /**
   * Gerencia a proteção do comando lobby.
   * 
   * @param evt Evento de comando
   * @param player Jogador
   */
  private void handleLobbyProtection(PlayerCommandPreprocessEvent evt, Player player) {
    String playerName = player.getName().toLowerCase();
    long lastAttempt = PROTECTION_LOBBY.getOrDefault(playerName, 0L);
    
    if (lastAttempt > System.currentTimeMillis()) {
      PROTECTION_LOBBY.remove(playerName);
      return;
    }
    
    evt.setCancelled(true);
    PROTECTION_LOBBY.put(playerName, System.currentTimeMillis() + LOBBY_PROTECTION_DELAY);
    player.sendMessage("§aVocê tem certeza? Utilize /lobby novamente para voltar ao lobby.");
  }
  
  /**
   * Verifica se é comando tell.
   * 
   * @param command Comando executado
   * @param args Argumentos do comando
   * @return true se for comando tell
   */
  private boolean isTellCommand(String command, String[] args) {
    return "tell".equals(command) && args.length > 1 && 
           COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("tell");
  }
  
  /**
   * Gerencia comando tell com verificação de mensagens privadas.
   * 
   * @param evt Evento de comando
   * @param player Jogador que enviou
   * @param targetName Nome do alvo
   */
  private void handleTellCommand(PlayerCommandPreprocessEvent evt, Player player, String targetName) {
    if (targetName.equalsIgnoreCase(player.getName())) {
      return;
    }
    
    Profile targetProfile = Profile.getProfile(targetName);
    if (targetProfile != null && 
        targetProfile.getPreferencesContainer().getPrivateMessages() != PrivateMessages.TODOS) {
      evt.setCancelled(true);
      player.sendMessage("§cEste usuário desativou as mensagens privadas.");
    }
  }
  
  // Eventos de interação
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteract(PlayerInteractEvent evt) {
    handlePlayerInteract(evt);
  }
  
  /**
   * Gerencia interações do jogador com itens da hotbar.
   * 
   * @param evt Evento de interação
   */
  private void handlePlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile == null || profile.getHotbar() == null) {
      return;
    }
    
    ItemStack item = player.getItemInHand();
    
    // Aceita tanto cliques com botão esquerdo quanto direito
    if ((evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK || 
         evt.getAction() == Action.LEFT_CLICK_AIR || evt.getAction() == Action.LEFT_CLICK_BLOCK) && 
        item != null && item.hasItemMeta()) {
      HotbarButton button = profile.getHotbar().compareButton(player, item);
      if (button != null) {
        evt.setCancelled(true);
        button.getAction().execute(profile);
        return; // Retorna imediatamente para evitar processamento adicional
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    // Não cancela mais interações com ArmorStands, pois agora eles seguem o jogador
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent evt) {
    // Não cancela mais interações com ArmorStands, pois agora eles seguem o jogador
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      handleInventoryClick(evt);
    }
  }
  
  /**
   * Gerencia cliques no inventário para itens da hotbar.
   * 
   * @param evt Evento de clique no inventário
   */
  private void handleInventoryClick(InventoryClickEvent evt) {
    Player player = (Player) evt.getWhoClicked();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile == null || profile.getHotbar() == null) {
      return;
    }
    
    ItemStack item = evt.getCurrentItem();
    if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
      return;
    }
    
    // Aceita tanto cliques com botão esquerdo quanto direito no inventário
    if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(player.getInventory())) {
      HotbarButton button = profile.getHotbar().compareButton(player, item);
      if (button != null) {
        evt.setCancelled(true);
        button.getAction().execute(profile);
      }
    }
  }
}
