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
 * Configuração interna do rank.yml
 * Contém as configurações de cargos e permissões
 * 
 * @author Luiz
 * @version 1.0
 */
public class Rank {
    
    // Configurações dos cargos
    private static final Map<String, rankConfig> rank = new HashMap<>();
    
    static {
        // Cargo Admin
        rank.put("admin", new rankConfig(
            "&4ADMIN",
            "&4&lADMIN &4",
            "rank.admin",
            true
        ));
        
        // Cargo Moderador
        rank.put("mod", new rankConfig(
            "&5MOD",
            "&5&lMOD &5",
            "rank.mod",
            true
        ));
        
        // Cargo Trial
        rank.put("trial", new rankConfig(
            "&5TRIAL",
            "&5&lTRIAL &5",
            "rank.trial",
            true
        ));
        
        // Cargo Staff
        rank.put("staff", new rankConfig(
            "&9STAFF",
            "&9&lSTAFF &9",
            "rank.staff",
            true
        ));
        
        // Cargo Builder
        rank.put("builder", new rankConfig(
            "&3BUILDER",
            "&3&lBUILDER &3",
            "rank.builder",
            true
        ));
        
        // Cargo Creator
        rank.put("creator", new rankConfig(
            "&bCREATOR",
            "&b&lCREATOR &b",
            "rank.creator",
            true
        ));
        
        // Cargo Emerald
        rank.put("emerald", new rankConfig(
            "&2EMERALD",
            "&2&lEMERALD &2",
            "rank.emerald",
            false
        ));
        
        // Cargo Gold
        rank.put("gold", new rankConfig(
            "&6GOLD",
            "&6&lGOLD &6",
            "rank.gold",
            false
        ));
        
        // Cargo Iron
        rank.put("iron", new rankConfig(
            "&fIRON",
            "&f&lIRON &f",
            "rank.iron",
            false
        ));
        
        // Cargo Membro
        rank.put("membro", new rankConfig(
            "&7Membro",
            "&7",
            "",
            false
        ));
    }
    
    /**
     * Classe interna para configuração de cargo.
     */
    public static class rankConfig {
        private final String name;
        private final String prefix;
        private final String permission;
        private final boolean alwaysVisible;
        
        public rankConfig(String name, String prefix, String permission, boolean alwaysVisible) {
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
     * @param rankName Nome do cargo
     * @return Configuração do cargo ou null se não existir
     */
    public static rankConfig getrank(String rankName) {
        return rank.get(rankName);
    }
    
    /**
     * Obtém todos os cargos configurados.
     * 
     * @return Mapa com todos os cargos
     */
    public static Map<String, rankConfig> getAllrank() {
        return new HashMap<>(rank);
    }
    
    /**
     * Verifica se um cargo existe.
     * 
     * @param rankName Nome do cargo
     * @return true se o cargo existe
     */
    public static boolean hasrank(String rankName) {
        return rank.containsKey(rankName);
    }
    
    /**
     * Obtém o nome de um cargo.
     * 
     * @param rankName Nome do cargo
     * @return Nome formatado do cargo ou null se não existir
     */
    public static String getrankName(String rankName) {
        rankConfig rankConfig = rank.get(rankName);
        return rankConfig != null ? rankConfig.getName() : null;
    }
    
    /**
     * Obtém o prefixo de um cargo.
     * 
     * @param rankName Nome do cargo
     * @return Prefixo do cargo ou null se não existir
     */
    public static String getrankPrefix(String rankName) {
        rankConfig rankConfig = rank.get(rankName);
        return rankConfig != null ? rankConfig.getPrefix() : null;
    }
    
    /**
     * Obtém a permissão de um cargo.
     * 
     * @param rankName Nome do cargo
     * @return Permissão do cargo ou null se não existir
     */
    public static String getrankPermission(String rankName) {
        rankConfig rankConfig = rank.get(rankName);
        return rankConfig != null ? rankConfig.getPermission() : null;
    }
    
    /**
     * Verifica se um cargo é sempre visível.
     * 
     * @param rankName Nome do cargo
     * @return true se o cargo é sempre visível
     */
    public static boolean isrankAlwaysVisible(String rankName) {
        rankConfig rankConfig = rank.get(rankName);
        return rankConfig != null && rankConfig.isAlwaysVisible();
    }
    
    /**
     * Obtém a configuração do rank.yml como KConfig (método de compatibilidade).
     * 
     * @return KConfig com as configurações dos rank
     */
    public static KConfig getConfig() {
        try {
            String yamlContent = convertToYaml();
            InputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8));
            FileConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            return new KConfig(config);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar rank.yml interno", e);
        }
    }
    
    /**
     * Converte as configurações Java para YAML.
     * 
     * @return String com o conteúdo YAML
     */
    private static String convertToYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("rank:\n");
        
        for (Map.Entry<String, rankConfig> entry : rank.entrySet()) {
            String rankName = entry.getKey();
            rankConfig rankConfig = entry.getValue();
            
            yaml.append("  \n");
            yaml.append("  ").append(rankName).append(":\n");
            yaml.append("    name: '").append(rankConfig.getName()).append("'\n");
            yaml.append("    prefix: '").append(rankConfig.getPrefix()).append("'\n");
            yaml.append("    permission: '").append(rankConfig.getPermission()).append("'\n");
            yaml.append("    alwaysvisible: ").append(rankConfig.isAlwaysVisible()).append("\n");
        }
        
        return yaml.toString();
    }
    
    /**
     * Obtém o conteúdo YAML como string (método de compatibilidade).
     * 
     * @return String com o conteúdo do rank.yml
     */
    public static String getYamlContent() {
        return convertToYaml();
    }
}
