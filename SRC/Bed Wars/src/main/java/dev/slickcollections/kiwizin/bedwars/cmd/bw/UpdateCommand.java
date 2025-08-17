package dev.slickcollections.kiwizin.bedwars.cmd.bw;

import dev.slickcollections.kiwizin.bedwars.cmd.SubCommand;
import org.bukkit.entity.Player;

public class UpdateCommand extends SubCommand {
  
  public UpdateCommand() {
    super("atualizar", "atualizar", "Atualizar o kBedWars.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    player.sendMessage("§aO plugin já se encontra em sua última versão.");
  }
  
}
