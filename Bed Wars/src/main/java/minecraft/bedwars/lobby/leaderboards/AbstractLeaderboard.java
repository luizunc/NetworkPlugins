package minecraft.bedwars.lobby.leaderboards;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.lobby.Leaderboard;
import minecraft.core.core.database.Database;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata base para todas as leaderboards
 * Fornece funcionalidades comuns e tratamento de erros
 */
public abstract class AbstractLeaderboard extends Leaderboard {
  
  protected static final int MAX_ENTRIES = 100;
  protected static final int ENTRIES_PER_PAGE = 10;
  protected int currentPage = 0;
  protected List<String[]> cachedData = new ArrayList<>();
  
  public AbstractLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  /**
   * Obtém os dados da leaderboard com tratamento de erros
   */
  protected List<String[]> getLeaderboardData(String[] weeklyStats, String[] monthlyStats, String[] totalStats) {
    try {
      // Se não temos dados em cache, recarregar
      if (cachedData.isEmpty()) {
        String[] statsToQuery;
        if (canSeeWeekly()) {
          statsToQuery = weeklyStats;
        } else if (canSeeMonthly()) {
          statsToQuery = monthlyStats;
        } else {
          statsToQuery = totalStats;
        }
        
        List<String[]> allData = Database.getInstance().getLeaderBoard("bedwars", statsToQuery);
        cachedData = normalizeLeaderboardData(allData);
      }
      
      // Retornar apenas a página atual (10 entradas)
      return getCurrentPageData();
    } catch (Exception e) {
      Main.getInstance().getLogger().warning("Erro ao obter dados da leaderboard " + getType() + ": " + e.getMessage());
      return createEmptyLeaderboard();
    }
  }
  
  /**
   * Método de compatibilidade para leaderboards antigas
   */
  protected List<String[]> getLeaderboardData(String[] monthlyStats, String[] totalStats) {
    return getLeaderboardData(monthlyStats, monthlyStats, totalStats);
  }
  
  /**
   * Verifica se deve recarregar o cache
   */
  private boolean shouldReloadCache() {
    // Recarregar se o cache está vazio
    return cachedData.isEmpty();
  }
  
  /**
   * Limpa o cache forçando recarregamento
   */
  public void clearCache() {
    cachedData.clear();
    currentPage = 0;
  }
  
  /**
   * Vai para uma página específica
   */
  public void goToPage(int page) {
    int maxPages = getTotalPages();
    if (page >= 0 && page < maxPages) {
      currentPage = page;
    }
  }
  
  /**
   * Obtém os dados da página atual
   */
  private List<String[]> getCurrentPageData() {
    int startIndex = currentPage * ENTRIES_PER_PAGE;
    int endIndex = Math.min(startIndex + ENTRIES_PER_PAGE, cachedData.size());
    
    if (startIndex >= cachedData.size()) {
      return createEmptyLeaderboard();
    }
    
    List<String[]> pageData = new ArrayList<>(cachedData.subList(startIndex, endIndex));
    
    // Garantir que sempre temos 10 entradas na página
    while (pageData.size() < ENTRIES_PER_PAGE) {
      pageData.add(new String[]{Language.lobby$leaderboard$empty, ""});
    }
    
    return pageData;
  }
  
  /**
   * Avança para a próxima página
   */
  public void nextPage() {
    int maxPages = getTotalPages();
    if (maxPages > 0) {
      currentPage = (currentPage + 1) % maxPages;
    }
  }
  
  /**
   * Volta para a página anterior
   */
  public void previousPage() {
    int maxPages = getTotalPages();
    if (maxPages > 0) {
      currentPage = (currentPage - 1 + maxPages) % maxPages;
    }
  }
  
  /**
   * Obtém a página atual
   */
  public int getCurrentPage() {
    return currentPage;
  }
  
  /**
   * Obtém o número total de páginas
   */
  public int getTotalPages() {
    int totalPages = (cachedData.size() + ENTRIES_PER_PAGE - 1) / ENTRIES_PER_PAGE;
    // Garantir pelo menos 1 página
    return Math.max(1, totalPages);
  }
  
  /**
   * Normaliza os dados da leaderboard para garantir até 100 entradas
   */
  private List<String[]> normalizeLeaderboardData(List<String[]> leaderboardData) {
    // Garantir que sempre temos pelo menos 100 entradas
    while (leaderboardData.size() < MAX_ENTRIES) {
      leaderboardData.add(new String[]{Language.lobby$leaderboard$empty, ""});
    }
    
    // Limitar a 100 entradas se houver mais
    if (leaderboardData.size() > MAX_ENTRIES) {
      leaderboardData = leaderboardData.subList(0, MAX_ENTRIES);
    }
    
    return leaderboardData;
  }
  
  /**
   * Cria uma leaderboard vazia com entradas padrão
   */
  private List<String[]> createEmptyLeaderboard() {
    List<String[]> emptyList = new ArrayList<>();
    for (int i = 0; i < ENTRIES_PER_PAGE; i++) {
      emptyList.add(new String[]{Language.lobby$leaderboard$empty, ""});
    }
    return emptyList;
  }
} 