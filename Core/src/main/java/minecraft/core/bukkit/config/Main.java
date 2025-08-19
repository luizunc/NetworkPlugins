package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do config.yml.
 * Contém as configurações principais do plugin com valores padrão.
 * 
 * @author Luiz
 * @version 1.0
 */
public final class Main {
    
    // Configurações do banco de dados
    private static final boolean DATABASE_ENABLED = true;
    private static final String DATABASE_TIPO = "mysql";
    private static final String MYSQL_HOST = "localhost";
    private static final String MYSQL_PORTA = "3306";
    private static final String MYSQL_NOME = "minecraft";
    private static final String MYSQL_USUARIO = "root";
    private static final String MYSQL_SENHA = "";
    private static final boolean MYSQL_HIKARI = true;
    private static final boolean MYSQL_MARIADB = false;
    
    // Configurações do lobby
    private static final String LOBBY_WORLD = "world";
    private static final double LOBBY_X = 0.0;
    private static final double LOBBY_Y = 64.0;
    private static final double LOBBY_Z = 0.0;
    private static final float LOBBY_YAW = 0.0f;
    private static final float LOBBY_PITCH = 0.0f;
    
    // Configurações de mensagens
    private static final String MESSAGE_PREFIX = "&7[&bCore&7] ";
    private static final String MESSAGE_WELCOME = "&aBem-vindo ao servidor!";
    private static final String MESSAGE_GOODBYE = "&cAté logo!";
    private static final String MESSAGE_NO_PERMISSION = "&cVocê não tem permissão!";
    private static final String MESSAGE_PLAYER_ONLY = "&cEste comando só pode ser usado por jogadores!";
    private static final String MESSAGE_RELOAD = "&aPlugin recarregado com sucesso!";
    private static final String MESSAGE_ERROR = "&cOcorreu um erro!";
    
    // Configurações gerais
    private static final boolean SETTINGS_DEBUG = false;
    private static final boolean SETTINGS_AUTO_UPDATE = true;
    private static final int SETTINGS_SAVE_INTERVAL = 300;
    private static final int SETTINGS_MAX_PLAYERS = 100;
    private static final String SETTINGS_MOTD = "&aBem-vindo ao servidor!";
    
    // Configurações de features
    private static final boolean FEATURE_FAKE_NAMES = true;
    private static final boolean FEATURE_FAKE_SKINS = true;
    private static final boolean FEATURE_PARTY_SYSTEM = true;
    private static final boolean FEATURE_ACHIEVEMENTS = true;
    private static final boolean FEATURE_TITLES = true;
    private static final boolean FEATURE_HOTBAR = true;
    private static final boolean FEATURE_SCOREBOARD = true;
    
    // Configurações de performance
    private static final boolean PERFORMANCE_ASYNC_SAVE = true;
    private static final int PERFORMANCE_CACHE_SIZE = 1000;
    private static final int PERFORMANCE_CONNECTION_POOL = 10;
    private static final int PERFORMANCE_TIMEOUT = 30;
    
