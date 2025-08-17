package dev.slickcollections.kiwizin.bedwars.cosmetics.object.winanimations;

import dev.slickcollections.kiwizin.bedwars.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.bedwars.nms.NMS;
import org.bukkit.entity.Player;

public class EnderDragonExecutor extends AbstractExecutor {
  
  public EnderDragonExecutor(Player player) {
    super(player);
    NMS.createMountableEnderDragon(player);
  }
}

