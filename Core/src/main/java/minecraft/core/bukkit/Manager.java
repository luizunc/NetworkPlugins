package minecraft.core;

import minecraft.core.bungee.Bungee;
import minecraft.core.core.libraries.profile.Mojang;
import minecraft.core.core.player.fake.FakeManager;
import minecraft.core.core.player.role.Rank;
import minecraft.core.core.reflection.Accessors;
import minecraft.core.core.reflection.acessors.MethodAccessor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para gerenciar operações entre Bukkit e BungeeCord
 * Fornece uma interface unificada para operações comuns
 */
public class Manager {
  
  // Estados
  public static volatile boolean BUNGEE = false;
  
  // Instâncias de reflexão
  private static volatile Object PROXY_SERVER;
  private static volatile MethodAccessor GET_NAME;
  private static volatile MethodAccessor GET_PLAYER;
  private static volatile MethodAccessor GET_SPIGOT;
  private static volatile MethodAccessor HAS_PERMISSION;
  private static volatile MethodAccessor SEND_MESSAGE;
  private static volatile MethodAccessor SEND_MESSAGE_COMPONENTS;
  private static volatile MethodAccessor IS_FAKE;
  private static volatile MethodAccessor GET_CURRENT;
  private static volatile MethodAccessor GET_FAKE;
      private static volatile MethodAccessor GET_FAKE_RANK;
  
  // Logger
  private static final Logger LOGGER = Logger.getLogger(Manager.class.getName());
  
  // Constantes
  private static final String SKIN_SEPARATOR = " : ";
  private static final String VALUE_TYPE = "value";
  
  static {
    initializeReflection();
  }
  
  /**
   * Inicializa a reflexão baseada no ambiente (Bukkit ou BungeeCord)
   */
  private static void initializeReflection() {
    try {
      // Tentar inicializar como BungeeCord
      initializeBungeeReflection();
      BUNGEE = true;
    } catch (ClassNotFoundException ignore) {
      try {
        // Inicializar como Bukkit
        initializeBukkitReflection();
        BUNGEE = false;
      } catch (ClassNotFoundException e) {
        LOGGER.log(Level.SEVERE, "Falha ao inicializar reflexão para Bukkit", e);
      }
    }
  }
  
  /**
   * Inicializa reflexão para BungeeCord
   */
  private static void initializeBungeeReflection() throws ClassNotFoundException {
    Class<?> proxyServer = Class.forName("net.md_5.bungee.api.ProxyServer");
    Class<?> proxiedPlayer = Class.forName("net.md_5.bungee.api.connection.ProxiedPlayer");
    Class<?> bungeeMain = Class.forName("minecraft.core.bungee.Bungee");
    
    PROXY_SERVER = Accessors.getMethod(proxyServer, "getInstance").invoke(null);
    GET_NAME = Accessors.getMethod(proxiedPlayer, "getName");
    GET_PLAYER = Accessors.getMethod(proxyServer, "getPlayer", String.class);
    HAS_PERMISSION = Accessors.getMethod(proxiedPlayer, "hasPermission", String.class);
    SEND_MESSAGE_COMPONENTS = Accessors.getMethod(proxiedPlayer, "sendMessage", BaseComponent[].class);
    IS_FAKE = Accessors.getMethod(bungeeMain, "isFake", String.class);
    GET_CURRENT = Accessors.getMethod(bungeeMain, "getCurrent", String.class);
    GET_FAKE = Accessors.getMethod(bungeeMain, "getFake", String.class);
            GET_FAKE_RANK = Accessors.getMethod(bungeeMain, "getRank", String.class);
  }
  
  /**
   * Inicializa reflexão para Bukkit
   */
  private static void initializeBukkitReflection() throws ClassNotFoundException {
    Class<?> player = Class.forName("org.bukkit.entity.Player");
    Class<?> spigot = Class.forName("org.bukkit.entity.Player$Spigot");
    Class<?> fakeManager = Class.forName("minecraft.core.core.player.fake.FakeManager");
    Class<?> profile = Class.forName("minecraft.core.core.player.Profile");
    
    GET_NAME = Accessors.getMethod(player, "getName");
    GET_PLAYER = Accessors.getMethod(profile, "findCached", String.class);
    HAS_PERMISSION = Accessors.getMethod(player, "hasPermission", String.class);
    SEND_MESSAGE = Accessors.getMethod(player, "sendMessage", String.class);
    GET_SPIGOT = Accessors.getMethod(player, "spigot");
    SEND_MESSAGE_COMPONENTS = Accessors.getMethod(spigot, "sendMessage", BaseComponent[].class);
    IS_FAKE = Accessors.getMethod(fakeManager, "isFake", String.class);
    GET_CURRENT = Accessors.getMethod(fakeManager, "getCurrent", String.class);
    GET_FAKE = Accessors.getMethod(fakeManager, "getFake", String.class);
            GET_FAKE_RANK = Accessors.getMethod(fakeManager, "getRank", String.class);
  }
  
