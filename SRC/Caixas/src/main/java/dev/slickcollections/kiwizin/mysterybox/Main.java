package dev.slickcollections.kiwizin.mysterybox;

import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.mysterybox.cmd.Commands;
import dev.slickcollections.kiwizin.mysterybox.hook.MBCoreHook;
import dev.slickcollections.kiwizin.mysterybox.hook.table.MysteryBoxTable;
import dev.slickcollections.kiwizin.mysterybox.listeners.Listeners;
import dev.slickcollections.kiwizin.mysterybox.lobby.BoxNPC;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Main extends KPlugin {
  
  public static Main instance;
  public static boolean kCosmetics;
  public static boolean validInit;
  private static Location lootLocation;
  
  public static Main getInstance() {
    return instance;
  }
  
  public static Location getLootChestsLocation() {
    return lootLocation;
  }
  
  public static void setLootChestsLocation(Location newLocation) {
    lootLocation = newLocation;
    getInstance().getConfig().set("menuLocation", BukkitUtils.serializeLocation(lootLocation));
    getInstance().saveConfig();
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {
    DataTable.registerTable(new MysteryBoxTable());
  }
  
  @Override
  public void enable() {
    kCosmetics = Bukkit.getPluginManager().getPlugin("kCosmetics") != null;
    
    saveDefaultConfig();
    lootLocation = Bukkit.getWorlds().get(0).getSpawnLocation().clone().add(0.5, 1, 0.5);
    if (getConfig().get("menuLocation") != null) {
      lootLocation = BukkitUtils.deserializeLocation(getConfig().getString("menuLocation")).add(0, 1, 0);
    }
    if (MinecraftVersion.getCurrentVersion().getCompareId() != 183) {
      this.setEnabled(false);
      this.getLogger().warning("O plugin apenas funciona na versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
    
    MBCoreHook.setupHook();
    BoxNPC.setupBoxNPCs();
    Listeners.setupListeners();
    Language.setupLanguage();
    Commands.setupCommands();
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      BoxNPC.listNPCs().forEach(BoxNPC::destroy);
    }
    
    this.getLogger().info("O plugin foi desativado.");
  }
}
