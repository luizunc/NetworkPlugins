package minecraft.lobby.lobby;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task para gerenciar entradas de lobbies e manter tempo fixo.
 */
public class LobbyEntryTask extends BukkitRunnable {
    
    private static final long DAY_TIME = 6000L;
    
    @Override
    public void run() {
        // Atualiza contadores
        for (Lobby lobby : Lobby.QUERY) {
            lobby.fetch();
        }
        
        // Mantém todos os mundos fixos em 6000 e ciclo desativado
        for (World world : Bukkit.getWorlds()) {
            world.setTime(DAY_TIME);
            world.setGameRuleValue("doDaylightCycle", "false");
        }
    }
}

