package minecraft.lobby;

import minecraft.core.bukkit.Core;
import minecraft.lobby.cmd.Commands;
import minecraft.lobby.hook.LCoreHook;
import minecraft.lobby.listeners.Listeners;
import minecraft.lobby.lobby.DeliveryNPC;
import minecraft.lobby.lobby.Lobby;
import minecraft.lobby.lobby.PlayNPC;
import minecraft.lobby.utils.tagger.TagUtils;
import minecraft.core.bukkit.plugin.KPlugin;
import minecraft.core.core.utils.BukkitUtils;

import java.io.File;
import java.io.FileInputStream;

public class Main extends KPlugin {
  
  public static String currentServerName;
  private static Main instance;
  private static boolean validInit;
  
  public static Main getInstance() {
    return instance;
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {
  }
  
  @Override
  public void enable() {
    saveDefaultConfig();
    currentServerName = getConfig().getString("lobby");
    if (getConfig().getString("spawn") != null) {
      Core.setLobby(BukkitUtils.deserializeLocation(getConfig().getString("spawn")));
    }
    
    LCoreHook.setupHook();
    Lobby.setupLobbies();
    Listeners.setupListeners();
    Language.setupLanguage();
    PlayNPC.setupNPCs();
    DeliveryNPC.setupNPCs();
    Commands.setupCommands();
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      DeliveryNPC.listNPCs().forEach(DeliveryNPC::destroy);
      TagUtils.reset();
    }
    
    File update = new File("plugins/kLobby/update", "kLobby.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update di aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
}
