package minecraft.core.core.player.role;

import minecraft.core.Manager;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.cache.RoleCache;
import minecraft.core.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa um role (cargo) no sistema Core.
 * Gerencia permissões, prefixos e configurações de visibilidade dos jogadores.
 */
public final class Role {
  
  // Cache estático de roles
  private static final List<Role> ROLES = new ArrayList<>();
  
  // Dados do role
  private final int id;
  private final String name;
  private final String prefix;
  private final String permission;
  private final boolean alwaysVisible;
  private final boolean broadcast;
  
  /**
   * Construtor que cria um novo role.
   * 
   * @param name Nome do role
   * @param prefix Prefixo do role
   * @param permission Permissão do role
   * @param alwaysVisible Se o role é sempre visível
   * @param broadcast Se o role pode fazer broadcast
   */
  public Role(String name, String prefix, String permission, boolean alwaysVisible, boolean broadcast) {
    this.id = ROLES.size();
    this.name = StringUtils.formatColors(Objects.requireNonNull(name, "Nome do role não pode ser nulo"));
    this.prefix = StringUtils.formatColors(Objects.requireNonNull(prefix, "Prefixo do role não pode ser nulo"));
    this.permission = Objects.requireNonNull(permission, "Permissão do role não pode ser nulo");
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
    Role fakeRole = Manager.getFakeRole(name);
    String prefix = fakeRole.getPrefix();
    
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
    Role role = getPlayerRole(target, true);
    String prefix = role.getPrefix();
    
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
    String rankAndName = RoleCache.isPresent(name) ? RoleCache.get(name) : Database.getInstance().getRankAndName(name);
    
    if (rankAndName == null) {
      return "&7" + name;
    }
    
    String[] parts = rankAndName.split(" : ");
    if (parts.length < 2) {
      return "&7" + name;
    }
    
    String roleName = parts[0];
    String playerName = parts[1];
    
    Role role = getRoleByName(roleName);
    String prefix = role.getPrefix();
    
    if (onlyColor) {
      prefix = StringUtils.getLastColor(prefix);
    }
    
    // Verifica se o nome é um fake
    if (!removeFake && Manager.isFake(playerName)) {
      playerName = Manager.getFake(playerName);
    }
    
    return prefix + playerName;
  }
  
  // Métodos estáticos para buscar roles
  
  /**
   * Obtém um role pelo nome.
   * 
   * @param name Nome do role
   * @return Role encontrado ou o último role se não encontrar
   */
  public static Role getRoleByName(String name) {
    if (name == null || ROLES.isEmpty()) {
      return getLastRole();
    }
    
    String strippedName = StringUtils.stripColors(name);
    return ROLES.stream()
        .filter(role -> StringUtils.stripColors(role.getName()).equalsIgnoreCase(strippedName))
        .findFirst()
        .orElseGet(Role::getLastRole);
  }
  
  /**
   * Obtém um role pela permissão.
   * 
   * @param permission Permissão do role
   * @return Role encontrado ou null se não encontrar
   */
  public static Role getRoleByPermission(String permission) {
    if (permission == null) {
      return null;
    }
    
    return ROLES.stream()
        .filter(role -> role.getPermission().equals(permission))
        .findFirst()
        .orElse(null);
  }
  
  /**
   * Obtém o role de um jogador.
   * 
   * @param player Objeto do jogador
   * @return Role do jogador
   */
  public static Role getPlayerRole(Object player) {
    return getPlayerRole(player, false);
  }
  
  /**
   * Obtém o role de um jogador.
   * 
   * @param player Objeto do jogador
   * @param removeFake Se deve remover fake
   * @return Role do jogador
   */
  public static Role getPlayerRole(Object player, boolean removeFake) {
    if (player == null) {
      return getLastRole();
    }
    
    String playerName = Manager.getName(player);
    
    // Verifica se é um fake
    if (!removeFake && Manager.isFake(playerName)) {
      return Manager.getFakeRole(playerName);
    }
    
    // Busca o role com permissão
    return ROLES.stream()
        .filter(role -> role.has(player))
        .findFirst()
        .orElseGet(Role::getLastRole);
  }
  
  /**
   * Obtém o último role da lista (role padrão).
   * 
   * @return Último role
   */
  public static Role getLastRole() {
    return ROLES.isEmpty() ? null : ROLES.get(ROLES.size() - 1);
  }
  
  /**
   * Obtém todos os roles.
   * 
   * @return Lista de roles
   */
  public static List<Role> listRoles() {
    return ROLES;
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
   * Verifica se este é o role padrão.
   * 
   * @return true se for o role padrão
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
   * Verifica se um jogador tem este role.
   * 
   * @param player Objeto do jogador
   * @return true se o jogador tem este role
   */
  public boolean has(Object player) {
    return this.isDefault() || Manager.hasPermission(player, this.permission);
  }
}
