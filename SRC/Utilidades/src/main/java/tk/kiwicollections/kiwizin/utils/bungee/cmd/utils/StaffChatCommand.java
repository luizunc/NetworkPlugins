package tk.kiwicollections.kiwizin.utils.bungee.cmd.utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.bungee.Bungee;
import tk.kiwicollections.kiwizin.utils.bungee.cmd.Commands;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

import java.util.ArrayList;

public class StaffChatCommand extends Commands {

   public static ArrayList<ProxiedPlayer> lixos = new ArrayList<>();
   public StaffChatCommand() {
       super("sc", "staffchat");
   }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!player.hasPermission("kutils.cmd.staffchat")) {
            player.sendMessage(TextComponent.fromLegacyText("§cVocê não tem permissão para executar este comando."));
            return;
        }
        if (args.length == 0) {
            if (!lixos.contains(player)) {
                lixos.add(player);
                sender.sendMessage(TextComponent.fromLegacyText("§aTudo que você digitar no chat sairá no chatstaff."));
            } else {
                lixos.remove(player);
                sender.sendMessage(TextComponent.fromLegacyText("§aTudo que você digitar no chat não sairá no chatstaff."));
            }
            return;
        }
        String msg = StringUtils.join(args, " ");
        BungeeCord.getInstance().getPlayers().stream().filter(playerx -> playerx.hasPermission("kutils.cmd.staffchat")).forEach(playerx -> {
            playerx.sendMessage(TextComponent.fromLegacyText("§6[SC] "
                    + Role.getPrefixed(player.getName()) + "§f: " + StringUtils.formatColors(msg)));
        });
    }
}
