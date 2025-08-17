package tk.kiwicollections.kiwizin.utils.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.kiwicollections.kiwizin.utils.Main;
import tk.kiwicollections.kiwizin.utils.upgrades.menu.MenuUpgrades;
import tk.slicecollections.maxteer.libraries.npclib.NPCLibrary;
import tk.slicecollections.maxteer.libraries.npclib.api.event.NPCLeftClickEvent;
import tk.slicecollections.maxteer.libraries.npclib.api.event.NPCRightClickEvent;
import tk.slicecollections.maxteer.libraries.npclib.api.npc.NPC;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.plugin.config.MConfig;

public class PlayerInteractListener implements Listener {

    private static final MConfig config = Main.getInstance().getConfig("upgrades");

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent evt) {
        Player player = evt.getPlayer();
        Profile profile = Profile.getProfile(player.getName());

        if (profile != null) {
            NPC npc = evt.getNPC();
            if (npc.data().has("n-upgrade")) {
                new MenuUpgrades(profile);
            }
        }
    }

}
