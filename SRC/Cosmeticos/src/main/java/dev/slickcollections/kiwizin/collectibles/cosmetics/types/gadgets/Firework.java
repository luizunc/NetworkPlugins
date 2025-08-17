package dev.slickcollections.kiwizin.collectibles.cosmetics.types.gadgets;

import dev.slickcollections.kiwizin.collectibles.cosmetics.object.GadgetCooldown;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.GadgetsCosmetic;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.collectibles.utils.FireworkUtil;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Firework extends GadgetsCosmetic {
  
  public Firework() {
    super("Decolar", EnumRarity.COMUM, "FIREWORK : 1 : nome>Decolar : desc>&7Vá até os céus com seu foguete.", "FIREWORK : 1 : nome>&6Engenhoca: &aDecolar");
  }
  
  @Override
  public void onClick(CUser user) {
    Player player = user.getPlayer();
    String cooldown = GadgetCooldown.getCooldown(player);
    if (!cooldown.isEmpty()) {
      player.sendMessage("§cAguarde " + cooldown + " para utilizar novamente uma Engenhoca!");
      return;
    }
    
    GadgetCooldown.setCooldown(player, 20);
    org.bukkit.entity.Firework firework = player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Firework.class);
    FireworkMeta meta = firework.getFireworkMeta();
    meta.setPower(2);
    meta.addEffect(FireworkUtil.getRandomEffect());
    firework.setFireworkMeta(meta);
    firework.setPassenger(player);
  }
}
