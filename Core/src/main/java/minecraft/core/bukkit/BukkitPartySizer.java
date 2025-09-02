package minecraft.core.bukkit;

import minecraft.core.bukkit.plugin.config.UtilsConfig;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.player.rank.RankPermissionUtils;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitPartySizer {
  
  private static final KConfig CONFIG;
  private static final Map<String, Integer> SIZES;
  
  static {
    CONFIG = UtilsConfig.getConfig();
    
    SIZES = new LinkedHashMap<>();
    // Tamanhos padrão baseados em permissões
            SIZES.put("rank.master", 20);
        SIZES.put("rank.youtuber", 15);
        SIZES.put("rank.mvpplus", 10);
        SIZES.put("rank.mvp", 5);
  }
  
  public static int getPartySize(Player player) {
    for (Map.Entry<String, Integer> entry : SIZES.entrySet()) {
      if (RankPermissionUtils.hasRankOrHigher(player, entry.getKey())) {
        return entry.getValue();
      }
    }
    
    return 3;
  }
}
