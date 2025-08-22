package minecraft.bedwars.lobby;

import minecraft.bedwars.Main;
import minecraft.bedwars.config.LobbiesConfig;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.servers.ServerPing;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lobby {
  
  public static final List<String> WARNINGS = new ArrayList<>();
  private static final List<Lobby> LOBBIES = new ArrayList<>();
  
  private int slot;
  private ServerPing serverPing;
  private int maxPlayers;
  private String icon;
  private String serverName;
  
  public Lobby(int slot, String icon, int maxPlayers, String ip, String serverName) {
    this.slot = slot;
    this.icon = icon;
    this.serverPing = new ServerPing(new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1])));
    this.maxPlayers = maxPlayers;
    this.serverName = serverName;
  }
  
  public static void setupLobbies() {
    // Carregar lobbies do sistema interno
    for (String serverName : LobbiesConfig.getServerNames()) {
      LobbiesConfig.LobbyItem item = LobbiesConfig.getLobby(serverName);
      if (item != null) {
        String[] serverInfo = item.getServerName().split(" ; ");
        if (serverInfo.length >= 2) {
          LOBBIES.add(new Lobby(
              item.getSlot(),
              item.getIcon(),
              item.getMaxPlayers(),
              serverInfo[0],
              serverInfo[1]
          ));
        } else {
          WARNINGS.add(" - (" + serverName + ") " + item.getServerName());
        }
      }
    }
    
    List<Lobby> query = new ArrayList<>();
    for (Lobby lobby : LOBBIES) {
      if (!ServerItem.alreadyQuerying(lobby.getServerName())) {
        query.add(lobby);
      }
    }
    
    if (!query.isEmpty()) {
      new BukkitRunnable() {
        @Override
        public void run() {
          query.forEach(Lobby::fetch);
        }
      }.runTaskTimerAsynchronously(Main.getInstance(), 0, 40);
    }
    
    Main.getInstance().getLogger().info("Sistema de lobbies interno carregado com sucesso.");
  }
  
  public static Collection<Lobby> listLobbies() {
    return LOBBIES;
  }
  
  public void fetch() {
    this.serverPing.fetch();
    ServerItem.SERVER_COUNT.put(this.serverName, this.serverPing.getOnline());
  }
  
  public int getSlot() {
    return this.slot;
  }

  public String getIcon() {
    return this.icon;
  }

  public int getPlayers() {
    return this.serverName.equals(Main.currentServerName) ? Bukkit.getOnlinePlayers().size() : ServerItem.getServerCount(this.serverName);
  }
  
  public int getMaxPlayers() {
    return this.maxPlayers;
  }
  
  public String getServerName() {
    return this.serverName;
  }
}
