package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetWolf;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MDyeColor;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class WolfPet extends PetController {
  
  public WolfPet() {
    NMS.getInstance().registerEntity(MEntityWolf.class, "Wolf", 95);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityWolf wolf = new MEntityWolf(pet, settings);
    if (wolf.world.addEntity(wolf, SpawnReason.CUSTOM)) {
      return wolf;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 0.8F)
  private class MEntityWolf extends EntityAgeablePet implements PetWolf {
    
    public MEntityWolf(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
      
      this.datawatcher.a(16, (byte) (0 | 0x4));
      this.datawatcher.a(17, pet.getPetOwner().getUniqueId().toString());
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(18, this.getHealth());
      this.datawatcher.a(19, (byte) 0);
      this.datawatcher.a(20, (byte) 14);
    }
    
    @Override
    public MDyeColor getColor() {
      return MDyeColor.getByDyeData(this.datawatcher.getInt(20));
    }
    
    @Override
    public void setColor(MDyeColor color) {
      this.datawatcher.watch(20, (byte) color.getDyeData());
    }
  }
}
