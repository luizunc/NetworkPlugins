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
    String[] weeklyStats;
    String[] monthlyStats;
    String[] totalStats;
    
    int currentMode = GlobalGameMode.getCurrentMode();
    switch (currentMode) {
      case GlobalGameMode.MODE_GERAL:
        // Modo Geral: combina todos os modos
        weeklyStats = new String[]{"weeklybeds"};
        monthlyStats = new String[]{"monthlybeds"};
        totalStats = new String[]{"solobeds", "duobeds", "quadbeds"};
        break;
      case GlobalGameMode.MODE_SOLO:
        // Modo Solo: apenas camas solo
        weeklyStats = new String[]{"solobeds"};
        monthlyStats = new String[]{"solobeds"};
        totalStats = new String[]{"solobeds"};
        break;
      case GlobalGameMode.MODE_DUPLAS:
        // Modo Duplas: apenas camas duplas
        weeklyStats = new String[]{"duobeds"};
        monthlyStats = new String[]{"duobeds"};
        totalStats = new String[]{"duobeds"};
        break;
      case GlobalGameMode.MODE_QUARTETOS:
        // Modo Quartetos: apenas camas quartetos
        weeklyStats = new String[]{"quadbeds"};
        monthlyStats = new String[]{"quadbeds"};
        totalStats = new String[]{"quadbeds"};
        break;
      default:
        weeklyStats = new String[]{"weeklybeds"};
        monthlyStats = new String[]{"monthlybeds"};
        totalStats = new String[]{"solobeds", "duobeds", "quadbeds"};
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
      case 0: return Language.lobby$leaderboard$beds$page1;
      case 1: return Language.lobby$leaderboard$beds$page2;
      case 2: return Language.lobby$leaderboard$beds$page3;
      case 3: return Language.lobby$leaderboard$beds$page4;
      case 4: return Language.lobby$leaderboard$beds$page5;
      case 5: return Language.lobby$leaderboard$beds$page6;
      case 6: return Language.lobby$leaderboard$beds$page7;
      case 7: return Language.lobby$leaderboard$beds$page8;
      case 8: return Language.lobby$leaderboard$beds$page9;
      case 9: return Language.lobby$leaderboard$beds$page10;
      default: return Language.lobby$leaderboard$beds$page1;
    }
  }
}
