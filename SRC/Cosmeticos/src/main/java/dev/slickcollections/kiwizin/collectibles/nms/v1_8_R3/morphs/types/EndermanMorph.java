package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Morph;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphController;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.morphs.MorphEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.morphs.EntityMorph;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class EndermanMorph extends MorphController {
  
  public EndermanMorph() {
    NMS.getInstance().registerEntity(MEntityEnderman.class, "Enderman", 58);
  }
  
  @Override
  protected MorphEntity createEntity(Morph pet) {
    MEntityEnderman enderman = new MEntityEnderman(pet);
    if (enderman.world.addEntity(enderman, SpawnReason.CUSTOM)) {
      return enderman;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 2.9F)
  private class MEntityEnderman extends EntityMorph {
    
    public MEntityEnderman(Morph pet) {
      super(pet);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, (short) 0);
      this.datawatcher.a(17, (byte) 0);
      this.datawatcher.a(18, (byte) 0);
    }
  }
}
