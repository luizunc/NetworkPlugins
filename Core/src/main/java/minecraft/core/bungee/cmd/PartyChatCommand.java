package minecraft.core.bungee.cmd;

import minecraft.core.bungee.party.BungeeParty;
import minecraft.core.bungee.party.BungeePartyManager;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Comando para chat de party no BungeeCord.
 * Permite que membros de uma party conversem entre si.
 * 
 * @author Luiz
 * @version 1.0
 */
public class PartyChatCommand extends Commands {

    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_USAGE = "§cUso: /p [mensagem] para conversar com a sua Party.";
    private static final String MSG_NO_PARTY = "§cVocê não pertence a uma Party.";
    private static final String MSG_PARTY_CHAT = "§d[Party] %s§f: %s";

    public PartyChatCommand() {
        super("p");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // Verifica se é um jogador
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(MSG_PLAYERS_ONLY));
            return;
        }
        
        ProxiedPlayer player = (ProxiedPlayer) sender;
        
        // Verifica argumentos
        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_USAGE));
            return;
        }
        
        // Obtém a party do jogador
        BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
        if (party == null) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NO_PARTY));
            return;
        }
        
        // Envia a mensagem para a party
        sendPartyMessage(party, player, args);
    }

    /**
     * Envia mensagem para todos os membros da party.
     * 
     * @param party Party que receberá a mensagem
     * @param player Jogador que enviou a mensagem
     * @param args Argumentos contendo a mensagem
     */
    private void sendPartyMessage(BungeeParty party, ProxiedPlayer player, String[] args) {
        String playerPrefix = Rank.getPrefixed(player.getName());
        String message = StringUtils.join(args, " ");
        String formattedMessage = String.format(MSG_PARTY_CHAT, playerPrefix, message);
        
        party.broadcast(formattedMessage);
    }
}
