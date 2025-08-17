package dev.slickcollections.kiwizin.murder.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.murder.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations.CowboyExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.object.winanimations.VictoryHeatExecutor;
import dev.slickcollections.kiwizin.murder.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class VictoryHeat extends WinAnimation {

    public VictoryHeat(ConfigurationSection section) {
        super(section.getLong("id"), "victoryheat", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
    }

    public AbstractExecutor execute(Player player) {
        return new VictoryHeatExecutor(player);
    }
}
