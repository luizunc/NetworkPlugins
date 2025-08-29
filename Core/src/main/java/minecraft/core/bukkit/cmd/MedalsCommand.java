package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.menu.MedalsCommandMenu;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.Medal;
import minecraft.core.core.player.fake.FakeManager;

import java.util.ArrayList;
import java.util.List;

public class MedalsCommand extends Commands {

    public MedalsCommand() {
        super("medals", "medalha", "medal");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;

        if (FakeManager.isFake(player.getName())) {
            player.sendMessage("§cNão é possível executar este comando com o /nick ativado.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5f, 2.0f);
            return;
        }

        List<String> medalhasDisponiveis = new ArrayList<>();
        for (Medal medal : Medal.values()) {
            String permission = medal.getPermission();
            if (player.hasPermission(permission)) {
                medalhasDisponiveis.add(medal.getName());
            }
        }

        if (args.length == 0) {
            new MedalsCommandMenu(Profile.getProfile(player.getName()));
        }
    }
}
