package tk.kiwicollections.kiwizin.utils.bungee.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.kiwicollections.kiwizin.utils.bungee.cmd.utils.StaffChatCommand;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

public class Listeners implements Listener {

    @EventHandler(priority = (byte) 128)
    public void onChat(ChatEvent evt) {
        if (evt.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) evt.getSender();
            if (!evt.isCommand()) {
                if (StaffChatCommand.lixos.contains(player)) {
                    evt.setCancelled(true);
                    BungeeCord.getInstance().getPlayers().stream().filter(playerx -> playerx.hasPermission("kutils.cmd.staffchat")).forEach(playerx -> {
                        playerx.sendMessage(TextComponent.fromLegacyText("§6[SC] " +
                                Role.getPrefixed(player.getName()) + "§f: " + StringUtils.formatColors(evt.getMessage())));
                    });
                }
            }
        }
    }
}
