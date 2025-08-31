package minecraft.core.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import minecraft.core.bungee.cmd.Commands;
import minecraft.core.bungee.listener.Listeners;
import minecraft.core.core.database.Database;
import minecraft.core.core.player.rank.Rank;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import minecraft.core.bukkit.plugin.config.UtilsConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Classe principal do BungeeCord para o sistema Core.
 * Gerencia funcionalidades de nick names, roles e skins
 * 
 * @author Luiz
 * @version 1.0
 */
public class Bungee extends Plugin {
  
  // Constantes
  private static final String STEVE = "eyJ0aW1lc3RhbXAiOjE1ODcxNTAzMTc3MjAsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWRJOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIn19fQ==";
  private static final String ALEX = "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==";
  
  // Subcanais para comunicação
  private static final String CORE_CHANNEL = "Core";
  private static final String NICK_BOOK_SUBCHANNEL = "NICK_BOOK";
  private static final String NICK_BOOK2_SUBCHANNEL = "NICK_BOOK2";
  
  // Instância única
  private static Bungee instance;
  
  // Maps thread-safe para nick names, ranks e skins
  private static final Map<String, String> nickNames = new ConcurrentHashMap<>();
  private static final Map<String, Rank> nickRanks = new ConcurrentHashMap<>();
  private static final Map<String, String> nickSkins = new ConcurrentHashMap<>();
  
  // Cache de nomes aleatórios
  private static volatile List<String> randoms;
  
  // Configurações
  private static final List<String> NICK_RANKS_LIST = Arrays.asList("Membro", "VIP", "MVP");
  private static final String KICK_APPLY_MESSAGE = "§cVocê foi desconectado para aplicar o nick.";
  private static final String KICK_REMOVE_MESSAGE = "§cVocê foi desconectado para remover o nick.";
  
  // Configurações
  private volatile Configuration config;
  private volatile Configuration utils;
  
  public Bungee() {
    instance = this;
  }
  
  public static Bungee getInstance() {
    return instance;
  }

  /**
   * Envia rank para um jogador
   * 
   * @param player Jogador alvo
   * @param sound Som a ser reproduzido (opcional)
   */
  public static void sendRole(ProxiedPlayer player, String sound) {
    if (player == null || !player.isConnected()) {
      return;
    }
    
    try {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF(NICK_BOOK_SUBCHANNEL);
      out.writeUTF(player.getName());
      if (sound != null) {
        out.writeUTF(sound);
      }
      player.getServer().sendData(CORE_CHANNEL, out.toByteArray());
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao enviar rank para " + player.getName(), e);
    }
  }

  /**
   * Envia skin para um jogador
   * 
   * @param player Jogador alvo
   * @param roleName Nome do rank
   * @param sound Som a ser reproduzido (opcional)
   */
  public static void sendSkin(ProxiedPlayer player, String roleName, String sound) {
    if (player == null || !player.isConnected() || roleName == null) {
      return;
    }
    
    try {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF(NICK_BOOK2_SUBCHANNEL);
      out.writeUTF(player.getName());
      out.writeUTF(roleName);
      if (sound != null) {
        out.writeUTF(sound);
      }
      player.getServer().sendData(CORE_CHANNEL, out.toByteArray());
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao enviar skin para " + player.getName(), e);
    }
  }

  /**
   * Aplica nick name para um jogador
   * 
   * @param player Jogador que receberá o nick
   * @param nickName Nome nick
   * @param role Role nick
   * @param skin Skin nick
   */
  public static void applyNick(ProxiedPlayer player, String nickName, String role, String skin) {
    try {
      String playerName = player.getName();
      
      // Aplica o nick
      nickNames.put(playerName, nickName);
      nickRanks.put(playerName, Rank.getRankByName(role));
      nickSkins.put(playerName, skin);
      
      // Desconecta o jogador para aplicar as mudanças
      player.disconnect(new TextComponent(KICK_APPLY_MESSAGE));
      
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao aplicar nick para " + player.getName(), e);
    }
  }
  
  /**
   * Remove nick name de um jogador
   * 
   * @param player Jogador que terá o nick removido
   */
  public static void removeNick(ProxiedPlayer player) {
    try {
      String playerName = player.getName();
      
      // Remove o nick
      nickNames.remove(playerName);
      nickRanks.remove(playerName);
      nickSkins.remove(playerName);
      
      // Desconecta o jogador para aplicar as mudanças
      player.disconnect(new TextComponent(KICK_REMOVE_MESSAGE));
      
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao remover nick de " + player.getName(), e);
    }
  }
  
  /**
   * Obtém o nome atual de um jogador (real ou nick)
   * 
   * @param playerName Nome do jogador
   * @return Nome atual do jogador
   */
  public static String getCurrent(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    
    return isNick(playerName) ? getNick(playerName) : playerName;
  }
  
  /**
   * Obtém o nome nick de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Nome nick ou null
   */
  public static String getNick(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    
    return nickNames.get(playerName);
  }
  
  /**
   * Obtém o rank nick de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Role nick ou rank padrão
   */
  public static Rank getRank(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return Rank.getLastRole();
    }
    
