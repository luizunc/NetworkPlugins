package minecraft.lobby.listeners.player;

import minecraft.core.core.libraries.npclib.api.event.NPCRightClickEvent;
import minecraft.core.core.libraries.npclib.api.npc.NPC;
import minecraft.core.core.player.Profile;
import minecraft.core.core.servers.ServerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractListener implements Listener {
  
  @EventHandler
  public void onNPCRightClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        ServerItem si = ServerItem.getServerItem(npc.data().get("play-npc"));
        if (si != null) {
          si.connect(profile);
        }
      }
    }
  }
}
