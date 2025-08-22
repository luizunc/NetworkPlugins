package minecraft.bedwars.cmd.bw;

import minecraft.bedwars.cmd.SubCommand;
import minecraft.bedwars.game.BedWars;
import minecraft.core.core.game.GameState;
import minecraft.core.core.player.Profile;
import org.bukkit.entity.Player;

public class StartCommand extends SubCommand {
  
  public StartCommand() {
    super("iniciar", "iniciar", "Iniciar a partida.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      BedWars game = profile.getGame(BedWars.class);
      if (game != null) {
        if (game.getState() == GameState.AGUARDANDO) {
          player.sendMessage("§aVocê iniciou a partida!");
          game.start();
        } else {
          player.sendMessage("§cA partida já está em andamento.");
        }
      }
    }
  }
}
