package minecraft.core.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.bukkit.cmd.Commands;
import minecraft.core.core.database.Database;
import minecraft.core.bukkit.hook.KCoreExpansion;
import minecraft.core.bukkit.hook.protocollib.FakeAdapter;
import minecraft.core.bukkit.hook.protocollib.HologramAdapter;
import minecraft.core.bukkit.hook.protocollib.NPCAdapter;
import minecraft.core.core.libraries.MinecraftVersion;
import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.npclib.NPCLibrary;
import minecraft.core.bukkit.listeners.Listeners;
import minecraft.core.bukkit.listeners.PluginMessageListener;
import minecraft.core.core.nms.NMS;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.fake.FakeManager;
import minecraft.core.core.player.role.Role;
import minecraft.core.bukkit.plugin.KPlugin;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.titles.Title;
import minecraft.core.core.utils.SlickUpdater;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class Core extends KPlugin {
  

  public static final List<String> minigames = Arrays.asList("Sky Wars", "The Bridge", "Murder", "Bed Wars", "Build Battle");
  
  public static boolean validInit;
  public static String minigame = "";
  
  /**
   * Copyright (c) 2020-2021 SliceCollections
   * Uma edição de um plugin chamado mCore.
   * Criador: https://github.com/maxteer
   * Source Code (mCore): https://github.com/slicecollections/mCore
   */
  
  private static Core instance;
  private static Location lobby;
  
  public static Location getLobby() {
    return lobby;
  }
  
  public static void setLobby(Location location) {
    lobby = location;
  }
  
  public static Core getInstance() {
    return instance;
  }
  
  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (player != null) {
      Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
        if (player.isOnline()) {
          player.closeInventory();
          NMS.sendActionBar(player, "");
          player.sendMessage("§aConectando...");
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("Connect");
          out.writeUTF(name);
          player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
        }
      });
    }
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {}
  
  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("Sua versao nao e compativel com o plugin, utilize a versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
    
    saveDefaultConfig();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();
    
    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }
    

    
    // Remover /reload
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
      knownCommands.remove("rl");
      knownCommands.remove("reload");
      knownCommands.remove("bukkit:rl");
      knownCommands.remove("bukkit:reload");
    } catch (ReflectiveOperationException ex) {
      getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
    }
    
    if (!PlaceholderAPIPlugin.getInstance().getDescription().getVersion().equals("2.10.5")) {
      Bukkit.getConsoleSender().sendMessage(" \n §6§lAVISO IMPORTANTE\n \n §7Utilize a versão 2.10.5 do PlaceHolderAPI, você está utilizando a v" + PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "\n ");
      System.exit(0);
      return;
    }
    
    PlaceholderAPI.registerExpansion(new KCoreExpansion());
    
    Database.setupDatabase(
        getConfig().getString("database.tipo"),
        getConfig().getString("database.mysql.host"),
        getConfig().getString("database.mysql.porta"),
        getConfig().getString("database.mysql.nome"),
        getConfig().getString("database.mysql.usuario"),
        getConfig().getString("database.mysql.senha"),
        getConfig().getBoolean("database.mysql.hikari", false),
        getConfig().getBoolean("database.mysql.mariadb", false),
        getConfig().getString("database.mongodb.url", "")
    );
    
    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);
    
    setupRoles();
    FakeManager.setupFake();
    Title.setupTitles();


    ServerItem.setupServers();
    Achievement.setupAchievements();
    
    Commands.setupCommands();
    Listeners.setupListeners();
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
    
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "kCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "kCore", new PluginMessageListener());
    
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> new SlickUpdater(this, 2).run());
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          this.getLogger().info("Saved" + profile.getName());
          profile.destroy();
        }
      });
      Database.getInstance().close();
    }
    
    File update = new File("plugins/kCore/update", "kCore.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/kCore.jar"));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/kCore.jar"));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do kCore aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
  
  private void setupRoles() {
    // Como roles.yml não será mais incluído, vamos criar um role padrão
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
    }
  }
}
