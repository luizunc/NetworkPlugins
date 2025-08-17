package dev.slickcollections.kiwizin.collectibles;

import dev.slickcollections.kiwizin.collectibles.cmd.Commands;
import dev.slickcollections.kiwizin.collectibles.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.table.CosmeticsTable;
import dev.slickcollections.kiwizin.collectibles.listeners.Listeners;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;

public class Main extends KPlugin {
  
  public static boolean kMysteryBox;
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
    DataTable.registerTable(new CosmeticsTable());
  }
  
  @Override
  public void enable() {
    saveDefaultConfig();
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("O plugin apenas funciona na versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
    
    kMysteryBox = Bukkit.getPluginManager().getPlugin("kMysteryBox") != null;
  
    Cosmetic.setupCosmetics();
    Language.setupLanguage();
    
    Commands.setupCommands();
    Listeners.setupListeners();
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      File update = new File("plugins/kCosmetics/update", "kCosmetics.jar");
      if (update.exists()) {
        try {
          this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
          this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
          this.getFileUtils().deleteFile(update.getParentFile());
          this.getLogger().info("Update aplicada.");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
}
