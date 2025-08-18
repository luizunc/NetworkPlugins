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
    name = "CoreProfile",
    create = "CREATE TABLE IF NOT EXISTS `CoreProfile` (`name` VARCHAR(32), `cash` LONG, `role` TEXT, `preferences` TEXT, `titles` TEXT, `achievements` TEXT, `selected` TEXT, `created` LONG, `clan` TEXT, `lastlogin` LONG, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `CoreProfile` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `CoreProfile` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
  update = "UPDATE `CoreProfile` SET `cash` = ?, `role` = ?, `preferences` = ?, `titles` = ?, `achievements` = ?, `selected` = ?, `created` = ?, `clan` = ?, `lastlogin` = ? WHERE LOWER(`name`) = ?"
)
public class CoreTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `CoreProfile` LIKE 'cash'") == null) {
                  ((MySQLDatabase) database).execute("ALTER TABLE `CoreProfile` ADD `cash` LONG AFTER `name`");
      }
    } else if (database instanceof HikariDatabase) {
              if (((HikariDatabase) database).query("SHOW COLUMNS FROM `CoreProfile` LIKE 'cash'") == null) {
                  ((HikariDatabase) database).execute("ALTER TABLE `CoreProfile` ADD `cash` LONG AFTER `name`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"pl\": 0}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{\"title\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("clan", new DataContainer(""));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    return defaultValues;
  }
}
