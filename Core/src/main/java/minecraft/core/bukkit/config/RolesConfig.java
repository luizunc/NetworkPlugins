package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Configuração interna do roles.yml
 * Contém as configurações de cargos e permissões
 */
public class RolesConfig {
    
    private static final String ROLES_YAML_CONTENT = 
        "roles:\n" +
        "  \n" +
        "  master:\n" +
        "    name: '&6Master'\n" +
        "    prefix: '&6[Master] '\n" +
        "    permission: 'role.master'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  gerente:\n" +
        "    name: '&4Gerente'\n" +
        "    prefix: '&4[Gerente] '\n" +
        "    permission: 'role.gerente'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  admin:\n" +
        "    name: '&cAdmin'\n" +
        "    prefix: '&c[Admin] '\n" +
        "    permission: 'role.admin'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  moderador:\n" +
        "    name: '&2Moderador'\n" +
        "    prefix: '&2[Moderador] '\n" +
        "    permission: 'role.moderador'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  ajudante:\n" +
        "    name: '&eAjudante'\n" +
        "    prefix: '&e[Ajudante] '\n" +
        "    permission: 'role.ajudante'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  youtuber:\n" +
        "    name: '&cYouTuber'\n" +
        "    prefix: '&c[YouTuber] '\n" +
        "    permission: 'role.youtuber'\n" +
        "    alwaysvisible: true\n" +
        "  \n" +
        "  mvpplus:\n" +
        "    name: '&bMVP&6+'\n" +
        "    prefix: '&b[MVP&6+&b] '\n" +
        "    permission: 'role.mvpplus'\n" +
        "  \n" +
        "  mvp:\n" +
        "    name: '&6MVP'\n" +
        "    prefix: '&6[MVP] '\n" +
        "    permission: 'role.mvp'\n" +
        "  \n" +
        "  vip:\n" +
        "    name: '&eVIP'\n" +
        "    prefix: '&e[VIP] '\n" +
        "    permission: 'role.vip'\n" +
        "  \n" +
        "  membro:\n" +
        "    name: '&7Membro'\n" +
        "    prefix: '&7'\n" +
        "    permission: ''\n" +
        "    broadcast: false";
    
    /**
     * Obtém a configuração do roles.yml como KConfig
     * @return KConfig com as configurações dos roles
     */
    public static KConfig getConfig() {
        try {
            InputStream inputStream = new ByteArrayInputStream(ROLES_YAML_CONTENT.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar roles.yml interno", e);
        }
    }
    
    /**
     * Obtém o conteúdo YAML como string
     * @return String com o conteúdo do roles.yml
     */
    public static String getYamlContent() {
        return ROLES_YAML_CONTENT;
    }
}
