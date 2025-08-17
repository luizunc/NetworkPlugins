package dev.slickcollections.kiwizin.murder.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.murder.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations.CowboyExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations.ThorExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Thor extends WinAnimation {

    public Thor(ConfigurationSection section) {
        super(section.getLong("id"), "cowboy", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
    }

    public AbstractExecutor execute(Player player) {
        return new ThorExecutor(player);
    }
}
