package minecraft.core.bukkit.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import minecraft.core.bukkit.Core;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.role.Rank;
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
            } else if (params.equals("rank")) {
            return Rank.getPlayerRank(player).getName();
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
      String table = "skywars";
      String value = params.replace("SkyWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "solo" + value, "duo" + value, "ranked" + value));
      } else if (value.equals("solokills") || value.equals("solodeaths") || value.equals("soloassists") || value.equals("sologames") || value.equals("solowins")) {
        return StringUtils.formatNumber(profile.getStats("skywars", value));
      } else if (value.equals("duokills") || value.equals("duodeaths") || value.equals("duoassists") || value.equals("duogames") || value.equals("duowins")) {
        return StringUtils.formatNumber(profile.getStats("skywars", value));
      } else if (value.equals("rankedkills") || value.equals("rankeddeaths") || value.equals("rankedassists") || value.equals("rankedgames") || value.equals("rankedwins") || value.equals("rankedpoints")) {
        return StringUtils.formatNumber(profile.getStats("skywars", value));
      } else if (value.equals("monthlykills") || value.equals("monthlydeaths") || value.equals("monthlyassists") || value.equals("monthlywins") || value.equals("monthlygames") || value.equals("monthlypoints")) {
        return StringUtils.formatNumber(profile.getStats("skywars", value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getDataContainer("skywars", "coins").getAsDouble());
      }
    // The Bridge, Build Battle e Murder removidos do sistema
    } else if (params.startsWith("BedWars_")) {
      String table = "bedwars";
      String value = params.replace("BedWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("bedslosteds") || value.equals("finalkills") || value.equals("finaldeaths") || value.equals("bedsdestroyeds") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "solo" + value, "duo" + value, "4v4" + value));
      } else if (value.equals("duokills") || value.equals("duodeaths") || value.equals("duo") || value.equals("duogames") || value.equals("duofinalkills") || value.equals("duofinaldeaths") || value.equals("duobedsdestroyeds") || value.equals("duobedslosteds") || value.equals("duowins")) {
        return StringUtils.formatNumber(profile.getStats("bedwars", value));
      } else if (value.equals("solokills") || value.equals("solodeaths") || value.equals("solo") || value.equals("sologames") || value.equals("solofinalkills") || value.equals("solofinaldeaths") || value.equals("solobedsdestroyeds") || value.equals("solobedslosteds") || value.equals("solowins")) {
        return StringUtils.formatNumber(profile.getStats("bedwars", value));
      } else if (value.equals("4v4kills") || value.equals("4v4deaths") || value.equals("4v4") || value.equals("4v4games") || value.equals("4v4finalkills") || value.equals("4v4finaldeaths") || value.equals("4v4bedsdestroyeds") || value.equals("4v4bedslosteds") || value.equals("4v4wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    }
    
    return null;
  }
}
