package dev.slickcollections.kiwizin.murder.game.object;

import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import dev.slickcollections.kiwizin.murder.Main;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static dev.slickcollections.kiwizin.murder.utils.VoidChunkGenerator.VOID_CHUNK_GENERATOR;

public class MurderConfig {

  private KConfig config;

  private String yaml;
  private World world;
  private String name;
  private MurderMode mode;
  private String spawnLocation;
  private List<String> spawns;
  private List<String> golds;
  private int minPlayers;

  public MurderConfig(String yaml) {
    this.yaml = yaml;
    this.config = Main.getInstance().getConfig("arenas", this.yaml);
    this.name = this.config.getString("name");
    this.mode = MurderMode.fromName(this.config.getString("mode"));
    this.spawnLocation = this.config.getString("spawn");
    this.minPlayers = this.config.getInt("minPlayers");
    this.spawns = this.config.getStringList("spawns");
    this.golds = this.config.getStringList("golds");
    this.reload();
  }

  public void destroy() {
    if ((this.world = Bukkit.getWorld(this.yaml)) != null) {
      Bukkit.unloadWorld(this.world, false);
    }

    Main.getInstance().getFileUtils().deleteFile(new File(this.yaml));
    this.yaml = null;
    this.name = null;
    this.mode = null;
    this.spawns.clear();
    this.spawns = null;
    this.world = null;
    this.config = null;
  }

  public void reload() {
    File file = new File("plugins/kMurder/mundos/" + this.yaml);
    if ((this.world = Bukkit.getWorld(file.getName())) != null) {
      Bukkit.unloadWorld(this.world, false);
    }

    Main.getInstance().getFileUtils().deleteFile(new File(file.getName()));
    Main.getInstance().getFileUtils().copyFiles(file, new File(file.getName()));

    WorldCreator wc = WorldCreator.name(file.getName());
    wc.generator(VOID_CHUNK_GENERATOR);
    wc.generateStructures(false);
    this.world = wc.createWorld();
    this.world.setTime(0L);
    this.world.setStorm(false);
    this.world.setThundering(false);
    this.world.setAutoSave(false);
    this.world.setAnimalSpawnLimit(0);
    this.world.setWaterAnimalSpawnLimit(0);
    this.world.setKeepSpawnInMemory(false);
    this.world.setGameRuleValue("doMobSpawning", "false");
    this.world.setGameRuleValue("doDaylightCycle", "false");
    this.world.setGameRuleValue("mobGriefing", "false");
    this.world.getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
  }

  public Location getRandomGold() {
    return BukkitUtils.deserializeLocation(listGolds().get(ThreadLocalRandom.current().nextInt(listGolds().size())));
  }

  public World getWorld() {
    return this.world;
  }

  public KConfig getConfig() {
    return this.config;
  }

  public String getMapName() {
    return this.name;
  }

  public MurderMode getMode() {
    return this.mode;
  }

  public Location getSpawnLocation() {
    return BukkitUtils.deserializeLocation(this.spawnLocation);
  }

  public List<String> listSpawns() {
    return this.spawns;
  }

  public List<String> listGolds() {
    return this.golds;
  }

  public int getMinPlayers() {
    return minPlayers;
  }
}
