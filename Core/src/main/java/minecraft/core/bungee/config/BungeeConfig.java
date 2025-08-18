package minecraft.core.bungee.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do bungee.yml
 * Contém as configurações do plugin BungeeCord
 */
public class BungeeConfig {
    
    private static final String BUNGEE_YAML_CONTENT = 
        "author: LuizOtimizador\n" +
        "name: Core\n" +
        "version: 0.1\n" +
        "\n" +
        "main: minecraft.core.bungee.Bungee";
    
    /**
     * Obtém a configuração do bungee.yml
     * @return Configuration com as configurações do bungee
     */
    public static Configuration getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(BUNGEE_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar bungee.yml interno", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string
     * @return String com o conteúdo do bungee.yml
     */
    public static String getYamlContent() {
        return BUNGEE_YAML_CONTENT;
    }
}
