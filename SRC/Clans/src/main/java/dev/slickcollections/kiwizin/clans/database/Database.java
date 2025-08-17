package dev.slickcollections.kiwizin.clans.database;

import dev.slickcollections.kiwizin.clans.clan.Clan;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.util.List;

public abstract class Database {
  
  private static Database instance;
  
  public static void setupDatabase() {
    instance = new MySQLDatabase();
    
    Clan.setupClans();
  }
  
  public static Database getInstance() {
    return instance;
  }
  
  public abstract void closeConnection();
  
  public abstract void update(String sql, Object... vars);
  
  public abstract void execute(String sql, Object... vars);
  
  public abstract CachedRowSet query(String sql, Object... vars);
  
  public abstract Connection getConnection();
  
  public abstract List<String[]> getLeaderBoard(String table, String... columns);
}
