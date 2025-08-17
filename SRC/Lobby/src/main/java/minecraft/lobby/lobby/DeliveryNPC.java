package minecraft.lobby.lobby;

import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.holograms.api.Hologram;
import minecraft.core.core.libraries.npclib.NPCLibrary;
import minecraft.core.core.libraries.npclib.api.npc.NPC;
import minecraft.lobby.Main;
import minecraft.lobby.lobby.trait.NPCSkinTrait;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeliveryNPC {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("npcs");
  private static final List<DeliveryNPC> NPCS = new ArrayList<>();
  private String id;
  private Location location;
  private NPC npc;
  private Hologram hologram;
  
  public DeliveryNPC(Location location, String id) {
    this.location = location;
    this.id = id;
    if (!this.location.getChunk().isLoaded()) {
      this.location.getChunk().load(true);
    }
    
    this.spawn();
  }
  
  public static void setupNPCs() {
    if (!CONFIG.contains("delivery")) {
      CONFIG.set("delivery", new ArrayList<>());
    }
    
    for (String serialized : CONFIG.getStringList("delivery")) {
      if (serialized.split("; ").length > 6) {
        String id = serialized.split("; ")[6];
        
        NPCS.add(new DeliveryNPC(BukkitUtils.deserializeLocation(serialized), id));
      }
    }
    
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> NPCS.forEach(DeliveryNPC::update), 20, 20);
  }
  
  public static void add(String id, Location location) {
    NPCS.add(new DeliveryNPC(location, id));
    List<String> list = CONFIG.getStringList("delivery");
    list.add(BukkitUtils.serializeLocation(location) + "; " + id);
    CONFIG.set("delivery", list);
  }
  
  public static void remove(DeliveryNPC npc) {
    NPCS.remove(npc);
    List<String> list = CONFIG.getStringList("delivery");
    list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId());
    CONFIG.set("delivery", list);
    
    npc.destroy();
  }
  
  public static DeliveryNPC getById(String id) {
    return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
  }
  
  public static Collection<DeliveryNPC> listNPCs() {
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
    this.hologram.withLine("§cFuncionalidade removida");
    this.hologram.withLine("§bEntregador");
    this.hologram.withLine("§e§lCLIQUE DIREITO");
    
    this.npc = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] ");
    this.npc.data().set("delivery-npc", true);
    this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);
    this.npc.addTrait(new NPCSkinTrait(this.npc, "eyJ0aW1lc3RhbXAiOjE1ODM0NTc4OTkzMTksInByb2ZpbGVJZCI6IjIxMWNhN2E4ZWFkYzQ5ZTVhYjBhZjMzMTBlODY0M2NjIiwicHJvZmlsZU5hbWUiOiJNYXh0ZWVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MWU0NTc3OTgzZjEzZGI2YTRiMWMwNzQ1MGUyNzQ2MTVkMDMyOGUyNmI0MGQ3ZDMyMjA3MjYwOWJmZGQ0YTA4IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=", "SXnMF3f9x90fa+FdP2rLk/V6/zvMNuZ0sC4RQpPHF9JxdVWYRZm/+DhxkfjCHWKXV/4FSTN8LPPsxXd0XlYSElpi5OaT9/LGhITSK6BbeBfaYhLZnoD0cf9jG9nl9av38KipnkNXI+cO3wttB27J7KHznAmfrJd5bxdO/M0aGQYtwpckchYUBG6pDzaxN7tr4bFxDdxGit8Tx+aow/YtYSQn4VilBIy2y/c2a4PzWEpWyZQ94ypF5ZojvhaSPVl88Fbh+StdgfJUWNN3hNWt31P68KT4Jhx+SkT2LTuDj0jcYsiuxHP6AzZXtOtPPARqM0/xd53CUHCK+TEF5mkbJsG/PZYz/JRR1B1STk4D2cgbhunF87V4NLmCBtF5WDQYid11eO0OnROSUbFduCLj0uJ6QhNRRdhSh54oES7vTi0ja3DftTjdFhPovDAXQxCn+ROhTeSxjW5ZvP6MpmJERCSSihv/11VGIrVRfj2lo9MaxRogQE3tnyMNKWm71IRZQf806hwSgHp+5m2mhfnjYeGRZr44j21zqnSKudDHErPyEavLF83ojuMhNqTTO43ri3MVbMGix4TbIOgB2WDwqlcYLezENBIIkRsYO/Y1r5BWCA7DJ5IlpxIr9TCu39ppVmOGReDWA/Znyox5GP6JIM53kQoTOFBM3QWIQcmXll4="));
    this.npc.spawn(this.location);
  }

  public void update() {
    this.hologram.updateLine(0, "§cFuncionalidade removida");
    this.hologram.updateLine(1, "§bEntregador");
    this.hologram.updateLine(2, "§e§lCLIQUE DIREITO");
  }
  
  public void destroy() {
    this.id = null;
    this.location = null;
    
    this.npc.destroy();
    this.npc = null;
    HologramLibrary.removeHologram(this.hologram);
    this.hologram = null;
  }
  
  public String getId() {
    return id;
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  public NPC getNPC() {
    return this.npc;
  }
  
  public Hologram getHologram() {
    return this.hologram;
  }
}
