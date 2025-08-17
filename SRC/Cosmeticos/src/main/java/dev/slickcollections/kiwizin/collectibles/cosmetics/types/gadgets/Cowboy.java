package dev.slickcollections.kiwizin.collectibles.cosmetics.types.gadgets;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.GadgetCooldown;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.GadgetsCosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.libraries.npclib.npc.ai.NPCHolder;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Cowboy extends GadgetsCosmetic {
  
  public Cowboy() {
    super("Cowboy", EnumRarity.COMUM, "81 : 1 : nome>Cowboy : desc>&7Monte em seu amigo\n&7como um cowboy!",
        "81 : 1 : nome>&6Engenhoca: &aCowboy!");
  }
  
  @Override
  public void onClick(CUser user) {
    Player player = user.getPlayer();
    String cooldown = GadgetCooldown.getCooldown(player);
    if (!cooldown.isEmpty()) {
      player.sendMessage("§cAguarde " + cooldown + " para utilizar novamente uma Engenhoca!");
      return;
    }
    if (getNearbyEntities(player, player.getLocation(), 15.0).isEmpty()) {
      player.sendMessage("§cNão há jogadores para você montar.");
      return;
    }
    
    GadgetCooldown.setCooldown(player, 25);
    this.getNearbyEntities(player, player.getLocation(), 15.0).forEach(le -> le.setPassenger(player));
  }
  
  private List<Player> getNearbyEntities(Player player, Location location, double range) {
    List<Player> entities = new ArrayList<>();
    for (Entity entity : location.getWorld().getEntities()) {
      if (entity instanceof Player && !(entity instanceof ArmorStand) && !(entity instanceof NPCHolder) && entity.getLocation().distance(location) <= range) {
        entities.add((Player) entity);
      }
    }
    entities.remove(player);
    
    return entities;
  }
}
