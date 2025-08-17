package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetVillager;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MVillagerProfession;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class VillagerPet extends PetController {
  
  public VillagerPet() {
    NMS.getInstance().registerEntity(MEntityVillager.class, "Villager", 120);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityVillager villager = new MEntityVillager(pet, settings);
    if (villager.world.addEntity(villager, SpawnReason.CUSTOM)) {
      return villager;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 1.8F)
  private class MEntityVillager extends EntityAgeablePet implements PetVillager {
    
    public MEntityVillager(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, 0);
    }
    
    @Override
    public MVillagerProfession getProfession() {
      return MVillagerProfession.getById(this.datawatcher.getInt(16));
    }
    
    @Override
    public void setProfession(MVillagerProfession profession) {
      this.datawatcher.watch(16, profession.getId());
    }
  }
}
