package minecraft.bedwars.lobby;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.lobby.leaderboards.BedsLeaderboard;
import minecraft.bedwars.lobby.leaderboards.KillsLeaderboard;
import minecraft.bedwars.lobby.leaderboards.WinsLeaderboard;
import minecraft.bedwars.lobby.leaderboards.ModosLeaderboard;
import minecraft.bedwars.lobby.leaderboards.AbstractLeaderboard;
import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.holograms.api.Hologram;
import minecraft.core.core.player.Profile;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Leaderboard {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("leaderboards");
  private static final List<Leaderboard> LEADERBOARDS = new ArrayList<>();
  
  private Location location;
  private String id;
  private Hologram hologram;
  private boolean monthly = false;
  private int filterMode = 0; // 0 = Semanal, 1 = Mensal, 2 = Total
  
  public Leaderboard(Location location, String id) {
    this.location = location;
    this.id = id;
    
  }
  
  public static void setupLeaderboards() {
    List<String> boardList = CONFIG.getStringList("board-list");
    
    // Inverter a ordem das leaderboards: top vai para baixo, baixo vai para top
    for (int i = boardList.size() - 1; i >= 0; i--) {
      String serialized = boardList.get(i);
      if (serialized.split("; ").length > 6) {
        String id = serialized.split("; ")[6];
        String type = serialized.split("; ")[7];
        Leaderboard board = buildByType(BukkitUtils.deserializeLocation(serialized), id, type);
        if (board == null) {
          return;
        }
        
        LEADERBOARDS.add(board);
      }
    }
  
    Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () ->
        Profile.listProfiles().forEach(Profile::saveSync), 0, Language.lobby$leaderboard$minutes * 1200);
  
    Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () ->
        listLeaderboards().forEach(Leaderboard::update), 0, 1);
  }
  
  public static void add(Location location, String id, String type) {
    List<String> list = CONFIG.getStringList("board-list");
    list.add(BukkitUtils.serializeLocation(location) + "; " + id + "; " + type.toLowerCase());
    CONFIG.set("board-list", list);
    
    Leaderboard board = buildByType(location, id, type);
    LEADERBOARDS.add(board);
    if (board != null) {
      board.update();
    }
  }
  
  public static void remove(Leaderboard board) {
    LEADERBOARDS.remove(board);
    List<String> list = CONFIG.getStringList("board-list");
    list.remove(BukkitUtils.serializeLocation(board.getLocation()) + "; " + board.getId() + "; " + board.getType());
    CONFIG.set("board-list", list);
    
    board.destroy();
  }
  
  public static Leaderboard getById(String id) {
    return LEADERBOARDS.stream().filter(board -> board.getId().equals(id)).findFirst().orElse(null);
  }
  
  public static Collection<Leaderboard> listLeaderboards() {
    return LEADERBOARDS;
  }
  
  private static Leaderboard buildByType(Location location, String id, String type) {
    if (type.equalsIgnoreCase("vitorias")) {
      return new WinsLeaderboard(location, id);
    } else if (type.equalsIgnoreCase("abates")) {
      return new KillsLeaderboard(location, id);
    } else if (type.equalsIgnoreCase("camas")) {
      return new BedsLeaderboard(location, id);
    } else if (type.equalsIgnoreCase("modos")) {
      return new ModosLeaderboard(location, id);
    }
    
    return null;
  }
  
  public abstract String getType();

  public abstract List<String[]> getSplitted();
  
  public abstract List<String> getHologramLines();
  
  public void update() {
    try {
    List<String> lines = new ArrayList<>();
    
    List<String[]> list = this.getSplitted();
    for (String line : this.getHologramLines()) {
      // Se for leaderboard de modos, não processar placeholders de nomes/stats
      if (!this.getType().equals("modos")) {
        // Substituir placeholders para os 10 primeiros lugares
        for (int i = 0; i < 10; i++) {
          String name = i < list.size() ? list.get(i)[0] : Language.lobby$leaderboard$empty;
          String stats = i < list.size() ? list.get(i)[1] : "";
          
          // Se não há estatísticas, mostrar apenas o nome sem "- stats"
          if (stats.isEmpty()) {
            line = line.replace("{name_" + (i + 1) + "} §7- §e{stats_" + (i + 1) + "}", name);
          } else {
            line = line.replace("{name_" + (i + 1) + "}", name).replace("{stats_" + (i + 1) + "}", stats);
          }
        }
      }
      lines.add(line);
    }
  
    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
        try {
      if (this.hologram == null) {
        this.hologram = HologramLibrary.createHologram(this.location.clone(), lines);
        return;
      }
    
      int index = 1;
      for (String line : lines) {
            // Processar informações da página se for uma leaderboard paginada
            if (this instanceof AbstractLeaderboard) {
              AbstractLeaderboard abstractBoard = (AbstractLeaderboard) this;
              line = line.replace("{page_info}", (abstractBoard.getCurrentPage() + 1) + "/" + abstractBoard.getTotalPages());
              
              // Processar cores dos filtros
              if (this.getType().equals("modos")) {
                // Para leaderboard de modos: Geral/Solo/Duplas/Quartetos
                ModosLeaderboard modosBoard = (ModosLeaderboard) this;
                int currentMode = modosBoard.getCurrentGameMode();
                
                line = line.replace("{geral_color}", currentMode == 0 ? "§a" : "§7");
                line = line.replace("{solo_color}", currentMode == 1 ? "§a" : "§7");
                line = line.replace("{duplas_color}", currentMode == 2 ? "§a" : "§7");
                line = line.replace("{quartetos_color}", currentMode == 3 ? "§a" : "§7");
              } else {
                // Para leaderboards normais: Semanal/Mensal/Total
                if (filterMode == 0) {
                  // Semanal ativo
                  line = line.replace("{weekly_color}", "§a");
                  line = line.replace("{monthly_color}", "§7");
                  line = line.replace("{total_color}", "§7");
                } else if (filterMode == 1) {
                  // Mensal ativo
                  line = line.replace("{weekly_color}", "§7");
                  line = line.replace("{monthly_color}", "§a");
                  line = line.replace("{total_color}", "§7");
                } else {
                  // Total ativo
                  line = line.replace("{weekly_color}", "§7");
                  line = line.replace("{monthly_color}", "§7");
                  line = line.replace("{total_color}", "§a");
                }
              }
            } else {
              // Para leaderboards antigas, usar cores padrão
              line = line.replace("{page_info}", "1/1");
              line = line.replace("{weekly_color}", this.canSeeWeekly() ? "§a" : "§7");
              line = line.replace("{monthly_color}", this.canSeeMonthly() ? "§a" : "§7");
              line = line.replace("{total_color}", this.canSeeTotal() ? "§a" : "§7");
              
              // Processar cores de modos também para leaderboards antigas
              if (this.getType().equals("modos")) {
                ModosLeaderboard modosBoard = (ModosLeaderboard) this;
                int currentMode = modosBoard.getCurrentGameMode();
                
                line = line.replace("{geral_color}", currentMode == 0 ? "§a" : "§7");
                line = line.replace("{solo_color}", currentMode == 1 ? "§a" : "§7");
                line = line.replace("{duplas_color}", currentMode == 2 ? "§a" : "§7");
                line = line.replace("{quartetos_color}", currentMode == 3 ? "§a" : "§7");
              }
            }
            
        hologram.updateLine(index, line);
        if (hologram.getLine(index).getLine().equals("")) {
          hologram.getLine(index).setLocation(hologram.getLine(index).getLocation().add(0, Double.MAX_VALUE, 0));
        }
        hologram.getLine(index).setTouchable(this::onTouch);
        index++;
          }
        } catch (Exception e) {
          Main.getInstance().getLogger().warning("Erro ao atualizar holograma da leaderboard " + this.id + ": " + e.getMessage());
      }
    });
    } catch (Exception e) {
      Main.getInstance().getLogger().warning("Erro ao processar dados da leaderboard " + this.id + ": " + e.getMessage());
    }
  }
  
  public void destroy() {
    this.monthly = false;
    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
  }
  
  public String getId() {
    return this.id;
  }
  
  public boolean canSeeMonthly() {
    return filterMode == 1; // Mensal
  }
  
  public boolean canSeeTotal() {
    return filterMode == 2; // Total
  }
  
  public boolean canSeeWeekly() {
    return filterMode == 0; // Semanal
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  private void onTouch(Player touch) {
    try {
      EnumSound.CLICK.play(touch, 1.5F, 2.0F);
      
      // Se for uma leaderboard que estende AbstractLeaderboard, permitir navegação
      if (this instanceof AbstractLeaderboard) {
        AbstractLeaderboard abstractBoard = (AbstractLeaderboard) this;
        
        // Para leaderboard de modos, sempre alternar entre os modos
        if (this.getType().equals("modos")) {
          ModosLeaderboard modosBoard = (ModosLeaderboard) this;
          modosBoard.nextGameMode();
          touch.sendMessage("§aAlterado para " + modosBoard.getCurrentGameModeName());
        } else {
          // Para outras leaderboards, usar lógica normal
          // Clique com Shift para próxima página
          if (touch.isSneaking()) {
            abstractBoard.nextPage();
            touch.sendMessage("§aPágina " + (abstractBoard.getCurrentPage() + 1) + "/" + abstractBoard.getTotalPages());
          } else {
            // Para leaderboards normais
            if (filterMode == 0) {
              // Semanal -> Mensal
              filterMode = 1;
              touch.sendMessage("§aAlterado para Mensal");
            } else if (filterMode == 1) {
              // Mensal -> Total
              filterMode = 2;
              touch.sendMessage("§aAlterado para Total");
            } else {
              // Total -> Semanal
              filterMode = 0;
              touch.sendMessage("§aAlterado para Semanal");
            }
            // Resetar para primeira página quando alternar modo
            abstractBoard.clearCache(); // Limpar cache para recarregar dados
            abstractBoard.goToPage(0);
          }
        }
      } else {
        // Comportamento padrão para leaderboards antigas
        if (this.getType().equals("modos")) {
          // Para leaderboard de modos antiga
          ModosLeaderboard modosBoard = (ModosLeaderboard) this;
          modosBoard.nextGameMode();
          touch.sendMessage("§aAlterado para " + modosBoard.getCurrentGameModeName());
        } else {
          this.monthly = !monthly;
        }
      }
    } catch (Exception e) {
      Main.getInstance().getLogger().warning("Erro ao processar toque na leaderboard " + this.id + ": " + e.getMessage());
    }
  }
}