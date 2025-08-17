package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetSheep;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MWoolColor;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class SheepPet extends PetController {
  
  public SheepPet() {
    NMS.getInstance().registerEntity(MEntitySheep.class, "Sheep", 91);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntitySheep sheep = new MEntitySheep(pet, settings);
    if (sheep.world.addEntity(sheep, SpawnReason.CUSTOM)) {
      return sheep;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.9F, height = 1.3F)
  private class MEntitySheep extends EntityAgeablePet implements PetSheep {
    
    private int ticks;
    private boolean rainbow;
    
    public MEntitySheep(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
      this.ticks = 4;
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, (byte) 0);
    }
    
    @Override
    public void setRainbow(boolean rainbow) {
      this.rainbow = rainbow;
    }
    
    @Override
    public MWoolColor getColor() {
      return MWoolColor.getByWoolData(this.datawatcher.getByte(16) & 0xF);
    }
    
    @Override
    public void setColor(MWoolColor color) {
      if (this.rainbow) {
        return;
      }
      
      this.datawatcher.watch(16, (byte) (this.datawatcher.getByte(16) & 0xF0 | color.getWoolData() & 0xF));
    }
    
    @Override
    public void repeatTask() {
      super.repeatTask();
      if (this.rainbow) {
        if (this.ticks == 4) {
          this.datawatcher.watch(16, (byte) (this.datawatcher.getByte(16) & 0xF0 | this.getColor().getNextColor().getWoolData() & 0xF));
          this.ticks = 0;
        }
        
        this.ticks++;
      }
    }
  }
}
