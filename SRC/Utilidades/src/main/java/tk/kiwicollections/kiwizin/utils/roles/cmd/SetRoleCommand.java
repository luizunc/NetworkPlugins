package tk.kiwicollections.kiwizin.utils.roles.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.kiwicollections.kiwizin.utils.roles.menu.MenuSetRole;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.role.Role;
import tk.slicecollections.maxteer.utils.StringUtils;

public class SetRoleCommand extends Commands {

    public SetRoleCommand() {
        super("setrole", "setgroup");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
            return;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("kutils.cmd.setgroup")) {
            player.sendMessage("§cVocê não tem permissão para executar este comando.");
            return;
        }
        if (args.length == 0) {
            player.sendMessage("Utilize /setrole [player] ou /setrole [player] [grupo]");
            return;
        }
        if (args.length == 1) {
            new MenuSetRole(Profile.getProfile(player.getName()), args[0]);
        } else {
            if (Role.getRoleByName(args[1]) == null) {
                player.sendMessage("§cUtilize apenas grupos válidos.");
                return;
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " parent set " + args[1]);
            player.sendMessage(Language.roles$set.replace("{player}", args[0]).replace("{role}", Role.getRoleByName(args[1]).getName()));
            Bukkit.getOnlinePlayers().forEach(playerr -> NMS.sendTitle(playerr, Language.roles$titles$header.replace("{player}", StringUtils.getFirstColor(Role.getRoleByName(args[1]).getName()) + " " + args[0]), Language.roles$titles$footer.replace("{role}", Role.getRoleByName(args[1]).getName())));
            if (Bukkit.getPlayerExact(args[0]) != null) {
                if (Bukkit.getPlayerExact(args[0]).isOnline()) {
                    Bukkit.getPlayerExact(args[0]).sendMessage(Language.roles$notification.replace("{role}", Role.getRoleByName(args[1]).getName()));
                }
            }
        }
    }
}