    /**
     * Construtor privado para evitar instanciação.
     */
    private Main() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
    }
    
    // Métodos para banco de dados
    public static boolean isDatabaseEnabled() {
        return DATABASE_ENABLED;
    }
    
    public static String getDatabaseTipo() {
        return DATABASE_TIPO;
    }
    
    public static String getMysqlHost() {
        return MYSQL_HOST;
    }
    
    public static String getMysqlPorta() {
        return MYSQL_PORTA;
    }
    
    public static String getMysqlNome() {
        return MYSQL_NOME;
    }
    
    public static String getMysqlUsuario() {
        return MYSQL_USUARIO;
    }
    
    public static String getMysqlSenha() {
        return MYSQL_SENHA;
    }
    
    public static boolean isMysqlHikari() {
        return MYSQL_HIKARI;
    }
    
    public static boolean isMysqlMariadb() {
        return MYSQL_MARIADB;
    }
    
    // Métodos para lobby
    public static String getLobbyWorld() {
        return LOBBY_WORLD;
    }
    
    public static double getLobbyX() {
        return LOBBY_X;
    }
    
    public static double getLobbyY() {
        return LOBBY_Y;
    }
    
    public static double getLobbyZ() {
        return LOBBY_Z;
    }
    
    public static float getLobbyYaw() {
        return LOBBY_YAW;
    }
    
    public static float getLobbyPitch() {
        return LOBBY_PITCH;
    }
    
    // Métodos para mensagens
    public static String getMessagePrefix() {
        return MESSAGE_PREFIX;
    }
    
    public static String getMessageWelcome() {
        return MESSAGE_WELCOME;
    }
    
    public static String getMessageGoodbye() {
        return MESSAGE_GOODBYE;
    }
    
    public static String getMessageNoPermission() {
        return MESSAGE_NO_PERMISSION;
    }
    
    public static String getMessagePlayerOnly() {
        return MESSAGE_PLAYER_ONLY;
    }
    
    public static String getMessageReload() {
        return MESSAGE_RELOAD;
    }
    
    public static String getMessageError() {
        return MESSAGE_ERROR;
    }
    
    // Métodos para configurações gerais
    public static boolean isSettingsDebug() {
        return SETTINGS_DEBUG;
    }
    
    public static boolean isSettingsAutoUpdate() {
        return SETTINGS_AUTO_UPDATE;
    }
    
    public static int getSettingsSaveInterval() {
        return SETTINGS_SAVE_INTERVAL;
    }
    
    public static int getSettingsMaxPlayers() {
        return SETTINGS_MAX_PLAYERS;
    }
    
    public static String getSettingsMotd() {
        return SETTINGS_MOTD;
    }
    
    // Métodos para features
    public static boolean isFeatureFakeNames() {
        return FEATURE_FAKE_NAMES;
    }
    
    public static boolean isFeatureFakeSkins() {
        return FEATURE_FAKE_SKINS;
    }
    
    public static boolean isFeaturePartySystem() {
        return FEATURE_PARTY_SYSTEM;
    }
    
    public static boolean isFeatureAchievements() {
        return FEATURE_ACHIEVEMENTS;
    }
    
    public static boolean isFeatureTitles() {
        return FEATURE_TITLES;
    }
    
    public static boolean isFeatureHotbar() {
        return FEATURE_HOTBAR;
    }
    
    public static boolean isFeatureScoreboard() {
        return FEATURE_SCOREBOARD;
    }
    
    // Métodos para performance
    public static boolean isPerformanceAsyncSave() {
        return PERFORMANCE_ASYNC_SAVE;
    }
    
    public static int getPerformanceCacheSize() {
        return PERFORMANCE_CACHE_SIZE;
    }
    
    public static int getPerformanceConnectionPool() {
        return PERFORMANCE_CONNECTION_POOL;
    }
    
    public static int getPerformanceTimeout() {
        return PERFORMANCE_TIMEOUT;
    }
    
    /**
     * Obtém a configuração do config.yml como KConfig (método de compatibilidade).
     * 
     * @return KConfig com as configurações principais
     * @throws RuntimeException Se houver erro ao carregar a configuração
     */
    public static KConfig getConfig() {
        try {
            String yamlContent = convertToYaml();
            InputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar config.yml interno", e);
        }
    }
    
    /**
     * Converte as configurações Java para YAML.
     * 
     * @return String com o conteúdo YAML
     */
    private static String convertToYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("database:\n");
        yaml.append("  enabled: ").append(DATABASE_ENABLED).append("\n");
        yaml.append("  tipo: '").append(DATABASE_TIPO).append("'\n");
        yaml.append("  mysql:\n");
        yaml.append("    host: '").append(MYSQL_HOST).append("'\n");
        yaml.append("    porta: '").append(MYSQL_PORTA).append("'\n");
        yaml.append("    nome: '").append(MYSQL_NOME).append("'\n");
        yaml.append("    usuario: '").append(MYSQL_USUARIO).append("'\n");
        yaml.append("    senha: '").append(MYSQL_SENHA).append("'\n");
        yaml.append("    hikari: ").append(MYSQL_HIKARI).append("\n");
        yaml.append("    mariadb: ").append(MYSQL_MARIADB).append("\n");
        yaml.append("\n");
        yaml.append("lobby:\n");
        yaml.append("  world: '").append(LOBBY_WORLD).append("'\n");
        yaml.append("  x: ").append(LOBBY_X).append("\n");
        yaml.append("  y: ").append(LOBBY_Y).append("\n");
        yaml.append("  z: ").append(LOBBY_Z).append("\n");
        yaml.append("  yaw: ").append(LOBBY_YAW).append("\n");
        yaml.append("  pitch: ").append(LOBBY_PITCH).append("\n");
        yaml.append("\n");
        yaml.append("messages:\n");
        yaml.append("  prefix: '").append(MESSAGE_PREFIX).append("'\n");
        yaml.append("  welcome: '").append(MESSAGE_WELCOME).append("'\n");
        yaml.append("  goodbye: '").append(MESSAGE_GOODBYE).append("'\n");
        yaml.append("  no-permission: '").append(MESSAGE_NO_PERMISSION).append("'\n");
        yaml.append("  player-only: '").append(MESSAGE_PLAYER_ONLY).append("'\n");
        yaml.append("  reload: '").append(MESSAGE_RELOAD).append("'\n");
        yaml.append("  error: '").append(MESSAGE_ERROR).append("'\n");
        yaml.append("\n");
        yaml.append("settings:\n");
        yaml.append("  debug: ").append(SETTINGS_DEBUG).append("\n");
        yaml.append("  auto-update: ").append(SETTINGS_AUTO_UPDATE).append("\n");
        yaml.append("  save-interval: ").append(SETTINGS_SAVE_INTERVAL).append("\n");
        yaml.append("  max-players: ").append(SETTINGS_MAX_PLAYERS).append("\n");
        yaml.append("  motd: '").append(SETTINGS_MOTD).append("'\n");
        yaml.append("  \n");
        yaml.append("  features:\n");
        yaml.append("    fake-names: ").append(FEATURE_FAKE_NAMES).append("\n");
        yaml.append("    fake-skins: ").append(FEATURE_FAKE_SKINS).append("\n");
        yaml.append("    party-system: ").append(FEATURE_PARTY_SYSTEM).append("\n");
        yaml.append("    achievements: ").append(FEATURE_ACHIEVEMENTS).append("\n");
        yaml.append("    titles: ").append(FEATURE_TITLES).append("\n");
        yaml.append("    hotbar: ").append(FEATURE_HOTBAR).append("\n");
        yaml.append("    scoreboard: ").append(FEATURE_SCOREBOARD).append("\n");
        yaml.append("    \n");
        yaml.append("  performance:\n");
        yaml.append("    async-save: ").append(PERFORMANCE_ASYNC_SAVE).append("\n");
        yaml.append("    cache-size: ").append(PERFORMANCE_CACHE_SIZE).append("\n");
        yaml.append("    connection-pool: ").append(PERFORMANCE_CONNECTION_POOL).append("\n");
        yaml.append("    timeout: ").append(PERFORMANCE_TIMEOUT);
        
        return yaml.toString();
    }
    
    /**
     * Obtém o conteúdo YAML como string (método de compatibilidade).
     * 
     * @return String com o conteúdo do config.yml
     */
    public static String getYamlContent() {
        return convertToYaml();
    }
}
