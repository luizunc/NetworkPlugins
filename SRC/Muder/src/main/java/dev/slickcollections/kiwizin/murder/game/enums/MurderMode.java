package dev.slickcollections.kiwizin.murder.game.enums;

import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.game.interfaces.LoadCallback;
import dev.slickcollections.kiwizin.murder.game.types.AssassinsMurder;
import dev.slickcollections.kiwizin.murder.game.types.ClassicMurder;

public enum MurderMode {
  CLASSIC(16, "Clássico"),
  ASSASSINS(16, "Assassinos");

  private int size;
  private String name;

  MurderMode(int size, String name) {
    this.size = size;
    this.name = name;
  }

  public int getSize() {
    return this.size;
  }

  public String getName() {
    return this.name;
  }

  public Murder buildGame(String name, LoadCallback callback) {
    if (this == CLASSIC) {
      return new ClassicMurder(name, callback);
    }

    return new AssassinsMurder(name, callback);
  }

  private static final MurderMode[] VALUES = values();

  public static MurderMode fromName(String name) {
    for (MurderMode mode : VALUES) {
      if (name.equalsIgnoreCase(mode.name())) {
        return mode;
      }
    }

    return null;
  }
}
