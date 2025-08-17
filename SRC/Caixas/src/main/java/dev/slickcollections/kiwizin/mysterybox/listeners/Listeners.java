package dev.slickcollections.kiwizin.mysterybox.listeners;

import dev.slickcollections.kiwizin.mysterybox.Main;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.hook.container.MysteryBoxQueueContainer;
import dev.slickcollections.kiwizin.mysterybox.lobby.BoxNPC;
import dev.slickcollections.kiwizin.mysterybox.menus.MenuBoxes;
import dev.slickcollections.kiwizin.mysterybox.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class Listeners implements Listener {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("LISTENERS");
  
  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) {
    LOGGER.run(Level.SEVERE, "Could not pass PlayerJoinEvent for ${n} v${v}", () -> {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        MysteryBoxQueueContainer container = profile.getAbstractContainer("kMysteryBox", "queueRewards", MysteryBoxQueueContainer.class);
        container.listQueuedContents().stream().filter(BoxContent::canBeDispatched).forEach(reward -> {
          container.removeFromQueue(reward);
          reward.dispatch(profile);
        });
      }
    });
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    NMS.BLOCK_CAMERA.remove(evt.getPlayer().getName());
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      if (evt.getClickedBlock() != null) {
        BoxNPC bn = BoxNPC.getByLocation(evt.getClickedBlock().getLocation());
        if (bn != null) {
          new MenuBoxes(profile, bn);
        }
      }
    }
  }
}
