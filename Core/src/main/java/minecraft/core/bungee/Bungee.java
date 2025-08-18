package minecraft.core.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import minecraft.core.bungee.cmd.Commands;
import minecraft.core.bungee.listener.Listeners;
import minecraft.core.core.database.Database;
import minecraft.core.core.player.role.Role;
import minecraft.core.core.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import minecraft.core.bukkit.plugin.config.UtilsConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Classe principal do plugin Core para BungeeCord
 * Gerencia funcionalidades de fake names, roles e skins
 */
public class Bungee extends Plugin {
  
  // Constantes
  public static final String STEVE =
      "eyJ0aW1lc3RhbXAiOjE1ODcxNTAzMTc3MjAsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIn19fQ==:syZ2Mt1vQeEjh/t8RGbv810mcfTrhQvnwEV7iLCd+5udVeroTa5NjoUehgswacTML3k/KxHZHaq4o6LmACHwsj/ivstW4PWc2RmVn+CcOoDKI3ytEm70LvGz0wAaTVKkrXHSw/RbEX/b7g7oQ8F67rzpiZ1+Z3TKaxbgZ9vgBQZQdwRJjVML2keI0669a9a1lWq3V/VIKFZc1rMJGzETMB2QL7JVTpQFOH/zXJGA+hJS5bRol+JG3LZTX93+DililM1e8KEjKDS496DYhMAr6AfTUfirLAN1Jv+WW70DzIpeKKXWR5ZeI+9qf48+IvjG8DhRBVFwwKP34DADbLhuebrolF/UyBIB9sABmozYdfit9uIywWW9+KYgpl2EtFXHG7CltIcNkbBbOdZy0Qzq62Tx6z/EK2acKn4oscFMqrobtioh5cA/BCRb9V4wh0fy5qx6DYHyRBdzLcQUfb6DkDx1uyNJ7R5mO44b79pSo8gdd9VvMryn/+KaJu2UvyCrMVUtOOzoIh4nCMc9wXOFW3jZ7ZTo4J6c28ouL98rVQSAImEd/P017uGvWIT+hgkdXnacVG895Y6ilXqJToyvf1JUQb4dgry0WTv6UTAjNgrm5a8mZx9OryLuI2obas97LCon1rydcNXnBtjUk0TUzdrvIa5zNstYZPchUb+FSnU=";
  public static final String ALEX =
      "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";
  
  // Canais de plugin
  private static final String CORE_CHANNEL = "Core";
  private static final String FAKE_BOOK_SUBCHANNEL = "FAKE_BOOK";
  private static final String FAKE_BOOK2_SUBCHANNEL = "FAKE_BOOK2";
  
  // Instância singleton
  private static volatile Bungee instance;
  
  // Maps thread-safe para fake names, roles e skins
  private static final Map<String, String> fakeNames = new ConcurrentHashMap<>();
  private static final Map<String, Role> fakeRoles = new ConcurrentHashMap<>();
  private static final Map<String, String> fakeSkins = new ConcurrentHashMap<>();
  
  // Cache de nomes aleatórios
  private static volatile List<String> randoms;
  
