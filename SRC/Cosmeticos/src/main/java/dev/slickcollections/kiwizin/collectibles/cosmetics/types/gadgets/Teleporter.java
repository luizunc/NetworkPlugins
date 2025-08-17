package dev.slickcollections.kiwizin.collectibles.cosmetics.types.gadgets;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.GadgetCooldown;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.GadgetsCosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;

public class Teleporter extends GadgetsCosmetic {
  
  public Teleporter() {
    super("Teleporte", EnumRarity.RARO, "ENDER_PEARL : 1 : nome>Teleporte : desc>&7Teleporte-se com uma pérola do fim.", "ENDER_PEARL : 1 : nome>&6Engenhoca: &aTeleporte");
  }
  
  @Override
  public void onClick(CUser user) {
    Player player = user.getPlayer();
    String cooldown = GadgetCooldown.getCooldown(player);
    if (!cooldown.isEmpty()) {
      player.sendMessage("§cAguarde " + cooldown + " para utilizar novamente uma Engenhoca!");
      return;
    }
    
    GadgetCooldown.setCooldown(player, 10);
    player.launchProjectile(EnderPearl.class).setPassenger(player);
  }
}
