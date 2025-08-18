package minecraft.lobby.lobby;

import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.holograms.api.Hologram;
import minecraft.core.core.libraries.npclib.NPCLibrary;
import minecraft.core.core.libraries.npclib.api.npc.NPC;
import minecraft.lobby.Main;
import minecraft.lobby.lobby.trait.NPCHandTrait;
import minecraft.lobby.lobby.trait.NPCSkinTrait;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Gerencia NPCs de jogos no lobby.
 */
public class PlayNPC {
  
  private static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("NPCS");
  private static final List<PlayNPC> NPCS = new ArrayList<>();
  
  private String id;
  private ServerEntry entry;
  private Location location;
  private NPC npc;
  private Hologram hologram;
  
  public PlayNPC(Location location, String id, ServerEntry entry) {
    this.location = location;
    this.id = id;
    this.entry = entry;
    if (!this.location.getChunk().isLoaded()) {
      this.location.getChunk().load(true);
    }
    
    this.spawn();
  }
  
  /**
   * Configura os NPCs de jogos.
   */
  public static void setupNPCs() {
    ServerEntry.setupEntries();
    LOGGER.info("NPCs de jogos configurados com sucesso.");
    
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> NPCS.forEach(PlayNPC::update), 20, 20);
  }
  
  /**
   * Adiciona um novo NPC de jogo.
   */
  public static void add(String id, Location location, ServerEntry mode) {
    NPCS.add(new PlayNPC(location, id, mode));
    LOGGER.info("NPC " + id + " adicionado com sucesso.");
  }
  
  /**
   * Remove um NPC de jogo.
   */
  public static void remove(PlayNPC npc) {
    NPCS.remove(npc);
    npc.destroy();
    LOGGER.info("NPC " + npc.getId() + " removido com sucesso.");
  }
  
  public static PlayNPC getById(String id) {
    return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
  }
  
  public static Collection<PlayNPC> listNPCs() {
    return NPCS;
  }
  
  public void spawn() {
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }
    
    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
    
    this.hologram = HologramLibrary.createHologram(this.location.clone().add(0, 0.5, 0));
    for (int index = this.entry.listHologramLines().size(); index > 0; index--) {
      this.hologram
          .withLine(this.entry.listHologramLines().get(index - 1).replace("{players}", StringUtils.formatNumber(this.entry.getServerItem().getBalancer().getTotalNumber())));
    }
    
    this.npc = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] ");
    this.npc.data().set("play-npc", this.entry.getKey());
    this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);
    this.npc.addTrait(new NPCHandTrait(this.npc, this.entry.getHand()));
    this.npc.addTrait(new NPCSkinTrait(this.npc, this.entry.getSkinValue(), this.entry.getSkinSignature()));
    this.npc.spawn(this.location);
  }
  
  public void update() {
    int size = this.entry.listHologramLines().size();
    for (int index = size; index > 0; index--) {
      this.hologram.updateLine(size - (index - 1),
          this.entry.listHologramLines().get(index - 1).replace("{players}", StringUtils.formatNumber(this.entry.getServerItem().getBalancer().getTotalNumber())));
    }
  }

  public void destroy() {
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }
    
    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
  }
  
  public String getId() {
    return this.id;
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  public ServerEntry getEntry() {
    return this.entry;
  }
}
