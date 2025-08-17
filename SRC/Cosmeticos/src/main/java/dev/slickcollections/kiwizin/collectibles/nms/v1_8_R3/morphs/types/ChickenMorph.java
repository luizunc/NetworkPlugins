package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Morph;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphController;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.morphs.MorphEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.EntityAgeableMorph;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class ChickenMorph extends MorphController {
  
  public ChickenMorph() {
    NMS.getInstance().registerEntity(KEntityChicken.class, "Chicken", 93);
  }
  
  @Override
  protected MorphEntity createEntity(Morph pet) {
    KEntityChicken chicken = new KEntityChicken(pet);
    if (chicken.world.addEntity(chicken, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return chicken;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.3F, height = 0.7F)
  private class KEntityChicken extends EntityAgeableMorph {
    
    public KEntityChicken(Morph pet) {
      super(pet);
    }
  }
}
