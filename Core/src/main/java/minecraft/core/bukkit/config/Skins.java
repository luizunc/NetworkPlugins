package minecraft.core.bukkit.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuração das skins disponíveis na biblioteca.
 * Contém a estante de skins prontas do servidor.
 * 
 * @author Luiz
 * @version 3.1
 */
public class Skins {
    
    // Biblioteca de skins disponíveis
    // Formato: "NomeSkin" -> "NomeSkin:Value:Signature"
    public static Map<String, String> LIBRARY = new HashMap<>();
    
    static {
        // Inicializa as skins personalizadas
        initializeCustomSkins();
    }
    
    /**
     * Inicializa skins personalizadas com value e signature específicos.
     */
    private static void initializeCustomSkins() {
        
        // =========================================================
        // ADICIONE SUAS SKINS PERSONALIZADAS AQUI!
        // =========================================================
        // Use o método addSkin() para adicionar cada skin:
        // addSkin("Nome da Skin", "Value da Skin", "Signature da Skin");
        
        // Exemplos das suas skins:
        addSkin("Quadrado",
            "ewogICJ0aW1lc3RhbXAiIDogMTYzMjYzODE2NjA4OCwKICAicHJvZmlsZUlkIiA6ICI2NDU4Mjc0MjEyNDg0MDY0YTRkMDBlNDdjZWM4ZjcyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaDNtMXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgwNTUzODU1MjAzZGQ0MTY0MjM5MGYxMWE4MGZjMjJkYjA5MDdmNGRiMTU2OTRlOWUxZTkwYTA5MmU4OGU2MiIKICAgIH0KICB9Cn0=",
            "LkwPFyePt5+0A0JWtYzRQKIq3PSn181EW8eU8b9QQu9bDM4OmfTanVCoBVNsqkIMploHupwnCHirMeX8fHtAkNHZOS2Ub3FncCjWYiMsXrQsHs53mp440G7nfjCQNKQDGFYmHuyZW2OznOWfKSqnqSFZpCjyjfDSyb1qijCr6P9X95nM+uwiTCXiOU5IfFOffPBhCqH1+HI7nNMKDpGryaPXQbC3iZN/pqB7HqOXFbbDekDjeCdAMxa4U4a4zKC043FxqUqLEmu/xXE7adf94ciW/Fco98AtYKtDKs2V06oK+zcn136KiiVMAC8UeQruFzV1XZsFDHPzmYlEav3vTPzVVCByQxHD9q5Bku+aNXrx8bw+rC+8VhK25ZM9iUkDzeP3UJ8VZ6ncLpvN+Hl0F0tXmtOVoeuiyjV45nIsuHu437ECx6Xifvdv8wCzibpGdEYekP23nWE0Enczq8f7N/8ZecwYkTzE0owz7vF2hvOSwOLhH+2E2oZv00iW2GbEshl7Q628uCaSeL4wb8E69VCsSfzv7TdpFcf5J8wIbSu5OJtf8p2W5/FXcZbGxf5Xt/DUwMLRCMXGq9+JKl/Kp43wahwXv1epljnI/h4tCDO2OkAPNLt+c5KEGNkaJyLq8a6iKqxyh5onhELLjgrGyBgzjeH+Oc7/llPV6Dm7PcY=");
            
        addSkin("Triângulo", 
             "ewogICJ0aW1lc3RhbXAiIDogMTYzMjY2NTEwODI4MiwKICAicHJvZmlsZUlkIiA6ICIxZjEyNTNhYTVkYTQ0ZjU5YWU1YWI1NmFhZjRlNTYxNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RNaUt5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE1YzVhMDFjY2RjZTkxNTE5MDQ0ZGIwNWRjNmMwYmY4YzE0ZWM2YmI5NGJmZTU3Yzk1MGYzOWIxMWM2ZmQwNGQiCiAgICB9CiAgfQp9", 
             "ZD3GxVdEzNn/VS9yPttK+kfGkgsdTmScHpxCjHkWyLBATGr6YO3nILoz51a/zOXcOeuAKyIGJDrh7F4IjZT5xoMcghw9T7BwCakI7weUS2WThjHkZeo3/91rkc3eICXCEMsP2p3iktOPstiK0NbLQC7vs2L+PzURHOSpb8kjwgbRzkejOgiKXltXLvZcpHZahkZZAZnziUed966NQemQ57TQ3Rm5YnRk22J2ibxNgU2I3X/rREgEMVFY3lvqDKp1snYCMtp/f4Fvxz+FNVGj241D/F2RxlO5RoeAWtlUTO/YDwrpLdnkVpuWIzLcm1dD9J/5ANS9D3HLmighiDcm0vybuMj7QmNMFEsjjfCD1skHA8MerBSy7k7K9eG4sFYu3PUl/ZdubQcn7OrMU1L+aOn+d9WVTMVuJbnrPQ0cSzdGBa1knwRfPYpiVG6LYLbi6t/woYwTBsRaCy+BZlYwflcDIYAX4Py9nE/48z1t9CrVyunJ9vA1m14awOzTHDri+zxY90NWoYo/5TGsRR7jNpihzG8h8yS0JChImccvFqhbGPHGqFL28kbTcj7xOZi0GetvclCaVcIrsWWiYgpe1St2Y7pAWUsHbg0Tr39sIfSn3Krlm3NDSQLPzEiGaiwU9sSoT8hfKCbFXFnSazZLDm+E7yTw9CIOlzzX2xm8iYs=");
        
        addSkin("Redondo",
             "ewogICJ0aW1lc3RhbXAiIDogMTYzMzEzMDUwNjg3NywKICAicHJvZmlsZUlkIiA6ICJkMWY2OTc0YzE2ZmI0ZjdhYjI1NjU4NzExNjM3M2U2NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaW9saWVzdGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc2MzU2YTliODMwMjExZjM1NmVmNzhkZTE4NDc5ZTkxYzhlZmQ1MTE4MGEyNjE0ZDU3MmFlYzE2MGQzYzM1MSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
             "rcJMBm4EGj6dDlMbXXkyn41o+7Lppofo1T5hRWctdVCJw9t7HlHe2maXtc8QktF8Yb1ycOCij2i8MYPcbKgVxnzA9B4EctIE/k5WNuawprOdJttks4PX4j0fSgwyMmWzeTzgsFNT7mKrUEsX7F/4bwTyaUSTpwFtm+0yZIoLqZOg9Zcsg5lYCUiVLG9fjeYgumk0uHrz1eSD1uZJYU5Dr1lQMqrmDLXi5cS4o616zGmhBJrqxVdl9G3NGgcVVqzW4+BC/YtVt3d1mnvGGjpcfyS6TxbjWvyJ6BQEsQ3kHPr1DUdO+Q+q5jMAwQpBSxHvtDlzZMAf/+fmB1M0Di29WjGfhOTspmQb5zQYDoW4bUv4/v8t90KL+sk03MAqCx/05yoqRikxUE9bNs7eOOjDKUD5GhLgUEPTOHTsUe0JoLphXHAaz0q2RH5D02BXKN95m27+BckvnYL0sA+dSXggS4QYxIFvVDKYLJXtuxsvzOhxSJg4TEwhXXg7aR50+LsyHbqxT5EW0Teb/dnrncJEhvSeUt97QOvEC+IOxKXyWZQljK38KRfN64H+XfYFvrLXqvsD0ArUCy388WIF0m57K9apRMt3HYm4QMuRmqr7sPNYBp2GuGguA9wItqhqVGAH9AdgvR41hB3p1mSnWiEQ9b/J0v1V4QoIZxCX24HMeRY=");
        
    }
    
