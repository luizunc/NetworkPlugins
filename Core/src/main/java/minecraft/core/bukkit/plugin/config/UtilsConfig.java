package minecraft.core.bukkit.plugin.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna das configurações utils
 * Contém as configurações de cash, fake e party
 */
public class UtilsConfig {
    
    private static final String UTILS_YAML_CONTENT = 
        "cash:\n" +
        "  enabled: true\n" +
        "  symbol: '$\'\n" +
        "  format: \"&a{amount}\"\n" +
        "  \n" +
        "fake:\n" +
        "  enabled: true\n" +
        "  rank:\n" +
        "    - \"&7Membro\"\n" +
        "  \n" +
        "party:\n" +
        "  enabled: true\n" +
        "  max-size: 5\n" +
        "  \n" +
        "  commands:\n" +
        "    create: 'party create'\n" +
        "    invite: 'party invite'\n" +
        "    accept: 'party accept'\n" +
        "    leave: 'party leave'\n" +
        "    kick: 'party kick'\n" +
        "    disband: 'party disband'\n" +
        "    chat: 'party chat'\n" +
        "    list: 'party list'\n" +
        "    \n" +
        "  messages:\n" +
        "    created: \"&aParty criada com sucesso!\"\n" +
        "    invited: \"&aVocê foi convidado para uma party!\"\n" +
        "    joined: \"&aVocê entrou na party!\"\n" +
        "    left: \"&cVocê saiu da party!\"\n" +
        "    kicked: \"&cVocê foi expulso da party!\"\n" +
        "    disbanded: \"&cParty foi desfeita!\"\n" +
        "    full: \"&cParty está cheia!\"\n" +
        "    not-in-party: \"&cVocê não está em uma party!\"\n" +
        "    already-in-party: \"&cVocê já está em uma party!\"\n" +
        "    player-not-found: \"&cJogador não encontrado!\"\n" +
        "    no-permission: \"&cVocê não tem permissão!\"\n" +
        "    \n" +
        "  format:\n" +
        "    prefix: \"&7[&bParty&7] \"\n" +
        "    chat: \"&7[&bParty&7] &f{player}: &7{message}\"\n" +
        "    list: \"&7- &f{player} &7({rank})\"\n" +
        "    \n" +
        "  ranks:\n" +
        "    leader: \"&cLíder\"\n" +
        "    member: \"&7Membro\"";
    
    /**
     * Obtém a configuração utils como KConfig
     * @return KConfig com as configurações do utils
     */
    public static KConfig getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(UTILS_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações utils internas", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string
     * @return String com o conteúdo das configurações utils
     */
    public static String getYamlContent() {
        return UTILS_YAML_CONTENT;
    }
}
