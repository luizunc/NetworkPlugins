package minecraft.lobby.lobby;

import org.bukkit.scheduler.BukkitRunnable;

import static minecraft.lobby.lobby.Lobby.QUERY;

public class LobbyEntryTask extends BukkitRunnable {
  
  @Override
  public void run() {
    QUERY.forEach(Lobby::fetch);
  }
}
