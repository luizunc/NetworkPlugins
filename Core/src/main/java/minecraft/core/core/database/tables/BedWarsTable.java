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

@DataTableInfo(name = "bedwars",
    create = "CREATE TABLE IF NOT EXISTS `bedwars` (`name` VARCHAR(32), `" +
        "solokills` LONG, `solodeaths` LONG, `sologames` LONG, `solobedsdestroyeds` LONG, `solobedslosteds` LONG, `solofinalkills` LONG, `solofinaldeaths` LONG, `solowins` LONG, `duokills` LONG, `duodeaths` LONG, `duogames` LONG, `duobedsdestroyeds` LONG, `duobedslosteds` LONG, `duofinalkills` LONG, `duofinaldeaths` LONG, `duowins` LONG, `4v4kills` LONG," +
        " `4v4deaths` LONG, `4v4games` LONG, `4v4bedsdestroyeds` LONG, `4v4bedslosteds` LONG, `4v4finalkills` LONG, `4v4finaldeaths` LONG, `4v4wins` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlyassists` LONG, `monthlybeds` LONG, `monthlywins` LONG, `monthlygames` LONG, `month` TEXT, `coins` DOUBLE," +
        " `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `favorites` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `bedwars` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `bedwars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `bedwars` SET `solokills` = ?, `solodeaths` = ?, `sologames` = ?, `solobedsdestroyeds` = ?, `solobedslosteds` = ?, `solofinalkills` = ?, `solofinaldeaths` = ?, `solowins` = ?, `duokills` = ?, `duodeaths` = ?, `duogames` = ?, `duobedsdestroyeds` = ?, `duobedslosteds` = ?, `duofinalkills` = ?, `duofinaldeaths` = ?, `duowins` = ?, `4v4kills` = ?, `4v4deaths` = ?, `4v4games` = ?, `4v4bedsdestroyeds` = ?, `4v4bedslosteds` = ?, `4v4finalkills` = ?, `4v4finaldeaths` = ?, `monthlykills` = ?, `monthlydeaths` = ?, `monthlyassists` = ?, `monthlybeds` = ?, `monthlywins` = ?, `monthlygames` = ?, `month` = ?, `4v4wins` = ?, `coins` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `favorites` = ? WHERE LOWER(`name`) = ?")
public class BedWarsTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `bedwars` LIKE 'lastmap'") == null) {
                  ((MySQLDatabase) database).execute(
              "ALTER TABLE `bedwars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`");
      }
    } else if (database instanceof HikariDatabase) {
              if (((HikariDatabase) database).query("SHOW COLUMNS FROM `bedwars` LIKE 'lastmap'") == null) {
                  ((HikariDatabase) database).execute(
              "ALTER TABLE `bedwars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    for (String key : new String[]{"solo", "duo", "4v4"}) {
      defaultValues.put(key + "kills", new DataContainer(0L));
      defaultValues.put(key + "deaths", new DataContainer(0L));
      defaultValues.put(key + "games", new DataContainer(0L));
      defaultValues.put(key + "bedsdestroyeds", new DataContainer(0L));
      defaultValues.put(key + "bedslosteds", new DataContainer(0L));
      defaultValues.put(key + "finalkills", new DataContainer(0L));
      defaultValues.put(key + "finaldeaths", new DataContainer(0L));
      defaultValues.put(key + "wins", new DataContainer(0L));
    }
    for (String key : new String[]{"kills", "deaths",
        "assists", "beds", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("favorites", new DataContainer("{}"));
    return defaultValues;
  }
}