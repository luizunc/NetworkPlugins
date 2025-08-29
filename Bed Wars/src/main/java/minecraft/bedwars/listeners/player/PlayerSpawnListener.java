package minecraft.bedwars.listeners.player;

import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.core.core.game.GameState;
import minecraft.core.core.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerSpawnListener implements Listener {
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent evt) {
        Player player = evt.getPlayer();
        Profile profile = Profile.getProfile(player.getName());
        
        if (profile != null) {
            BedWars game = profile.getGame(BedWars.class);
            if (game != null) {
                if (game.getState() == GameState.AGUARDANDO) {
                    // Se o jogo ainda estiver aguardando, teleportar para localização de espera
                    if (game.getConfig().getWaitingLocation() != null) {
                        evt.setRespawnLocation(game.getConfig().getWaitingLocation());
                    }
                } else if (game.getState() == GameState.EMJOGO) {
                    // Se o jogo estiver em andamento, teleportar para a ilha do time
                    BedWarsTeam team = game.getTeam(player);
                    if (team != null) {
                        evt.setRespawnLocation(team.getLocation());
                    }
                }
            }
        }
    }
} 