package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetSlime;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityPet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MSlimeSize;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SlimePet extends PetController {
  
  public SlimePet() {
    NMS.getInstance().registerEntity(MEntitySlime.class, "Slime", 55);
    NMS.getInstance().registerEntity(MEntityMagmaCube.class, "MagmaCube", 62);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    PetSettingsEntry magmaEntry = settings.stream().filter(settingsEntry -> settingsEntry.getKey().equals("slimemagma")).findFirst().get();
    EntityPet slime = magmaEntry.getValue().getAsBoolean() ? new MEntityMagmaCube(pet, settings) : new MEntitySlime(pet, settings);
    if (slime.world.addEntity(slime, SpawnReason.CUSTOM)) {
      return slime;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 0.6F)
  private class MEntitySlime extends EntityPet implements PetSlime {
    
    private int jumpDelay;
    
    public MEntitySlime(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
      this.jumpDelay = ThreadLocalRandom.current().nextInt(4) + 4;
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, (byte) 2);
    }
    
    @Override
    public MSlimeSize getSize() {
      return MSlimeSize.getBySize(this.datawatcher.getInt(16));
    }
    
    @Override
    public void setSize(MSlimeSize color) {
      this.datawatcher.watch(16, (byte) color.getSize());
    }
    
    @Override
    public void repeatTask() {
      super.repeatTask();
      if (this.onGround && --jumpDelay <= 0 && this.passenger == null && !this.getBukkitEntity().isInsideVehicle()) {
        this.jumpDelay = ThreadLocalRandom.current().nextInt(4) + 4;
        getControllerJump().a();
      }
    }
  }
  
  @EntitySize(width = 0.6F, height = 0.6F)
  private class MEntityMagmaCube extends EntityPet implements PetSlime {
    
    private int jumpDelay;
    
    public MEntityMagmaCube(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
      this.jumpDelay = ThreadLocalRandom.current().nextInt(4) + 4;
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(16, (byte) 2);
    }
    
    @Override
    public MSlimeSize getSize() {
      return MSlimeSize.getBySize(this.datawatcher.getInt(16));
    }
    
    @Override
    public void setSize(MSlimeSize color) {
      this.datawatcher.watch(16, (byte) color.getSize());
    }
    
    @Override
    public void repeatTask() {
      super.repeatTask();
      if (this.onGround && --jumpDelay <= 0 && this.passenger == null && !this.getBukkitEntity().isInsideVehicle()) {
        this.jumpDelay = ThreadLocalRandom.current().nextInt(4) + 4;
        getControllerJump().a();
      }
    }
  }
}
