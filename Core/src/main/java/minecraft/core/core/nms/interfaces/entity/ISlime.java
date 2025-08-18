package minecraft.core.core.nms.interfaces.entity;

import minecraft.core.core.libraries.holograms.api.HologramLine;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;

public interface ISlime {
  
  void setPassengerOf(Entity entity);
  
  void setLocation(double x, double y, double z);
  
  boolean isDead();
  
  void killEntity();
  
  Slime getEntity();
  
  HologramLine getLine();
}
