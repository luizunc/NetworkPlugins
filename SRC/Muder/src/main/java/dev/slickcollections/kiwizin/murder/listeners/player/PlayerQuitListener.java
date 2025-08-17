package dev.slickcollections.kiwizin.murder.listeners.player;

import dev.slickcollections.kiwizin.murder.cmd.mm.BuildCommand;
import dev.slickcollections.kiwizin.murder.cmd.mm.CreateCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.slickcollections.kiwizin.murder.tagger.TagUtils;

public class PlayerQuitListener implements Listener {

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    evt.setQuitMessage(null);
    BuildCommand.remove(evt.getPlayer());
    TagUtils.reset(evt.getPlayer().getName());
    CreateCommand.CREATING.remove(evt.getPlayer());
  }
}
