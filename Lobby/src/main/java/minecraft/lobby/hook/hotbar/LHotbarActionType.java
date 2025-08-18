package minecraft.lobby.hook.hotbar;

import minecraft.lobby.menus.MenuLobbies;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.HotbarActionType;

public class LHotbarActionType extends HotbarActionType {
  
  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    }
  }
}
