package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Location;

import java.util.*;

public class WinsLeaderboard extends Leaderboard {
  
  public WinsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$wins$hologram;
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("bedwars", (this.canSeeMonthly() ?
        Collections.singletonList("monthlywins") : Arrays.asList("1v1wins", "2v2wins", "4v4wins")).toArray(new String[0]));
    while (list.size() < 10) {
      list.add(new String[]{Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  
  @Override
  public String getType() {
    return "vitorias";
  }
}