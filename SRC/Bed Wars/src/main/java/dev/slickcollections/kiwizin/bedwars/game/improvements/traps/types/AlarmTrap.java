package dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types;

import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsTeam;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.Trap;
import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.game.GameTeam;
import dev.slickcollections.kiwizin.player.Profile;
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