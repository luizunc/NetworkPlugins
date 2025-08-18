package minecraft.core.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.bukkit.cmd.Commands;
import minecraft.core.bukkit.config.RolesConfig;
import minecraft.core.bukkit.hook.CoreExpansion;
import minecraft.core.bukkit.hook.protocollib.FakeAdapter;
import minecraft.core.bukkit.hook.protocollib.HologramAdapter;
import minecraft.core.bukkit.hook.protocollib.NPCAdapter;
import minecraft.core.bukkit.listeners.Listeners;
import minecraft.core.bukkit.listeners.PluginMessageListener;
import minecraft.core.bukkit.plugin.KPlugin;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.database.Database;
import minecraft.core.core.libraries.MinecraftVersion;
import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.npclib.NPCLibrary;
import minecraft.core.core.nms.NMS;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.fake.FakeManager;
import minecraft.core.core.player.role.Role;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.titles.Title;
import minecraft.core.core.utils.SlickUpdater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Classe principal do plugin Core para Bukkit/Spigot.
 * Gerencia o sistema completo de perfis, jogos, banco de dados e funcionalidades do servidor.
 * 
 * Copyright (c) 2020-2021 SliceCollections
 * Uma edição de um plugin chamado mCore.
 * Criador: https://github.com/maxteer
 * Source Code (mCore): https://github.com/slicecollections/mCore
 */
@SuppressWarnings("unchecked")
public class Core extends KPlugin {
  
  // Constantes do sistema
  public static final List<String> MINIGAMES = Arrays.asList("Sky Wars", "Bed Wars");
  public static final String PLACEHOLDER_API_VERSION = "2.10.5";
  public static final String BUNGEECORD_CHANNEL = "BungeeCord";
  public static final String CORE_CHANNEL = "Core";
  
  // Estados do sistema
  public static volatile boolean validInit = false;
  public static volatile String minigame = "";
  
  // Instância singleton e localização do lobby
  private static volatile Core instance;
  private static volatile Location lobby;
  
  // Comandos que devem ser removidos por segurança
  private static final String[] COMMANDS_TO_REMOVE = {"rl", "reload", "bukkit:rl", "bukkit:reload"};
  
  /**
   * Obtém a localização do lobby.
   * 
   * @return Localização do lobby
   */
  public static Location getLobby() {
    return lobby;
  }
  
  /**
   * Define a localização do lobby.
   * 
   * @param location Nova localização do lobby
   */
  public static void setLobby(Location location) {
    lobby = location;
  }
  
  /**
   * Obtém a instância singleton do Core.
   * 
   * @return Instância do Core
   */
  public static Core getInstance() {
    return instance;
  }
  
  /**
   * Envia um jogador para um servidor específico via BungeeCord.
   * 
   * @param profile Perfil do jogador
   * @param serverName Nome do servidor de destino
   */
  public static void sendServer(Profile profile, String serverName) {
    if (profile == null || serverName == null || serverName.trim().isEmpty()) {
      return;
    }
    
    if (!getInstance().isEnabled()) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (player == null || !player.isOnline()) {
      return;
    }
    
    Bukkit.getScheduler().runTask(getInstance(), () -> {
      if (player.isOnline()) {
        try {
          player.closeInventory();
          NMS.sendActionBar(player, "");
          player.sendMessage("§aConectando...");
          
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("Connect");
          out.writeUTF(serverName);
          player.sendPluginMessage(getInstance(), BUNGEECORD_CHANNEL, out.toByteArray());
        } catch (Exception e) {
          getInstance().getLogger().log(Level.WARNING, 
              "Erro ao conectar jogador " + player.getName() + " ao servidor " + serverName, e);
        }
      }
    });
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {
    // Método vazio - não há necessidade de carregamento prévio
  }
  
  @Override
  public void enable() {
    try {
      if (!initializeSystem()) {
        return;
      }
      
      setupBasicConfiguration();
      setupDatabase();
      setupLibraries();
      setupSystems();
      setupCommandsAndListeners();
      setupProtocolLib();
      setupPluginChannels();
      scheduleUpdater();
      
      validInit = true;
      this.getLogger().info("O plugin foi ativado com sucesso.");
      
    } catch (Exception e) {
      this.getLogger().log(Level.SEVERE, "Erro crítico ao ativar o plugin", e);
      this.setEnabled(false);
    }
  }
  
  @Override
  public void disable() {
    try {
      if (validInit) {
        saveOnlineProfiles();
        Database.getInstance().close();
      }
      
      processUpdate();
      this.getLogger().info("O plugin foi desativado com sucesso.");
      
    } catch (Exception e) {
      this.getLogger().log(Level.SEVERE, "Erro ao desativar o plugin", e);
    }
  }
  
  /**
   * Inicializa o sistema verificando compatibilidade.
   * 
   * @return true se a inicialização foi bem-sucedida
   */
  private boolean initializeSystem() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("Sua versão não é compatível com o plugin, utilize a versão 1_8_R3 (Atual: " + 
          MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return false;
    }
    
    if (!isPlaceholderAPIVersionValid()) {
      return false;
    }
    
    return true;
  }
  
  /**
   * Configura as configurações básicas do sistema.
   */
  private void setupBasicConfiguration() {
    saveDefaultConfig();
    
    if (Bukkit.getWorlds().isEmpty()) {
      this.getLogger().warning("Nenhum mundo encontrado para definir o lobby!");
      return;
    }
    
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();
    
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }
    
