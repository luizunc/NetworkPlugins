package minecraft.lobby.lobby;

import minecraft.lobby.Main;
import minecraft.lobby.config.EntriesConfig;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma entrada de servidor com configurações de NPC.
 */
public class ServerEntry {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("ENTRIES");
  private static final List<ServerEntry> ENTRIES = new ArrayList<>();
  
  private final String key;
  private final List<String> holograms;
  private final ItemStack hand;
  private final String skinValue;
  private final String skinSignature;
  
  public ServerEntry(String key, List<String> holograms, ItemStack hand, String skinValue, String skinSignature) {
    this.key = key;
    this.holograms = holograms;
    this.hand = hand;
    this.skinValue = skinValue;
    this.skinSignature = skinSignature;
  }
  
  /**
   * Configura as entradas de servidor usando as configurações hardcoded.
   */
  public static void setupEntries() {
    EntriesConfig.getEntries().forEach((key, entry) -> {
      ServerEntry se = new ServerEntry(
          entry.getKey(),
          entry.getHolograms(),
          BukkitUtils.deserializeItemStack(entry.getHand()),
          entry.getSkinValue(),
          entry.getSkinSignature()
      );
      
      if (se.getServerItem() == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), 
            () -> LOGGER.warning("A entry " + key + " possui uma key invalida."));
        return;
      }
      
      ENTRIES.add(se);
    });
  }
  
  public static ServerEntry getByKey(String key) {
    return ENTRIES.stream().filter(entry -> entry.getKey().equals(key)).findFirst().orElse(null);
  }
  
  public String getKey() {
    return this.key;
  }
  
  public ServerItem getServerItem() {
    return ServerItem.getServerItem(this.key);
  }
  
  public List<String> listHologramLines() {
    return this.holograms;
  }

  public ItemStack getHand() {
    return this.hand;
  }
  
  public String getSkinValue() {
    return this.skinValue;
  }
  
  public String getSkinSignature() {
    return this.skinSignature;
  }
}
