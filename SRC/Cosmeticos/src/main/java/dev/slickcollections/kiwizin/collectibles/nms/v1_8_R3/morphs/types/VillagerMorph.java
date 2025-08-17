package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Morph;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphController;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.morphs.MorphEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.EntityMorph;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class VillagerMorph extends MorphController {
  
  public VillagerMorph() {
    NMS.getInstance().registerEntity(MEntityVillager.class, "Villager", 120);
  }
  
  @Override
  protected MorphEntity createEntity(Morph pet) {
    MEntityVillager villager = new MEntityVillager(pet);
    if (villager.world.addEntity(villager, SpawnReason.CUSTOM)) {
      return villager;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 1.8F)
  private class MEntityVillager extends EntityMorph {
    
    public MEntityVillager(Morph pet) {
      super(pet);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, 0);
    }
    
  }
}
