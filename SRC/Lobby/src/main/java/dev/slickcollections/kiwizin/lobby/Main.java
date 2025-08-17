package dev.slickcollections.kiwizin.lobby;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.lobby.cmd.Commands;
import dev.slickcollections.kiwizin.lobby.hook.LCoreHook;
import dev.slickcollections.kiwizin.lobby.listeners.Listeners;
import dev.slickcollections.kiwizin.lobby.lobby.DeliveryNPC;
import dev.slickcollections.kiwizin.lobby.lobby.Lobby;
import dev.slickcollections.kiwizin.lobby.lobby.PlayNPC;
import dev.slickcollections.kiwizin.lobby.utils.tagger.TagUtils;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.utils.BukkitUtils;

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
