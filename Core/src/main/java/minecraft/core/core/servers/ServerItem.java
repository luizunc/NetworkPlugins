package minecraft.core.core.servers;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.Profile;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.bukkit.config.Servers;
import minecraft.core.core.servers.balancer.BaseBalancer;
import minecraft.core.core.servers.balancer.Server;
import minecraft.core.core.servers.balancer.type.LeastConnection;

import java.util.*;

public class ServerItem {
  
  public static final KConfig CONFIG = Servers.getConfig();
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
    try {
      if (CONFIG == null) {
        return;
      }
      
      // Limpar lista de servidores
      SERVERS.clear();
      DISABLED_SLOTS.clear();
      
      // Carregar slots desabilitados
      if (CONFIG.contains("disabled-slots")) {
        List<Integer> disabledSlots = CONFIG.getIntegerList("disabled-slots");
        DISABLED_SLOTS.addAll(disabledSlots);
      }
      
      // Carregar servidores
      if (CONFIG.contains("items")) {
        for (String serverKey : CONFIG.getConfigurationSection("items").getKeys(false)) {
          String slotStr = CONFIG.getString("items." + serverKey + ".slot");
          String icon = CONFIG.getString("items." + serverKey + ".icon");
          
          if (slotStr != null && icon != null) {
            try {
              int slot = Integer.parseInt(slotStr);
              List<String> serverNames = CONFIG.getStringList("items." + serverKey + ".servernames");
              
              BaseBalancer<Server> balancer = new LeastConnection<>();
              for (String serverName : serverNames) {
                String[] parts = serverName.split(" ; ");
                if (parts.length == 2) {
                  String ipPort = parts[0].trim();
                  String name = parts[1].trim();
                  int max = CONFIG.getInt("items." + serverKey + ".max-players", 100);
                                Server server = new Server(ipPort, name, max);
              balancer.add(name, server);
                }
              }
              
              SERVERS.add(new ServerItem(serverKey, slot, icon, balancer));
            } catch (NumberFormatException e) {
              // Slot inválido, ignorar
            }
          }
        }
      }
    } catch (Exception e) {
      // Erro ao carregar servidores, usar configuração vazia
    }
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
    // Sistema simples: conecta diretamente ao primeiro servidor disponível
    for (Server server : balancer.getList()) {
      Core.sendServer(profile, server.getName());
      return;
    }
    profile.getPlayer().sendMessage("§cNão foi possível se conectar a esse servidor no momento!");
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
