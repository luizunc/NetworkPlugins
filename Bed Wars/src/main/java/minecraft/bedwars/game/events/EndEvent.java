package minecraft.bedwars.game.events;

import minecraft.bedwars.Language;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsEvent;

public class EndEvent extends BedWarsEvent {
  
  @Override
  public void execute(BedWars game) {
    game.stop(null);
  }
  
  @Override
  public String getName() {
    return Language.options$events$end;
  }
}
