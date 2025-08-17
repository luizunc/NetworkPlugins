package dev.slickcollections.kiwizin.collectibles.cosmetics.object;

import dev.slickcollections.kiwizin.collectibles.Main;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.MorphCosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphController;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphType;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.role.Role;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Morph extends BukkitRunnable {
  
  private Player owner;
  private MorphController petController;
  private MorphCosmetic cosmetic;
  
  public Morph(CUser owner, MorphCosmetic cosmetic) {
    this.owner = owner.getPlayer();
    this.cosmetic = cosmetic;
    this.petController = this.cosmetic.getEntityType().createNewEntity();
    this.petController.spawn(this);
    this.petController.getEntity().setPetCustomName(Role.getPrefixed(this.owner.getName()));
    this.runTaskTimer(Main.getInstance(), 0, 20);
  }
  
  @Override
  public synchronized void cancel() throws IllegalStateException {
    super.cancel();
    if (this.owner != null) {
      for (Player player : this.owner.getWorld().getPlayers()) {
        player.showPlayer(this.owner);
      }
      this.owner.spigot().setCollidesWithEntities(true);
      if (this.owner.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
        this.owner.removePotionEffect(PotionEffectType.INVISIBILITY);
      }
    }
    if (this.petController != null) {
      this.petController.remove();
    }
    this.petController = null;
    this.owner = null;
    this.cosmetic = null;
  }
  
  @Override
  public void run() {
    for (Player player : this.owner.getWorld().getPlayers()) {
      player.hidePlayer(this.owner);
    }
    
    this.owner.spigot().setCollidesWithEntities(false);
    this.owner.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
    NMS.sendActionBar(this.owner, "§cSua mutação está ativada!");
  }
  
  public boolean update() {
    if (this.getMorphOwner() == null || !this.getMorphOwner().isOnline()) {
      this.despawn();
      return false;
    }
    
    return true;
  }
  
  public void respawn() {
    this.petController.remove();
    this.petController.spawn(this);
  }
  
  public void despawn() {
    this.cancel();
  }
  
  public Player getMorphOwner() {
    return this.owner;
  }
  
  public MorphType getPetType() {
    return this.cosmetic.getEntityType();
  }
  
  public MorphController getPetController() {
    return this.petController;
  }
}
