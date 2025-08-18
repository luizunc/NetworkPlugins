package minecraft.lobby.cmd.pl;

import minecraft.lobby.cmd.SubCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Comando para gerenciar o modo construtor.
 * Permite aos jogadores ativar/desativar o modo de construção.
 */
public final class BuildCommand extends SubCommand {
    
    private static final String COMMAND_NAME = "build";
    private static final String COMMAND_USAGE = "build";
    private static final String COMMAND_DESCRIPTION = "Ativar/Desativar modo construtor.";
    private static final String BUILD_MODE_ENABLED = "§aModo construtor ativado.";
    private static final String BUILD_MODE_DISABLED = "§cModo construtor desativado.";
    
    private static final List<String> BUILDERS = new ArrayList<>();
    
    /**
     * Constrói o comando de modo construtor.
     */
    public BuildCommand() {
        super(COMMAND_NAME, COMMAND_USAGE, COMMAND_DESCRIPTION, true);
    }
    
    /**
     * Remove um jogador da lista de construtores.
     * @param player jogador a ser removido
     */
    public static void remove(Player player) {
        BUILDERS.remove(player.getName());
    }
    
    /**
     * Verifica se um jogador é construtor.
     * @param player jogador a ser verificado
     * @return true se é construtor, false caso contrário
     */
    public static boolean isBuilder(Player player) {
        return BUILDERS.contains(player.getName());
    }
    
    @Override
    public void perform(CommandSender sender, String[] args) {
        // Comando apenas para jogadores
    }
    
    @Override
    public void perform(Player player, String[] args) {
        if (isBuilder(player)) {
            disableBuildMode(player);
        } else {
            enableBuildMode(player);
        }
    }
    
    /**
     * Ativa o modo construtor para o jogador.
     * @param player jogador para ativar o modo
     */
    private void enableBuildMode(Player player) {
        BUILDERS.add(player.getName());
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(BUILD_MODE_ENABLED);
    }
    
    /**
     * Desativa o modo construtor para o jogador.
     * @param player jogador para desativar o modo
     */
    private void disableBuildMode(Player player) {
        BUILDERS.remove(player.getName());
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(BUILD_MODE_DISABLED);
    }
}
