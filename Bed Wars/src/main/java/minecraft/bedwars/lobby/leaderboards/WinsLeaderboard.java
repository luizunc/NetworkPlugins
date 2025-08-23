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
    // Obter a página atual
    int currentPage = 0;
    if (this instanceof AbstractLeaderboard) {
      currentPage = ((AbstractLeaderboard) this).getCurrentPage();
    }
    
    // Retornar a página específica baseada na página atual
    switch (currentPage) {
      case 0: return Language.lobby$leaderboard$wins$page1;
      case 1: return Language.lobby$leaderboard$wins$page2;
      case 2: return Language.lobby$leaderboard$wins$page3;
      case 3: return Language.lobby$leaderboard$wins$page4;
      case 4: return Language.lobby$leaderboard$wins$page5;
      case 5: return Language.lobby$leaderboard$wins$page6;
      case 6: return Language.lobby$leaderboard$wins$page7;
      case 7: return Language.lobby$leaderboard$wins$page8;
      case 8: return Language.lobby$leaderboard$wins$page9;
      case 9: return Language.lobby$leaderboard$wins$page10;
      default: return Language.lobby$leaderboard$wins$page1;
    }
  }
}