    removeReloadCommands();
          PlaceholderAPI.registerExpansion(new CoreExpansion());
  }
  
  /**
   * Configura o banco de dados.
   */
  private void setupDatabase() {
    KConfig config = getConfig("config");
    
    if (!config.getBoolean("database.enabled", true)) {
      this.getLogger().info("Banco de dados desabilitado na configuração.");
      return;
    }
    
    try {
      Database.setupDatabase(
          config.getString("database.tipo"),
          config.getString("database.mysql.host"),
          config.getString("database.mysql.porta"),
          config.getString("database.mysql.nome"),
          config.getString("database.mysql.usuario"),
          config.getString("database.mysql.senha"),
          config.getBoolean("database.mysql.hikari", false),
          config.getBoolean("database.mysql.mariadb", false),
          config.getString("database.mongodb.url")
      );
      this.getLogger().info("Banco de dados configurado com sucesso.");
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar banco de dados: " + e.getMessage(), e);
      this.getLogger().info("O plugin continuará funcionando sem banco de dados.");
    }
  }
  
  /**
   * Configura as bibliotecas do sistema.
   */
  private void setupLibraries() {
    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);
  }
  
  /**
   * Configura os sistemas principais.
   */
  private void setupSystems() {
    setupRoles();
    FakeManager.setupFake();
    Title.setupTitles();
    ServerItem.setupServers();
    Achievement.setupAchievements();
  }
  
  /**
   * Configura comandos e listeners.
   */
  private void setupCommandsAndListeners() {
    Commands.setupCommands();
    Listeners.setupListeners();
  }
  
  /**
   * Configura o ProtocolLib.
   */
  private void setupProtocolLib() {
    try {
      if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
        this.getLogger().warning("ProtocolLib não encontrado. Algumas funcionalidades podem não funcionar.");
        return;
      }
      
      ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter(this));
      ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter(this));
      ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter(this));
      
      this.getLogger().info("ProtocolLib listeners configurados com sucesso.");
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar ProtocolLib listeners", e);
    }
  }
  
  /**
   * Configura os canais de plugin.
   */
  private void setupPluginChannels() {
    try {
      getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEECORD_CHANNEL);
      getServer().getMessenger().registerOutgoingPluginChannel(this, CORE_CHANNEL);
      getServer().getMessenger().registerIncomingPluginChannel(this, CORE_CHANNEL, new PluginMessageListener());
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar canais de plugin", e);
    }
  }
  
  /**
   * Agenda o atualizador do sistema.
   */
  private void scheduleUpdater() {
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> new SlickUpdater(this, 2).run());
  }
  
  /**
   * Salva os perfis dos jogadores online.
   */
  private void saveOnlineProfiles() {
    Bukkit.getOnlinePlayers().forEach(player -> {
      try {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          this.getLogger().info("Perfil salvo: " + profile.getName());
          profile.destroy();
        }
      } catch (Exception e) {
        this.getLogger().log(Level.WARNING, "Erro ao salvar perfil do jogador " + player.getName(), e);
      }
    });
  }
  
  /**
   * Remove os comandos de reload do servidor por segurança.
   */
  private void removeReloadCommands() {
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass()
          .getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      
      @SuppressWarnings("unchecked")
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
      
      for (String command : COMMANDS_TO_REMOVE) {
        knownCommands.remove(command);
      }
      
    } catch (ReflectiveOperationException e) {
      getLogger().log(Level.SEVERE, "Não foi possível remover comandos de reload", e);
    }
  }
  
  /**
   * Verifica se a versão do PlaceholderAPI é válida.
   * 
   * @return true se a versão é válida
   */
  private boolean isPlaceholderAPIVersionValid() {
    String currentVersion = PlaceholderAPIPlugin.getInstance().getDescription().getVersion();
    if (!PLACEHOLDER_API_VERSION.equals(currentVersion)) {
      Bukkit.getConsoleSender().sendMessage(
          " \n §6§lAVISO IMPORTANTE\n \n " +
          "§7Utilize a versão " + PLACEHOLDER_API_VERSION + " do PlaceHolderAPI, " +
          "você está utilizando a v" + currentVersion + "\n "
      );
      System.exit(0);
      return false;
    }
    return true;
  }
  
  /**
   * Processa atualização do plugin se existir.
   */
  private void processUpdate() {
    File updateFile = new File("plugins/Core/update", "Core.jar");
    if (updateFile.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/Core.jar"));
        this.getFileUtils().copyFile(new FileInputStream(updateFile), new File("plugins/Core.jar"));
        this.getFileUtils().deleteFile(updateFile.getParentFile());
        this.getLogger().info("Update do Core aplicada com sucesso.");
      } catch (Exception e) {
        this.getLogger().log(Level.SEVERE, "Erro ao processar atualização", e);
      }
    }
  }
  
  /**
   * Configura os roles padrão do sistema.
   */
  private void setupRoles() {
    if (Role.listRoles().isEmpty()) {
      try {
        KConfig rolesConfig = RolesConfig.getConfig();
        if (rolesConfig.contains("roles")) {
                  for (String roleName : rolesConfig.getConfigurationSection("roles").getKeys(false)) {
          String name = rolesConfig.getString("roles." + roleName + ".name");
          if (name == null) name = "&7Membro";
          String prefix = rolesConfig.getString("roles." + roleName + ".prefix");
          if (prefix == null) prefix = "&7";
          String permission = rolesConfig.getString("roles." + roleName + ".permission");
          if (permission == null) permission = "";
          boolean alwaysVisible = rolesConfig.getBoolean("roles." + roleName + ".alwaysvisible", false);
          boolean broadcast = rolesConfig.getBoolean("roles." + roleName + ".broadcast", true);
            
            Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast));
          }
        }
      } catch (Exception e) {
        this.getLogger().log(Level.WARNING, "Erro ao carregar roles internos, usando padrão", e);
        Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
      }
    }
  }
}
