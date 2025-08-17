package dev.slickcollections.kiwizin.mysterybox.api;

import dev.slickcollections.kiwizin.mysterybox.box.Box;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.box.loot.LootMenu;
import dev.slickcollections.kiwizin.mysterybox.hook.container.MysteryBoxContainer;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.entity.Player;

public class MysteryBoxAPI {
  
  public static void reloadContents() {
    BoxContent.setupRewards();
  }
  
  public static long getMysteryBoxes(Profile profile) {
    return profile.getStats("kMysteryBox", "magic");
  }
  
  public static void openMenu(Player player) {
    new LootMenu(player, Box.getFirst());
  }
}
