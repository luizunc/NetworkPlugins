package dev.slickcollections.kiwizin.bedwars.lobby.leaderboards;


import dev.slickcollections.kiwizin.bedwars.Language;
import dev.slickcollections.kiwizin.bedwars.lobby.Leaderboard;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.exception.ProfileLoadException;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Location;

import java.util.*;

public class BedsLeaderboard extends Leaderboard {
  
  public BedsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "abates";
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("kCoreBedWars", (this.canSeeMonthly() ?
        Collections.singletonList("monthlybeds") : Arrays.asList("1v1bedsdestroyeds", "2v2bedsdestroyeds", "4v4bedsdestroyeds")).toArray(new String[0]));
    while (list.size() < 10) {
      list.add(new String[]{Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$beds$hologram;
  }
}
