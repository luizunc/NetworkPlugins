package minecraft.core.core.player;

import me.clip.placeholderapi.PlaceholderAPI;
import minecraft.core.bukkit.Core;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.data.interfaces.AbstractContainer;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.game.Game;
import minecraft.core.core.game.GameTeam;
import minecraft.core.bukkit.hook.FriendsHook;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.hotbar.Hotbar;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.scoreboard.KScoreboard;
import minecraft.core.core.titles.TitleManager;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Representa o perfil de um jogador no sistema Core.
 * Gerencia dados, estatísticas, configurações e estado do jogador.
 */
public class Profile {
  
  // Cache estático para UUIDs e perfis
  private static final Map<String, UUID> UUID_CACHE = new HashMap<>();
  private static final Map<String, Profile> PROFILES = new ConcurrentHashMap<>();
  private static final SimpleDateFormat COMPARE_SDF = new SimpleDateFormat("yyyy/MM/dd");
  
  // Dados do perfil
  private String name;
  private Game<? extends GameTeam> game;
  private Hotbar hotbar;
  private KScoreboard scoreboard;
  private Map<String, Long> lastHit = new HashMap<>();
  private Map<String, Map<String, DataContainer>> tableMap;
  private Player player;
  
  /**
   * Construtor que cria um novo perfil para um jogador.
   * 
   * @param name Nome do jogador
   * @throws ProfileLoadException Se houver erro ao carregar o perfil
   */
  public Profile(String name) throws ProfileLoadException {
    this.name = Objects.requireNonNull(name, "Nome do jogador não pode ser nulo");
    this.tableMap = Database.getInstance().load(name);
    this.getDataContainer("account", "lastlogin").set(System.currentTimeMillis());
  }
  
  /**
   * Cria ou carrega um perfil existente.
   * 
   * @param playerName Nome do jogador
   * @return Perfil do jogador
   * @throws ProfileLoadException Se houver erro ao carregar o perfil
   */
  public static Profile createOrLoadProfile(String playerName) throws ProfileLoadException {
    Objects.requireNonNull(playerName, "Nome do jogador não pode ser nulo");
    
    return PROFILES.computeIfAbsent(playerName.toLowerCase(), key -> {
      try {
        return new Profile(playerName);
      } catch (ProfileLoadException e) {
        throw new RuntimeException("Erro ao criar perfil para " + playerName, e);
      }
    });
  }
  
  /**
   * Carrega um perfil se ele existir.
   * 
   * @param playerName Nome do jogador
   * @return Perfil do jogador ou null se não existir
   * @throws ProfileLoadException Se houver erro ao carregar o perfil
   */
  public static Profile loadIfExists(String playerName) throws ProfileLoadException {
    Objects.requireNonNull(playerName, "Nome do jogador não pode ser nulo");
    
    Profile profile = PROFILES.get(playerName.toLowerCase());
    if (profile == null) {
      String existingName = Database.getInstance().exists(playerName);
      if (existingName != null) {
        profile = new Profile(existingName);
      }
    }
    
    return profile;
  }
  
  /**
   * Obtém um perfil do cache.
   * 
   * @param playerName Nome do jogador
   * @return Perfil do jogador ou null se não estiver no cache
   */
  public static Profile getProfile(String playerName) {
    return playerName != null ? PROFILES.get(playerName.toLowerCase()) : null;
  }
  
  /**
   * Remove um perfil do cache.
   * 
   * @param playerName Nome do jogador
   * @return Perfil removido ou null se não existia
   */
  public static Profile unloadProfile(String playerName) {
    if (playerName == null) {
      return null;
    }
    
    UUID_CACHE.remove(playerName.toLowerCase());
    return PROFILES.remove(playerName.toLowerCase());
  }
  
  /**
   * Encontra um jogador no cache pelo nome.
   * 
   * @param playerName Nome do jogador
   * @return Jogador ou null se não encontrado
   */
  public static Player findCached(String playerName) {
    if (playerName == null) {
      return null;
    }
    
    UUID uuid = UUID_CACHE.get(playerName.toLowerCase());
    return uuid != null ? Bukkit.getPlayer(uuid) : null;
  }
  
  /**
   * Verifica se um jogador está online.
   * 
   * @param playerName Nome do jogador
   * @return true se o jogador está online
   */
  public static boolean isOnline(String playerName) {
    return playerName != null && PROFILES.containsKey(playerName.toLowerCase());
  }
  
