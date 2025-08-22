package minecraft.bedwars.cosmetics.types.winanimations;

import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.object.winanimations.WitherExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Wither extends WinAnimation {
  
  public Wither(ConfigurationSection section) {
    super(section.getLong("id"), "wither", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new WitherExecutor(player);
  }
}
