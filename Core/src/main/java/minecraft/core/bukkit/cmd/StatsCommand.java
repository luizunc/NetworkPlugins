package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.menus.profile.MenuStatistics;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.ShowStatistics;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para visualizar estatísticas de jogadores.
 * Permite ver estatísticas próprias ou de outros jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class StatsCommand extends Commands {

    // Constantes
    private static final String ERROR_PROFILE = "§cErro ao carregar perfil do jogador: ";
    private static final String PLAYER_NOT_FOUND = "§cJogador não encontrado: ";
    private static final String USAGE_MESSAGE = "§eUso: §f/estatisticas [jogador]";

    /**
     * Construtor do comando de estatísticas.
     */
    public StatsCommand() {
        super("estatisticas", "stats");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;

        // Se não há argumentos, mostra as próprias estatísticas
        if (args.length == 0) {
            showOwnStatistics(player);
            return;
        }

        // Se há argumentos, mostra estatísticas do jogador especificado
        String targetName = args[0];
        showOtherStatistics(player, targetName);
    }

    /**
     * Mostra as estatísticas do próprio jogador.
     * 
     * @param player Jogador que executou o comando
     */
    private void showOwnStatistics(Player player) {
        try {
            Profile profile = Profile.createOrLoadProfile(player.getName());
            new MenuStatistics(player, profile, true);
        } catch (ProfileLoadException e) {
            Core.getInstance().getLogger().warning(ERROR_PROFILE + player.getName() + ": " + e.getMessage());
            player.sendMessage(ERROR_PROFILE + player.getName());
            EnumSound.VILLAGER_NO.play(player, 1.0f, 1.0f);
        }
    }

    /**
     * Mostra as estatísticas de outro jogador.
     * 
     * @param player Jogador que executou o comando
     * @param targetName Nome do jogador alvo
     */
    private void showOtherStatistics(Player player, String targetName) {
        // Verifica se o jogador alvo está online
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer != null) {
            try {
                Profile targetProfile = Profile.createOrLoadProfile(targetPlayer.getName());
                
                // Verifica se o jogador alvo permite que outros vejam suas estatísticas
                if (targetProfile.getPreferencesContainer().getShowStatistics() == ShowStatistics.DESATIVADO) {
                    player.sendMessage("§cEste jogador desabilitou a visualização de suas estatísticas.");
                    EnumSound.VILLAGER_NO.play(player, 1.0f, 1.0f);
                    return;
                }
                
                new MenuStatistics(player, targetProfile, true);
            } catch (ProfileLoadException e) {
                Core.getInstance().getLogger().warning(ERROR_PROFILE + targetName + ": " + e.getMessage());
                player.sendMessage(ERROR_PROFILE + targetName);
                EnumSound.VILLAGER_NO.play(player, 1.0f, 1.0f);
            }
            return;
        }

        // Se não está online, mostra apenas o aviso
        player.sendMessage("§cO jogador está offline.");
        EnumSound.VILLAGER_NO.play(player, 1.0f, 1.0f);
    }
}
