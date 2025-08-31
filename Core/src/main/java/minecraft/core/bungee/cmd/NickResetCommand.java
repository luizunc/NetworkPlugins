package minecraft.core.bungee.cmd;

import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Comando para remover nicks ativos no BungeeCord.
 * Permite que administradores removam nicks de jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class NickResetCommand extends Commands {

    // Constantes
    private static final String PERMISSION_NICK = "core.cmd.nick";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_USAGE = "§cUso: /reset nick";
    private static final String MSG_NOT_USING_NICK = "§cVocê não está usando um nickname falso.";
    private static final String MSG_NICK_REMOVED = "§aSeu nickname falso foi removido com sucesso.";

    public NickResetCommand() {
        super("reset", "nick");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // Verifica se é um jogador
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(MSG_PLAYERS_ONLY));
            return;
        }
        
        ProxiedPlayer player = (ProxiedPlayer) sender;
        
        // Verifica permissão
        if (!player.hasPermission(PERMISSION_NICK)) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NO_PERMISSION));
            return;
        }
        
        // Remove o próprio nick
        removeOwnNick(player);
    }

    /**
     * Remove o próprio nick do jogador.
     * 
     * @param player Jogador que executou o comando
     */
    private void removeOwnNick(ProxiedPlayer player) {
        // Verifica se o jogador está usando nick
        if (!Bungee.isNick(player.getName())) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NOT_USING_NICK));
            return;
        }
        
        // Remove o nick
        Bungee.removeNick(player);
        player.sendMessage(TextComponent.fromLegacyText(MSG_NICK_REMOVED));
    }
}
