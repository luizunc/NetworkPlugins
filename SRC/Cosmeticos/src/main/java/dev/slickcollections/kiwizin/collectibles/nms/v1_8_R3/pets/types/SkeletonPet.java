package dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Pet;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.settings.PetSettingsEntry;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetEntity;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets.PetSkeleton;
import dev.slickcollections.kiwizin.collectibles.nms.v1_8_R3.pets.EntityPet;
import dev.slickcollections.kiwizin.collectibles.utils.entity.EntitySize;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.List;

public class SkeletonPet extends PetController {
  
  public SkeletonPet() {
    NMS.getInstance().registerEntity(MEntitySkeleton.class, "Skeleton", 51);
  }
  
  @Override
  protected PetEntity createEntity(Pet pet, List<PetSettingsEntry> settings) {
    MEntitySkeleton skeleton = new MEntitySkeleton(pet, settings);
    if (skeleton.world.addEntity(skeleton, SpawnReason.CUSTOM)) {
      return skeleton;
    }
    
    return null;
  }
  
  @EntitySize(width = 0.6F, height = 1.9F)
  private class MEntitySkeleton extends EntityPet implements PetSkeleton {
    
    public MEntitySkeleton(Pet pet, List<PetSettingsEntry> settings) {
      super(pet, settings);
    }
    
    @Override
    protected void h() {
      super.h();
      this.datawatcher.a(13, (byte) 0);
    }
    
    @Override
    public void repeatTask() {
      super.repeatTask();
      super.setEquipment(4, CraftItemStack.asNMSCopy(this.pet.getPetOwner().getInventory().getHelmet()));
      super.setEquipment(3, CraftItemStack.asNMSCopy(this.pet.getPetOwner().getInventory().getChestplate()));
      super.setEquipment(2, CraftItemStack.asNMSCopy(this.pet.getPetOwner().getInventory().getLeggings()));
      super.setEquipment(1, CraftItemStack.asNMSCopy(this.pet.getPetOwner().getInventory().getBoots()));
    }
    
    @Override
    public void setWither(boolean wither) {
      this.datawatcher.watch(13, wither ? (byte) 1 : (byte) 0);
    }
  }
}
