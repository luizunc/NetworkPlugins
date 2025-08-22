package minecraft.bedwars.cosmetics.types.winanimations;

import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.object.winanimations.VictoryHeatExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
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
