package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.bedwars.lobby.GlobalGameMode;
import org.bukkit.Location;

import java.util.List;

public class KillsLeaderboard extends AbstractLeaderboard {
  
  public KillsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "abates";
  }
  
  @Override
  public List<String[]> getSplitted() {
    // Obter estatísticas baseadas no modo global atual
    String[] weeklyStats = {"weeklykills"};
    String[] monthlyStats = {"monthlykills"};
    String[] totalStats;
    
    int currentMode = GlobalGameMode.getCurrentMode();
    switch (currentMode) {
      case GlobalGameMode.MODE_GERAL:
        // Modo Geral: combina todos os modos
        totalStats = new String[]{"solokills", "duokills", "quadkills"};
        break;
      case GlobalGameMode.MODE_SOLO:
        // Modo Solo: apenas abates solo
        totalStats = new String[]{"solokills"};
        break;
      case GlobalGameMode.MODE_DUPLAS:
        // Modo Duplas: apenas abates duplas
        totalStats = new String[]{"duokills"};
        break;
      case GlobalGameMode.MODE_QUARTETOS:
        // Modo Quartetos: apenas abates quartetos
        totalStats = new String[]{"quadkills"};
        break;
      default:
        totalStats = new String[]{"solokills", "duokills", "quadkills"};
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
      case 0: return Language.lobby$leaderboard$kills$page1;
      case 1: return Language.lobby$leaderboard$kills$page2;
      case 2: return Language.lobby$leaderboard$kills$page3;
      case 3: return Language.lobby$leaderboard$kills$page4;
      case 4: return Language.lobby$leaderboard$kills$page5;
      case 5: return Language.lobby$leaderboard$kills$page6;
      case 6: return Language.lobby$leaderboard$kills$page7;
      case 7: return Language.lobby$leaderboard$kills$page8;
      case 8: return Language.lobby$leaderboard$kills$page9;
      case 9: return Language.lobby$leaderboard$kills$page10;
      default: return Language.lobby$leaderboard$kills$page1;
    }
  }
}