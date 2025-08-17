package dev.slickcollections.kiwizin.murder.lobby.leaderboards;

import dev.slickcollections.kiwizin.database.Database;
import org.bukkit.Location;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.lobby.Leaderboard;

import java.util.List;

public class KillerLeaderboard extends Leaderboard {

  public KillerLeaderboard(Location location, String id) {
    super(location, id);
  }

  @Override
  public String getType() {
    return "assassino";
  }

  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("kCoreMurder", "clkillerwins");
    while (list.size() < 10) {
      list.add(new String[] {Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }

  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$wins_as_murder$hologram;
  }
}
