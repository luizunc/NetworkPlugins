package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Location;

import java.util.*;

public class KillsLeaderboard extends Leaderboard {
  
  public KillsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "abates";
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("bedwars", (this.canSeeMonthly() ?
        Collections.singletonList("monthlykills") : Arrays.asList("1v1finalkills", "2v2finalkills", "4v4finalkills")).toArray(new String[0]));
    
    while (list.size() < 10) {
      list.add(new String[]{Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$kills$hologram;
  }
}