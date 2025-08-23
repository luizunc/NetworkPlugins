package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.bedwars.lobby.GlobalGameMode;
import org.bukkit.Location;

import java.util.List;

public class WinsLeaderboard extends AbstractLeaderboard {
  
  public WinsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "vitorias";
  }
  
  @Override
  public List<String[]> getSplitted() {
    // Obter estatísticas baseadas no modo global atual
    String[] weeklyStats = {"weeklywins"};
    String[] monthlyStats = {"monthlywins"};
    String[] totalStats;
    
    int currentMode = GlobalGameMode.getCurrentMode();
    switch (currentMode) {
      case GlobalGameMode.MODE_GERAL:
        // Modo Geral: combina todos os modos
        totalStats = new String[]{"solowins", "duowins", "quadwins"};
        break;
      case GlobalGameMode.MODE_SOLO:
        // Modo Solo: apenas vitórias solo
        totalStats = new String[]{"solowins"};
        break;
      case GlobalGameMode.MODE_DUPLAS:
        // Modo Duplas: apenas vitórias duplas
        totalStats = new String[]{"duowins"};
        break;
      case GlobalGameMode.MODE_QUARTETOS:
        // Modo Quartetos: apenas vitórias quartetos
        totalStats = new String[]{"quadwins"};
        break;
      default:
        totalStats = new String[]{"solowins", "duowins", "quadwins"};
        break;
    }
    
    return getLeaderboardData(weeklyStats, monthlyStats, totalStats);
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$wins$hologram;
  }
}