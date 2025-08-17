package dev.slickcollections.kiwizin.murder.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.murder.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations.EnderDragonExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class EnderDragon extends WinAnimation {

    public EnderDragon(ConfigurationSection section) {
        super(section.getLong("id"), "ender_dragon", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
    }

    public AbstractExecutor execute(Player player) {
        return new EnderDragonExecutor(player);
    }
}
