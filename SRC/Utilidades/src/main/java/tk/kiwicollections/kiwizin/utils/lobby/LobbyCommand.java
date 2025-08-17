package tk.kiwicollections.kiwizin.utils.lobby;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.Main;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.game.Game;
import tk.slicecollections.maxteer.game.GameTeam;
import tk.slicecollections.maxteer.player.Profile;

public class LobbyCommand extends Commands {

    public LobbyCommand() {
        super("lobby");
    }


    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return;
        }
        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        if (Language.options$bungeecord) {
            if (!profile.playingGame()) {
                Core.sendServer(Profile.getProfile(player.getName()), Main.getInstance().getConfig().getString("lobby_servername"));
                return;
            }
        }
        if (profile.playingGame()) {
            if (profile.getGame() != null) {
                profile.getGame().leave(profile, null);
            }
        }
        player.sendMessage(Language.lobby$spigot$no_playing);
    }
}
