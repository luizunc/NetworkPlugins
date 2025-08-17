package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityPet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class EndermanPet extends PetController {
  
  public EndermanPet() {
    NMS.getInstance().registerEntity(MEntityEnderman.class, "Enderman", 58);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityEnderman enderman = new MEntityEnderman(pet, settings);
    if (enderman.world.addEntity(enderman, SpawnReason.CUSTOM)) {
      return enderman;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 2.9F)
  private class MEntityEnderman extends EntityPet {
    
    public MEntityEnderman(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, Short.valueOf((short) 0));
      this.datawatcher.a(17, Byte.valueOf((byte) 0));
      this.datawatcher.a(18, Byte.valueOf((byte) 0));
    }
  }
}
