package minecraft.core.core.player.role;

import minecraft.core.Manager;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.cache.RankCache;
import minecraft.core.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa um rank (cargo) no sistema Core.
 * Gerencia permissões, prefixos e configurações de visibilidade dos jogadores.
 */
public final class Rank {
  
  // Cache estático de ranks
  private static final List<Rank> RANKS = new ArrayList<>();
  
  // Dados do rank
  private final int id;
  private final String name;
  private final String prefix;
  private final String permission;
  private final boolean alwaysVisible;
  private final boolean broadcast;
  
  /**
   * Construtor que cria um novo rank.
   * 
   * @param name Nome do rank
   * @param prefix Prefixo do rank
   * @param permission Permissão do rank
   * @param alwaysVisible Se o rank é sempre visível
   * @param broadcast Se o rank pode fazer broadcast
   */
  public Rank(String name, String prefix, String permission, boolean alwaysVisible, boolean broadcast) {
    this.id = RANKS.size();
    this.name = StringUtils.formatColors(Objects.requireNonNull(name, "Nome do rank não pode ser nulo"));
    this.prefix = StringUtils.formatColors(Objects.requireNonNull(prefix, "Prefixo do rank não pode ser nulo"));
    this.permission = Objects.requireNonNull(permission, "Permissão do rank não pode ser nulo");
    this.alwaysVisible = alwaysVisible;
    this.broadcast = broadcast;
  }
  
  // Métodos estáticos para obter nomes formatados
  
  /**
   * Obtém o nome com prefixo do jogador.
   * 
   * @param name Nome do jogador
   * @return Nome com prefixo
   */
  public static String getPrefixed(String name) {
    return getPrefixed(name, false);
  }
  
  /**
   * Obtém o nome colorido do jogador.
   * 
   * @param name Nome do jogador
   * @return Nome colorido
   */
  public static String getColored(String name) {
    return getColored(name, false);
  }
  
  /**
   * Obtém o nome com prefixo do jogador.
   * 
   * @param name Nome do jogador
   * @param removeFake Se deve remover fake
   * @return Nome com prefixo
   */
  public static String getPrefixed(String name, boolean removeFake) {
    return getTaggedName(name, false, removeFake);
  }
  
  /**
   * Obtém o nome colorido do jogador.
   * 
   * @param name Nome do jogador
   * @param removeFake Se deve remover fake
   * @return Nome colorido
   */
  public static String getColored(String name, boolean removeFake) {
    return getTaggedName(name, true, removeFake);
  }
  
  /**
   * Obtém o nome com tag (prefixo ou cor) do jogador.
   * 
   * @param name Nome do jogador
   * @param onlyColor Se deve retornar apenas a cor
   * @param removeFake Se deve remover fake
   * @return Nome com tag
   */
  private static String getTaggedName(String name, boolean onlyColor, boolean removeFake) {
    if (name == null) {
      return "&7null";
    }
    
    // Verifica se é um fake
    if (!removeFake && Manager.isFake(name)) {
      return getFakeTaggedName(name, onlyColor);
    }
    
    // Verifica se o jogador está online
    Object target = Manager.getPlayer(name);
    if (target != null) {
      return getOnlinePlayerTaggedName(name, target, onlyColor);
    }
    
    // Busca no cache ou banco de dados
    return getOfflinePlayerTaggedName(name, onlyColor, removeFake);
  }
  
  /**
   * Obtém o nome com tag para um fake.
   * 
   * @param name Nome do fake
   * @param onlyColor Se deve retornar apenas a cor
   * @return Nome com tag
   */
  private static String getFakeTaggedName(String name, boolean onlyColor) {
    Rank fakeRank = Manager.getFakeRank(name);
    String prefix = fakeRank.getPrefix();
    
    if (onlyColor) {
      prefix = StringUtils.getLastColor(prefix);
    }
    
    return prefix + Manager.getFake(name);
  }
  
  /**
   * Obtém o nome com tag para um jogador online.
   * 
   * @param name Nome do jogador
   * @param target Objeto do jogador
   * @param onlyColor Se deve retornar apenas a cor
   * @return Nome com tag
   */
  private static String getOnlinePlayerTaggedName(String name, Object target, boolean onlyColor) {
    Rank rank = getPlayerRank(target, true);
    String prefix = rank.getPrefix();
    
    if (onlyColor) {
      prefix = StringUtils.getLastColor(prefix);
    }
    
    return prefix + name;
  }
  
