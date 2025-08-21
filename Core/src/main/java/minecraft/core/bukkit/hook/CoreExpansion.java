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

/**
 * Expansão do PlaceholderAPI para o plugin Core.
 * Fornece placeholders personalizados para estatísticas, status e mensagens.
 * 
 * @author Luiz
 * @version 1.0
 */
@SuppressWarnings("all")
public class CoreExpansion extends PlaceholderExpansion {
  
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
    
    // Placeholders de online
    if (params.startsWith("online")) {
      return handleOnlinePlaceholder(params);
    }
    
    // Placeholders de jogador
    switch (params) {
      case "rank":
        return Rank.getPlayerRank(player).getName();
        
      case "cash":
        return StringUtils.formatNumber(CashManager.getCash(player));
        
      case "status_jogadores":
        return profile.getPreferencesContainer().getPlayerVisibility().getName();
        
      case "status_jogadores_nome":
        return profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS ? "§aON" : "§cOFF";
        
      case "status_jogadores_inksack":
        return profile.getPreferencesContainer().getPlayerVisibility().getInkSack();
        
      case "entrymessage":
        return handleEntryMessage(profile);
    }
    
    // Placeholders de SkyWars
    if (params.startsWith("SkyWars_")) {
      return handleSkyWarsPlaceholder(profile, params);
    }
    
    // Placeholders de BedWars
    if (params.startsWith("BedWars_")) {
      return handleBedWarsPlaceholder(profile, params);
    }
    
    return null;
  }
  
  /**
   * Processa placeholders relacionados ao número de jogadores online.
   */
  private String handleOnlinePlaceholder(String params) {
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
  }
  
  /**
   * Processa o placeholder de mensagem de entrada.
   */
  private String handleEntryMessage(Profile profile) {
    String entryMessageId = profile.getDataContainer("account", "entrymessage").getAsString();
    
    if (entryMessageId != null && !entryMessageId.isEmpty() && !entryMessageId.equals("0")) {
      try {
        int messageId = Integer.parseInt(entryMessageId);
        String actualMessage = getMessageById(messageId);
        if (actualMessage != null) {
          return actualMessage;
        }
      } catch (NumberFormatException e) {
        return getMessageById(1);
      }
    }
    
    return getMessageById(1);
  }
  
  /**
   * Processa placeholders relacionados ao SkyWars.
   */
  private String handleSkyWarsPlaceholder(Profile profile, String params) {
    String table = "skywars";
    String value = params.replace("SkyWars_", "");
    
    // Estatísticas gerais
    if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || 
        value.equals("games") || value.equals("wins")) {
      return StringUtils.formatNumber(profile.getStats(table, "solo" + value, "duo" + value, "ranked" + value));
    }
    
    // Estatísticas solo
    if (value.equals("solokills") || value.equals("solodeaths") || value.equals("soloassists") || 
        value.equals("sologames") || value.equals("solowins")) {
      return StringUtils.formatNumber(profile.getStats("skywars", value));
    }
    
    // Estatísticas duo
    if (value.equals("duokills") || value.equals("duodeaths") || value.equals("duoassists") || 
        value.equals("duogames") || value.equals("duowins")) {
      return StringUtils.formatNumber(profile.getStats("skywars", value));
    }
    
    // Estatísticas ranked
    if (value.equals("rankedkills") || value.equals("rankeddeaths") || value.equals("rankedassists") || 
        value.equals("rankedgames") || value.equals("rankedwins") || value.equals("rankedpoints")) {
      return StringUtils.formatNumber(profile.getStats("skywars", value));
    }
    
    // Estatísticas mensais
    if (value.equals("monthlykills") || value.equals("monthlydeaths") || value.equals("monthlyassists") || 
        value.equals("monthlywins") || value.equals("monthlygames") || value.equals("monthlypoints")) {
      return StringUtils.formatNumber(profile.getStats("skywars", value));
    }
    
    // Coins
    if (value.equals("coins")) {
      return StringUtils.formatNumber(profile.getDataContainer("skywars", "coins").getAsDouble());
    }
    
    return null;
  }
  
  /**
   * Processa placeholders relacionados ao BedWars.
   */
  private String handleBedWarsPlaceholder(Profile profile, String params) {
    String table = "bedwars";
    String value = params.replace("BedWars_", "");
    
    // Estatísticas gerais
    if (value.equals("kills") || value.equals("deaths") || value.equals("bedslosteds") || 
        value.equals("finalkills") || value.equals("finaldeaths") || value.equals("bedsdestroyeds") || 
        value.equals("games") || value.equals("wins")) {
      return StringUtils.formatNumber(profile.getStats(table, "solo" + value, "duo" + value, "4v4" + value));
    }
    
    // Estatísticas duo
    if (value.equals("duokills") || value.equals("duodeaths") || value.equals("duo") || 
        value.equals("duogames") || value.equals("duofinalkills") || value.equals("duofinaldeaths") || 
        value.equals("duobedsdestroyeds") || value.equals("duobedslosteds") || value.equals("duowins")) {
      return StringUtils.formatNumber(profile.getStats("bedwars", value));
    }
    
    // Estatísticas solo
    if (value.equals("solokills") || value.equals("solodeaths") || value.equals("solo") || 
        value.equals("sologames") || value.equals("solofinalkills") || value.equals("solofinaldeaths") || 
        value.equals("solobedsdestroyeds") || value.equals("solobedslosteds") || value.equals("solowins")) {
      return StringUtils.formatNumber(profile.getStats("bedwars", value));
    }
    
    // Estatísticas 4v4
    if (value.equals("4v4kills") || value.equals("4v4deaths") || value.equals("4v4") || 
        value.equals("4v4games") || value.equals("4v4finalkills") || value.equals("4v4finaldeaths") || 
        value.equals("4v4bedsdestroyeds") || value.equals("4v4bedslosteds") || value.equals("4v4wins")) {
      return StringUtils.formatNumber(profile.getStats(table, value));
    }
    
    // Coins
    if (value.equals("coins")) {
      return StringUtils.formatNumber(profile.getCoins(table));
    }
    
    return null;
  }
  
  /**
   * Obtém a mensagem de entrada pelo ID.
   */
  private String getMessageById(int messageId) {
    String[] availableMessages = {
        "§6entrou no lobby!",                    // ID 1 - Padrão
        "§aentrou juntamente com brr brr patapim", // ID 2 - Braintrot
        "§4entrou pronto para batalhar",         // ID 3 - Gladiador
        "§2§kentrou no lobby",                   // ID 4 - Glitch
        "§despalhou doces no lobby",             // ID 5 - Doce
        "§6tudo deles e nada nosso"              // ID 6 - Regresso
    };
    
    if (messageId > 0 && messageId <= availableMessages.length) {
      return availableMessages[messageId - 1];
    }
    return null;
  }
}

