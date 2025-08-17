package tk.kiwicollections.kiwizin.utils.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.cmd.utils.StaffChatCommand;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
        if (StaffChatCommand.BAIANOS.contains(evt.getPlayer())) {
            evt.setCancelled(true);
            Player player = evt.getPlayer();
            Bukkit.getOnlinePlayers().stream().filter(playerx -> playerx.hasPermission("kutils.cmd.staffchat")).forEach(playerx -> {
                if (Language.staffchat$warning_actionbar) {
                    NMS.sendActionBar(playerx, Language.staffchat$actionbar);
                }
                if (Language.staffchat$play_sound) {
                    EnumSound.valueOf(Language.staffchat$sound).play(playerx, 1.0F, 1.0F);
                }
                playerx.sendMessage(Language.staffchat$format.replace("{player}", Role.getPrefixed(player.getName(), true)).replace("{message}", evt.getMessage().replace("&", "§")));
            });
        }
    }

}
