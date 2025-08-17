package tk.kiwicollections.kiwizin.utils.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.Main;
import tk.slicecollections.maxteer.nms.NMS;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if (Language.lobby$titles$welcome$enabled) {
            NMS.sendTitle(player, Language.lobby$titles$welcome$header, Language.lobby$titles$welcome$footer);
        }
    }

}
