package dev.slickcollections.kiwizin.clans.listeners;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatHandler {
  
  public static final ChatHandler CREATING = new ChatHandler(), INVITE = new ChatHandler();
  
  private final Map<UUID, String[]> creating = new HashMap<>();
  
  public void setCreating(Player player) {
    creating.put(player.getUniqueId(), new String[2]);
  }
  
  public void clearCreating(Player player) {
    creating.remove(player.getUniqueId());
  }
  
  public boolean isCreating(Player player) {
    return creating.containsKey(player.getUniqueId());
  }
  
  public String[] getCreating(Player player) {
    return creating.get(player.getUniqueId());
  }
}
