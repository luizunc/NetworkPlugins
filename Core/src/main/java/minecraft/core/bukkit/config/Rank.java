package minecraft.core.bukkit.config;

import minecraft.core.bukkit.plugin.config.KConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração interna do roles.yml
 * Contém as configurações de cargos e permissões
 * 
 * @author Luiz
 * @version 1.0
 */
public class Rank {
    
    // Configurações dos cargos
    private static final Map<String, RoleConfig> ROLES = new HashMap<>();
    
    static {
        // Cargo Master
        ROLES.put("master", new RoleConfig(
            "&6Master",
            "&6[Master] ",
            "role.master",
            true
        ));
        
        // Cargo Gerente
        ROLES.put("gerente", new RoleConfig(
            "&4Gerente",
            "&4[Gerente] ",
            "role.gerente",
            true
        ));
        
        // Cargo Admin
        ROLES.put("admin", new RoleConfig(
            "&cAdmin",
            "&c[Admin] ",
            "role.admin",
            true
        ));
        
        // Cargo Moderador
        ROLES.put("moderador", new RoleConfig(
            "&2Moderador",
            "&2[Moderador] ",
            "role.moderador",
            true
        ));
        
        // Cargo Ajudante
        ROLES.put("ajudante", new RoleConfig(
            "&eAjudante",
            "&e[Ajudante] ",
            "role.ajudante",
            true
        ));
        
        // Cargo YouTuber
        ROLES.put("youtuber", new RoleConfig(
            "&cYouTuber",
            "&c[YouTuber] ",
            "role.youtuber",
            true
        ));
        
        // Cargo MVP+
        ROLES.put("mvpplus", new RoleConfig(
            "&bMVP&6+",
            "&b[MVP&6+&b] ",
            "role.mvpplus",
            false
        ));
        
        // Cargo MVP
        ROLES.put("mvp", new RoleConfig(
            "&6MVP",
            "&6[MVP] ",
            "role.mvp",
            false
        ));
        
        // Cargo VIP
        ROLES.put("vip", new RoleConfig(
            "&eVIP",
            "&e[VIP] ",
            "role.vip",
            false
        ));
        
        // Cargo Membro
        ROLES.put("membro", new RoleConfig(
            "&7Membro",
            "&7",
            "",
            false
        ));
    }
    
    /**
     * Classe interna para configuração de cargo.
     */
    public static class RoleConfig {
        private final String name;
        private final String prefix;
        private final String permission;
        private final boolean alwaysVisible;
        
        public RoleConfig(String name, String prefix, String permission, boolean alwaysVisible) {
            this.name = name;
            this.prefix = prefix;
            this.permission = permission;
            this.alwaysVisible = alwaysVisible;
        }
        
        public String getName() {
            return name;
        }
        
        public String getPrefix() {
            return prefix;
        }
        
        public String getPermission() {
            return permission;
        }
        
        public boolean isAlwaysVisible() {
            return alwaysVisible;
        }
    }
    
    /**
     * Obtém a configuração de um cargo específico.
     * 
     * @param roleName Nome do cargo
     * @return Configuração do cargo ou null se não existir
     */
    public static RoleConfig getRole(String roleName) {
        return ROLES.get(roleName);
    }
    
    /**
     * Obtém todos os cargos configurados.
     * 
     * @return Mapa com todos os cargos
     */
    public static Map<String, RoleConfig> getAllRoles() {
        return new HashMap<>(ROLES);
    }
    
    /**
     * Verifica se um cargo existe.
     * 
     * @param roleName Nome do cargo
     * @return true se o cargo existe
     */
    public static boolean hasRole(String roleName) {
        return ROLES.containsKey(roleName);
    }
    
    /**
     * Obtém o nome de um cargo.
     * 
     * @param roleName Nome do cargo
     * @return Nome formatado do cargo ou null se não existir
     */
    public static String getRoleName(String roleName) {
        RoleConfig role = ROLES.get(roleName);
        return role != null ? role.getName() : null;
    }
    
    /**
     * Obtém o prefixo de um cargo.
     * 
     * @param roleName Nome do cargo
     * @return Prefixo do cargo ou null se não existir
     */
    public static String getRolePrefix(String roleName) {
        RoleConfig role = ROLES.get(roleName);
        return role != null ? role.getPrefix() : null;
    }
    
    /**
     * Obtém a permissão de um cargo.
     * 
     * @param roleName Nome do cargo
     * @return Permissão do cargo ou null se não existir
     */
    public static String getRolePermission(String roleName) {
        RoleConfig role = ROLES.get(roleName);
        return role != null ? role.getPermission() : null;
    }
    
    /**
     * Verifica se um cargo é sempre visível.
     * 
     * @param roleName Nome do cargo
     * @return true se o cargo é sempre visível
     */
    public static boolean isRoleAlwaysVisible(String roleName) {
        RoleConfig role = ROLES.get(roleName);
        return role != null && role.isAlwaysVisible();
    }
    
    /**
     * Obtém a configuração do roles.yml como KConfig (método de compatibilidade).
     * 
     * @return KConfig com as configurações dos roles
     */
    public static KConfig getConfig() {
        try {
            String yamlContent = convertToYaml();
            InputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar roles.yml interno", e);
        }
    }
    
    /**
     * Converte as configurações Java para YAML.
     * 
     * @return String com o conteúdo YAML
     */
    private static String convertToYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("roles:\n");
        
        for (Map.Entry<String, RoleConfig> entry : ROLES.entrySet()) {
            String roleName = entry.getKey();
            RoleConfig role = entry.getValue();
            
            yaml.append("  \n");
            yaml.append("  ").append(roleName).append(":\n");
            yaml.append("    name: '").append(role.getName()).append("'\n");
            yaml.append("    prefix: '").append(role.getPrefix()).append("'\n");
            yaml.append("    permission: '").append(role.getPermission()).append("'\n");
            yaml.append("    alwaysvisible: ").append(role.isAlwaysVisible()).append("\n");
        }
        
        return yaml.toString();
    }
    
    /**
     * Obtém o conteúdo YAML como string (método de compatibilidade).
     * 
     * @return String com o conteúdo do roles.yml
     */
    public static String getYamlContent() {
        return convertToYaml();
    }
}