  /**
   * Obtém o nome com tag para um jogador offline.
   * 
   * @param name Nome do jogador
   * @param onlyColor Se deve retornar apenas a cor
   * @param removeFake Se deve remover fake
   * @return Nome com tag
   */
  private static String getOfflinePlayerTaggedName(String name, boolean onlyColor, boolean removeFake) {
    String rankAndName = RankCache.isPresent(name) ? RankCache.get(name) : Database.getInstance().getRankAndName(name);
    
    if (rankAndName == null) {
      return "&7" + name;
    }
    
    String[] parts = rankAndName.split(" : ");
    if (parts.length < 2) {
      return "&7" + name;
    }
    
    String rankName = parts[0];
    String playerName = parts[1];
    
    Rank rank = getRankByName(rankName);
    String prefix = rank.getPrefix();
    
    if (onlyColor) {
      prefix = StringUtils.getLastColor(prefix);
    }
    
    // Verifica se o nome é um fake
    if (!removeFake && Manager.isFake(playerName)) {
      playerName = Manager.getFake(playerName);
    }
    
    return prefix + playerName;
  }
  
  // Métodos estáticos para buscar ranks
  
  /**
   * Obtém um rank pelo nome.
   * 
   * @param name Nome do rank
   * @return Rank encontrado ou o último rank se não encontrar
   */
  public static Rank getRankByName(String name) {
    if (name == null || RANKS.isEmpty()) {
      return getLastRank();
    }
    
    String strippedName = StringUtils.stripColors(name);
    return RANKS.stream()
        .filter(rank -> StringUtils.stripColors(rank.getName()).equalsIgnoreCase(strippedName))
        .findFirst()
        .orElseGet(Rank::getLastRank);
  }
  
  /**
   * Obtém um rank pela permissão.
   * 
   * @param permission Permissão do rank
   * @return Rank encontrado ou null se não encontrar
   */
  public static Rank getRankByPermission(String permission) {
    if (permission == null) {
      return null;
    }
    
    return RANKS.stream()
        .filter(rank -> rank.getPermission().equals(permission))
        .findFirst()
        .orElse(null);
  }
  
  /**
   * Obtém o rank de um jogador.
   * 
   * @param player Objeto do jogador
   * @return Rank do jogador
   */
  public static Rank getPlayerRank(Object player) {
    return getPlayerRank(player, false);
  }
  
  /**
   * Obtém o rank de um jogador.
   * 
   * @param player Objeto do jogador
   * @param removeFake Se deve remover fake
   * @return Rank do jogador
   */
  public static Rank getPlayerRank(Object player, boolean removeFake) {
    if (player == null) {
      return getLastRank();
    }
    
    String playerName = Manager.getName(player);
    
    // Verifica se é um fake
    if (!removeFake && Manager.isFake(playerName)) {
      return Manager.getFakeRank(playerName);
    }
    
    // Busca o rank mais alto com permissão (menor ID = mais alto)
    return RANKS.stream()
        .filter(rank -> rank.has(player))
        .min((r1, r2) -> Integer.compare(r1.getId(), r2.getId()))
        .orElseGet(Rank::getLastRank);
  }
  
  /**
   * Obtém o último rank da lista (rank padrão).
   * 
   * @return Último rank
   */
  public static Rank getLastRank() {
    return RANKS.isEmpty() ? null : RANKS.get(RANKS.size() - 1);
  }
  
  /**
   * Obtém todos os ranks.
   * 
   * @return Lista de ranks
   */
  public static List<Rank> listRanks() {
    return RANKS;
  }
  
  // Getters
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getPrefix() {
    return this.prefix;
  }
  
  public String getPermission() {
    return this.permission;
  }
  
  /**
   * Verifica se este é o rank padrão.
   * 
   * @return true se for o rank padrão
   */
  public boolean isDefault() {
    return this.permission.isEmpty();
  }
  
  public boolean isAlwaysVisible() {
    return this.alwaysVisible;
  }
  
  public boolean isBroadcast() {
    return this.broadcast;
  }
  
  /**
   * Verifica se um jogador tem este rank.
   * 
   * @param player Objeto do jogador
   * @return true se o jogador tem este rank
   */
  public boolean has(Object player) {
    if (this.isDefault()) {
      return true;
    }
    
    // Verifica se é um jogador OP (tem todas as permissões)
    if (player instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player bukkitPlayer = (org.bukkit.entity.Player) player;
      if (bukkitPlayer.isOp()) {
        return true;
      }
    }
    
    return Manager.hasPermission(player, this.permission);
  }
}
