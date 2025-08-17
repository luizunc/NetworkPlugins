package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.lobby.cmd.kl.BuildCommand;
import dev.slickcollections.kiwizin.lobby.utils.tagger.TagUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    evt.setQuitMessage(null);
    BuildCommand.remove(evt.getPlayer());
    TagUtils.reset(evt.getPlayer().getName());
  }
}
