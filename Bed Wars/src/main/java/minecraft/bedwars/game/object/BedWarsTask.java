package minecraft.bedwars.game.object;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.game.generators.Generator;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsEvent;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.utils.PlayerUtils;
import minecraft.core.core.game.GameState;
import minecraft.core.core.nms.NMS;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static minecraft.bedwars.game.BedWars.TRACKING_FORMAT;

public class BedWarsTask {
  
  private BedWars game;
  private BukkitTask task, secondary;
  
  public BedWarsTask(BedWars game) {
    this.game = game;
  }
  
  public void cancel() {
    if (this.task != null) {
      this.task.cancel();
      this.task = null;
    }
    
    if (this.secondary != null) {
      this.secondary.cancel();
      this.secondary = null;
    }
  }
  
  public void reset() {
    this.cancel();
    this.task = new BukkitRunnable() {
      
      @Override
      public void run() {
        if (game.getOnline() < game.getConfig().getMinPlayers()) {
          if (game.getTimer() != (Language.options$start$waiting + 1)) {
            game.setTimer(Language.options$start$waiting + 1);
          }
    
          game.listPlayers().forEach(player -> Profile.getProfile(player.getName()).update());
          return;
        }
        
        if (game.getTimer() == 0) {
          game.start();
          return;
        }
        
        if (game.getTimer() == (Language.options$start$waiting + 1)) {
          game.setTimer(Language.options$start$waiting);
        }
        
        game.listPlayers().forEach(player -> {
          Profile.getProfile(player.getName()).update();
          if (game.getTimer() == 10 || game.getTimer() <= 5) {
            EnumSound.CLICK.play(player, 0.5F, 2.0F);
          }
        });
        
        if (game.getTimer() == 30 || game.getTimer() == 15 || game.getTimer() == 10 || game.getTimer() <= 5) {
          game.broadcastMessage(Language.ingame$broadcast$starting.replace("{time}", StringUtils.formatNumber(game.getTimer())).replace("{s}", game.getTimer() > 1 ? "s" : ""));
        }
        
        game.setTimer(game.getTimer() - 1);
      }
    }.runTaskTimer(Main.getInstance(), 0, 20);
  }
  
  public void swap(BedWarsTeam winners) {
    this.cancel();
    if (this.game.getState() == GameState.EMJOGO) {
      this.game.setTimer(0);
      this.game.getWorld().getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
      this.secondary = new BukkitRunnable() {
        
        @Override
        public void run() {
          game.listTeams().forEach(team -> team.getGenerator().update());
        }
      }.runTaskTimer(Main.getInstance(), 0, 5);
      
      this.task = new BukkitRunnable() {
        
        @Override
        public void run() {
          Map.Entry<Integer, BedWarsEvent> entry = game.getNextEvent();
          if (entry != null) {
            if (entry.getKey() == game.getTimer()) {
              entry.getValue().execute(game);
              game.generateEvent();
            }
          } else {
            game.generateEvent();
          }
          game.listGenerators().forEach(Generator::update);
          game.listTeams().forEach(BedWarsTeam::tick);
          
          List<Player> players = game.listPlayers(false);
          players.forEach(player -> {
            if (game.getState() != GameState.AGUARDANDO && game.getState() != GameState.INICIANDO && !game.getConfig().getCubeId().contains(player.getLocation())) {
              if (game.isSpectator(player)) {
                player.teleport(game.getConfig().getCubeId().getCenterLocation());
              }
            }
  
            if (game.getState() == GameState.EMJOGO && !game.isSpectator(player)) {
              PlayerUtils.renewItems(player);
              BedWarsTeam team = game.getTeam(player);
              boolean hasInvisibilityState = team.getEquipment(player).update();
              if (hasInvisibilityState) {
                team.refresh(player);
                game.updateTags();
              }
    
              ItemStack item = player.getItemInHand();
              if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("§aLocalizador")) {
                BedWarsTeam target = game.listTeams().stream().filter(bt -> bt.getId().equals(team.getEquipment(player).getTracking())).findFirst().orElse(null);
                if (target != null) {
                  if (target.isAlive()) {
                    Player targetPlayer = PlayerUtils.getMoreNearby(player, target.listPlayers());
                    player.setCompassTarget(targetPlayer.getLocation());
                    NMS.sendActionBar(player, "§7Rastreando: " + game.getTeam(targetPlayer).getColored(targetPlayer) + " §a(" + TRACKING_FORMAT.format(player.getLocation().distance(targetPlayer.getLocation())) + " Blocos)");
                  }
                }
              }
            }
          });
          
          game.listPlayers().forEach(player -> {
            Profile.getProfile(player.getName()).update();
          });
          game.setTimer(game.getTimer() + 1);
        }
      }.runTaskTimer(Main.getInstance(), 0, 20);
    } else if (this.game.getState() == GameState.ENCERRADO) {
      this.game.setTimer(10);
      List<AbstractExecutor> executors = new ArrayList<>();
      if (winners != null) {
        winners.listPlayers().forEach(player -> executors.add(
            Profile.getProfile(player.getName()).getAbstractContainer("bedwars", "selected", SelectedContainer.class).getSelected(CosmeticType.WIN_ANIMATION, WinAnimation.class)
                .execute(player)));
      }
      
      this.task = new BukkitRunnable() {
        
        @Override
        public void run() {
          if (game.getTimer() == 0) {
            executors.forEach(AbstractExecutor::cancel);
            executors.clear();
            game.listPlayers().forEach(player -> game.leave(Profile.getProfile(player.getName()), null));
            game.reset();
            return;
          }
          
          executors.forEach(executor -> {
            if (winners == null || !winners.listPlayers().contains(executor.getPlayer())) {
              return;
            }
            
            executor.tick();
          });
          game.setTimer(game.getTimer() - 1);
        }
      }.runTaskTimer(Main.getInstance(), 0, 20);
      
      
    }
    
  }
}
