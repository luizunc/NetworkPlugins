package dev.slickcollections.kiwizin.bedwars.game.improvements.traps;

import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.bedwars.game.BedWarsTeam;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types.AlarmTrap;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types.CounterOffensiveTrap;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types.ItsaTrap;
import dev.slickcollections.kiwizin.bedwars.game.improvements.traps.types.MinerFatigueTrap;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Material;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Trap {
  
  private static final Set<Trap> TRAPS = new LinkedHashSet<>();
  
  protected String icon;
  protected Material material;
  
  public Trap(String icon, Material material) {
    this.icon = icon;
    this.material = material;
  }
  
  public static void setupTraps() {
    TRAPS.add(new ItsaTrap());
    TRAPS.add(new AlarmTrap());
    TRAPS.add(new CounterOffensiveTrap());
    TRAPS.add(new MinerFatigueTrap());
  }
  
  public static Set<Trap> listTraps() {
    return TRAPS;
  }
  
  public void onEnter(BedWarsTeam owner, Profile profile) {
    BedWars game = profile.getGame(BedWars.class);
    if (game != null && game.isSpectator(profile.getPlayer())) {
      return;
    }
    
    if (game != null && !game.isSpectator(profile.getPlayer())) {
      owner.setLastTrapped(profile.getPlayer());
      owner.listPlayers().forEach(aps -> {
        if (aps.getPlayer() != null) {
          NMS.sendTitle(aps.getPlayer(),
              "§c§lARMADILHA ATIVADA", "§fUm jogador caiu na armadilha", 20, 120, 20);
        }
      });
      
    }
  }
  
  public String getIcon() {
    return this.icon;
  }
  
  public Material getMaterial() {
    return material;
  }
  
  public void setMaterial(Material material) {
    this.material = material;
  }
}