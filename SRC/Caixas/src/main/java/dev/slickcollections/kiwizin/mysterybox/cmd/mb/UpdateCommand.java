package dev.slickcollections.kiwizin.mysterybox.cmd.mb;

import dev.slickcollections.kiwizin.mysterybox.cmd.SubCommand;
import org.bukkit.entity.Player;

public class UpdateCommand extends SubCommand {
  
  public UpdateCommand() {
    super("atualizar", "atualizar", "Atualizar o kMysteryBox.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    player.sendMessage("§aO plugin já se encontra em sua última versão.");
  }
}
