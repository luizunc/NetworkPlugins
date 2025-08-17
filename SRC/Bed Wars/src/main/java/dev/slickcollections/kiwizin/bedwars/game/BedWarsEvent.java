package dev.slickcollections.kiwizin.bedwars.game;

import dev.slickcollections.kiwizin.bedwars.Language;
import dev.slickcollections.kiwizin.bedwars.Main;
import dev.slickcollections.kiwizin.bedwars.game.events.*;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class BedWarsEvent {
  
  public static final Map<Integer, BedWarsEvent> SOLO = new HashMap<>();
  public static KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("EVENTS");
  private static BedWarsEvent END_EVENT, BED_EVENT, DIAMOND_EVENT, EMERALD_EVENT;

  public static void setupEvents() {
    END_EVENT = new EndEvent();
    BED_EVENT = new BedDestroy();
    DIAMOND_EVENT = new DiamondUpgrade();
    EMERALD_EVENT = new EmeraldUpgrade();
    for (String evt : Language.options$events$all$timings) {
      Object[] event = parseEvent(evt);
      if (event == null) {
        LOGGER.log(Level.WARNING, "O evento  \"" + evt + "\" nao e valido");
        continue;
      }
      
      SOLO.put((int) event[0], (BedWarsEvent) event[1]);
    }
  }

  private static Object[] parseEvent(String evt) {
    String[] splitter = evt.split(":");
    if (splitter.length <= 1) {
      return null;
    }
    
    int time = 0;
    try {
      if (splitter[1].startsWith("-")) {
        throw new Exception();
      }
      time = Integer.parseInt(splitter[1]);
    } catch (Exception ex) {
      return null;
    }
    
    String eventName = splitter[0];
    if (eventName.equalsIgnoreCase("fim")) {
      return new Object[]{time, END_EVENT};
    } else if (eventName.equalsIgnoreCase("beddestroy")) {
      return new Object[]{time, BED_EVENT};
    } else if (eventName.equalsIgnoreCase("diamond")) {
      return new Object[]{time, DIAMOND_EVENT};
    } else if (eventName.equalsIgnoreCase("emerald")) {
      return new Object[]{time, EMERALD_EVENT};
    }
    
    return null;
  }
  
  public abstract void execute(BedWars game);
  
  public abstract String getName();
}