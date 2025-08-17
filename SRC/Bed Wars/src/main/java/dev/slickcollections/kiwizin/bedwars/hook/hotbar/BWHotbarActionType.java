package dev.slickcollections.kiwizin.bedwars.hook.hotbar;

import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.enums.BedWarsMode;
import dev.slickcollections.kiwizin.bedwars.menus.MenuLobbies;
import dev.slickcollections.kiwizin.bedwars.menus.MenuPlay;
import dev.slickcollections.kiwizin.bedwars.menus.MenuShop;
import dev.slickcollections.kiwizin.bedwars.menus.MenuSpectator;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;

public class BWHotbarActionType extends HotbarActionType {
  
  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("loja")) {
      new MenuShop(profile);
    } else if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    } else if (action.equalsIgnoreCase("espectar")) {
      BedWars game = profile.getGame(BedWars.class);
      if (game != null) {
        new MenuSpectator(profile.getPlayer(), game);
      }
    } else if (action.equalsIgnoreCase("jogar")) {
      new MenuPlay(profile, profile.getGame(BedWars.class) == null ? BedWarsMode.SOLO : profile.getGame(BedWars.class).getMode());
    } else if (action.equalsIgnoreCase("sair")) {
      profile.getGame(BedWars.class).leave(profile, null);
    }
  }
}