  // Configurações internas
  private static final List<String> FAKE_ROLES_LIST = Arrays.asList("Membro", "VIP", "MVP");
  private static final String KICK_APPLY_MESSAGE = "§cVocê foi desconectado para aplicar o fake.";
  private static final String KICK_REMOVE_MESSAGE = "§cVocê foi desconectado para remover o fake.";
  
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
   * Envia role para um jogador
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
      out.writeUTF(FAKE_BOOK_SUBCHANNEL);
      out.writeUTF(player.getName());
      if (sound != null) {
        out.writeUTF(sound);
      }
      player.getServer().sendData(CORE_CHANNEL, out.toByteArray());
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao enviar role para " + player.getName(), e);
    }
  }

  /**
   * Envia skin para um jogador
   * 
   * @param player Jogador alvo
   * @param roleName Nome do role
   * @param sound Som a ser reproduzido (opcional)
   */
  public static void sendSkin(ProxiedPlayer player, String roleName, String sound) {
    if (player == null || !player.isConnected() || roleName == null) {
      return;
    }
    
    try {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF(FAKE_BOOK2_SUBCHANNEL);
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
   * Aplica fake name para um jogador
   * 
   * @param player Jogador alvo
   * @param fakeName Nome fake
   * @param role Role fake
   * @param skin Skin fake
   */
  public static void applyFake(ProxiedPlayer player, String fakeName, String role, String skin) {
    if (player == null || !player.isConnected()) {
      return;
    }
    
    try {
      player.disconnect(TextComponent.fromLegacyText(KICK_APPLY_MESSAGE));
      
      String playerName = player.getName();
      fakeNames.put(playerName, fakeName);
      fakeRoles.put(playerName, Role.getRoleByName(role));
      fakeSkins.put(playerName, skin);
      
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao aplicar fake para " + player.getName(), e);
    }
  }

  /**
   * Remove fake name de um jogador
   * 
   * @param player Jogador alvo
   */
  public static void removeFake(ProxiedPlayer player) {
    if (player == null || !player.isConnected()) {
      return;
    }
    
    try {
      player.disconnect(TextComponent.fromLegacyText(KICK_REMOVE_MESSAGE));
      
      String playerName = player.getName();
      fakeNames.remove(playerName);
      fakeRoles.remove(playerName);
      fakeSkins.remove(playerName);
      
    } catch (Exception e) {
      getInstance().getLogger().log(Level.WARNING, "Erro ao remover fake de " + player.getName(), e);
    }
  }
  
  /**
   * Obtém o nome atual de um jogador (real ou fake)
   * 
   * @param playerName Nome do jogador
   * @return Nome atual
   */
  public static String getCurrent(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    return isFake(playerName) ? getFake(playerName) : playerName;
  }
  
  /**
   * Obtém o nome fake de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Nome fake ou null
   */
  public static String getFake(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return null;
    }
    return fakeNames.get(playerName);
  }
  
  /**
   * Obtém o role fake de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Role fake ou role padrão
   */
  public static Role getRole(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return Role.getLastRole();
    }
    return fakeRoles.getOrDefault(playerName, Role.getLastRole());
  }
  
  /**
   * Obtém a skin fake de um jogador
   * 
   * @param playerName Nome do jogador
   * @return Skin fake ou skin padrão
   */
  public static String getSkin(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return STEVE;
    }
    return fakeSkins.getOrDefault(playerName, STEVE);
  }
  
  /**
   * Verifica se um jogador está usando nome fake
   * 
   * @param playerName Nome do jogador
   * @return true se está usando nome fake
   */
  public static boolean isFake(String playerName) {
    if (playerName == null || playerName.trim().isEmpty()) {
      return false;
    }
    return fakeNames.containsKey(playerName);
  }
  
  /**
   * Verifica se um nome está disponível para uso
   * 
   * @param name Nome a ser verificado
   * @return true se está disponível
   */
  public static boolean isUsable(String name) {
    if (name == null || name.trim().isEmpty()) {
      return false;
    }
    
    return !fakeNames.containsKey(name) && 
           !fakeNames.containsValue(name) && 
           getInstance().getProxy().getPlayer(name) == null;
  }
  
  /**
   * Lista jogadores com nome fake
   * 
   * @return Lista de nomes
   */
  public static List<String> listNicked() {
    return new ArrayList<>(fakeNames.keySet());
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
            randoms = getInstance().getConfig().getStringList("fake.randoms");
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
   * Verifica se um role é válido para fake
   * 
   * @param roleName Nome do role
   * @return true se é válido
   */
  public static boolean isFakeRole(String roleName) {
    if (roleName == null || roleName.trim().isEmpty()) {
      return false;
    }
    
    return FAKE_ROLES_LIST.stream().anyMatch(role -> role.equalsIgnoreCase(roleName));
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
      
      // Carregar utils.yml da classe interna
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
        config.getBoolean("database.mysql.mariadb", false),
        config.getString("database.mongodb.url", "")
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
    
    try {
      // Converter role único para lista se necessário
      if (utils.get("fake.role") instanceof String) {
        String roleString = utils.getString("fake.role");
        utils.set("fake.role", Arrays.asList(roleString));
        YamlConfiguration.getProvider(YamlConfiguration.class)
            .save(utils, new File("plugins/Core/utils.yml"));
      }
    } catch (IOException e) {
      this.getLogger().log(Level.WARNING, "Erro ao configurar roles", e);
    }
    
    // Criar role padrão se não houver nenhum
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
    }
  }
  
  /**
   * Obtém a configuração principal
   */
  public Configuration getConfig() {
    return utils;
  }
}
