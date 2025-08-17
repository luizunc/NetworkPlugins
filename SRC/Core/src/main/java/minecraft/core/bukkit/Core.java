package minecraft.core.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.bukkit.cmd.Commands;
import minecraft.core.bukkit.hook.KCoreExpansion;
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
 * Classe principal do plugin Core para Bukkit/Spigot
 * 
 * Copyright (c) 2020-2021 SliceCollections
 * Uma edição de um plugin chamado mCore.
 * Criador: https://github.com/maxteer
 * Source Code (mCore): https://github.com/slicecollections/mCore
 */
@SuppressWarnings("unchecked")
public class Core extends KPlugin {
  
  // Constantes
  public static final List<String> MINIGAMES = Arrays.asList("Sky Wars", "The Bridge", "Murder", "Bed Wars", "Build Battle");
  public static final String PLACEHOLDER_API_VERSION = "2.10.5";
  public static final String BUNGEECORD_CHANNEL = "BungeeCord";
  public static final String KCORE_CHANNEL = "kCore";
  
  // Estados
  public static volatile boolean validInit = false;
  public static volatile String minigame = "";
  
  // Instância singleton
  private static volatile Core instance;
  private static volatile Location lobby;
  
  // Getters e Setters
  public static Location getLobby() {
    return lobby;
  }
  
  public static void setLobby(Location location) {
    lobby = location;
  }
  
  public static Core getInstance() {
    return instance;
  }
  
  /**
   * Envia um jogador para um servidor específico via BungeeCord
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
          getInstance().getLogger().log(Level.WARNING, "Erro ao conectar jogador " + player.getName() + " ao servidor " + serverName, e);
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
      // Verificar compatibilidade do NMS
      if (!NMS.setupNMS()) {
        this.setEnabled(false);
        this.getLogger().warning("Sua versão não é compatível com o plugin, utilize a versão 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
        return;
      }
      
      // Salvar configuração padrão
      saveDefaultConfig();
      
      // Configurar lobby
      if (Bukkit.getWorlds().isEmpty()) {
        this.getLogger().warning("Nenhum mundo encontrado para definir o lobby!");
        return;
      }
      lobby = Bukkit.getWorlds().get(0).getSpawnLocation();
      
      // Remover proteção de spawn
      if (Bukkit.getSpawnRadius() != 0) {
        Bukkit.setSpawnRadius(0);
      }
      
      // Remover comandos de reload
      removeReloadCommands();
      
      // Verificar versão do PlaceholderAPI
      if (!isPlaceholderAPIVersionValid()) {
        return;
      }
      
      // Registrar expansão do PlaceholderAPI
      PlaceholderAPI.registerExpansion(new KCoreExpansion());
      
      // Configurar banco de dados
      setupDatabase();
      
      // Configurar bibliotecas
      NPCLibrary.setupNPCs(this);
      HologramLibrary.setupHolograms(this);
      
      // Configurar sistemas
      setupRoles();
      FakeManager.setupFake();
      Title.setupTitles();
      ServerItem.setupServers();
      Achievement.setupAchievements();
      
      // Configurar comandos e listeners
      Commands.setupCommands();
      Listeners.setupListeners();
      
      // Configurar ProtocolLib
      setupProtocolLib();
      
      // Configurar canais de plugin
      setupPluginChannels();
      
      // Agendar atualizador
      Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> new SlickUpdater(this, 2).run());
      
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
        // Salvar perfis dos jogadores online
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
        
        // Fechar conexão com banco de dados
        Database.getInstance().close();
      }
      
      // Processar atualização se existir
      processUpdate();
      
      this.getLogger().info("O plugin foi desativado com sucesso.");
      
    } catch (Exception e) {
      this.getLogger().log(Level.SEVERE, "Erro ao desativar o plugin", e);
    }
  }
  
  /**
   * Remove os comandos de reload do servidor
   */
  private void removeReloadCommands() {
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass()
          .getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      
      @SuppressWarnings("unchecked")
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
      
      // Comandos a serem removidos
      String[] commandsToRemove = {"rl", "reload", "bukkit:rl", "bukkit:reload"};
      for (String command : commandsToRemove) {
        knownCommands.remove(command);
      }
      
    } catch (ReflectiveOperationException e) {
      getLogger().log(Level.SEVERE, "Não foi possível remover comandos de reload", e);
    }
  }
  
  /**
   * Verifica se a versão do PlaceholderAPI é válida
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
   * Configura o banco de dados
   */
  private void setupDatabase() {
    KConfig config = getConfig("config");
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
  }
  
  /**
   * Configura os listeners do ProtocolLib
   */
  private void setupProtocolLib() {
    try {
      ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
      ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
      ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar ProtocolLib listeners", e);
    }
  }
  
  /**
   * Configura os canais de plugin
   */
  private void setupPluginChannels() {
    try {
      getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEECORD_CHANNEL);
      getServer().getMessenger().registerOutgoingPluginChannel(this, KCORE_CHANNEL);
      getServer().getMessenger().registerIncomingPluginChannel(this, KCORE_CHANNEL, new PluginMessageListener());
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar canais de plugin", e);
    }
  }
  
  /**
   * Processa atualização do plugin se existir
   */
  private void processUpdate() {
    File updateFile = new File("plugins/kCore/update", "kCore.jar");
    if (updateFile.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/kCore.jar"));
        this.getFileUtils().copyFile(new FileInputStream(updateFile), new File("plugins/kCore.jar"));
        this.getFileUtils().deleteFile(updateFile.getParentFile());
        this.getLogger().info("Update do kCore aplicada com sucesso.");
      } catch (Exception e) {
        this.getLogger().log(Level.SEVERE, "Erro ao processar atualização", e);
      }
    }
  }
  
  /**
   * Configura os roles padrão
   */
  private void setupRoles() {
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
    }
  }
}