    return nickRanks.getOrDefault(playerName, Rank.getLastRole());
  }
  
  /**
   * Obtém a skin nick de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Skin nick ou skin padrão
   */
  public static String getSkin(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return STEVE;
    }
    
    return nickSkins.getOrDefault(playerName, STEVE);
  }
  
  /**
   * Verifica se um jogador está usando nome nick
   * 
   * @param playerName Nome do jogador
   * @return true se está usando nome nick
   */
  public static boolean isNick(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return false;
    }
    
    return nickNames.containsKey(playerName);
  }
  
  /**
   * Verifica se um nome está disponível para uso
   * 
   * @param name Nome a ser verificado
   * @return true se está disponível
   */
  public static boolean isUsable(String name) {
    return checkNicknameAvailability(name) == null;
  }
  
  /**
   * Verifica a disponibilidade de um nickname para nick.
   * 
   * @param name Nome a ser verificado
   * @return Mensagem de erro ou null se disponível
   */
  public static String checkNicknameAvailability(String name) {
    // Verifica se já está sendo usado por outro nick
    if (nickNames.containsKey(name) || nickNames.containsValue(name)) {
      return "§cO nickname não está disponível para uso.";
    }
    
    // Verifica se está online no servidor
    if (getInstance().getProxy().getPlayer(name) != null) {
      return "§cO nickname não está disponível para uso.";
    }
    
    return null; // Nickname disponível
  }
  
  /**
   * Lista jogadores com nome nick
   * 
   * @return Lista de jogadores com nick
   */
  public static List<String> listNicked() {
    if (nickNames == null || nickNames.isEmpty()) {
      return new ArrayList<>();
    }
    
    return new ArrayList<>(nickNames.keySet());
  }
  
  /**
   * Obtém lista de nomes aleatórios
   * 
   * @return Lista de nomes aleatórios
   */
  public static List<String> getRandomNicks() {
    if (randoms == null) {
      synchronized (Bungee.class) {
        if (randoms == null) {
          try {
            randoms = new ArrayList<>();
          } catch (Exception e) {
            getInstance().getLogger().log(Level.WARNING, "Erro ao carregar nomes aleatórios", e);
            randoms = new ArrayList<>();
          }
        }
      }
    }
    return new ArrayList<>(randoms);
  }
  
  /**
   * Verifica se um rank é válido para nick
   * 
   * @param roleName Nome do rank
   * @return true se o rank é válido
   */
  public static boolean isNickRole(String roleName) {
    if (roleName == null || roleName.trim().isEmpty()) {
      return false;
    }
    
    return NICK_RANKS_LIST.stream().anyMatch(rank -> rank.equalsIgnoreCase(roleName));
  }
  
  /**
   * Copia um arquivo a partir de um InputStream
   * 
   * @param input InputStream de origem
   * @param output Arquivo de destino
   */
  public static void copyFile(InputStream input, File output) {
    if (input == null || output == null) {
      return;
    }
    
    try (FileOutputStream fileOutput = new FileOutputStream(output)) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = input.read(buffer)) > 0) {
        fileOutput.write(buffer, 0, length);
      }
    } catch (IOException e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao copiar arquivo " + output.getName(), e);
    } finally {
      try {
        input.close();
      } catch (IOException ignore) {
        // Ignorar erro ao fechar
      }
    }
  }
  
  @Override
  public void onEnable() {
    try {
      // Salvar configurações padrão
      saveDefaultConfig();
      
      // Configurar banco de dados
      setupDatabase();
      
      // Configurar roles
      setupRoles();
      
      // Configurar comandos e listeners
      Commands.setupCommands();
      getProxy().getPluginManager().registerListener(this, new Listeners());
      
      // Registrar canal
      getProxy().registerChannel(CORE_CHANNEL);
      
      this.getLogger().info("O plugin foi ativado com sucesso.");
      
    } catch (Exception e) {
      this.getLogger().log(Level.SEVERE, "Erro crítico ao ativar o plugin", e);
    }
  }
  
  @Override
  public void onDisable() {
    try {
      this.getLogger().info("O plugin foi desativado com sucesso.");
    } catch (Exception e) {
      this.getLogger().log(Level.SEVERE, "Erro ao desativar o plugin", e);
    }
  }
  
  /**
   * Salva configurações padrão
   */
  public void saveDefaultConfig() {
    try {
      // Carregar config.yml do arquivo
      File configFile = new File("plugins/Core/config.yml");
      if (!configFile.exists()) {
        configFile.getParentFile().mkdirs();
        copyFile(getResourceAsStream("config.yml"), configFile);
      }
      
      this.config = YamlConfiguration.getProvider(YamlConfiguration.class)
          .load(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
      
      // Carregar bungee.yml do arquivo
      File bungeeFile = new File("plugins/Core/bungee.yml");
      if (!bungeeFile.exists()) {
        bungeeFile.getParentFile().mkdirs();
        copyFile(getResourceAsStream("bungee.yml"), bungeeFile);
      }
      
      // Carregar configurações utils da classe interna
      this.utils = UtilsConfig.getConfig().getConfiguration();
      
    } catch (Exception e) {
      this.getLogger().log(Level.WARNING, "Erro ao carregar configurações", e);
    }
  }
  
  /**
   * Configura o banco de dados
   */
  private void setupDatabase() {
    if (config == null) {
      this.getLogger().warning("Configuração não encontrada, não foi possível configurar o banco de dados");
      return;
    }
    
    Database.setupDatabase(
        config.getString("database.tipo"),
        config.getString("database.mysql.host"),
        config.getString("database.mysql.porta"),
        config.getString("database.mysql.nome"),
        config.getString("database.mysql.usuario"),
        config.getString("database.mysql.senha"),
        config.getBoolean("database.mysql.hikari", false),
                config.getBoolean("database.mysql.mariadb", false)
      );
  }
  
  /**
   * Configura os roles padrão
   */
  private void setupRoles() {
    if (utils == null) {
      this.getLogger().warning("Configuração utils não encontrada");
      return;
    }
    
    // Carregar ranks usando o método do aCore
    Rank.loadRanks();
  }
  
  /**
   * Obtém a configuração principal
   */
  public Configuration getConfig() {
    return utils;
  }
}
