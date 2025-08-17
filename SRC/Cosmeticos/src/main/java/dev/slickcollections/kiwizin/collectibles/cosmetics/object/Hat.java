package dev.slickcollections.kiwizin.collectibles.cosmetics.object;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.HatCosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat {
  
  private Player owner;
  private ItemStack hat;
  
  public Hat(CUser owner, HatCosmetic cosmetic) {
    this.owner = owner.getPlayer();
    this.hat = cosmetic.getIcon("§a");
    this.apply();
  }
  
  public void apply() {
    this.owner.getInventory().setHelmet(this.hat);
    this.owner.updateInventory();
  }
  
  public void destroy() {
    this.owner.getInventory().setHelmet(null);
    this.owner.updateInventory();
    this.owner = null;
    this.hat = null;
  }
}
