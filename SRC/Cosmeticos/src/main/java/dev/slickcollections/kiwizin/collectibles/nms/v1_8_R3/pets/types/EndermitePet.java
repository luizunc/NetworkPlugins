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

public class EndermitePet extends PetController {
  
  public EndermitePet() {
    NMS.getInstance().registerEntity(MEntityEndermite.class, "Endermite", 67);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityEndermite endermite = new MEntityEndermite(pet, settings);
    if (endermite.world.addEntity(endermite, SpawnReason.CUSTOM)) {
      return endermite;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.4F, height = 0.3F)
  private class MEntityEndermite extends EntityPet {
    
    public MEntityEndermite(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
}
