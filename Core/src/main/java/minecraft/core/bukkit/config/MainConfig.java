package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do config.yml.
 * Contém as configurações principais do plugin com valores padrão.
 */
public final class MainConfig {
    
    // Conteúdo padrão do config.yml
    private static final String CONFIG_YAML_CONTENT = 
        "database:\n" +
        "  enabled: true\n" +
        "  tipo: 'mysql'\n" +
        "  mysql:\n" +
        "    host: 'localhost'\n" +
        "    porta: '3306'\n" +
        "    nome: 'minecraft'\n" +
        "    usuario: 'root'\n" +
        "    senha: ''\n" +
        "    hikari: true\n" +
        "    mariadb: false\n" +
        "  mongodb:\n" +
        "    url: 'mongodb://localhost:27017/minecraft'\n" +
        "\n" +
        "lobby:\n" +
        "  world: 'world'\n" +
        "  x: 0.0\n" +
        "  y: 64.0\n" +
        "  z: 0.0\n" +
        "  yaw: 0.0\n" +
        "  pitch: 0.0\n" +
        "\n" +
        "messages:\n" +
        "  prefix: '&7[&bCore&7] '\n" +
        "  welcome: '&aBem-vindo ao servidor!'\n" +
        "  goodbye: '&cAté logo!'\n" +
        "  no-permission: '&cVocê não tem permissão!'\n" +
        "  player-only: '&cEste comando só pode ser usado por jogadores!'\n" +
        "  reload: '&aPlugin recarregado com sucesso!'\n" +
        "  error: '&cOcorreu um erro!'\n" +
        "\n" +
        "settings:\n" +
        "  debug: false\n" +
        "  auto-update: true\n" +
        "  save-interval: 300\n" +
        "  max-players: 100\n" +
        "  motd: '&aBem-vindo ao servidor!'\n" +
        "  \n" +
        "  features:\n" +
        "    fake-names: true\n" +
        "    fake-skins: true\n" +
        "    party-system: true\n" +
        "    achievements: true\n" +
        "    titles: true\n" +
        "    hotbar: true\n" +
        "    scoreboard: true\n" +
        "    \n" +
        "  performance:\n" +
        "    async-save: true\n" +
        "    cache-size: 1000\n" +
        "    connection-pool: 10\n" +
        "    timeout: 30";
    
    /**
     * Construtor privado para evitar instanciação.
     */
    private MainConfig() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
    }
    
    /**
     * Obtém a configuração do config.yml como KConfig.
     * 
     * @return KConfig com as configurações principais
     * @throws RuntimeException Se houver erro ao carregar a configuração
     */
    public static KConfig getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(CONFIG_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar config.yml interno", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string.
     * 
     * @return String com o conteúdo do config.yml
     */
    public static String getYamlContent() {
        return CONFIG_YAML_CONTENT;
    }
}
