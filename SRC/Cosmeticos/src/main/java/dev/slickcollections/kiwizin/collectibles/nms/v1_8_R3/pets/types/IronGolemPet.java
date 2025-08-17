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

public class IronGolemPet extends PetController {
  
  public IronGolemPet() {
    NMS.getInstance().registerEntity(MEntityIronGolem.class, "VillagerGolem", 99);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityIronGolem irongolem = new MEntityIronGolem(pet, settings);
    if (irongolem.world.addEntity(irongolem, SpawnReason.CUSTOM)) {
      return irongolem;
    }
    
    return null;
  }
  
  @EntitySize(width = 1.4F, height = 2.9F)
  private class MEntityIronGolem extends EntityPet {
    
    public MEntityIronGolem(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
}
