package minecraft.core.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do plugin.yml
 * Contém as configurações do plugin Bukkit
 */
public class PluginConfig {
    
    private static final String PLUGIN_YAML_CONTENT = 
        "name: Core\n" +
        "version: 0.1\n" +
        "main: minecraft.core.bukkit.Core\n" +
        "author: LuizOtimizador\n" +
        "description: Plugin Core para Bukkit/Spigot\n" +
        "website: https://github.com/LuizOtimizador\n" +
        "api-version: 1.13\n" +
        "depend: [ProtocolLib, PlaceholderAPI]\n" +
        "softdepend: [Vault]\n" +
        "commands:\n" +
        "  core:\n" +
        "    description: Comando principal do Core\n" +
        "    usage: /<command> [reload|info]\n" +
        "    permission: core.admin\n" +
        "    permission-message: §cVocê não tem permissão para usar este comando!\n" +
        "permissions:\n" +
        "  core.admin:\n" +
        "    description: Permissão de administrador do Core\n" +
        "    default: op\n" +
        "  core.user:\n" +
        "    description: Permissão de usuário do Core\n" +
        "    default: true";
    
    /**
     * Obtém a configuração do plugin.yml como FileConfiguration
     * @return FileConfiguration com as configurações do plugin
     */
    public static FileConfiguration getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(PLUGIN_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            return YamlConfiguration.loadConfiguration(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar plugin.yml interno", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string
     * @return String com o conteúdo do plugin.yml
     */
    public static String getYamlContent() {
        return PLUGIN_YAML_CONTENT;
    }
}
