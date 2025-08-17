package dev.slickcollections.kiwizin.murder.cmd.mm;

import dev.slickcollections.kiwizin.murder.game.ArenaRollbackerTask;
import org.bukkit.entity.Player;
import dev.slickcollections.kiwizin.murder.cmd.SubCommand;

public class UpdateCommand extends SubCommand {

  public UpdateCommand() {
    super("atualizar", "atualizar", "Atualizar o kMurder.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    player.sendMessage("§aO plugin já se encontra em sua última versão.");
  }
}
