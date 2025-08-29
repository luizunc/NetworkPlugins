package minecraft.bedwars.cmd.pl;

import minecraft.bedwars.cmd.SubCommand;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.object.BedWarsConfig;
import org.bukkit.entity.Player;

public class TestWaitingLocationCommand extends SubCommand {
  
  public TestWaitingLocationCommand() {
    super("testwaiting", "testwaiting", "Testar a localização de espera da arena atual.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    BedWars game = BedWars.getByWorldName(player.getWorld().getName());
    if (game == null) {
      player.sendMessage("§cNão existe uma arena neste mundo.");
      return;
    }
    
    BedWarsConfig config = game.getConfig();
    if (config.getWaitingLocation() != null) {
      player.sendMessage("§aLocalização de espera encontrada!");
      player.sendMessage("§7Mundo: " + config.getWaitingLocation().getWorld().getName());
      player.sendMessage("§7X: " + config.getWaitingLocation().getX());
      player.sendMessage("§7Y: " + config.getWaitingLocation().getY());
      player.sendMessage("§7Z: " + config.getWaitingLocation().getZ());
      
      // Teleportar o jogador para a localização de espera
      player.teleport(config.getWaitingLocation());
      player.sendMessage("§aVocê foi teleportado para a localização de espera!");
    } else {
      player.sendMessage("§cNenhuma localização de espera configurada para esta arena.");
    }
  }
} 