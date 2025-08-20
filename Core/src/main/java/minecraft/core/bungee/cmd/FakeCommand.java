package minecraft.core.bungee.cmd;

import minecraft.core.Manager;
import minecraft.core.bungee.Bungee;
import minecraft.core.core.player.role.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static minecraft.core.bungee.Bungee.ALEX;
import static minecraft.core.bungee.Bungee.STEVE;

/**
 * Comando para aplicar fake no BungeeCord.
 * Permite que jogadores com permissão apliquem nicks falsos.
 * 
 * @author Luiz
 * @version 1.0
 */
public class FakeCommand extends Commands {

    // Constantes
    private static final String PERMISSION_FAKE = "core.cmd.fake";
    private static final String DEFAULT_ROLE = "Membro";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_USAGE = "§cUso: /fake <nick>";

    public FakeCommand() {
        super("fake");
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
        
        // Verifica argumentos
        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(MSG_USAGE));
            return;
        }
        
        String fakeName = args[0];
        
        // Verifica disponibilidade do nick com feedback específico
        String availabilityCheck = Bungee.checkNicknameAvailability(fakeName);
        if (availabilityCheck != null) {
            player.sendMessage(TextComponent.fromLegacyText(availabilityCheck));
            return;
        }
        
        // Aplica o fake
        applyFake(player, fakeName);
    }

    /**
     * Aplica o fake no jogador.
     * 
     * @param player Jogador que receberá o fake
     * @param fakeName Nome falso a ser aplicado
     */
    private void applyFake(ProxiedPlayer player, String fakeName) {
        // Cargo sempre "Membro" e skin do nick escolhido
        String finalRoleName = DEFAULT_ROLE;
        String finalSkin = Manager.getSkin(fakeName, "value") + ":" + Manager.getSkin(fakeName, "signature");
        
        Bungee.applyFake(player, fakeName, finalRoleName, finalSkin);
    }
}
