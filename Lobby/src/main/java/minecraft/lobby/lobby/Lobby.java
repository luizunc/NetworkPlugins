package minecraft.lobby.lobby;

import minecraft.lobby.Main;
import minecraft.lobby.config.LobbiesConfig;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.servers.ServerPing;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Lobby {
  
  public static final List<Lobby> QUERY = new ArrayList<>();
  public static final List<String> WARNINGS = new ArrayList<>();
  private static final List<Lobby> LOBBIES = new ArrayList<>();
  private final int slot;
  private final ServerPing serverPing;
  private final int maxPlayers;
  private final String icon;
  private final String serverName;
  
  public Lobby(int slot, String icon, int maxPlayers, String ip, String serverName) {
    this.slot = slot;
    this.icon = icon;
    this.serverPing = new ServerPing(
        new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1])));
    this.maxPlayers = maxPlayers;
    this.serverName = serverName;
  }
  
  public static void setupLobbies() {
    new LobbyEntryTask().runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 30);
    
    for (Map.Entry<String, LobbiesConfig.LobbyItem> entry : LobbiesConfig.getLobbyItems().entrySet()) {
      String key = entry.getKey();
      LobbiesConfig.LobbyItem item = entry.getValue();
      
      String servername = item.getServerName();
      if (servername.split(" ; ").length < 2) {
        WARNINGS.add(" - (" + key + ") " + servername);
        continue;
      }
      
      LOBBIES.add(
          new Lobby(item.getSlot(),
              item.getIcon(),
              item.getMaxPlayers(), servername.split(" ; ")[0],
              servername.split(" ; ")[1]));
    }
    
    for (Lobby lobby : LOBBIES) {
      if (!ServerItem.alreadyQuerying(lobby.getServerName())) {
        QUERY.add(lobby);
      }
    }
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
    return this.serverName.equals(Main.getCurrentServerName()) ? Bukkit.getOnlinePlayers().size()
        : ServerItem.getServerCount(this.serverName);
  }
  
  public int getMaxPlayers() {
    return this.maxPlayers;
  }
  
  public String getServerName() {
    return this.serverName;
  }
}
