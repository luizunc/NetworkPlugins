package minecraft.bedwars.cosmetics.types.winanimations;

import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.object.winanimations.EnderDragonExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
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
