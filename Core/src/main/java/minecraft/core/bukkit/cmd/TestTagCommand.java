package minecraft.core.bukkit.cmd;

import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.rank.RankPermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando de teste para verificar se a tag Membro está aparecendo para todos os ranks.
 * 
 * @author Luiz
 * @version 1.0
 */
public class TestTagCommand extends Commands {

    public TestTagCommand() {
        super("testtag");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
            return;
        }

        Player player = (Player) sender;
        
        player.sendMessage("§e=== Teste de Tags Disponíveis ===");
        
        // Listar todas as tags e verificar permissões
        for (Rank rank : Rank.listRoles()) {
            boolean isDefault = rank.isDefault();
            boolean hasPermission = RankPermissionUtils.hasRankOrHigher(player, rank.getPermission());
            boolean canUse = isDefault || hasPermission;
            
            String status = canUse ? "§a✓" : "§c✗";
            String permissionInfo = isDefault ? "§7(Padrão)" : (hasPermission ? "§a(Tem permissão)" : "§c(Sem permissão)");
            
            player.sendMessage(String.format("%s %s %s", status, rank.getName(), permissionInfo));
        }
        
        player.sendMessage("§e=================================");
        player.sendMessage("§7A tag 'Membro' deve sempre aparecer como ✓ (Padrão)");
        player.sendMessage("§7Use /tag para abrir o menu e verificar se a tag Membro aparece!");
    }
} 