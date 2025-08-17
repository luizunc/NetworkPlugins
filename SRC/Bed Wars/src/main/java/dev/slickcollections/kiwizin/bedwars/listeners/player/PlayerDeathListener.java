package dev.slickcollections.kiwizin.bedwars.listeners.player;


import dev.slickcollections.kiwizin.bedwars.Main;
import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent evt) {
    Player player = evt.getEntity();
    evt.setDeathMessage(null);
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      evt.setDroppedExp(0);
      player.setHealth(20.0);
      
      BedWars game = profile.getGame(BedWars.class);
      evt.getDrops().clear();
      if (game == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), profile::refresh, 3);
      } else {
        List<Profile> hitters = profile.getLastHitters();
        Profile killer = hitters.size() > 0 ? hitters.get(0) : null;
        game.kill(profile, killer);
        for (Profile hitter : hitters) {
          if (!hitter.equals(killer) && hitter.playingGame() && hitter.getGame().equals(game) && !game.isSpectator(hitter.getPlayer())) {
            hitter.addStats("kCoreBedWars", game.getMode().getStats() + "assists");
          }
        }
        hitters.clear();
      }
    }
  }
}