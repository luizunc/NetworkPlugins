package minecraft.core.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Configuração interna do plugin.yml
 * Contém as configurações do plugin Bukkit
 * 
 * @author Luiz
 * @version 1.0
 */
public class Plugin {
    
    // Configurações básicas do plugin
    private static final String PLUGIN_NAME = "Core";
    private static final String PLUGIN_VERSION = "0.1";
    private static final String PLUGIN_MAIN = "minecraft.core.bukkit.Core";
    private static final String PLUGIN_AUTHOR = "LuizOtimizador";
    private static final String PLUGIN_DESCRIPTION = "Plugin Core para Bukkit/Spigot";
    private static final String PLUGIN_WEBSITE = "https://github.com/LuizOtimizador";
    private static final String PLUGIN_API_VERSION = "1.13";
    
    // Dependências
    private static final List<String> PLUGIN_DEPEND = Arrays.asList("ProtocolLib", "PlaceholderAPI");
    private static final List<String> PLUGIN_SOFTDEPEND = Arrays.asList("Vault");
    
    // Configurações do comando principal
    private static final String COMMAND_CORE_DESCRIPTION = "Comando principal do Core";
    private static final String COMMAND_CORE_USAGE = "/<command> [reload|info]";
    private static final String COMMAND_CORE_PERMISSION = "core.admin";
    private static final String COMMAND_CORE_PERMISSION_MESSAGE = "§cVocê não tem permissão para usar este comando!";
    
    // Configurações de permissões
    private static final String PERMISSION_ADMIN_DESCRIPTION = "Permissão de administrador do Core";
    private static final String PERMISSION_ADMIN_DEFAULT = "op";
    private static final String PERMISSION_USER_DESCRIPTION = "Permissão de usuário do Core";
    private static final String PERMISSION_USER_DEFAULT = "true";
    
    /**
     * Obtém o nome do plugin.
     * 
     * @return Nome do plugin
     */
    public static String getPluginName() {
        return PLUGIN_NAME;
    }
    
    /**
     * Obtém a versão do plugin.
     * 
     * @return Versão do plugin
     */
    public static String getPluginVersion() {
        return PLUGIN_VERSION;
    }
    
    /**
     * Obtém a classe principal do plugin.
     * 
     * @return Classe principal
     */
    public static String getPluginMain() {
        return PLUGIN_MAIN;
    }
    
    /**
     * Obtém o autor do plugin.
     * 
     * @return Autor do plugin
     */
    public static String getPluginAuthor() {
        return PLUGIN_AUTHOR;
    }
    
    /**
     * Obtém a descrição do plugin.
     * 
     * @return Descrição do plugin
     */
    public static String getPluginDescription() {
        return PLUGIN_DESCRIPTION;
    }
    
    /**
     * Obtém o website do plugin.
     * 
     * @return Website do plugin
     */
    public static String getPluginWebsite() {
        return PLUGIN_WEBSITE;
    }
    
    /**
     * Obtém a versão da API do plugin.
     * 
     * @return Versão da API
     */
    public static String getPluginApiVersion() {
        return PLUGIN_API_VERSION;
    }
    
    /**
     * Obtém as dependências obrigatórias.
     * 
     * @return Lista de dependências obrigatórias
     */
    public static List<String> getPluginDepend() {
        return PLUGIN_DEPEND;
    }
    
    /**
     * Obtém as dependências opcionais.
     * 
     * @return Lista de dependências opcionais
     */
    public static List<String> getPluginSoftdepend() {
        return PLUGIN_SOFTDEPEND;
    }
    
    /**
     * Obtém a descrição do comando principal.
     * 
     * @return Descrição do comando
     */
    public static String getCommandCoreDescription() {
        return COMMAND_CORE_DESCRIPTION;
    }
    
    /**
     * Obtém o uso do comando principal.
     * 
     * @return Uso do comando
     */
    public static String getCommandCoreUsage() {
        return COMMAND_CORE_USAGE;
    }
    
