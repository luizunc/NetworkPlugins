package dev.slickcollections.kiwizin.murder.hook.hotbar;

import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.menus.MenuLobbies;
import dev.slickcollections.kiwizin.murder.menus.MenuPlay;
import dev.slickcollections.kiwizin.murder.menus.MenuShop;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;

public class MMHotbarActionType extends HotbarActionType {

  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("loja")) {
      new MenuShop(profile);
    } else if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    } else if (action.equalsIgnoreCase("jogar")) {
      new MenuPlay(profile, profile.getGame(Murder.class).getMode());
    } else if (action.equalsIgnoreCase("sair")) {
      profile.getGame(Murder.class).leave(profile, null);
    }
  }
}
