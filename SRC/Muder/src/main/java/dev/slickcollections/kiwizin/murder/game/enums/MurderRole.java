package dev.slickcollections.kiwizin.murder.game.enums;

public enum MurderRole {
  KILLER("§cAssassino"),
  DETECTIVE("§6Detetive"),
  BYSTANDER("§aInocente");

  private String name;

  MurderRole(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public String getPlayers() {
    return this == KILLER ? "Assassino" : "Jogadores";
  }

  public boolean isKiller() {
    return this == KILLER;
  }

  public boolean isDetective() {
    return this == DETECTIVE;
  }

  public boolean isBystander() {
    return this == BYSTANDER;
  }
}
