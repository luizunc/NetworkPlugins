package minecraft.lobby.listeners.server;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Listener para eventos do servidor.
 * Controla eventos relacionados ao mundo, clima e tempo.
 */
public class ServerListener implements Listener {
    
    private static final long DAY_TIME = 6000L;
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent evt) {
        evt.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockBurn(BlockBurnEvent evt) {
        evt.setCancelled(true);
    }
    
    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent evt) {
        evt.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent evt) {
        evt.setCancelled(true);
    }
    
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent evt) {
        evt.setCancelled(evt.toWeatherState());
    }
    
    /**
     * Ao carregar um mundo, garante que fique travado no dia.
     */
    @EventHandler
    public void onWorldLoad(WorldLoadEvent evt) {
        applyAlwaysDay(evt.getWorld());
    }
    
    private void applyAlwaysDay(World world) {
        world.setTime(DAY_TIME);
        world.setGameRuleValue("doDaylightCycle", "false");
    }
}
