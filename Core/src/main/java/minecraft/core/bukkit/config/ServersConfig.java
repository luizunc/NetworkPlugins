package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do servers.yml
 * Contém as configurações dos servidores e menu
 */
public class ServersConfig {
    
    private static final String SERVERS_YAML_CONTENT = 
        "title: 'Modos de Jogo'\n" +
        "rows: 3\n" +
        "\n" +
        "disabled-slots: \n" +
        "  - 10\n" +
        "\n" +
        "items:\n" +
        "  \n" +
        "  lobby:\n" +
        "    slot: 10\n" +
        "    icon: 'BOOKSHELF : 1 : nome>&6Lobby Principal : desc>&e► Clique para conectar!'\n" +
        "    max-players: 50\n" +
        "    servernames:\n" +
        "      - '127.0.0.1:25565 ; lobby'\n" +
        "  \n" +
        "  skywars:\n" +
        "    slot: 12\n" +
        "    icon: 'GRASS : 1 : nome>&aSky Wars : desc>&8Sobrevivência\\n \\n  &7Você tem medo de altura? Então este\\n  &7jogo não é para você! No Sky Wars, você\\n  &7deverá eliminar os seus adversários com\\n  &7a ajuda dos diversos Kits e Habilidades.\\n \\n  &7▪ &fSolo\\n  &7▪ &fDupla\\n \\n&e► Clique para conectar!\\n&7{players} jogando.'\n" +
        "    max-players: 50\n" +
        "    servernames:\n" +
        "      - '127.0.0.1:25566 ; skywars'\n" +
        "  \n" +

        "  bedwars:\n" +
        "    slot: 15\n" +
        "    icon: 'BED : 1 : nome>&aBed Wars : desc>&8Sobrevivência em Equipe\\n \\n  &7Projeta sua cama com sua equipe\\n  &7e destrua a cama do inimigo para\\n  &7vencer!\\n \\n  &7▪ &fDupla\\n  &7▪ &fQuarteto\\n \\n&e► Clique para conectar!\\n&7{players} jogando.'\n" +
        "    max-players: 50\n" +
        "    servernames:\n" +
        "      - '127.0.0.1:25569 ; bedwars'";
    
    /**
     * Obtém a configuração do servers.yml como KConfig
     * @return KConfig com as configurações dos servidores
     */
    public static KConfig getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(SERVERS_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar servers.yml interno", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string
     * @return String com o conteúdo do servers.yml
     */
    public static String getYamlContent() {
        return SERVERS_YAML_CONTENT;
    }
}
