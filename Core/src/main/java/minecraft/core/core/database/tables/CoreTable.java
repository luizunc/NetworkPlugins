package minecraft.core.core.database.tables;

import minecraft.core.core.database.Database;
import minecraft.core.core.database.HikariDatabase;
import minecraft.core.core.database.MySQLDatabase;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.data.DataTable;
import minecraft.core.core.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "account",
    create = "CREATE TABLE IF NOT EXISTS `account` (`name` VARCHAR(32), `cash` LONG, `rank` TEXT, `tag` TEXT, `medalha` TEXT, `preferences` TEXT, `titles` TEXT, `achievements` TEXT, `selected` TEXT, `skins` TEXT, `created` LONG, `clan` TEXT, `lastlogin` LONG, `entryanimation` TEXT, `entrymessage` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `account` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `account` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
          update = "UPDATE `account` SET `cash` = ?, `rank` = ?, `tag` = ?, `medalha` = ?, `preferences` = ?, `titles` = ?, `achievements` = ?, `selected` = ?, `skins` = ?, `created` = ?, `clan` = ?, `lastlogin` = ?, `entryanimation` = ?, `entrymessage` = ? WHERE LOWER(`name`) = ?"
)
public class CoreTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'cash'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `cash` LONG AFTER `name`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'entryanimation'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `entryanimation` TEXT AFTER `lastlogin`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'entrymessage'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `entrymessage` TEXT AFTER `entryanimation`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'tag'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `tag` TEXT AFTER `rank`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'medalha'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `medalha` TEXT AFTER `tag`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'skins'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `account` ADD `skins` TEXT AFTER `selected`");
      }
    } else if (database instanceof HikariDatabase) {
              if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'cash'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `cash` LONG AFTER `name`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'entryanimation'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `entryanimation` TEXT AFTER `lastlogin`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'entrymessage'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `entrymessage` TEXT AFTER `entryanimation`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'tag'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `tag` TEXT AFTER `rank`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'medalha'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `medalha` TEXT AFTER `tag`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `account` LIKE 'skins'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `account` ADD `skins` TEXT AFTER `selected`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
            defaultValues.put("rank", new DataContainer("Membro"));
    defaultValues.put("tag", new DataContainer(""));
    defaultValues.put("medalha", new DataContainer(""));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"pl\": 0, \"ss\": 0}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{\"title\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("skins", new DataContainer("{}"));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("clan", new DataContainer(""));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("entryanimation", new DataContainer("[]"));
    defaultValues.put("entrymessage", new DataContainer("1"));
    return defaultValues;
  }
}
