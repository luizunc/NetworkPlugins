package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.bedwars.lobby.GlobalGameMode;
import org.bukkit.Location;

import java.util.List;

public class BedsLeaderboard extends AbstractLeaderboard {
  
  public BedsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "camas";
  }
  
  @Override
  public List<String[]> getSplitted() {
    // Obter estatísticas baseadas no modo global atual
    String[] weeklyStats = {"weeklybeds"};
    String[] monthlyStats = {"monthlybeds"};
    String[] totalStats;
    
    int currentMode = GlobalGameMode.getCurrentMode();
    switch (currentMode) {
      case GlobalGameMode.MODE_GERAL:
        // Modo Geral: combina todos os modos
        totalStats = new String[]{"solobeds", "duobeds", "quadbeds"};
        break;
      case GlobalGameMode.MODE_SOLO:
        // Modo Solo: apenas camas solo
        totalStats = new String[]{"solobeds"};
        break;
      case GlobalGameMode.MODE_DUPLAS:
        // Modo Duplas: apenas camas duplas
        totalStats = new String[]{"duobeds"};
        break;
      case GlobalGameMode.MODE_QUARTETOS:
        // Modo Quartetos: apenas camas quartetos
        totalStats = new String[]{"quadbeds"};
        break;
      default:
        totalStats = new String[]{"solobeds", "duobeds", "quadbeds"};
        break;
    }
    
    return getLeaderboardData(weeklyStats, monthlyStats, totalStats);
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$beds$hologram;
  }
}
