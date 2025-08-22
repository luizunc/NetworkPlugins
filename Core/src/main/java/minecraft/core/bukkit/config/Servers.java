package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuração interna do servers.yml.
 * Contém as configurações dos servidores e menu de navegação.
 * 
 * @author Luiz
 * @version 1.0
 */
public class Servers {
    
    // Configurações do menu
    private static final String MENU_TITLE = "Modos de Jogo";
    private static final int MENU_ROWS = 4;
    private static final List<Integer> DISABLED_SLOTS = Arrays.asList(10);
    
    // Configurações dos servidores
    private static final Map<String, ServerConfig> SERVERS = new HashMap<>();
    
    static {
        // Servidor Lobby Principal
        SERVERS.put("lobby", new ServerConfig(
            31,
            "BOOKSHELF : 1 : nome>&aLobby Principal : desc>&7Clique para conectar!",
            50,
            Arrays.asList("127.0.0.1:25566 ; lobby")
        ));
        
        // Servidor Sky Wars
        SERVERS.put("skywars", new ServerConfig(
            11,
            "EYE_OF_ENDER : 1 : nome>&aSky Wars : desc>&7{players} conectados.",
            50,
            Arrays.asList("127.0.0.1:25568 ; skywars")
        ));
        
        // Servidor Bed Wars
        SERVERS.put("bedwars", new ServerConfig(
            12,
            "BED : 1 : nome>&aBed Wars : desc>&7{players} conectados.",
            50,
            Arrays.asList("127.0.0.1:25567 ; bedwars")
        ));
    }
    
    /**
     * Classe interna para configuração de servidor.
     */
    public static class ServerConfig {
        private final int slot;
        private final String icon;
        private final int maxPlayers;
        private final List<String> serverNames;
        
        public ServerConfig(int slot, String icon, int maxPlayers, List<String> serverNames) {
            this.slot = slot;
            this.icon = icon;
            this.maxPlayers = maxPlayers;
            this.serverNames = serverNames;
        }
        
        public int getSlot() {
            return slot;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public int getMaxPlayers() {
            return maxPlayers;
        }
        
        public List<String> getServerNames() {
            return serverNames;
        }
    }
    
    /**
     * Obtém o título do menu.
     * 
     * @return Título do menu
     */
    public static String getMenuTitle() {
        return MENU_TITLE;
    }
    
    /**
     * Obtém o número de linhas do menu.
     * 
     * @return Número de linhas
     */
    public static int getMenuRows() {
        return MENU_ROWS;
    }
    
    /**
     * Obtém os slots desabilitados.
     * 
     * @return Lista de slots desabilitados
     */
    public static List<Integer> getDisabledSlots() {
        return DISABLED_SLOTS;
    }
    
    /**
     * Obtém a configuração de um servidor específico.
     * 
     * @param serverName Nome do servidor
     * @return Configuração do servidor ou null se não existir
     */
    public static ServerConfig getServer(String serverName) {
        return SERVERS.get(serverName);
    }
    
    /**
     * Obtém todos os servidores configurados.
     * 
     * @return Mapa com todos os servidores
     */
    public static Map<String, ServerConfig> getAllServers() {
        return new HashMap<>(SERVERS);
    }
    
    /**
     * Verifica se um servidor existe.
     * 
     * @param serverName Nome do servidor
     * @return true se o servidor existe
     */
    public static boolean hasServer(String serverName) {
        return SERVERS.containsKey(serverName);
    }
    
    /**
     * Obtém a configuração como KConfig (método de compatibilidade).
     * 
     * @return KConfig com as configurações dos servidores
     * @throws RuntimeException se houver erro ao carregar a configuração
     */
    public static KConfig getConfig() {
        try {
            // Converte as configurações Java para YAML temporariamente
            String yamlContent = convertToYaml();
            InputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configuração servers.yml interna", e);
        }
    }
    
    /**
     * Converte as configurações Java para YAML.
     * 
     * @return String com o conteúdo YAML
     */
    private static String convertToYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("# Configuração do Menu de Servidores\n");
        yaml.append("# Contém informações sobre modos de jogo e navegação\n\n");
        yaml.append("title: '").append(MENU_TITLE).append("'\n");
        yaml.append("rows: ").append(MENU_ROWS).append("\n\n");
        yaml.append("# Slots desabilitados no menu\n");
        yaml.append("disabled-slots:\n");
        for (int slot : DISABLED_SLOTS) {
            yaml.append("  - ").append(slot).append("\n");
        }
        yaml.append("\n# Itens do menu de servidores\n");
        yaml.append("items:\n");
        
        for (Map.Entry<String, ServerConfig> entry : SERVERS.entrySet()) {
            String serverName = entry.getKey();
            ServerConfig server = entry.getValue();
            
            yaml.append("  \n  # Servidor ").append(serverName.substring(0, 1).toUpperCase())
                .append(serverName.substring(1)).append("\n");
            yaml.append("  ").append(serverName).append(":\n");
            yaml.append("    slot: ").append(server.getSlot()).append("\n");
            yaml.append("    icon: '").append(server.getIcon()).append("'\n");
            yaml.append("    max-players: ").append(server.getMaxPlayers()).append("\n");
            yaml.append("    servernames:\n");
            for (String serverNameItem : server.getServerNames()) {
                yaml.append("      - '").append(serverNameItem).append("'\n");
            }
        }
        
        return yaml.toString();
    }
    
    /**
     * Obtém o conteúdo YAML como string (método de compatibilidade).
     * 
     * @return String com o conteúdo do servers.yml
     */
    public static String getYamlContent() {
        return convertToYaml();
    }
}
