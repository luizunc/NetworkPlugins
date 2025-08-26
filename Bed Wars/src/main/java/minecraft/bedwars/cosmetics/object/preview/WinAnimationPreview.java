package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
import minecraft.core.core.game.FakeGame;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.Hotbar;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class WinAnimationPreview extends AbstractPreview<WinAnimation> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[1];

  static {
    createLocations();
  }
  
  private Location oldLocation;
  private AbstractExecutor executor;
  private int taskId;
  private BukkitTask autoReturnTask;
  private BukkitTask animationStartTask;
  private boolean animationStarted = false;
  
  public WinAnimationPreview(Profile profile, WinAnimation cosmetic) {
    super(profile, cosmetic);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    
    // Limpar o inventário do jogador para deixar a hotbar vazia
    this.player.getInventory().clear();
    
    profile.setHotbar(null);
    
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    // Teleportar jogador para a área de preview
    this.player.teleport(LOCATIONS[0]);
    
    // Enviar mensagem informativa
    this.player.sendMessage("§aVocê entrou no preview da comemoração!");
    this.player.sendMessage("§7A comemoração será executada em 3 segundos.");
    
    // Executar a animação após 3 segundos
    this.animationStartTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (this.player != null && this.player.isOnline() && !this.animationStarted) {
        this.animationStarted = true;
        this.player.sendMessage("§aExecutando comemoração...");
        this.executor = cosmetic.execute(this.player);
        // Executar o tick da animação a cada segundo
        this.taskId = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
          if (this.executor != null) {
            this.executor.tick();
          }
        }, 20L, 20L).getTaskId(); // A cada segundo
        
        // Iniciar timer de 10 segundos para voltar automaticamente após iniciar a comemoração
        this.autoReturnTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
          if (this.player != null && this.player.isOnline()) {
            this.player.sendMessage("§cTempo do preview expirado! Retornando ao lobby...");
            this.returnToMenu();
            this.stop();
          }
        }, 200L); // 10 segundos = 200 ticks
      }
    }, 60L); // 3 segundos = 60 ticks
    
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("comemoracoes")) {
      String value = CONFIG.getString("comemoracoes.1");
      if (value != null) {
        LOCATIONS[0] = BukkitUtils.deserializeLocation(value);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.stop();
    }
  }
  
  public void stop() {
    // Cancelar o timer de início da animação
    if (this.animationStartTask != null) {
      this.animationStartTask.cancel();
      this.animationStartTask = null;
    }
    
    // Cancelar o timer de retorno automático
    if (this.autoReturnTask != null) {
      this.autoReturnTask.cancel();
      this.autoReturnTask = null;
    }
    
    this.oldLocation = null;
    if (this.executor != null) {
      this.executor.cancel();
      this.executor = null;
    }
    if (this.taskId != 0) {
      Bukkit.getScheduler().cancelTask(this.taskId);
      this.taskId = 0;
    }
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      this.player.setAllowFlight(this.player.hasPermission("core.fly"));
      profile.setGame(null);
      
      // Limpar o inventário e aplicar a hotbar do lobby
      this.player.getInventory().clear();
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refresh();
      
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      // Retornar ao lobby do BedWars em vez de abrir o menu de cosméticos
    }
  }
  
  @Override
  public void run() {
    // Este método é necessário para BukkitRunnable, mas não é usado neste preview
  }
} 