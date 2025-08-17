package dev.slickcollections.kiwizin.bedwars;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.bedwars.cmd.Commands;
import dev.slickcollections.kiwizin.bedwars.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.hook.BWCoreHook;
import dev.slickcollections.kiwizin.bedwars.listeners.Listeners;
import dev.slickcollections.kiwizin.bedwars.lobby.*;
import dev.slickcollections.kiwizin.bedwars.utils.tagger.TagUtils;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;

public class Main extends KPlugin {
  
  public static boolean kCosmetics, kMysteryBox;
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
    kMysteryBox = Bukkit.getPluginManager().getPlugin("kMysteryBox") != null;
    kCosmetics = Bukkit.getPluginManager().getPlugin("kCosmetics") != null;
    
    BedWars.setupGames();
    Language.setupLanguage();
    
    Listeners.setupListeners();
    BWCoreHook.setupHook();
    Cosmetic.setupCosmetics();
    PlayNPC.setupNPCs();
    Commands.setupCommands();
    
    StatsNPC.setupNPCs();
    DeliveryNPC.setupNPCs();
    Lobby.setupLobbies();
    Leaderboard.setupLeaderboards();
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      TagUtils.reset();
      DeliveryNPC.listNPCs().forEach(DeliveryNPC::destroy);
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      Leaderboard.listLeaderboards().forEach(Leaderboard::destroy);
    }
    
    File update = new File("plugins/kBedWars/update", "kBedWars.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do kBedWars aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
}
