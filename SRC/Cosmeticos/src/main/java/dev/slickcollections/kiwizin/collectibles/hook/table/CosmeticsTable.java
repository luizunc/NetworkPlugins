package dev.slickcollections.kiwizin.collectibles.hook.table;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "kCosmetics",
    create = "CREATE TABLE IF NOT EXISTS `kCosmetics` (`name` VARCHAR(32), `cosmetics` TEXT, `selected` TEXT, `petSettings` TEXT, `companionsNames` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCosmetics` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCosmetics` VALUES (?, ?, ?, ?, ?)",
    update = "UPDATE `kCosmetics` SET `cosmetics` = ?, `selected` = ?, `petSettings` = ?, `companionsNames` = ? WHERE LOWER(`name`) = ?"
)
public class CosmeticsTable extends DataTable {
  
  public void init(Database database) {
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("petSettings", new DataContainer("{}"));
    defaultValues.put("companionsNames", new DataContainer("{}"));
    return defaultValues;
  }
}
