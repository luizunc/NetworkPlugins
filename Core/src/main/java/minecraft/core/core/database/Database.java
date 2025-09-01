package minecraft.core.core.database;

import minecraft.core.Manager;
import minecraft.core.bukkit.Core;
import minecraft.core.bungee.Bungee;
import minecraft.core.core.database.cache.RankCache;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.exception.ProfileLoadException;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Classe abstrata que define a interface para diferentes tipos de banco de dados.
 * Suporta MySQL, MariaDB e HikariCP.
 */
public abstract class Database {
  
  protected static final Logger LOGGER;
  private static Database instance;
  private static DatabaseChangeListener changeListener;
  
  static {
    LOGGER = Manager.BUNGEE ? Bungee.getInstance().getLogger() : Core.getInstance().getLogger();
  }
  
  /**
   * Configura o banco de dados baseado no tipo especificado.
   * 
   * @param type Tipo do banco de dados (mysql)
   * @param mysqlHost Host do MySQL
   * @param mysqlPort Porta do MySQL
   * @param mysqlDbname Nome do banco MySQL
   * @param mysqlUsername Usuário do MySQL
   * @param mysqlPassword Senha do MySQL
   * @param hikari Se deve usar HikariCP
   * @param mariadb Se deve usar MariaDB
   */
  public static void setupDatabase(String type, String mysqlHost, String mysqlPort, String mysqlDbname, 
                                  String mysqlUsername, String mysqlPassword, boolean hikari, boolean mariadb) {
    if (type == null) {
      throw new IllegalArgumentException("Tipo de banco de dados não pode ser nulo");
    }
    
    instance = createDatabaseInstance(type, mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, 
                                    mysqlPassword, hikari, mariadb);
    
    // Configura limpeza automática do cache de ranks
    scheduleRankCacheCleanup();
    
    // Inicia o monitoramento de mudanças no banco de dados
    startDatabaseChangeMonitoring();
  }
  
  /**
   * Inicia o monitoramento de mudanças no banco de dados.
   */
  private static void startDatabaseChangeMonitoring() {
    if (instance != null) {
      changeListener = new DatabaseChangeListener(instance);
      changeListener.startMonitoring();
      LOGGER.info("Monitoramento de mudanças no banco de dados iniciado com sucesso!");
    }
  }
  
  /**
   * Para o monitoramento de mudanças no banco de dados.
   */
  public static void stopDatabaseChangeMonitoring() {
    if (changeListener != null) {
      changeListener.stopMonitoring();
      changeListener = null;
      LOGGER.info("Monitoramento de mudanças no banco de dados parado!");
    }
  }
  
  /**
   * Verifica se o monitoramento de mudanças está ativo.
   */
  public static boolean isDatabaseChangeMonitoringActive() {
    return changeListener != null && changeListener.isMonitoring();
  }
  
  /**
   * Cria a instância apropriada do banco de dados.
   * 
   * @param type Tipo do banco de dados
   * @param mysqlHost Host do MySQL
   * @param mysqlPort Porta do MySQL
   * @param mysqlDbname Nome do banco MySQL
   * @param mysqlUsername Usuário do MySQL
   * @param mysqlPassword Senha do MySQL
   * @param hikari Se deve usar HikariCP
   * @param mariadb Se deve usar MariaDB
   * @return Instância do banco de dados
   */
  private static Database createDatabaseInstance(String type, String mysqlHost, String mysqlPort, 
                                                String mysqlDbname, String mysqlUsername, String mysqlPassword, 
                                                boolean hikari, boolean mariadb) {
    return createSQLDatabase(mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, hikari, mariadb);
  }
  
  /**
   * Cria uma instância de banco SQL (MySQL/MariaDB).
   * 
   * @param mysqlHost Host do MySQL
   * @param mysqlPort Porta do MySQL
   * @param mysqlDbname Nome do banco MySQL
   * @param mysqlUsername Usuário do MySQL
   * @param mysqlPassword Senha do MySQL
   * @param hikari Se deve usar HikariCP
   * @param mariadb Se deve usar MariaDB
   * @return Instância do banco SQL
   */
  private static Database createSQLDatabase(String mysqlHost, String mysqlPort, String mysqlDbname, 
                                           String mysqlUsername, String mysqlPassword, boolean hikari, boolean mariadb) {
    if (hikari) {
      return new HikariDatabase(mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb);
    } else {
      return new MySQLDatabase(mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb);
    }
  }
  
  /**
   * Agenda a limpeza automática do cache de ranks.
   */
  private static void scheduleRankCacheCleanup() {
    Timer timer = new Timer();
    long initialDelay = TimeUnit.SECONDS.toMillis(60);
    long period = TimeUnit.SECONDS.toMillis(60);
    
    timer.scheduleAtFixedRate(RankCache.clearCache(), initialDelay, period);
  }
  
  /**
   * Obtém a instância singleton do banco de dados.
   * 
   * @return Instância do banco de dados
   * @throws IllegalStateException Se o banco não foi configurado
   */
  public static Database getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Banco de dados não foi configurado. Chame setupDatabase() primeiro.");
    }
    return instance;
  }
  
  /**
   * Converte dados do banco de dados (não suportado para todos os tipos).
   * 
   * @param player Jogador para conversão
   */
  public void convertDatabase(Object player) {
    if (!Manager.BUNGEE) {
      if (player instanceof org.bukkit.entity.Player) {
        ((org.bukkit.entity.Player) player).sendMessage("§cRecurso não suportado para seu tipo de Banco de Dados.");
      }
    }
  }
  
  // Métodos abstratos que devem ser implementados pelas classes filhas
  
  /**
   * Obtém o rank e nome de um jogador.
   * 
   * @param player Nome do jogador
   * @return String contendo rank e nome
   */
  public abstract String getRankAndName(String player);
  
  /**
   * Obtém a tag e nome de um jogador.
   * 
   * @param player Nome do jogador
   * @return String contendo tag e nome
   */
  public abstract String getTagAndName(String player);
  
  /**
   * Obtém uma preferência de um jogador.
   * 
   * @param player Nome do jogador
   * @param id ID da preferência
   * @param def Valor padrão
   * @return Valor da preferência
   */
  public abstract boolean getPreference(String player, String id, boolean def);
  
  /**
   * Obtém o leaderboard de uma tabela.
   * 
   * @param table Nome da tabela
   * @param columns Colunas a serem retornadas
   * @return Lista de arrays com os dados do leaderboard
   */
  public abstract List<String[]> getLeaderBoard(String table, String... columns);
  
  /**
   * Fecha a conexão com o banco de dados.
   */
  public abstract void close();
  
  /**
   * Carrega os dados de um jogador.
   * 
   * @param name Nome do jogador
   * @return Mapa com os dados do jogador
   * @throws ProfileLoadException Se houver erro ao carregar o perfil
   */
  public abstract Map<String, Map<String, DataContainer>> load(String name) throws ProfileLoadException;
  
  /**
   * Salva os dados de um jogador de forma assíncrona.
   * 
   * @param name Nome do jogador
   * @param tableMap Dados a serem salvos
   */
  public abstract void save(String name, Map<String, Map<String, DataContainer>> tableMap);
  
  /**
   * Salva os dados de um jogador de forma síncrona.
   * 
   * @param name Nome do jogador
   * @param tableMap Dados a serem salvos
   */
  public abstract void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap);
  
  /**
   * Verifica se um jogador existe no banco de dados.
   * 
   * @param name Nome do jogador
   * @return Nome do jogador se existir, null caso contrário
   */
  public abstract String exists(String name);
}
