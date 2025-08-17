package dev.slickcollections.kiwizin.collectibles.cosmetics.object;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.CompanionCosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.CompanionEntities;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.companions.CompanionEntity;
import org.bukkit.entity.Player;

public class Companion {
  
  private CUser user;
  private Player owner;
  private final CompanionCosmetic cosmetic;
  private CompanionEntity entity;
  
  public Companion(CUser owner, CompanionCosmetic cosmetic) {
    this.user = owner;
    this.owner = owner.getPlayer();
    this.cosmetic = cosmetic;
    this.entity = this.spawn();
  }
  
  public void changeName(String name) {
    this.entity.setCompanionName(name);
  }
  
  public CompanionEntity spawn() {
    return CompanionEntities.createForType(this.cosmetic.getClass(), this);
  }
  
  public boolean update() {
    if (this.getCompanionOwner() == null || !this.getCompanionOwner().isOnline()) {
      this.despawn();
      return false;
    }
    
    return true;
  }
  
  public void despawn() {
    this.user = null;
    this.owner = null;
    if (this.entity != null) {
      this.entity.kill();
    }
    this.entity = null;
  }
  
  public CUser getClient() {
    return this.user;
  }
  
  public Player getCompanionOwner() {
    return this.owner;
  }
  
  public CompanionCosmetic getCosmetic() {
    return this.cosmetic;
  }
}
