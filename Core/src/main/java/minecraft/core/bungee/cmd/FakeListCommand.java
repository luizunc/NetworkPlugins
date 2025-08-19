package minecraft.core.bungee.cmd;

import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

/**
 * Comando para listar jogadores com nicknames falsos.
 * Mostra todos os jogadores que estão usando fake no servidor.
 * 
 * @author Luiz
 * @version 1.0
 */
public class FakeListCommand extends Commands {

    // Constantes
    private static final String PERMISSION_FAKELIST = "core.cmd.fakelist";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_HEADER = " \n§eLista de nicknames falsos:\n \n";
    private static final String MSG_FOOTER = "\n ";
    private static final String MSG_NO_FAKES = "§cNão há nenhum usuário utilizando um nickname falso.";
    private static final String MSG_FAKE_FORMAT = "§c%s §fé na verdade §a%s";

    public FakeListCommand() {
        super("fakel");
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
        if (!player.hasPermission(PERMISSION_FAKELIST)) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_NO_PERMISSION));
            return;
        }
        
        // Obtém e exibe a lista de fakes
        displayFakeList(player);
    }

    /**
     * Exibe a lista de jogadores com nicknames falsos.
     * 
     * @param player Jogador que solicitou a lista
     */
    private void displayFakeList(ProxiedPlayer player) {
        List<String> nicked = Bungee.listNicked();
        StringBuilder sb = new StringBuilder();
        
        // Constrói a lista de fakes
        for (int index = 0; index < nicked.size(); index++) {
            String realName = nicked.get(index);
            String fakeName = Bungee.getFake(realName);
            
            sb.append(String.format(MSG_FAKE_FORMAT, fakeName, realName));
            
            // Adiciona quebra de linha se não for o último
            if (index + 1 < nicked.size()) {
                sb.append("\n");
            }
        }
        
        // Limpa a lista para liberar memória
        nicked.clear();
        
        // Verifica se há fakes para exibir
        if (sb.length() == 0) {
            sb.append(MSG_NO_FAKES);
        }
        
        // Envia a mensagem formatada
        String finalMessage = MSG_HEADER + sb.toString() + MSG_FOOTER;
        player.sendMessage(TextComponent.fromLegacyText(finalMessage));
    }
}