    /**
     * Adiciona uma skin personalizada à biblioteca.
     * 
     * @param skinName Nome da skin que aparecerá na estante
     * @param value Value da skin (dados Base64)
     * @param signature Signature da skin (assinatura RSA)
     */
    private static void addSkin(String skinName, String value, String signature) {
        if (skinName != null && !skinName.isEmpty() && 
            value != null && !value.isEmpty() && 
            signature != null && !signature.isEmpty()) {
            
            // Formato simplificado: "NomeSkin:Value:Signature"
            String skinData = skinName + ":" + value + ":" + signature;
            LIBRARY.put(skinName, skinData);
        }
    }
    
    /**
     * Obtém a biblioteca de skins.
     * 
     * @return Mapa com todas as skins disponíveis
     */
    public static Map<String, String> getLibrary() {
        return new HashMap<>(LIBRARY);
    }
    
    /**
     * Verifica se uma skin existe na biblioteca.
     * 
     * @param skinName Nome da skin
     * @return true se a skin existe
     */
    public static boolean hasSkin(String skinName) {
        return LIBRARY.containsKey(skinName);
    }
    
    /**
     * Obtém os dados de uma skin específica.
     * 
     * @param skinName Nome da skin
     * @return Dados da skin ou null se não existir
     */
    public static String getSkinData(String skinName) {
        return LIBRARY.get(skinName);
    }
    
    /**
     * Obtém o value de uma skin específica.
     * 
     * @param skinName Nome da skin
     * @return Value da skin ou null se não existir
     */
    public static String getSkinValue(String skinName) {
        String skinData = LIBRARY.get(skinName);
        if (skinData != null) {
            String[] parts = skinData.split(":");
            if (parts.length >= 2) {
                return parts[1]; // Retorna o value (posição 1)
            }
        }
        return null;
    }
    
    /**
     * Obtém a signature de uma skin específica.
     * 
     * @param skinName Nome da skin
     * @return Signature da skin ou null se não existir
     */
    public static String getSkinSignature(String skinName) {
        String skinData = LIBRARY.get(skinName);
        if (skinData != null) {
            String[] parts = skinData.split(":");
            if (parts.length >= 3) {
                return parts[2]; // Retorna a signature (posição 2)
            }
        }
        return null;
    }
} 