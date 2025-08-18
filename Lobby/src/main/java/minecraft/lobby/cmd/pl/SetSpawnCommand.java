package minecraft.lobby.cmd.pl;

import minecraft.core.bukkit.Core;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.lobby.Main;
import minecraft.lobby.cmd.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para definir o spawn do servidor.
 * Permite aos administradores definir a localização de spawn do lobby.
 */
public final class SetSpawnCommand extends SubCommand {
    
    private static final String COMMAND_NAME = "setspawn";
    private static final String COMMAND_USAGE = "setspawn";
    private static final String COMMAND_DESCRIPTION = "Setar o spawn do servidor.";
    private static final String SUCCESS_MESSAGE = "§aSpawn setado.";
    private static final String CONFIG_SPAWN_KEY = "spawn";
    
    /**
     * Constrói o comando de definição de spawn.
     */
    public SetSpawnCommand() {
        super(COMMAND_NAME, COMMAND_USAGE, COMMAND_DESCRIPTION, true);
    }
    
    @Override
    public void perform(CommandSender sender, String[] args) {
        // Comando apenas para jogadores
    }
    
    @Override
    public void perform(Player player, String[] args) {
        Location spawnLocation = createSpawnLocation(player);
        saveSpawnLocation(spawnLocation);
        setLobbySpawn(spawnLocation);
        notifyPlayer(player);
    }
    
    /**
     * Cria a localização de spawn baseada na posição do jogador.
     * @param player jogador que define o spawn
     * @return localização do spawn
     */
    private Location createSpawnLocation(Player player) {
        Location playerLocation = player.getLocation();
        Location spawnLocation = playerLocation.getBlock().getLocation().add(0.5, 0, 0.5);
        
        // Mantém a direção do jogador
        spawnLocation.setYaw(playerLocation.getYaw());
        spawnLocation.setPitch(playerLocation.getPitch());
        
        return spawnLocation;
    }
    
    /**
     * Salva a localização do spawn na configuração.
     * @param location localização a ser salva
     */
    private void saveSpawnLocation(Location location) {
        String serializedLocation = BukkitUtils.serializeLocation(location);
        Main.getInstance().getConfig().set(CONFIG_SPAWN_KEY, serializedLocation);
        Main.getInstance().saveConfig();
    }
    
    /**
     * Define o spawn do lobby no sistema.
     * @param location localização do spawn
     */
    private void setLobbySpawn(Location location) {
        Core.setLobby(location);
    }
    
    /**
     * Notifica o jogador sobre o sucesso da operação.
     * @param player jogador a ser notificado
     */
    private void notifyPlayer(Player player) {
        player.sendMessage(SUCCESS_MESSAGE);
    }
}
