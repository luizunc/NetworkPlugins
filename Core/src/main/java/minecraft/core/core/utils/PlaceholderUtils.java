package minecraft.core.core.utils;

import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.servers.ServerItem;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitário para processamento de placeholders com sintaxe {...}.
 * Sistema próprio de placeholders com sintaxe {...}.
 * 
 * @author Luiz
 * @version 1.0
 */
public class PlaceholderUtils {
    
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)\\}");
    
    /**
     * Processa placeholders em uma string usando a sintaxe {...}.
     * 
     * @param player Jogador para processar os placeholders
     * @param text Texto contendo placeholders
     * @return Texto com placeholders processados
     */
    public static String setPlaceholders(Player player, String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            return text;
        }
        
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = processPlaceholder(player, profile, placeholder);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * Processa um placeholder específico.
     * 
     * @param player Jogador
     * @param profile Perfil do jogador
     * @param placeholder Nome do placeholder
     * @return Valor do placeholder
     */
    private static String processPlaceholder(Player player, Profile profile, String placeholder) {
        // Placeholders de online
        if (placeholder.startsWith("online")) {
            return handleOnlinePlaceholder(placeholder);
        }
        
        // Placeholders de jogador
        switch (placeholder) {
            case "rank":
                return Rank.getRank(player).getName();
                
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
        if (placeholder.startsWith("SkyWars_")) {
            return handleSkyWarsPlaceholder(profile, placeholder);
        }
        
        // Placeholders de BedWars
        if (placeholder.startsWith("BedWars_")) {
            return handleBedWarsPlaceholder(profile, placeholder);
        }
        
        // Placeholders específicos do Core
        if (placeholder.startsWith("Core_")) {
            return handleCorePlaceholder(profile, placeholder);
        }
        
        // Placeholders específicos do perfil
        if (placeholder.equals("perfil")) {
            return "";
        }
        
        return "{" + placeholder + "}";
    }
    
    /**
     * Processa placeholders relacionados ao número de jogadores online.
     */
    private static String handleOnlinePlaceholder(String placeholder) {
        if (placeholder.contains("online_")) {
            String server = placeholder.replace("online_", "");
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
    private static String handleEntryMessage(Profile profile) {
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
    private static String handleSkyWarsPlaceholder(Profile profile, String placeholder) {
        String table = "skywars";
        String value = placeholder.replace("SkyWars_", "");
        
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
        if (value.equals("rankedkills") || value.equals("rankeddeaths") || 
            value.equals("rankedassists") || value.equals("rankedgames") || 
            value.equals("rankedwins") || value.equals("rankedpoints")) {
            return StringUtils.formatNumber(profile.getStats("skywars", value));
        }
        
        // Coins
        if (value.equals("coins")) {
            return StringUtils.formatNumber(profile.getCoins(table));
        }
        
        return "0";
    }
    
    /**
     * Processa placeholders relacionados ao BedWars.
     */
    private static String handleBedWarsPlaceholder(Profile profile, String placeholder) {
        String table = "bedwars";
        String value = placeholder.replace("BedWars_", "");
        
        // Estatísticas gerais
        if (value.equals("kills") || value.equals("deaths") || value.equals("bedslosteds") || 
            value.equals("finalkills") || value.equals("finaldeaths") || value.equals("beds") || 
            value.equals("games") || value.equals("wins")) {
            return StringUtils.formatNumber(profile.getStats(table, "solo" + value, "duo" + value, "quad" + value));
        }
        
        // Estatísticas duo
        if (value.equals("duokills") || value.equals("duodeaths") || value.equals("duo") || 
            value.equals("duogames") || value.equals("duofinalkills") || value.equals("duofinaldeaths") || 
            value.equals("duobeds") || value.equals("duobedslosteds") || value.equals("duowins")) {
            return StringUtils.formatNumber(profile.getStats("bedwars", value));
        }
        
        // Estatísticas solo
        if (value.equals("solokills") || value.equals("solodeaths") || value.equals("solo") || 
            value.equals("sologames") || value.equals("solofinalkills") || value.equals("solofinaldeaths") || 
            value.equals("solobeds") || value.equals("solobedslosteds") || value.equals("solowins")) {
            return StringUtils.formatNumber(profile.getStats("bedwars", value));
        }
        
        // Estatísticas quad
        if (value.equals("quadkills") || value.equals("quaddeaths") || value.equals("quad") || 
            value.equals("quadgames") || value.equals("quadfinalkills") || value.equals("quadfinaldeaths") || 
            value.equals("quadbeds") || value.equals("quadbedslosteds") || value.equals("quadwins")) {
            return StringUtils.formatNumber(profile.getStats(table, value));
        }
        
        // Coins
        if (value.equals("coins")) {
            return StringUtils.formatNumber(profile.getCoins(table));
        }
        
        return "0";
    }
    
    /**
     * Processa placeholders específicos do Core.
     */
    private static String handleCorePlaceholder(Profile profile, String placeholder) {
        String value = placeholder.replace("Core_", "");
        
        // Placeholders básicas do Core
        if (value.equals("rank")) {
            return Rank.getRank(profile.getPlayer()).getName();
        } else if (value.equals("cash")) {
            return StringUtils.formatNumber(CashManager.getCash(profile.getPlayer()));
        } else if (value.equals("online")) {
            long online = 0;
            for (ServerItem si : ServerItem.listServers()) {
                online += si.getBalancer().getTotalNumber();
            }
            return StringUtils.formatNumber(online);
        } else if (value.equals("entrymessage")) {
            return handleEntryMessage(profile);
        } else if (value.equals("status_jogadores")) {
            String name = profile.getPreferencesContainer().getPlayerVisibility().getName();
            return name != null ? name : "§aAtivado";
        } else if (value.equals("status_jogadores_nome")) {
            return profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS ? "§aON" : "§cOFF";
        } else if (value.equals("status_jogadores_inksack")) {
            String inkSack = profile.getPreferencesContainer().getPlayerVisibility().getInkSack();
            return inkSack != null ? inkSack : "10";
        // Placeholders específicos do Core para estatísticas
        } else if (value.equals("SkyWars_kills")) {
            return StringUtils.formatNumber(profile.getStats("skywars", "solokills", "duokills", "rankedkills"));
        } else if (value.equals("SkyWars_wins")) {
            return StringUtils.formatNumber(profile.getStats("skywars", "solowins", "duowins", "rankedwins"));
        } else if (value.equals("BedWars_kills")) {
            return StringUtils.formatNumber(profile.getStats("bedwars", "solokills", "duokills", "quadkills"));
        } else if (value.equals("BedWars_wins")) {
            return StringUtils.formatNumber(profile.getStats("bedwars", "solowins", "duowins", "quadwins"));
        }
        
        return "0";
    }
    
    /**
     * Obtém a mensagem de entrada pelo ID.
     */
    private static String getMessageById(int messageId) {
        switch (messageId) {
            case 1:
                return "§aBem-vindo ao servidor!";
            case 2:
                return "§6Divirta-se jogando!";
            case 3:
                return "§eQue tal jogar SkyWars?";
            case 4:
                return "§bBedWars é muito divertido!";
            default:
                return "§aBem-vindo ao servidor!";
        }
    }
    
    /**
     * Converte placeholders de %...% para {...}.
     * 
     * @param text Texto com placeholders %...%
     * @return Texto com placeholders {...}
     */
    public static String convertPlaceholders(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        return text.replaceAll("%([^%]+)%", "{$1}");
    }
    
    /**
     * Processa placeholders vindas do Lobby (sintaxe %Core_...%) 
     * convertendo automaticamente para o sistema do Core.
     * 
     * @param player Jogador para processar os placeholders
     * @param text Texto contendo placeholders %Core_...%
     * @return Texto com placeholders processados
     */
    public static String setPlaceholdersFromLobby(Player player, String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // Converte %Core_...% para {Core_...}
        String convertedText = text.replaceAll("%Core_([^%]+)%", "{Core_$1}");
        
        // Processa com o sistema próprio do Core
        return setPlaceholders(player, convertedText);
    }
    
    /**
     * Método compatível com PlaceholderAPI para uso direto no Lobby.
     * Processa tanto placeholders do Core quanto placeholders básicas.
     * Este método substitui completamente o PlaceholderAPI.setPlaceholders().
     * 
     * @param player Jogador para processar os placeholders
     * @param text Texto contendo placeholders
     * @return Texto com placeholders processados
     */
    public static String setPlaceholdersCompatible(Player player, String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            return text;
        }
        
        // Processa placeholders básicas primeiro
        text = text.replace("{player}", player.getName());
        text = text.replace("%player%", player.getName());
        
        // Converte e processa placeholders do Core
        text = setPlaceholdersFromLobby(player, text);
        
        // Verificação de segurança para placeholders que podem retornar null
        if (text.contains("null")) {
            text = text.replace("null", "");
        }
        
        return text;
    }
} 