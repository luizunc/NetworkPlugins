package minecraft.core.bungee.cmd;

import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

/**
 * Comando para listar nicks ativos no BungeeCord.
 * Permite que jogadores com permissão vejam quem está usando nicks.
 * 
 * @author Luiz
 * @version 1.0
 */
public class NickListCommand extends Commands {

    // Constantes
    private static final String PERMISSION_MOD = "core.mod";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_HEADER = " \n§eLista em nicks:\n \n";
    private static final String MSG_FOOTER = "\n ";
    private static final String MSG_NO_NICKS = "§cNão há usuários utilizando nicks.";
    private static final String MSG_NICK_FORMAT = "§c%s §f= §acorefakereal:%s";

    public NickListCommand() {
        super("list", "nicks");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // Verifica se é um jogador
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(MSG_PLAYERS_ONLY));
            return;
        }
        
        ProxiedPlayer player = (ProxiedPlayer) sender;
        
        // Verifica permissão - apenas Mod ou acima
        if (!player.hasPermission(PERMISSION_MOD)) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NO_PERMISSION));
            return;
        }
        
        // Lista os nicks
        listNicks(player);
    }

    /**
     * Lista os nicks ativos.
     * 
     * @param player Jogador que solicitou a lista
     */
    private void listNicks(ProxiedPlayer player) {
        List<String> nicked = Bungee.listNicked();
        StringBuilder sb = new StringBuilder();

        // Constrói a lista de nicks
        for (int index = 0; index < nicked.size(); index++) {
            String nickName = nicked.get(index);
            sb.append(String.format(MSG_NICK_FORMAT, nickName, nickName));
            
            // Adiciona quebra de linha se não for o último
            if (index + 1 < nicked.size()) {
                sb.append("\n");
            }
        }

        // Limpa a lista para liberar memória
        nicked.clear();
        
        // Verifica se há nicks para exibir
        if (sb.length() == 0) {
            sb.append(MSG_NO_NICKS);
        }

        // Envia a mensagem formatada
        String finalMessage = MSG_HEADER + sb.toString() + MSG_FOOTER;
        player.sendMessage(TextComponent.fromLegacyText(finalMessage));
    }
}
