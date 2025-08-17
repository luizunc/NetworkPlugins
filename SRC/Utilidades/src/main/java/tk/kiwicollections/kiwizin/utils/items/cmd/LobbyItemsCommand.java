package tk.kiwicollections.kiwizin.utils.items.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.player.hotbar.Hotbar;

public class LobbyItemsCommand extends Commands {

    public LobbyItemsCommand() {
        super("lobbyitems");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
            return;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("kutils.cmd.items")) {
            player.sendMessage("§cVocê não tem permissão para executar este comando.");
            return;
        }
        player.getActivePotionEffects().clear();
        tk.slicecollections.maxteer.collectibles.api.CosmeticsAPI.disable(player);
        Profile.getProfile(player.getName()).setHotbar(Hotbar.getHotbarById("lobby"));
        Profile.getProfile(player.getName()).getHotbar().apply(Profile.getProfile(player.getName()));
        tk.slicecollections.maxteer.collectibles.api.CosmeticsAPI.enable(player);
    }
}
