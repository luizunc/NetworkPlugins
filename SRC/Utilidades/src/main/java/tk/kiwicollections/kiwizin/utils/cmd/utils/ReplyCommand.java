package tk.kiwicollections.kiwizin.utils.cmd.utils;

import com.comphenix.protocol.error.Report;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

import static tk.kiwicollections.kiwizin.utils.Main.reply;

public class ReplyCommand extends Commands {

    public ReplyCommand() {
        super("r", "reply");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§cUtilize /tell [mensagem]");
            return;
        }
        Player target = reply.get(player);
        if (target == null) {
            player.sendMessage("§cVocê não tem ninguém para responder.");
            return;
        }
        if (!target.isOnline()) {
            player.sendMessage("§cUsuário não encontrado.");
            return;
        }
        reply.put(target, player);
        String msg = StringUtils.join(args, " ");
        if (player.hasPermission("kutils.tell.color")) {
            msg = msg.replace("&", "§");
        }
        target.sendMessage(Language.tell$format$target.replace("{player}", Role.getPrefixed(player.getName())).replace("{message}", msg));
        player.sendMessage(Language.tell$format$sender.replace("{player}", Role.getPrefixed(target.getName())).replace("{message}", msg));
    }
}
