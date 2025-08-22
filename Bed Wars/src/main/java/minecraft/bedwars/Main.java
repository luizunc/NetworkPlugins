package minecraft.bedwars;

import minecraft.core.bukkit.Core;
import minecraft.bedwars.cmd.Commands;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.hook.BWCoreHook;
import minecraft.bedwars.listeners.Listeners;
import minecraft.bedwars.lobby.Lobby;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.bedwars.lobby.PlayNPC;
import minecraft.bedwars.utils.tagger.TagUtils;
import minecraft.core.core.libraries.MinecraftVersion;
import minecraft.core.bukkit.plugin.KPlugin;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;

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
  public void load() {}
  
  @Override
  public void enable() {
    if (MinecraftVersion.getCurrentVersion().getCompareId() != 183) {
      this.setEnabled(false);
      this.getLogger().warning("O plugin apenas funciona na versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
   
    saveDefaultConfig();
    currentServerName = getConfig().getString("lobby");
    if (getConfig().getString("spawn") != null) {
      Core.setLobby(BukkitUtils.deserializeLocation(getConfig().getString("spawn")));
    }
    
    BedWars.setupGames();
    Language.setupLanguage();
    
    Listeners.setupListeners();
    BWCoreHook.setupHook();
    Cosmetic.setupCosmetics();
    PlayNPC.setupNPCs();
    Commands.setupCommands();
    
    Lobby.setupLobbies();
    Leaderboard.setupLeaderboards();
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      TagUtils.reset();
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      Leaderboard.listLeaderboards().forEach(Leaderboard::destroy);
    }
    
    File update = new File("plugins/bedwars/update", "bedwars.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do bedwars aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
}
