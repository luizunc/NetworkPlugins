package dev.slickcollections.kiwizin.bedwars.lobby.leaderboards;

import dev.slickcollections.kiwizin.bedwars.Language;
import dev.slickcollections.kiwizin.bedwars.lobby.Leaderboard;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.exception.ProfileLoadException;
import dev.slickcollections.kiwizin.utils.StringUtils;
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
    List<String[]> list = Database.getInstance().getLeaderBoard("kCoreBedWars", (this.canSeeMonthly() ?
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