  /**
   * Obtém a skin de um jogador
   * 
   * @param player Nome do jogador
   * @param type Tipo de skin (value ou signature)
   * @return Valor da skin ou skin padrão
   */
  public static String getSkin(String player, String type) {
    if (player == null || player.trim().isEmpty()) {
      return getDefaultSkin();
    }
    
    try {
      String uuid = Mojang.getUUID(player);
      if (uuid == null) {
        return getDefaultSkin();
      }
      
      String textures = Mojang.getSkinProperty(uuid);
      if (textures == null) {
        return getDefaultSkin();
      }
      
      String[] parts = textures.split(SKIN_SEPARATOR);
      if (parts.length < 3) {
        return getDefaultSkin();
      }
      
      return VALUE_TYPE.equalsIgnoreCase(type) ? parts[1] : parts[2];
      
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter skin do jogador " + player, e);
      return getDefaultSkin();
    }
  }
  
  /**
   * Obtém a skin padrão baseada no ambiente
   */
  private static String getDefaultSkin() {
    return BUNGEE ? Bungee.STEVE : FakeManager.ALEX;
  }
  
  /**
   * Envia uma mensagem para um jogador
   * 
   * @param player Objeto do jogador (Bukkit Player ou BungeeCord ProxiedPlayer)
   * @param message Mensagem a ser enviada
   */
  public static void sendMessage(Object player, String message) {
    if (player == null || message == null) {
      return;
    }
    
    try {
      if (BUNGEE) {
        sendMessage(player, TextComponent.fromLegacyText(message));
      } else {
        SEND_MESSAGE.invoke(player, message);
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao enviar mensagem para jogador", e);
    }
  }
  
  /**
   * Envia componentes de mensagem para um jogador
   * 
   * @param player Objeto do jogador
   * @param components Componentes da mensagem
   */
  public static void sendMessage(Object player, BaseComponent... components) {
    if (player == null || components == null) {
      return;
    }
    
    try {
      Object target = BUNGEE ? player : GET_SPIGOT.invoke(player);
      SEND_MESSAGE_COMPONENTS.invoke(target, new Object[]{components});
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao enviar componentes de mensagem para jogador", e);
    }
  }
  
  /**
   * Obtém o nome de um jogador
   * 
   * @param player Objeto do jogador
   * @return Nome do jogador ou null se inválido
   */
  public static String getName(Object player) {
    if (player == null) {
      return null;
    }
    
    try {
      return (String) GET_NAME.invoke(player);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter nome do jogador", e);
      return null;
    }
  }
  
  /**
   * Obtém um jogador pelo nome
   * 
   * @param name Nome do jogador
   * @return Objeto do jogador ou null se não encontrado
   */
  public static Object getPlayer(String name) {
    if (name == null || name.trim().isEmpty()) {
      return null;
    }
    
    try {
      return GET_PLAYER.invoke(BUNGEE ? PROXY_SERVER : null, name);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter jogador " + name, e);
      return null;
    }
  }
  
  /**
   * Obtém o nome atual de um jogador (real ou fake)
   * 
   * @param playerName Nome do jogador
   * @return Nome atual do jogador
   */
  public static String getCurrent(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    
    try {
      return (String) GET_CURRENT.invoke(null, playerName);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter nome atual do jogador " + playerName, e);
      return playerName;
    }
  }
  
  /**
   * Obtém o nome fake de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Nome fake ou null se não houver
   */
  public static String getFake(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    
    try {
      return (String) GET_FAKE.invoke(null, playerName);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter nome fake do jogador " + playerName, e);
      return null;
    }
  }
  
  /**
   * Obtém o rank fake de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Rank fake ou null se não houver
   */
  public static Rank getFakeRank(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    
    try {
      return (Rank) GET_FAKE_RANK.invoke(null, playerName);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao obter rank fake do jogador " + playerName, e);
      return null;
    }
  }
  
  /**
   * Verifica se um jogador tem uma permissão
   * 
   * @param player Objeto do jogador
   * @param permission Permissão a ser verificada
   * @return true se o jogador tem a permissão
   */
  public static boolean hasPermission(Object player, String permission) {
    if (player == null || permission == null || permission.trim().isEmpty()) {
      return false;
    }
    
    try {
      return (boolean) HAS_PERMISSION.invoke(player, permission);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao verificar permissão " + permission + " do jogador", e);
      return false;
    }
  }
  
  /**
   * Verifica se um jogador está usando nome fake
   * 
   * @param playerName Nome do jogador
   * @return true se o jogador está usando nome fake
   */
  public static boolean isFake(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return false;
    }
    
    try {
      return (boolean) IS_FAKE.invoke(null, playerName);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao verificar se jogador " + playerName + " está fake", e);
      return false;
    }
  }
}
