package minecraft.core.bungee.cmd;

import minecraft.core.Manager;
import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Comando para aplicar nick no BungeeCord.
 * Permite que jogadores com permissão apliquem nicks falsos.
 * 
 * @author Luiz
 * @version 1.0
 */
public class NickCommand extends Commands {

    // Constantes
    private static final String PERMISSION_NICK = "rank.partner";
    private static final String DEFAULT_ROLE = "Membro";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_USAGE = "§cUso: /nick <nick>";

    public NickCommand() {
        super("nick");
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
        
        // Verifica argumentos
        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_USAGE));
            return;
        }
        
        String nickName = args[0];
        
        // Verifica disponibilidade do nick com feedback específico
        String availabilityCheck = Bungee.checkNicknameAvailability(nickName);
        if (availabilityCheck != null) {
            player.sendMessage(TextComponent.fromLegacyText(availabilityCheck));
            return;
        }
        
        // Aplica o nick
        applyNick(player, nickName);
    }

    /**
     * Aplica o nick no jogador.
     * 
     * @param player Jogador que receberá o nick
     * @param nickName Nome falso a ser aplicado
     */
    private void applyNick(ProxiedPlayer player, String nickName) {
        // Cargo sempre "Membro" e skin do nick escolhido
        String finalRoleName = DEFAULT_ROLE;
        String finalSkin = Manager.getSkin(nickName, "value") + ":" + Manager.getSkin(nickName, "signature");
        
        Bungee.applyNick(player, nickName, finalRoleName, finalSkin);
    }
}
