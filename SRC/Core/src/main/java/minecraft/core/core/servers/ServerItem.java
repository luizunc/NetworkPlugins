package minecraft.core.core.servers;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.Profile;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.servers.balancer.BaseBalancer;
import minecraft.core.core.servers.balancer.Server;
import minecraft.core.core.servers.balancer.type.LeastConnection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ServerItem {
  
  public static final KConfig CONFIG = null;
  public static final List<Integer> DISABLED_SLOTS = new ArrayList<>();
  public static final Map<String, Integer> SERVER_COUNT = new HashMap<>();
  private static final List<ServerItem> SERVERS = new ArrayList<>();
  private final String key;
  private final int slot;
  private final String icon;
  private final BaseBalancer<Server> balancer;
  
  public ServerItem(String key, int slot, String icon, BaseBalancer<Server> baseBalancer) {
    this.key = key;
    this.slot = slot;
    this.icon = icon;
    this.balancer = baseBalancer;
  }
  
  public static void setupServers() {
    // Como servers.yml não será mais incluído, não há servidores para configurar
    // O sistema de servidores será desabilitado
  }
  
  public static Collection<ServerItem> listServers() {
    return SERVERS;
  }

  public static ServerItem getServerItem(String key) {
    return SERVERS.stream().filter(si -> si.getKey().equals(key)).findFirst().orElse(null);
  }

  public static boolean alreadyQuerying(String servername) {
    return SERVERS.stream().anyMatch(si -> si.getBalancer().keySet().contains(servername));
  }

  public static int getServerCount(ServerItem serverItem) {
    return serverItem.getBalancer().getTotalNumber();
  }
  
  public static int getServerCount(String servername) {
    return SERVER_COUNT.get(servername) == null ? 0 : SERVER_COUNT.get(servername);
  }
  
  public void connect(Profile profile) {
    Server server = balancer.next();
    if (server != null) {
      Core.sendServer(profile, server.getName());
    } else {
      profile.getPlayer().sendMessage("§cNão foi possível se conectar a esse servidor no momento!");
    }
  }
  
  public String getKey() {
    return this.key;
  }
  
  public int getSlot() {
    return this.slot;
  }
  
  public String getIcon() {
    return this.icon;
  }
  
  public BaseBalancer<Server> getBalancer() {
    return this.balancer;
  }
}
