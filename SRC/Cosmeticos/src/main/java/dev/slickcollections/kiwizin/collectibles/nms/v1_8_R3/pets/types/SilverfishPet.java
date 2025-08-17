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

public class SilverfishPet extends PetController {
  
  public SilverfishPet() {
    NMS.getInstance().registerEntity(MEntitySilverfish.class, "Silverfish", 60);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntitySilverfish silverfish = new MEntitySilverfish(pet, settings);
    if (silverfish.world.addEntity(silverfish, SpawnReason.CUSTOM)) {
      return silverfish;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.3F, height = 0.7F)
  private class MEntitySilverfish extends EntityPet {
    
    public MEntitySilverfish(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
}
