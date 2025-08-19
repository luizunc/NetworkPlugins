package minecraft.core.bukkit.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import minecraft.core.bukkit.Core;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.role.Role;
import minecraft.core.core.servers.ServerItem;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class CoreExpansion extends PlaceholderExpansion {
  
  // private static final SimpleDateFormat MURDER_FORMAT = new SimpleDateFormat("mm:ss"); // Removido - Murder não é mais suportado
  
  @Override
  public boolean canRegister() {
    return true;
  }
  
  @Override
  public String getAuthor() {
    return "Luiz";
  }
  
  @Override
  public String getIdentifier() {
    return "Core";
  }
  
  @Override
  public String getVersion() {
    return Core.getInstance().getDescription().getVersion();
  }
  
  @Override
  public String onPlaceholderRequest(Player player, String params) {
    Profile profile = null;
    if (player == null || (profile = Profile.getProfile(player.getName())) == null) {
      return "";
    }
    
    if (params.startsWith("online")) {
      if (params.contains("online_")) {
        String server = params.replace("online_", "");
        ServerItem si = ServerItem.getServerItem(server);
        if (si != null) {
          return StringUtils.formatNumber(si.getBalancer().getTotalNumber());
        }
        
        return "entry invalida";
      }
      
      long online = 0;
      for (ServerItem si : ServerItem.listServers()) {
        online += si.getBalancer().getTotalNumber();
      }
      return StringUtils.formatNumber(online);
    } else if (params.equals("role")) {
      return Role.getPlayerRole(player).getName();
    } else if (params.equals("cash")) {
      return StringUtils.formatNumber(CashManager.getCash(player));
    } else if (params.equals("status_jogadores")) {
      return profile.getPreferencesContainer().getPlayerVisibility().getName();
    } else if (params.equals("status_jogadores_nome")) {
      if (profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS) {
        return "§aON";
      }
      
      return "§cOFF";
    } else if (params.equals("status_jogadores_inksack")) {
      return profile.getPreferencesContainer().getPlayerVisibility().getInkSack();
    } else if (params.startsWith("SkyWars_")) {
      String table = "CoreSkyWars";
      String value = params.replace("SkyWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "1v1" + value, "2v2" + value, "ranked" + value));
      } else if (value.equals("1v1kills") || value.equals("1v1deaths") || value.equals("1v1assists") || value.equals("1v1games") || value.equals("1v1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("2v2kills") || value.equals("2v2deaths") || value.equals("2v2assists") || value.equals("2v2games") || value.equals("2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("rankedkills") || value.equals("rankeddeaths") || value.equals("rankedassists") || value.equals("rankedgames") || value.equals("rankedwins") || value.equals("rankedpoints")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    // The Bridge, Build Battle e Murder removidos do sistema
    } else if (params.startsWith("BedWars_")) {
      String table = "CoreBedWars";
      String value = params.replace("BedWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("bedslosteds") || value.equals("finalkills") || value.equals("finaldeaths") || value.equals("bedsdestroyeds") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "1v1" + value, "4v4" + value, "2v2" + value));
      } else if (value.equals("2v2kills") || value.equals("2v2deaths") || value.equals("2v2") || value.equals("2v2games") || value.equals("2v2finalkills") || value.equals("2v2finaldeaths") || value.equals("2v2bedsdestroyeds") || value.equals("2v2bedslosteds") || value.equals("2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("1v1kills") || value.equals("1v1deaths") || value.equals("1v1") || value.equals("1v1games") || value.equals("1v1finalkills") || value.equals("1v1finaldeaths") || value.equals("1v1bedsdestroyeds") || value.equals("1v1bedslosteds") || value.equals("1v1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("4v4kills") || value.equals("4v4deaths") || value.equals("4v4") || value.equals("4v4games") || value.equals("4v4finalkills") || value.equals("4v4finaldeaths") || value.equals("4v4bedsdestroyeds") || value.equals("4v4bedslosteds") || value.equals("4v4wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    }
    
    return null;
  }
}
