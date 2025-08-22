package minecraft.bedwars.cosmetics.object.winanimations;

import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.nms.NMS;
import org.bukkit.entity.Player;

public class EnderDragonExecutor extends AbstractExecutor {
  
  public EnderDragonExecutor(Player player) {
    super(player);
    NMS.createMountableEnderDragon(player);
  }
}

