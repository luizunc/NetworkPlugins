package minecraft.bedwars.cosmetics.types.winanimations;

import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.object.winanimations.FireworksExecutor;
import minecraft.bedwars.cosmetics.types.WinAnimation;
import minecraft.core.core.player.Profile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Fireworks extends WinAnimation {
  
  public Fireworks(ConfigurationSection section) {
    super(0, "fireworks", 0.0, "", section.getString("name"), section.getString("icon"));
  }
  
  @Override
  public boolean has(Profile profile) {
    return true;
  }
  
  public AbstractExecutor execute(Player player) {
    return new FireworksExecutor(player);
  }
}
