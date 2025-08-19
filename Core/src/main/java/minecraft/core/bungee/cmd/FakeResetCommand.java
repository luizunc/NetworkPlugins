package minecraft.core.bungee.cmd;

import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Comando para remover fake no BungeeCord.
 * Permite que jogadores removam seus nicknames falsos.
 * 
 * @author Luiz
 * @version 1.0
 */
public class FakeResetCommand extends Commands {

    // Constantes
    private static final String PERMISSION_FAKE = "core.cmd.fake";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_NOT_FAKE = "§cVocê não está utilizando um nickname falso.";

    public FakeResetCommand() {
        super("faker");
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
        if (!player.hasPermission(PERMISSION_FAKE)) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NO_PERMISSION));
            return;
        }
        
        // Verifica se está usando fake
        if (!Bungee.isFake(player.getName())) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NOT_FAKE));
            return;
        }
        
        // Remove o fake
        Bungee.removeFake(player);
    }
}
