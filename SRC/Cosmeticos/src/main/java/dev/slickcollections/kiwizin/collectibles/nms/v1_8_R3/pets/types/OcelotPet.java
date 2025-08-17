package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetOcelot;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MOcelotType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class OcelotPet extends PetController {
  
  public OcelotPet() {
    NMS.getInstance().registerEntity(MEntityOcelot.class, "Ozelot", 98);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityOcelot ocelot = new MEntityOcelot(pet, settings);
    if (ocelot.world.addEntity(ocelot, SpawnReason.CUSTOM)) {
      return ocelot;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 0.8F)
  private class MEntityOcelot extends EntityAgeablePet implements PetOcelot {
    
    public MEntityOcelot(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(18, (byte) 0);
    }
    
    @Override
    public MOcelotType getType() {
      return MOcelotType.getById(this.datawatcher.getInt(18));
    }
    
    @Override
    public void setType(MOcelotType type) {
      this.datawatcher.watch(18, (byte) type.getId());
    }
  }
}
