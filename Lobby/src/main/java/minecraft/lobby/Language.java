package minecraft.lobby;

import minecraft.core.bukkit.plugin.logger.KLogger;

import java.util.Arrays;
import java.util.List;

/**
 * Gerencia as mensagens e configurações de idioma do plugin.
 * Todas as configurações estão hardcoded nas variáveis estáticas.
 */
public final class Language {
    
    private static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger())
            .getModule("LANGUAGE");
    
    // Configurações do scoreboard
    public static final long scoreboards$scroller$every_tick = 1;
    public static final List<String> scoreboards$scroller$titles = Arrays
            .asList("§a§lHYPE MC", "§f§l§6§lH§a§lYPE MC", "§f§lH§6§lY§a§lPE MC",
                    "§f§lHY§6§lP§a§lE MC", "§f§lHYP§6§lE §a§lMC", "§f§lHYPE §6§lM§a§lC",
                    "§f§lHYPE M§6§lC", "§f§lHYPE MC", "§f§lHYPE MC",
                    "§f§lHYPE MC", "§f§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC",
                    "§f§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC",
                    "§f§lHYPE MC", "§f§lHYPE MC", "§f§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC",
                    "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC", "§a§lHYPE MC");
    
    public static final List<String> scoreboards$lobby = Arrays
            .asList("", "  Grupo: §a%Core_rank%", "  Cash: §b%Core_cash%", "", "  Jogadores: §a%Core_online%",
                    "", "  §7www.hypemc.com", "");
    
    // Configurações do chat
    public static final String chat$delay = "§cAguarde mais {time}s para falar novamente.";
    public static final String chat$color$default = "§7";
    public static final String chat$color$custom = "§f";
    public static final String chat$format$lobby = "{player}{color}: {message}";
    
    // Configurações do lobby
    public static final String lobby$broadcast = "{player} " + "%Core_entrymessage%";
    public static final boolean lobby$tab$enabled = true;
    public static final String lobby$tab$header = " \n§b§lHYPE MC\n  §fwww.hypemc.com\n ";
    public static final String lobby$tab$footer =
            " \n \n§aForúm: §fhypemc.com/forum\n§aTwitter: §f@HypeMC\n§aDiscord: §fhypemc.com/discord\n \n                                          §bAdquira VIP acessando: §floja.hypemc.com                                          \n ";
    
    /**
     * Configura o sistema de idioma do plugin.
     * Todas as configurações estão hardcoded nas variáveis estáticas.
     */
    public static void setupLanguage() {
        LOGGER.info("Sistema de idioma configurado com sucesso.");
    }
}
