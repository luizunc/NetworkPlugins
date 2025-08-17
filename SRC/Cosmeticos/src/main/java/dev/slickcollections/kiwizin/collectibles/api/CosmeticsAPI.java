package dev.slickcollections.kiwizin.collectibles.api;

import dev.slickcollections.kiwizin.collectibles.hook.Users;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.collectibles.menu.CosmeticsMenu;
import org.bukkit.entity.Player;

public class CosmeticsAPI {
  
  public static void openMenu(Player player) {
    CUser user = Users.getByName(player.getName());
    if (user != null) {
      new CosmeticsMenu(user);
    }
  }
  
  public static void enable(Player player) {
    CUser user = Users.getByName(player.getName());
    if (user != null) {
      user.enable();
    }
  }
  
  public static void disable(Player player) {
    CUser user = Users.getByName(player.getName());
    if (user != null) {
      user.disable();
    }
  }
}
