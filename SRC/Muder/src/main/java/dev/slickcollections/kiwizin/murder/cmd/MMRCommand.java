package dev.slickcollections.kiwizin.murder.cmd;

import dev.slickcollections.kiwizin.murder.game.enums.MurderSkin;
import dev.slickcollections.kiwizin.murder.game.types.AssassinsMurder;
import dev.slickcollections.kiwizin.murder.game.types.ClassicMurder;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.slickcollections.kiwizin.murder.game.Murder;

public class MMRCommand extends Commands {

  public MMRCommand() {
    super("mmr");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        if (!player.hasPermission("kmurder.cmd.mmr")) {
          player.sendMessage("§cVocê não possui permissão para utilizar esse comando.");
          return;
        }


        Murder game = profile.getGame(Murder.class);
        if (game == null) {
          player.sendMessage("§cVocê não se encontra em uma partida.");
          return;
        }

        if (game instanceof ClassicMurder) {
          player.sendMessage("§cEsta sala não possui sistema de personagens.");
          return;
        }

        StringBuilder message = new StringBuilder(" \nJogadores:\n");
        for (Player players : game.listPlayers(false)) {
          MurderSkin skin = ((AssassinsMurder) game).getSkin(players);
          if (skin == null) {
            continue;
          }

          message.append(Role.getColored(players.getName())).append(" §7é na verdade §akcorefakereal:").append(players.getName()).append("\n");
        }
        message.append("\n ");
        player.sendMessage(message.toString());
      }
    }
  }
}
