package minecraft.bedwars.game.improvements.traps.types;

import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.game.improvements.traps.Trap;
import minecraft.core.core.game.Game;
import minecraft.core.core.game.GameTeam;
import minecraft.core.core.player.Profile;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class AlarmTrap extends Trap {
  
  public AlarmTrap() {
    super("REDSTONE_TORCH_ON : 1 : nome>{color}Alarme : desc>&7Revela jogadores invisíveis que\n&7entrarem em sua base.",
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
      ap.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    }
  }
}