package minecraft.bedwars.game.object;

import minecraft.bedwars.Main;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.game.enums.BedWarsMode;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.utils.CubeID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static minecraft.bedwars.utils.VoidChunkGenerator.VOID_CHUNK_GENERATOR;

public class BedWarsConfig {
  
  private BedWars game;
  private KConfig config;
  
  private String yaml;
  private String name;
  private BedWarsMode mode;
  private World world;
  private List<BedWarsTeam> teams;
  private CubeID cubeId;
  private int minPlayers;
  private List<String> spawns;

  private List<String> generators;
  
  public BedWarsConfig(BedWars game) {
    this.game = game;
    this.yaml = game.getGameName();
    this.config = Main.getInstance().getConfig("arenas", this.yaml);
    this.name = this.config.getString("name");
    this.teams = new ArrayList<>();
    this.spawns = new ArrayList<>();
    this.generators = new ArrayList<>();
    this.name = this.config.getString("name");
    this.mode = BedWarsMode.fromName(this.config.getString("mode"));
    this.cubeId = new CubeID(config.getString("cubeId"));
    this.minPlayers = config.getInt("minPlayers");

    this.spawns.addAll(this.config.getStringList("spawns"));
    this.generators.addAll(this.config.getStringList("generators"));
    
    this.reload(null);
  }
  
  public void setupSpawns() {
    this.config.getStringList("spawns").forEach(spawn -> this.teams.add(new BedWarsTeam(
        this.game, spawn, this.mode.getSize())));
  }
  

  
  public void destroy() {
    if ((this.world = Bukkit.getWorld(this.yaml)) != null) {
      Bukkit.unloadWorld(this.world, false);
    }
    
    Main.getInstance().getFileUtils().deleteFile(new File(this.yaml));
    this.game = null;
    this.yaml = null;
    this.name = null;
    this.mode = null;
    this.generators.clear();
    this.generators = null;
    this.spawns.clear();
    this.spawns = null;
    this.teams.clear();
    this.teams = null;
    this.cubeId = null;
    this.world = null;
    this.config = null;
  }
  
  public void reload(final Runnable async) {
    File file = new File("plugins/bedwars/mundos/" + this.yaml);
    if (Bukkit.getWorld(file.getName()) != null) {
      Bukkit.unloadWorld(file.getName(), false);
    }
    
    Runnable delete = () -> {
      Main.getInstance().getFileUtils().deleteFile(new File(file.getName()));
      Main.getInstance().getFileUtils().copyFiles(file, new File(file.getName()));
      
      Runnable newWorld = () -> {
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
        if (async != null) {
          async.run();
        }
      };
      
      if (async == null) {
        newWorld.run();
        return;
      }
      
      Bukkit.getScheduler().runTask(Main.getInstance(), newWorld);
    };
    
    if (async == null) {
      delete.run();
      return;
    }
    
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), delete);
  }
  
  public String getMapName() {
    return this.name;
  }
  
  public KConfig getConfig() {
    return config;
  }
  
  public BedWarsMode getMode() {
    return this.mode;
  }
  
  public World getWorld() {
    return this.world;
  }
  
  public CubeID getCubeId() {
    return this.cubeId;
  }
  
  public int getMinPlayers() {
    return this.minPlayers;
  }
  
  public List<String> getSpawns() {
    return this.spawns;
  }
  

  
  public List<String> listGenerators() {
    return this.generators;
  }
  
  public List<String> listSpawns() {
    return this.spawns;
  }
  
  public List<BedWarsTeam> listTeams() {
    return this.teams;
  }
}
