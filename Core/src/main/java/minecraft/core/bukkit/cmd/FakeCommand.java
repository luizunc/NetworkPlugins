package minecraft.core.bukkit.cmd;

import minecraft.core.Manager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.fake.FakeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FakeCommand extends Commands {
    public FakeCommand() {
        super("fake", new String[]{"faker", "fakel"});
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
        } else {
            Player player = (Player)sender;
            if (player.hasPermission("core.cmd.fake") && (!label.equalsIgnoreCase("fakel") || player.hasPermission("core.cmd.fakelist"))) {
                Profile profile = Profile.getProfile(player.getName());
                if (label.equalsIgnoreCase("fake")) {
                    if (profile != null && profile.playingGame()) {
                        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
                        return;
                    }

                    if (args.length == 0) {
                        player.sendMessage("§cUso: /fake <nick>");
                        return;
                    }

                    String fakeName = args[0];
                    if (!FakeManager.isUsable(fakeName)) {
                        player.sendMessage("§cO nickname '" + fakeName + "' não está disponível para uso.");
                        return;
                    }

                    String finalRoleName = "Membro";
                    String finalSkin = Manager.getSkin(fakeName, "value") + ":" + Manager.getSkin(fakeName, "signature");
                    FakeManager.applyFake(player, fakeName, finalRoleName, finalSkin);
                } else if (label.equalsIgnoreCase("faker")) {
                    if (profile != null && profile.playingGame()) {
                        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
                        return;
                    }

                    if (!FakeManager.isFake(player.getName())) {
                        player.sendMessage("§cVocê não está utilizando um nickname falso.");
                        return;
                    }

                    FakeManager.removeFake(player);
                } else {
                    List<String> nicked = FakeManager.listNicked();
                    StringBuilder sb = new StringBuilder();

                    for(int index = 0; index < nicked.size(); ++index) {
                        sb.append("§c").append((String)nicked.get(index)).append(" §fé na verdade ").append("§acorefakereal:").append((String)nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
                    }

                    nicked.clear();
                    if (sb.length() == 0) {
                        sb.append("§cNão há nenhum usuário utilizando um nickname falso.");
                    }

                    player.sendMessage(" \n§eLista de nicknames falsos:\n \n" + sb + "\n ");
                }

            } else {
                player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
            }
        }
    }
}