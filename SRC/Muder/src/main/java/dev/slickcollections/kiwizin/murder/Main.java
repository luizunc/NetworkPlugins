package dev.slickcollections.kiwizin.murder;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.murder.lobby.*;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;
import dev.slickcollections.kiwizin.murder.cmd.Commands;
import dev.slickcollections.kiwizin.murder.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.hook.MMCoreHook;
import dev.slickcollections.kiwizin.murder.listeners.Listeners;
import dev.slickcollections.kiwizin.murder.tagger.TagUtils;

import java.io.File;
import java.io.FileInputStream;

public class Main extends KPlugin {

  private static Main instance;
  private static boolean validInit;
  public static boolean kCosmetics, kMysteryBox, kClans;
  public static String currentServerName;

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
    kCosmetics = Bukkit.getPluginManager().getPlugin("kCosmetics") != null;
    kMysteryBox = Bukkit.getPluginManager().getPlugin("kMysteryBox") != null;
    kClans = Bukkit.getPluginManager().getPlugin("kClans") != null;

    Murder.setupGames();

    MMCoreHook.setupHook();
    Lobby.setupLobbies();
    Cosmetic.setupCosmetics();

    PlayNPC.setupNPCs();
    StatsNPC.setupNPCs();
    Language.setupLanguage();
    DeliveryNPC.setupNPCs();
    Leaderboard.setupLeaderboards();

    Listeners.setupListeners();
    Commands.setupCommands();

    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }

  @Override
  public void disable() {
    if (validInit) {
      DeliveryNPC.listNPCs().forEach(DeliveryNPC::destroy);
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      StatsNPC.listNPCs().forEach(StatsNPC::destroy);
      Leaderboard.listLeaderboards().forEach(Leaderboard::destroy);
      TagUtils.reset();
    }

    File update = new File("plugins/kMurder/update", "kMurder.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do kMurder aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }

  public static Main getInstance() {
    return instance;
  }
}
