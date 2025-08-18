package minecraft.lobby.lobby;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task curta para travar constantemente todos os mundos no tick 6000
 * e manter o ciclo do dia desativado. Executa a cada tick.
 */
public class AlwaysDayTask extends BukkitRunnable {

    private static final long DAY_TIME = 6000L;

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(DAY_TIME);
            world.setGameRuleValue("doDaylightCycle", "false");
        }
    }
}


