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

public class SpiderPet extends PetController {
  
  public SpiderPet() {
    NMS.getInstance().registerEntity(MEntitySpider.class, "Spider", 52);
    NMS.getInstance().registerEntity(MEntityCaveSpider.class, "CaveSpider", 59);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    PetSettingsEntry caveEntry = settings.stream().filter(settingsEntry -> settingsEntry.getKey().equals("cave")).findFirst().get();
    EntityPet spider = caveEntry.getValue().getAsBoolean() ? new MEntityCaveSpider(pet, settings) : new MEntitySpider(pet, settings);
    if (spider.world.addEntity(spider, SpawnReason.CUSTOM)) {
      return spider;
    }
    
    return null;
  }
  
  @EntitySize(width = 1.4F, height = 0.9F)
  private class MEntitySpider extends EntityPet {
    
    public MEntitySpider(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
  
  @EntitySize(width = 0.7F, height = 0.5F)
  private class MEntityCaveSpider extends EntityPet {
    
    public MEntityCaveSpider(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
  }
}
