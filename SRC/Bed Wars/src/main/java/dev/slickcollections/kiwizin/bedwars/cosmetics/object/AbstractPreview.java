package dev.slickcollections.kiwizin.bedwars.cosmetics.object;

import dev.slickcollections.kiwizin.bedwars.Main;
import dev.slickcollections.kiwizin.bedwars.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractPreview<T extends Cosmetic> extends BukkitRunnable {
  
  public static final KConfig CONFIG = Main.getInstance().getConfig("previewLocations");
  protected Player player;
  protected T cosmetic;
  
  public AbstractPreview(Profile profile, T cosmetic) {
    this.player = profile.getPlayer();
    this.cosmetic = cosmetic;
  }
  
  public static boolean canDoKillEffect() {
    return CONFIG.getSection("killeffect") != null && CONFIG.getSection("killeffect").getKeys(false).size() > 2;
  }
  
  public static boolean canDoCage() {
    return CONFIG.getSection("cage") != null && CONFIG.getSection("cage").getKeys(false).size() > 1;
  }
  
  public abstract void returnToMenu();
}
