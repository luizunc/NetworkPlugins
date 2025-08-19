package minecraft.core.bukkit.cmd;

import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para visualizar os coins do jogador.
 * Mostra os coins de diferentes modalidades de jogo.
 * 
 * @author Luiz
 * @version 1.0
 */
public class CoinsCommand extends Commands {

    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_COINS_HEADER = "\n§eSeus coins:";
    private static final String MSG_COINS_FORMAT = " §8▪ §f%s &7%s";
    private static final String MSG_COINS_FOOTER = "\n";
    
    // Modalidades de jogo
    private static final String[] GAME_MODES = {
        "Bed Wars", 
        "Sky Wars"
    };

    public CoinsCommand() {
        super("coins");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_PLAYERS_ONLY);
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        
        // Exibe cabeçalho
        player.sendMessage(MSG_COINS_HEADER);

        // Exibe coins de cada modalidade
        for (String gameMode : GAME_MODES) {
            String coinsKey = "Core" + gameMode.replace(" ", "");
            long coins = (long) profile.getCoins(coinsKey);
            String formattedCoins = StringUtils.formatNumber(coins);
            
            player.sendMessage(String.format(MSG_COINS_FORMAT, gameMode, formattedCoins));
        }

        // Exibe rodapé
        player.sendMessage(MSG_COINS_FOOTER);
    }
}