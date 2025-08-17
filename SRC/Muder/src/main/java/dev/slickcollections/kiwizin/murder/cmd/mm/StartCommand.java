package dev.slickcollections.kiwizin.murder.cmd.mm;

import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.entity.Player;
import dev.slickcollections.kiwizin.murder.cmd.SubCommand;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;

public class StartCommand extends SubCommand {

  public StartCommand() {
    super("iniciar", "iniciar", "Iniciar a partida.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      Murder game = profile.getGame(Murder.class);
      if (game != null) {
        if (game.getState() == GameState.AGUARDANDO) {
          if (game.getOnline() < (game.getMode() == MurderMode.CLASSIC ? 3 : 1)) {
            player.sendMessage("§cNão há players suficientes para iniciar a partida.");
            return;
          }

          game.start();
          player.sendMessage("§aVocê iniciou a partida!");
        } else {
          player.sendMessage("§cA partida já está em andamento.");
        }
      }
    }
  }
}
