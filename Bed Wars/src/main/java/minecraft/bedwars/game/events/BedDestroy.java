package minecraft.bedwars.game.events;

import minecraft.bedwars.Language;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsEvent;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.core.core.utils.enums.EnumSound;

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
