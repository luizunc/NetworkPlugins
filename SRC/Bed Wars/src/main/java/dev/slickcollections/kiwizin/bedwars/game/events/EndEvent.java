package dev.slickcollections.kiwizin.bedwars.game.events;

import dev.slickcollections.kiwizin.bedwars.Language;
import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsEvent;

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
