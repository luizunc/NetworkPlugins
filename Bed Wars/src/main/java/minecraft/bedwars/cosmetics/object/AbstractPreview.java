package minecraft.bedwars.cosmetics.object;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.core.core.player.Profile;
import minecraft.core.bukkit.plugin.config.KConfig;
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
    return CONFIG.getSection("killeffect") != null && CONFIG.getSection("killeffect").getKeys(false).size() > 1;
  }
  
  public static boolean canDoCage() {
    return CONFIG.getSection("cage") != null && CONFIG.getSection("cage").getKeys(false).size() > 1;
  }
  
  public static boolean canDoShopkeeperSkin() {
    return CONFIG.getSection("vendedor") != null && CONFIG.getSection("vendedor").getKeys(false).size() > 1;
  }
  
  public static boolean canDoWinAnimation() {
    return CONFIG.getSection("comemoracoes") != null && CONFIG.getSection("comemoracoes").getKeys(false).size() > 0;
  }
  
  public static boolean canDoBreakEffect() {
    return CONFIG.getSection("cama") != null && CONFIG.getSection("cama").getKeys(false).size() > 0;
  }
  
  public abstract void returnToMenu();
}
