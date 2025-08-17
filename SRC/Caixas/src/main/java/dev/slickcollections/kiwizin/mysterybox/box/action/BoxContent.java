package dev.slickcollections.kiwizin.mysterybox.box.action;

import com.mongodb.client.MongoCursor;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MongoDBDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.mysterybox.Main;
import dev.slickcollections.kiwizin.mysterybox.hook.container.MysteryBoxRewardedContainer;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class BoxContent {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("LOOT_REWARD");
  public static final BoxContent ALL =
      new BoxContent("", "", EnumRarity.COMUM, new BoxContentAction("", ""), "");
  private static final List<BoxContent> CONTENTS = new ArrayList<>();
  
  private final String id;
  private final String name;
  private final String dependency;
  private final EnumRarity rarity;
  private final BoxContentAction action;
  
  public BoxContent(String id, String name, EnumRarity rarity, BoxContentAction action, String dependency) {
    this.id = id;
    this.name = name;
    this.rarity = rarity;
    this.dependency = dependency;
    this.action = action;
  }
  
  public static void setupRewards() {
    CONTENTS.clear();
    if (Database.getInstance() instanceof MongoDBDatabase) {
      MongoDBDatabase database = (MongoDBDatabase) Database.getInstance();
      try {
        MongoCursor<Document> rs = database.getExecutor().submit(() -> database.getDatabase().getCollection("kMysteryBoxContent").find().cursor()).get();
        while (rs.hasNext()) {
          Document document = rs.next();
          String id = document.getString("_id");
          String name = document.getString("name");
          EnumRarity rarity = EnumRarity.fromName(document.getString("rarity"));
          BoxContentAction action = new BoxContentAction(document.getString("action").split(" ; ")[0], document.getString("action").split(" ; ")[1]);
          String dependency = document.getString("dependency");
          CONTENTS.add(new BoxContent(id, name, rarity, action, dependency));
        }
      } catch (Exception ex) {
        LOGGER.log(Level.WARNING, "Ocorreu um erro ao buscar os premios: ", ex);
      }
    } else {
      CachedRowSet rs;
      if (Database.getInstance() instanceof MySQLDatabase) {
        rs = ((MySQLDatabase) Database.getInstance()).query("SELECT * FROM `kMysteryBoxContent`");
      } else {
        rs = ((HikariDatabase) Database.getInstance()).query("SELECT * FROM `kMysteryBoxContent`");
      }
      
      try {
        if (rs != null) {
          rs.beforeFirst();
          while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            EnumRarity rarity = EnumRarity.fromName(rs.getString("rarity"));
            BoxContentAction action = new BoxContentAction(rs.getString("action").split(" ; ")[0], rs.getString("action").split(" ; ")[1]);
            String dependency = rs.getString("dependency");
            CONTENTS.add(new BoxContent(id, name, rarity, action, dependency));
          }
        }
      } catch (SQLException ex) {
        LOGGER.log(Level.WARNING, "Ocorreu um erro ao buscar os premios: ", ex);
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (SQLException ignored) {}
        }
      }
    }
  }
  
  public static BoxContent getRandom() {
    return !CONTENTS.isEmpty() ? CONTENTS.get(ThreadLocalRandom.current().nextInt(CONTENTS.size())) : null;
  }
  
  public static BoxContent getById(String id) {
    return CONTENTS.stream().filter(content -> content.getUniqueId().equals(id)).findFirst().orElse(null);
  }

  public void dispatch(Profile profile) {
    profile.getAbstractContainer("kMysteryBox", "lastRewards", MysteryBoxRewardedContainer.class)
        .addContent(this);
    MysteryBoxRewardedContainer container = profile.getAbstractContainer("kMysteryBox", "alreadyRewarded", MysteryBoxRewardedContainer.class);
    boolean alreadyRewarded = container.alreadyRewarded(this);
    if (!alreadyRewarded) {
      container.addContent(this);
    }
    
    this.action.perform(profile.getName(), alreadyRewarded);
  }

  public String getUniqueId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public EnumRarity getRarity() {
    return this.rarity;
  }
  
  public boolean canBeDispatched() {
    if (this.dependency.equals("none")) {
      return true;
    }
    
    Plugin plugin = Bukkit.getPluginManager().getPlugin(this.dependency);
    return plugin != null && plugin.isEnabled();
  }
  
  public ItemStack getIcon(boolean removeOpen) {
    return BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&aCápsula Mágica : desc>&7Clique para abrir uma Cápsula\n&7Mágica que lhe garante\n&7um cosmético &9COMUM&7, &dRARO&7,\n&6ÉPICO&7 ou &bDIVINO&7.\n \n" + (removeOpen ? "" : "&eClique para abrir!") + " : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRlYmNmZTQxNDNhMTFlNjFkMzkzMTc5OWZiMzIyZTVhYTJhZTczMjc1YzUzYzJjNjc0MTYxNzhkMTQ5ZTE1MiJ9fX0=");
  }
}
