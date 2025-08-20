package minecraft.core.core.database.tables;

import minecraft.core.core.database.Database;
import minecraft.core.core.database.HikariDatabase;
import minecraft.core.core.database.MySQLDatabase;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.data.DataTable;
import minecraft.core.core.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "skywars",
    create = "CREATE TABLE IF NOT EXISTS `skywars` (`name` VARCHAR(32), `solokills` LONG, `solodeaths` LONG, `soloassists` LONG, `sologames` LONG, `solowins` LONG, `duokills` LONG, `duodeaths` LONG, `duoassists` LONG, `duogames` LONG, `duowins` LONG, `rankedkills` LONG, `rankeddeaths` LONG, `rankedassists` LONG, `rankedgames` LONG, `rankedwins` LONG, `rankedpoints` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlypoints` LONG, `monthlyassists` LONG, `monthlywins` LONG, `monthlygames` LONG, `month` TEXT, `coins` DOUBLE, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `kitconfig` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `skywars` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `skywars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `skywars` SET `solokills` = ?, `solodeaths` = ?, `soloassists` = ?, `sologames` = ?, `solowins` = ?, `duokills` = ?, `duodeaths` = ?, `duoassists` = ?, `duogames` = ?, `duowins` = ?, `rankedkills` = ?, `rankeddeaths` = ?, `rankedassists` = ?, `rankedgames` = ?, `rankedwins` = ?, `rankedpoints` = ?, `monthlykills` = ?, `monthlydeaths` = ?, `monthlypoints` = ?, `monthlyassists` = ?, `monthlywins` = ?, `monthlygames` = ?, `month` = ?, `coins` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `kitconfig` = ? WHERE LOWER(`name`) = ?"
)
public class SkyWarsTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `skywars` LIKE 'lastmap'") == null) {
                  ((MySQLDatabase) database).execute(
              "ALTER TABLE `skywars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
    } else if (database instanceof HikariDatabase) {
              if (((HikariDatabase) database).query("SHOW COLUMNS FROM `skywars` LIKE 'lastmap'") == null) {
                  ((HikariDatabase) database).execute(
              "ALTER TABLE `skywars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("solokills", new DataContainer(0L));
    defaultValues.put("solodeaths", new DataContainer(0L));
    defaultValues.put("soloassists", new DataContainer(0L));
    defaultValues.put("sologames", new DataContainer(0L));
    defaultValues.put("solowins", new DataContainer(0L));
    defaultValues.put("duokills", new DataContainer(0L));
    defaultValues.put("duodeaths", new DataContainer(0L));
    defaultValues.put("duoassists", new DataContainer(0L));
    defaultValues.put("duogames", new DataContainer(0L));
    defaultValues.put("duowins", new DataContainer(0L));
    defaultValues.put("rankedkills", new DataContainer(0L));
    defaultValues.put("rankeddeaths", new DataContainer(0L));
    defaultValues.put("rankedassists", new DataContainer(0L));
    defaultValues.put("rankedgames", new DataContainer(0L));
    defaultValues.put("rankedwins", new DataContainer(0L));
    defaultValues.put("rankedpoints", new DataContainer(0L));
    for (String key : new String[]{"kills", "deaths", "points",
        "assists", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("kitconfig", new DataContainer("{}"));
    return defaultValues;
  }
}
