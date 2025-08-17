package dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types;

import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsTeam;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.Trap;
import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.game.GameTeam;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CounterOffensiveTrap extends Trap {
  
  public CounterOffensiveTrap() {
    super("FEATHER : 1 : nome>{color}Armadilha Contra Ofensiva : desc>&7Garante Velocidade I e Super Pulo II\n&7por 10 segundos para os aliados\n&7perto de sua base.",
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
      owner.listPlayers().forEach(aps -> {
        if (aps.isPlayerTimeRelative() && owner.cubeId.contains(aps.getPlayer().getLocation())) {
          Player player = aps.getPlayer();
          player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
          player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 1));
        }
      });
    }
  }
}