package tk.kiwicollections.kiwizin.utils.cmd.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.slicecollections.maxteer.nms.NMS;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand extends Commands {

    private static final List<String> FLY = new ArrayList<>();

    public FlyCommand() {
        super("fly", "voar");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("kutils.cmd.fly")) {
            player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
            return;
        }
        if (hasFly(player)) {
            FLY.remove(player.getName());
            player.setAllowFlight(false);
            NMS.sendTitle(player, Language.lobby$titles$fly$header, Language.lobby$titles$fly$disabled$footer);
            player.sendMessage("§cModo voar desativado.");
        } else {
            FLY.add(player.getName());
            player.setAllowFlight(true);
            NMS.sendTitle(player, Language.lobby$titles$fly$header, Language.lobby$titles$fly$enabled$footer);
            player.sendMessage("§aModo voar ativado.");
        }
    }

    public static void remove(Player player) {
        FLY.remove(player.getName());
    }

    public static boolean hasFly(Player player) {
        return FLY.contains(player.getName());
    }
}
