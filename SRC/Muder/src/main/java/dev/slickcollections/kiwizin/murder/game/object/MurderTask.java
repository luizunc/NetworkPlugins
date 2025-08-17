package dev.slickcollections.kiwizin.murder.game.object;

import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.murder.container.SelectedContainer;
import dev.slickcollections.kiwizin.murder.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.murder.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.types.WinAnimation;
import dev.slickcollections.kiwizin.murder.game.types.ClassicMurder;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.game.MurderTeam;
import dev.slickcollections.kiwizin.murder.game.object.runnables.AssassinsIngame;
import dev.slickcollections.kiwizin.murder.game.object.runnables.AssassinsWaiting;
import dev.slickcollections.kiwizin.murder.game.object.runnables.ClassicIngame;
import dev.slickcollections.kiwizin.murder.game.object.runnables.ClassicWaiting;

import java.util.ArrayList;
import java.util.List;

public class MurderTask {

  private Murder game;
  private BukkitTask task;

  public MurderTask(Murder game) {
    this.game = game;
  }

  public void cancel() {
    if (this.task != null) {
      this.task.cancel();
      this.task = null;
    }
  }

  public void reset() {
    this.cancel();
    this.task = (this.game instanceof ClassicMurder ? new ClassicWaiting(this.game) : new AssassinsWaiting(this.game)).runTaskTimer(Main.getInstance(), 0, 20);
  }

  public void swap(MurderTeam winners) {
    this.cancel();
    if (this.game.getState() == GameState.EMJOGO) {
      this.game.setTimer(Language.options$ingame$time);
      this.game.getWorld().getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
      this.task = (this.game instanceof ClassicMurder ? new ClassicIngame(this.game) : new AssassinsIngame(this.game)).runTaskTimer(Main.getInstance(), 0, 20);
    } else if (this.game.getState() == GameState.ENCERRADO) {
      this.game.setTimer(10);
      List<AbstractExecutor> executors = new ArrayList<>();
      if (winners != null) {
        winners.listPlayers().forEach(player -> executors.add(
                Profile.getProfile(player.getName()).getAbstractContainer("kCoreMurder", "selected", SelectedContainer.class)
                        .getSelected(CosmeticType.WIN_ANIMATION, WinAnimation.class).execute(player)));
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
