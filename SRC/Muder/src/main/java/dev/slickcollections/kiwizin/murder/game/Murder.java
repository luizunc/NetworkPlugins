package dev.slickcollections.kiwizin.murder.game;

import static dev.slickcollections.kiwizin.murder.hook.MMCoreHook.reloadScoreboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;
import dev.slickcollections.kiwizin.murder.game.interfaces.LoadCallback;
import dev.slickcollections.kiwizin.murder.game.object.MurderConfig;
import dev.slickcollections.kiwizin.murder.game.object.MurderTask;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class Murder implements Game<MurderTeam> {

  protected String name;
  protected MurderConfig config;

  protected int timer;
  protected MurderTask task;
  protected GameState state;
  protected List<UUID> players;
  protected List<UUID> spectators;
  protected List<MurderTeam> teams;

  public Murder(String name) {
    this.name = name;
    this.config = new MurderConfig(name);
    this.timer = Language.options$start$waiting + 1;
    this.task = new MurderTask(this);
    this.task.reset();
    this.state = GameState.AGUARDANDO;
    this.players = new ArrayList<>();
    this.spectators = new ArrayList<>();
    this.teams = new ArrayList<>();
  }

  public boolean spectate(Player player, Player target) {
    if (this.getState() == GameState.AGUARDANDO) {
      player.sendMessage("§cA partida ainda não começou.");
      return false;
    }

    Profile profile = Profile.getProfile(player.getName());
    if (profile.playingGame()) {
      if (profile.getGame().equals(this)) {
        return false;
      }

      profile.getGame().leave(profile, this);
    }

    profile.setGame(this);
    spectators.add(player.getUniqueId());

    player.teleport(target.getLocation());
    reloadScoreboard(profile);
    for (Player players : Bukkit.getOnlinePlayers()) {
      if (!players.getWorld().equals(player.getWorld())) {
        player.hidePlayer(players);
        players.hidePlayer(player);
        continue;
      }

      if (isSpectator(players)) {
        players.showPlayer(player);
      } else {
        players.hidePlayer(player);
      }
      player.showPlayer(players);
    }

    profile.setHotbar(Hotbar.getHotbarById("spectator"));
    profile.refresh();
    player.setGameMode(GameMode.ADVENTURE);
    player.spigot().setCollidesWithEntities(false);
    player.setAllowFlight(true);
    player.setFlying(true);
    return true;
  }

  @Override
  public void broadcastMessage(String message) {
    this.broadcastMessage(message, true);
  }

  @Override
  public void broadcastMessage(String message, boolean spectators) {
    this.listPlayers().forEach(player -> player.sendMessage(message));
  }

  @Override
  public void reset() {
    this.players.clear();
    this.spectators.clear();
    this.listTeams().forEach(MurderTeam::reset);
    this.task.cancel();
    addToQueue(this);
  }

  public void setTimer(int timer) {
    this.timer = timer;
  }

  public int getTimer() {
    return this.timer;
  }

  @Override
  public String getGameName() {
    return this.name;
  }

  public MurderConfig getConfig() {
    return this.config;
  }

  public World getWorld() {
    return this.config.getWorld();
  }

  public MurderTask getTask() {
    return this.task;
  }

  public String getMapName() {
    return this.config.getMapName();
  }

  public MurderMode getMode() {
    return this.config.getMode();
  }

  @Override
  public boolean isSpectator(Player player) {
    return this.spectators.contains(player.getUniqueId());
  }

  public void setState(GameState state) {
    this.state = state;
  }

  @Override
  public GameState getState() {
    return this.state;
  }

  @Override
  public int getOnline() {
    return this.players.size();
  }

  @Override
  public int getMaxPlayers() {
    return this.config.listSpawns().size();
  }

  public MurderTeam getAvailableTeam() {
    for (MurderTeam team : this.listTeams()) {
      if (team.canJoin()) {
        return team;
      }
    }

    return null;
  }

  @Override
  public MurderTeam getTeam(Player player) {
    return this.listTeams().stream().filter(team -> team.hasMember(player)).findAny().orElse(null);
  }

  @Override
  public List<MurderTeam> listTeams() {
    return this.teams;
  }

  @Override
  public List<Player> listPlayers() {
    return this.listPlayers(true);
  }

  @Override
  public List<Player> listPlayers(boolean spectators) {
    List<Player> players = new ArrayList<>(
        spectators ? this.spectators.size() + this.players.size() : this.players.size());
    this.players.forEach(id -> players.add(Bukkit.getPlayer(id)));
    if (spectators) {
      this.spectators.stream().filter(id -> !this.players.contains(id))
          .forEach(id -> players.add(Bukkit.getPlayer(id)));
    }

    return players.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("GAME");
  protected static final List<Murder> QUEUE = new ArrayList<>();
  private static final Map<String, Murder> GAMES = new HashMap<>();

  public static void addToQueue(Murder game) {
    if (QUEUE.contains(game)) {
      return;
    }

    QUEUE.add(game);
  }

  public static void setupGames() {
    new ArenaRollbackerTask().runTaskTimer(Main.getInstance(), 0, 100);

    File ymlFolder = new File("plugins/kMurder/arenas");
    File mapFolder = new File("plugins/kMurder/mundos");

    if (!ymlFolder.exists() || !mapFolder.exists()) {
      if (!ymlFolder.exists()) {
        ymlFolder.mkdirs();
      }
      if (!mapFolder.exists()) {
        mapFolder.mkdirs();
      }
    }

    for (File file : ymlFolder.listFiles()) {
      load(file, null);
    }

    LOGGER.info("Foram carregadas " + GAMES.size() + " salas.");
  }

  public static void load(File yamlFile, LoadCallback callback) {
    String arenaName = yamlFile.getName().split("\\.")[0];

    try {
      File backup = new File("plugins/kMurder/mundos", arenaName);
      if (!backup.exists() || !backup.isDirectory()) {
        throw new IllegalArgumentException(
            "Backup do mapa nao encontrado para a arena \"" + yamlFile.getName() + "\"");
      }

      MurderMode mode = MurderMode
          .fromName(Main.getInstance().getConfig("arenas", arenaName).getString("mode"));
      if (mode == null) {
        throw new IllegalArgumentException(
            "Modo do mapa \"" + yamlFile.getName() + "\" nao e valido");
      }

      GAMES.put(arenaName, mode.buildGame(arenaName, callback));
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "load(\"" + yamlFile.getName() + "\"): ", ex);
    }
  }

  public static Murder getByWorldName(String worldName) {
    return GAMES.get(worldName);
  }

  public static int getWaiting(MurderMode mode) {
    int waiting = 0;
    List<Murder> games = listByMode(mode);
    for (Murder game : games) {
      if (game.getState() != GameState.EMJOGO) {
        waiting += game.getOnline();
      }
    }

    return waiting;
  }

  public static int getPlaying(MurderMode mode) {
    int playing = 0;
    List<Murder> games = listByMode(mode);
    for (Murder game : games) {
      if (game.getState() == GameState.EMJOGO) {
        playing += game.getOnline();
      }
    }

    return playing;
  }

  public static Murder findRandom(MurderMode mode) {
    List<Murder> games = GAMES.values().stream().filter(
        game -> game.getMode().equals(mode) && game.getState().canJoin() && game.getOnline() < game
            .getMaxPlayers())
        .sorted((g1, g2) -> Integer.compare(g2.getOnline(), g1.getOnline()))
        .collect(Collectors.toList());
    Murder game = games.stream().findFirst().orElse(null);
    if (game != null && game.getOnline() == 0) {
      game = games.get(ThreadLocalRandom.current().nextInt(games.size()));
    }

    return game;
  }

  public static Map<String, List<Murder>> getAsMap(MurderMode mode) {
    Map<String, List<Murder>> result = new HashMap<>();
    GAMES.values().stream().filter(
        game -> game.getMode().equals(mode) && game.getState().canJoin() && game.getOnline() < game
            .getMaxPlayers()).forEach(game -> {
      List<Murder> list = result.computeIfAbsent(game.getMapName(), k -> new ArrayList<>());

      if (game.getState().canJoin() && game.getOnline() < game.getMaxPlayers()) {
        list.add(game);
      }
    });

    return result;
  }

  public static List<Murder> listByMode(MurderMode mode) {
    return GAMES.values().stream().filter(mm -> mm.getMode().equals(mode))
        .collect(Collectors.toList());
  }
}
