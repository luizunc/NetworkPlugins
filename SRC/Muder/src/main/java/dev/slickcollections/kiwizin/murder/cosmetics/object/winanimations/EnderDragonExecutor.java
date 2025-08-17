package dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations;

import dev.slickcollections.kiwizin.murder.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.murder.nms.NMS;
import org.bukkit.entity.Player;

public class EnderDragonExecutor extends AbstractExecutor {

    public EnderDragonExecutor(Player player) {
        super(player);
        NMS.createMountableEnderDragon(player);
    }
}

