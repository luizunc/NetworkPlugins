package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.bedwars.lobby.GlobalGameMode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ModosLeaderboard extends Leaderboard {
  
  private int currentGameMode = 0; // 0 = Geral, 1 = Solo, 2 = Duplas, 3 = Quartetos
  
  public ModosLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "modos";
  }
  
  @Override
  public List<String[]> getSplitted() {
    // Retorna lista vazia pois não usa dados de jogadores
    return new ArrayList<>();
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$modos$hologram;
  }
  
  /**
   * Obtém o modo de jogo atual
   */
  public int getCurrentGameMode() {
    return currentGameMode;
  }
  
  /**
   * Avança para o próximo modo de jogo
   */
  public void nextGameMode() {
    currentGameMode = (currentGameMode + 1) % 4; // Cicla entre 0, 1, 2, 3
    GlobalGameMode.setCurrentMode(currentGameMode);
    
    // Limpar cache de todas as outras leaderboards para forçar atualização
    listLeaderboards().stream()
        .filter(board -> !board.getType().equals("modos"))
        .forEach(board -> {
          if (board instanceof AbstractLeaderboard) {
            ((AbstractLeaderboard) board).clearCache();
          }
        });
  }
  
  /**
   * Obtém o nome do modo atual
   */
  public String getCurrentGameModeName() {
    switch (currentGameMode) {
      case 0:
        return "Geral";
      case 1:
        return "Solo";
      case 2:
        return "Duplas";
      case 3:
        return "Quartetos";
      default:
        return "Geral";
    }
  }
} 