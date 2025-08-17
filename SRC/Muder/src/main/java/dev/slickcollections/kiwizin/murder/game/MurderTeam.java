package dev.slickcollections.kiwizin.murder.game;

import dev.slickcollections.kiwizin.game.GameTeam;
import dev.slickcollections.kiwizin.murder.game.enums.MurderRole;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class MurderTeam extends GameTeam {

  private int kills;
  private MurderRole role;

  public MurderTeam(Murder game, String location, int size, MurderRole role) {
    super(game, location, size);
    this.role = role;
  }

  @Override
  public void reset() {
    super.reset();
    this.kills = 0;
  }

  public void addKills() {
    this.kills++;
  }

  public MurderRole getRole() {
    return this.role;
  }

  public int getKills() {
    return this.kills;
  }

  @Override
  public String toString() {
    return "MurderTeam{" + "role=" + role + ", kills=" + kills + ", players=[" + this.listPlayers().stream().map(Player::getName).collect(Collectors.joining(", ")) + "]}";
  }
}
