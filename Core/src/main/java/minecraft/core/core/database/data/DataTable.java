package minecraft.core.core.database.data;

import minecraft.core.core.database.Database;
import minecraft.core.core.database.data.interfaces.DataTableInfo;
import minecraft.core.core.database.tables.*;
import minecraft.core.core.database.tables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DataTable {
  
  private static final List<DataTable> TABLES = new ArrayList<>();
  
  static {
    TABLES.add(new CoreTable());
    TABLES.add(new SkyWarsTable());
    TABLES.add(new BedWarsTable());
  }
  
  public static void registerTable(DataTable table) {
    TABLES.add(table);
  }
  
  public static Collection<DataTable> listTables() {
    return TABLES;
  }
  
  public abstract void init(Database database);
  
  public abstract Map<String, DataContainer> getDefaultValues();
  
  public DataTableInfo getInfo() {
    return this.getClass().getAnnotation(DataTableInfo.class);
  }
}