    /**
     * Obtém a permissão do comando principal.
     * 
     * @return Permissão do comando
     */
    public static String getCommandCorePermission() {
        return COMMAND_CORE_PERMISSION;
    }
    
    /**
     * Obtém a mensagem de permissão do comando principal.
     * 
     * @return Mensagem de permissão
     */
    public static String getCommandCorePermissionMessage() {
        return COMMAND_CORE_PERMISSION_MESSAGE;
    }
    
    /**
     * Obtém a descrição da permissão de administrador.
     * 
     * @return Descrição da permissão
     */
    public static String getPermissionAdminDescription() {
        return PERMISSION_ADMIN_DESCRIPTION;
    }
    
    /**
     * Obtém o valor padrão da permissão de administrador.
     * 
     * @return Valor padrão da permissão
     */
    public static String getPermissionAdminDefault() {
        return PERMISSION_ADMIN_DEFAULT;
    }
    
    /**
     * Obtém a descrição da permissão de usuário.
     * 
     * @return Descrição da permissão
     */
    public static String getPermissionUserDescription() {
        return PERMISSION_USER_DESCRIPTION;
    }
    
    /**
     * Obtém o valor padrão da permissão de usuário.
     * 
     * @return Valor padrão da permissão
     */
    public static String getPermissionUserDefault() {
        return PERMISSION_USER_DEFAULT;
    }
    
    /**
     * Obtém a configuração do plugin.yml como FileConfiguration (método de compatibilidade).
     * 
     * @return FileConfiguration com as configurações do plugin
     */
    public static FileConfiguration getConfig() {
        try {
            String yamlContent = convertToYaml();
            InputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8));
            return YamlConfiguration.loadConfiguration(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar plugin.yml interno", e);
        }
    }
    
    /**
     * Converte as configurações Java para YAML.
     * 
     * @return String com o conteúdo YAML
     */
    private static String convertToYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("name: ").append(PLUGIN_NAME).append("\n");
        yaml.append("version: ").append(PLUGIN_VERSION).append("\n");
        yaml.append("main: ").append(PLUGIN_MAIN).append("\n");
        yaml.append("author: ").append(PLUGIN_AUTHOR).append("\n");
        yaml.append("description: ").append(PLUGIN_DESCRIPTION).append("\n");
        yaml.append("website: ").append(PLUGIN_WEBSITE).append("\n");
        yaml.append("api-version: ").append(PLUGIN_API_VERSION).append("\n");
        yaml.append("depend: [").append(String.join(", ", PLUGIN_DEPEND)).append("]\n");
        yaml.append("softdepend: [").append(String.join(", ", PLUGIN_SOFTDEPEND)).append("]\n");
        yaml.append("commands:\n");
        yaml.append("  core:\n");
        yaml.append("    description: ").append(COMMAND_CORE_DESCRIPTION).append("\n");
        yaml.append("    usage: ").append(COMMAND_CORE_USAGE).append("\n");
        yaml.append("    permission: ").append(COMMAND_CORE_PERMISSION).append("\n");
        yaml.append("    permission-message: ").append(COMMAND_CORE_PERMISSION_MESSAGE).append("\n");
        yaml.append("permissions:\n");
        yaml.append("  core.admin:\n");
        yaml.append("    description: ").append(PERMISSION_ADMIN_DESCRIPTION).append("\n");
        yaml.append("    default: ").append(PERMISSION_ADMIN_DEFAULT).append("\n");
        yaml.append("  core.user:\n");
        yaml.append("    description: ").append(PERMISSION_USER_DESCRIPTION).append("\n");
        yaml.append("    default: ").append(PERMISSION_USER_DEFAULT);
        
        return yaml.toString();
    }
    
    /**
     * Obtém o conteúdo YAML como string (método de compatibilidade).
     * 
     * @return String com o conteúdo do plugin.yml
     */
    public static String getYamlContent() {
        return convertToYaml();
    }
}
