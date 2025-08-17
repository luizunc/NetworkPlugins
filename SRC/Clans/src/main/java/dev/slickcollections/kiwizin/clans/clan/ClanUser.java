package dev.slickcollections.kiwizin.clans.clan;

public class ClanUser {
  
  private final String name;
  private final long joined;
  private long lastBoletim;
  
  public ClanUser(String name, long joined, long lastBoletim) {
    this.name = name;
    this.joined = joined;
    this.lastBoletim = lastBoletim;
  }
  
  public void update() {
    this.lastBoletim = System.currentTimeMillis();
  }
  
  public String getName() {
    return name;
  }
  
  public long getJoined() {
    return joined;
  }
  
  public long getLastBoletim() {
    return lastBoletim;
  }
}