  /**
   * Obtém todos os perfis carregados.
   * 
   * @return Coleção de perfis
   */
  public static Collection<Profile> listProfiles() {
    return PROFILES.values();
  }
  
  /**
   * Registra um hit em outro jogador.
   * 
   * @param name Nome do jogador atingido
   */
  public void setHit(String name) {
    if (name != null) {
      this.lastHit.put(name, System.currentTimeMillis() + 8000);
    }
  }
  
  /**
   * Atualiza o scoreboard do jogador.
   */
  public void update() {
    if (this.scoreboard != null) {
      this.scoreboard.update();
    }
  }
  
  /**
   * Atualiza completamente o estado do jogador.
   */
  public void refresh() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }
    
    resetPlayerState(player);
    
    if (!playingGame()) {
      setLobbyState(player);
    }
    
    if (this.hotbar != null) {
      this.hotbar.apply(this);
    }
    
    this.refreshPlayers();
  }
  
  /**
   * Reseta o estado básico do jogador.
   * 
   * @param player Jogador a ser resetado
   */
  private void resetPlayerState(Player player) {
    player.setMaxHealth(20.0);
    player.setHealth(20.0);
    player.setFoodLevel(20);
    player.setExhaustion(0.0f);
    player.setExp(0.0f);
    player.setLevel(0);
    player.setAllowFlight(false);
    player.closeInventory();
    player.spigot().setCollidesWithEntities(true);
    
    // Remove efeitos de poção
    player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
  }
  
  /**
   * Define o estado do jogador no lobby.
   * 
   * @param player Jogador a ser configurado
   */
  private void setLobbyState(Player player) {
    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(Core.getLobby());
    player.setAllowFlight(player.hasPermission("core.fly"));
    
    // IMPORTANTE: NÃO alterar a coluna rank automaticamente
    // A coluna rank deve ser preservada e nunca alterada por este método
    
    // Aplicar a tag selecionada no tab list
    minecraft.core.core.utils.TagUtils.setTag(player, "", "", 0);
  }
  
  /**
   * Atualiza a visibilidade dos jogadores.
   */
  public void refreshPlayers() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }
    
    updateHotbarPlayers(player);
    
    if (!this.playingGame()) {
      updatePlayerVisibility(player);
    }
  }
  
  /**
   * Atualiza os itens da hotbar relacionados a jogadores.
   * 
   * @param player Jogador
   */
  private void updateHotbarPlayers(Player player) {
    if (this.hotbar != null) {
      this.hotbar.getButtons().stream()
          .filter(button -> "jogadores".equalsIgnoreCase(button.getAction().getValue()))
          .forEach(button -> {
            String icon = PlaceholderAPI.setPlaceholders(player, button.getIcon());
            player.getInventory().setItem(button.getSlot(), BukkitUtils.deserializeItemStack(icon));
          });
    }
  }
  
  /**
   * Atualiza a visibilidade dos jogadores.
   * 
   * @param player Jogador principal
   */
  private void updatePlayerVisibility(Player player) {
    Bukkit.getOnlinePlayers().forEach(otherPlayer -> {
      Profile otherProfile = Profile.getProfile(otherPlayer.getName());
      if (otherProfile != null && !otherProfile.playingGame()) {
        updateBidirectionalVisibility(player, otherPlayer, this, otherProfile);
      } else {
        hidePlayersFromEachOther(player, otherPlayer);
      }
    });
  }
  
  /**
   * Atualiza a visibilidade bidirecional entre dois jogadores.
   * 
   * @param player1 Primeiro jogador
   * @param player2 Segundo jogador
   * @param profile1 Perfil do primeiro jogador
   * @param profile2 Perfil do segundo jogador
   */
  private void updateBidirectionalVisibility(Player player1, Player player2, Profile profile1, Profile profile2) {
    boolean isFriend = FriendsHook.isFriend(player1.getName(), player2.getName());
    boolean isBlacklisted = FriendsHook.isBlacklisted(player1.getName(), player2.getName());
    
    // Visibilidade do player1 para player2
    updatePlayerVisibility(player1, player2, profile1, profile2, isFriend, isBlacklisted);
    
    // Visibilidade do player2 para player1
    updatePlayerVisibility(player2, player1, profile2, profile1, isFriend, isBlacklisted);
  }
  
  /**
   * Atualiza a visibilidade de um jogador para outro.
   * 
   * @param viewer Jogador que está vendo
   * @param target Jogador que está sendo visto
   * @param viewerProfile Perfil do viewer
   * @param targetProfile Perfil do target
   * @param isFriend Se são amigos
   * @param isBlacklisted Se estão na blacklist
   */
  private void updatePlayerVisibility(Player viewer, Player target, Profile viewerProfile, Profile targetProfile, 
                                     boolean isFriend, boolean isBlacklisted) {
    boolean shouldShow = (viewerProfile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS ||
                         Rank.getRank(target).isAlwaysVisible() || isFriend) && !isBlacklisted;
    
    if (shouldShow) {
      if (!viewer.canSee(target)) {
        TitleManager.show(viewerProfile, targetProfile);
      }
      viewer.showPlayer(target);
    } else {
      if (viewer.canSee(target)) {
        TitleManager.hide(viewerProfile, targetProfile);
      }
      viewer.hidePlayer(target);
    }
  }
  
  /**
   * Esconde jogadores um do outro.
   * 
   * @param player1 Primeiro jogador
   * @param player2 Segundo jogador
   */
  private void hidePlayersFromEachOther(Player player1, Player player2) {
    player1.hidePlayer(player2);
    player2.hidePlayer(player1);
  }
  
  /**
   * Salva o perfil de forma assíncrona.
   */
  public void save() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    
    Database.getInstance().save(this.name, this.tableMap);
  }
  
  /**
   * Salva o perfil de forma síncrona.
   */
  public void saveSync() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    
    Database.getInstance().saveSync(this.name, this.tableMap);
  }
  
  /**
   * Destrói o perfil e libera recursos.
   */
  public void destroy() {
    this.name = null;
    this.game = null;
    this.hotbar = null;
    
    if (this.scoreboard != null) {
      this.scoreboard.destroy();
      this.scoreboard = null;
    }
    
    if (this.lastHit != null) {
      this.lastHit.clear();
      this.lastHit = null;
    }
    
    if (this.tableMap != null) {
      this.tableMap.values().forEach(containerMap -> {
        containerMap.values().forEach(DataContainer::gc);
        containerMap.clear();
      });
      this.tableMap.clear();
      this.tableMap = null;
    }
  }
  
  // Getters e Setters
  
  public String getName() {
    return this.name;
  }
  
  public boolean isOnline() {
    return this.name != null && isOnline(this.name);
  }
  
  public Player getPlayer() {
    if (this.player == null) {
      this.player = this.name == null ? null : Bukkit.getPlayerExact(this.name);
    }
    
    return this.player;
  }
  
  public void setPlayer(Player player) {
    this.player = player;
    if (player != null && this.name != null) {
      UUID_CACHE.put(this.name.toLowerCase(), player.getUniqueId());
    }
  }
  
  public Game<?> getGame() {
    return this.getGame(Game.class);
  }
  
  public void setGame(Game<? extends GameTeam> game) {
    this.game = game;
    this.lastHit.clear();
    
    if (this.game != null) {
      TitleManager.leaveLobby(this);
    } else {
      scheduleLobbyJoin();
    }
  }
  
  /**
   * Agenda a entrada no lobby.
   */
  private void scheduleLobbyJoin() {
    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
      if (this.isOnline() && !this.playingGame()) {
        TitleManager.joinLobby(this);
      }
    }, 5);
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Game<?>> T getGame(Class<T> gameClass) {
    return this.game != null && gameClass.isAssignableFrom(this.game.getClass()) ? (T) this.game : null;
  }
  
  public Hotbar getHotbar() {
    return this.hotbar;
  }
  
  public void setHotbar(Hotbar hotbar) {
    this.hotbar = hotbar;
  }
  
  public boolean playingGame() {
    return this.game != null;
  }
  
  public List<Profile> getLastHitters() {
    long currentTime = System.currentTimeMillis();
    
    List<Profile> hitters = this.lastHit.entrySet().stream()
        .filter(entry -> entry.getValue() > currentTime && isOnline(entry.getKey()))
        .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
        .map(entry -> getProfile(entry.getKey()))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    
    // Limpa o cache após uso
    this.lastHit.clear();
    return hitters;
  }
  
  public KScoreboard getScoreboard() {
    return this.scoreboard;
  }
  
  public void setScoreboard(KScoreboard scoreboard) {
    if (this.scoreboard != null) {
      this.scoreboard.destroy();
    }
    this.scoreboard = scoreboard;
  }
  
  // Métodos de estatísticas
  
  public void addStats(String table, String... keys) {
    this.addStats(table, 1, keys);
  }
  
  public void addStats(String table, long amount, String... keys) {
    for (String key : keys) {
      // Atualiza estatísticas mensais se necessário
      updateMonthlyStats(table);
      this.getDataContainer(table, key).addLong(amount);
    }
    
    // Atualiza o título se as estatísticas modificadas são relevantes para títulos
    if (table.equals("skywars") || table.equals("bedwars")) {
      TitleManager.updatePlayerTitle(this);
    }
  }
  
  /**
   * Atualiza estatísticas mensais se necessário.
   * 
   * @param table Tabela das estatísticas
   */
  private void updateMonthlyStats(String table) {
    String monthKey = "month";
    String currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
    String storedMonth = this.getDataContainer(table, monthKey).getAsString();
    
    if (!currentMonth.equals(storedMonth)) {
      Map<String, DataContainer> containerMap = this.tableMap.get(table);
      containerMap.keySet().stream()
          .filter(key -> key.startsWith("monthly"))
          .forEach(key -> containerMap.get(key).set(0L));
      containerMap.get(monthKey).set(currentMonth);
    }
  }
  
  public void setStats(String table, long amount, String... keys) {
    for (String key : keys) {
      this.getDataContainer(table, key).set(amount);
    }
  }
  
  public void updateDailyStats(String table, String date, long amount, String key) {
    long currentExpire = this.getStats(table, date);
    this.setStats(table, System.currentTimeMillis(), date);
    
    if (amount == 0 || (this.getStats(table, key) > 0 && !COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire)))) {
      this.setStats(table, 0, key);
      return;
    }
    
    this.addStats(table, amount, key);
  }
  
  public int addCoins(String table, double amount) {
    this.getDataContainer(table, "coins").addDouble(amount);
    return (int) amount;
  }
  
  public void removeCoins(String table, double amount) {
    this.getDataContainer(table, "coins").removeDouble(amount);
  }
  
  public long getStats(String table, String... keys) {
    return Arrays.stream(keys)
        .mapToLong(key -> this.getDataContainer(table, key).getAsLong())
        .sum();
  }
  
  public long getDailyStats(String table, String date, String key) {
    long currentExpire = this.getStats(table, date);
    if (!COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire))) {
      this.setStats(table, 0, key);
    }
    
    this.setStats(table, System.currentTimeMillis(), date);
    return this.getStats(table, key);
  }
  
  public double getCoins(String table) {
    return this.getDataContainer(table, "coins").getAsDouble();
  }
  
  public String getFormatedStats(String table, String... keys) {
    return StringUtils.formatNumber(this.getStats(table, keys));
  }
  
  public String getFormatedStatsDouble(String table, String key) {
    return StringUtils.formatNumber(this.getDataContainer(table, key).getAsDouble());
  }
  
  // Containers específicos
  
  public minecraft.core.core.database.data.container.PreferencesContainer getPreferencesContainer() {
    return this.getAbstractContainer("account", "preferences", 
        minecraft.core.core.database.data.container.PreferencesContainer.class);
  }
  
  public minecraft.core.core.database.data.container.TitlesContainer getTitlesContainer() {
    return this.getAbstractContainer("account", "titles", 
        minecraft.core.core.database.data.container.TitlesContainer.class);
  }
  
  public minecraft.core.core.database.data.container.AchievementsContainer getAchievementsContainer() {
    return this.getAbstractContainer("account", "achievements", 
        minecraft.core.core.database.data.container.AchievementsContainer.class);
  }
  
  public minecraft.core.core.database.data.container.SelectedContainer getSelectedContainer() {
    return this.getAbstractContainer("account", "selected", 
        minecraft.core.core.database.data.container.SelectedContainer.class);
  }
  
  public minecraft.core.core.database.data.container.SkinsContainer getSkinsContainer() {
    return this.getAbstractContainer("account", "skins", 
        minecraft.core.core.database.data.container.SkinsContainer.class);
  }
  
  public DataContainer getDataContainer(String table, String key) {
    return this.tableMap.get(table).get(key);
  }
  
  public <T extends AbstractContainer> T getAbstractContainer(String table, String key, Class<T> containerClass) {
    return this.getDataContainer(table, key).getContainer(containerClass);
  }
  
  public Map<String, Map<String, DataContainer>> getTableMap() {
    return this.tableMap;
  }
}