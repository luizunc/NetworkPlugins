package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando principal do sistema Core.
 * Mostra informações básicas sobre o plugin.
 * 
 * @author Luiz
 * @version 1.0
 */
public class CoreCommand extends Commands {

    // Constantes
    private static final String PERMISSION_ADMIN = "core.admin";
    
    // Mensagens
    private static final String MSG_VERSION = "§6Core §bv%s §7Criado por §6Luiz§7.";
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_ADMIN_INFO = "§6§l[CORE]\n§7Versão: §bv%s\n§7Autor: §6Luiz";

    public CoreCommand() {
        super("core", "c");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_PLAYERS_ONLY);
            return;
        }

        Player player = (Player) sender;
        
        // Verifica permissão de administrador
        if (!player.hasPermission(PERMISSION_ADMIN)) {
            player.sendMessage(String.format(MSG_VERSION, 
                Core.getInstance().getDescription().getVersion()));
            return;
        }

        // Mostra informações para administradores
        String version = Core.getInstance().getDescription().getVersion();
        player.sendMessage(String.format(MSG_ADMIN_INFO, version));
    }
}