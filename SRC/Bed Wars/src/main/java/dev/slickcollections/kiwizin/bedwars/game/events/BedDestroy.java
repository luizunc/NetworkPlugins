package dev.slickcollections.kiwizin.bedwars.game.events;

import dev.slickcollections.kiwizin.bedwars.Language;
import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsEvent;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsTeam;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;

public class BedDestroy extends BedWarsEvent {
  
  @Override
  public void execute(BedWars game) {
    game.listTeams().forEach(BedWarsTeam::breakBed);
    game.listPlayers(false).forEach(player -> {
      EnumSound.ENDERDRAGON_GROWL.play(player, 1.0F, 1.0F);
      player.sendMessage("§aTodas as camas foram destruidas.");
    });
  }
  
  @Override
  public String getName() {
    return Language.options$events$beddestroy;
  }
}
