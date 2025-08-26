package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.types.BreakEffect;
import minecraft.bedwars.menus.cosmetics.MenuCosmetics;
import minecraft.core.core.game.FakeGame;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.Hotbar;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BreakEffectPreview extends AbstractPreview<BreakEffect> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[1];

  static {
    createLocations();
  }
  
  private Location oldLocation;
  private Location bedLocation;
  
  public BreakEffectPreview(Profile profile, BreakEffect cosmetic) {
    super(profile, cosmetic);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    profile.setHotbar(null);
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    // Teleportar jogador para a área de preview
    this.player.teleport(LOCATIONS[0]);
    
    // Colocar uma cama para o jogador quebrar
    this.bedLocation = LOCATIONS[0].clone().add(0, 0, 2);
    this.bedLocation.getBlock().setType(Material.BED);
    
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("cama")) {
      String value = CONFIG.getString("cama.1");
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
  
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getBlock().getType().name().contains("BED")) {
      evt.setCancelled(true);
      evt.getBlock().setType(Material.AIR);
      
      // Limpar o bloco adjacente da cama também
      if (evt.getBlock().getRelative(1, 0, 0).getType().name().contains("BED")) {
        evt.getBlock().getRelative(1, 0, 0).setType(Material.AIR);
      } else if (evt.getBlock().getRelative(-1, 0, 0).getType().name().contains("BED")) {
        evt.getBlock().getRelative(-1, 0, 0).setType(Material.AIR);
      } else if (evt.getBlock().getRelative(0, 0, 1).getType().name().contains("BED")) {
        evt.getBlock().getRelative(0, 0, 1).setType(Material.AIR);
      } else if (evt.getBlock().getRelative(0, 0, -1).getType().name().contains("BED")) {
        evt.getBlock().getRelative(0, 0, -1).setType(Material.AIR);
      }
      
      // Executar o efeito de quebra de cama
      this.cosmetic.showIn(this.player, evt.getBlock().getLocation());
      
      // Retornar ao menu após 3 segundos
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
        this.returnToMenu();
        this.stop();
      }, 60L); // 3 segundos = 60 ticks
    }
  }
  
  public void stop() {
    this.oldLocation = null;
    this.bedLocation = null;
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      this.player.setAllowFlight(this.player.hasPermission("core.fly"));
      profile.setGame(null);
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      new MenuCosmetics<>(profile, "Quebra de Cama", BreakEffect.class);
    }
  }
  
  @Override
  public void run() {
    // Este método é necessário para BukkitRunnable, mas não é usado neste preview
  }
} 