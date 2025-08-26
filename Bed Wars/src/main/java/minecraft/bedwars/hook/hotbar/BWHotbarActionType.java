package minecraft.bedwars.hook.hotbar;

import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.enums.BedWarsMode;
import minecraft.bedwars.menus.MenuLobbies;
import minecraft.bedwars.menus.MenuPlay;
import minecraft.bedwars.menus.MenuShop;
import minecraft.bedwars.menus.MenuSpectator;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.HotbarActionType;
import minecraft.core.core.player.hotbar.Hotbar;

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
    } else if (action.equalsIgnoreCase("sair_preview")) {
      // Retornar ao lobby do BedWars
      profile.setGame(null);
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refresh();
      profile.refreshPlayers();
    }
  }
}
