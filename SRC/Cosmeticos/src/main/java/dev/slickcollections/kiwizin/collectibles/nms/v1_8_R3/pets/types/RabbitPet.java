package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetRabbit;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityAgeablePet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MRabbitType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RabbitPet extends PetController {
  
  public RabbitPet() {
    NMS.getInstance().registerEntity(MEntityRabbit.class, "Rabbit", 101);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntityRabbit rabbit = new MEntityRabbit(pet, settings);
    if (rabbit.world.addEntity(rabbit, SpawnReason.CUSTOM)) {
      return rabbit;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 0.7F)
  private class MEntityRabbit extends EntityAgeablePet implements PetRabbit {
    
    private int jumpDelay;
    
    public MEntityRabbit(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
      this.jumpDelay = ThreadLocalRandom.current().nextInt(15) + 10;
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(18, (byte) 0);
    }
    
    @Override
    public MRabbitType getType() {
      return MRabbitType.getById(this.datawatcher.getInt(18));
    }
    
    @Override
    public void setType(MRabbitType type) {
      this.datawatcher.watch(18, (byte) type.getId());
    }
    
    @Override
    public void repeatTask() {
      super.repeatTask();
      if (this.onGround && --jumpDelay <= 0 && this.passenger == null && !this.getBukkitEntity().isInsideVehicle()) {
        this.jumpDelay = ThreadLocalRandom.current().nextInt(15) + 10;
        getControllerJump().a();
      }
    }
  }
}
