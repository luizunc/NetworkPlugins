package minecraft.bedwars.game.improvements.traps.types;

import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.game.improvements.traps.Trap;
import minecraft.core.core.game.Game;
import minecraft.core.core.game.GameTeam;
import minecraft.core.core.player.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItsaTrap extends Trap {
  
  public ItsaTrap() {
    super("TRIPWIRE_HOOK : 1 : nome>{color}É uma armadilha! : desc>&7Inflige Cegueira e Lentidão\n&7por 8 segundos.",
        Material.DIAMOND);
  }
  
  @Override
  public void onEnter(BedWarsTeam owner, Profile ap) {
    super.onEnter(owner, ap);
    BedWars game = ap.getGame(BedWars.class);
    if (game == null) {
      return;
    }
    
    
    if (!owner.equals(game.getTeam(ap.getPlayer())) && ap.playingGame()) {
      owner.removeTrap(this);
      Player player = ap.getPlayer();
      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0));
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 1));
    }
  }
}