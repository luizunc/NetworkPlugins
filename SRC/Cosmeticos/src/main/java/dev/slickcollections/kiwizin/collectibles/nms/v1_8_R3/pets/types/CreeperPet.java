package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetCreeper;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityPet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class CreeperPet extends PetController {
  
  public CreeperPet() {
    NMS.getInstance().registerEntity(MEntityCreeper.class, "Creeper", 50);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityCreeper creeper = new MEntityCreeper(pet, settings);
    if (creeper.world.addEntity(creeper, SpawnReason.CUSTOM)) {
      return creeper;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 1.9F)
  private class MEntityCreeper extends EntityPet implements PetCreeper {
    
    public MEntityCreeper(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, (byte) -1);
      this.datawatcher.a(17, (byte) 0);
      this.datawatcher.a(18, (byte) 0);
    }
    
    @Override
    public void setPowered(boolean powered) {
      this.datawatcher.watch(17, powered ? (byte) 1 : (byte) 0);
    }
  }
}
