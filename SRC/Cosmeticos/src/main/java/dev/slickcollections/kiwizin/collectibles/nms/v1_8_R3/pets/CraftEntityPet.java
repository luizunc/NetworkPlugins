package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets;

import dev.slickcollections.kiwizin.collectibles.Main;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class CraftEntityPet extends CraftCreature implements PetEntity {
  
  public CraftEntityPet(EntityPet entityPet) {
    super(entityPet.world.getServer(), entityPet);
    this.setMetadata("KCOSMETICS_PET", new FixedMetadataValue(Main.getInstance(), true));
  }
  
  @Override
  public void remove() {
  }
  
  @Override
  public void setPetCustomName(String customName) {
    this.getHandle().setPetCustomName(customName);
  }
  
  @Override
  public void kill() {
    this.getHandle().kill();
  }
  
  @Override
  public boolean isRiding() {
    return this.getHandle().isRiding();
  }
  
  @Override
  public Entity getBukkitEntity() {
    return this;
  }
  
  @Override
  public EntityPet getHandle() {
    return (EntityPet) super.getHandle();
  }
  
  @Override
  public EntityType getType() {
    return EntityType.UNKNOWN;
  }
}
