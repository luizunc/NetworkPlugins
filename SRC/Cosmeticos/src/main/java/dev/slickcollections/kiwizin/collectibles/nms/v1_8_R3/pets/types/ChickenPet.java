package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class ChickenPet extends PetController {
  
  public ChickenPet() {
    NMS.getInstance().registerEntity(MEntityChicken.class, "Chicken", 93);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityChicken chicken = new MEntityChicken(pet, settings);
    if (chicken.world.addEntity(chicken, SpawnReason.CUSTOM)) {
      return chicken;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.3F, height = 0.7F)
  private class MEntityChicken extends EntityAgeablePet {
    
    public MEntityChicken(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
}
