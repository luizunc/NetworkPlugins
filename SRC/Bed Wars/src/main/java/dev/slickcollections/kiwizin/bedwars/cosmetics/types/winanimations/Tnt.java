package dev.slickcollections.kiwizin.bedwars.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.bedwars.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.bedwars.cosmetics.object.winanimations.TntExecutor;
import dev.slickcollections.kiwizin.bedwars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Tnt extends WinAnimation {
  
  public Tnt(ConfigurationSection section) {
    super(section.getLong("id"), "tnt", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new TntExecutor(player);
  }
}
