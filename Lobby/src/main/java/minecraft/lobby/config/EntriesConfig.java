package minecraft.lobby.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuração dos NPCs de entrada para diferentes minigames.
 * Define os NPCs que permitem aos jogadores entrar em diferentes modos de jogo.
 */
public final class EntriesConfig {
    
    // Chaves dos minigames
    private static final String KEY_SKYWARS = "skywars";
    private static final String KEY_BEDWARS = "bedwars";
    
    // Hologramas dos minigames
    private static final List<String> HOLOGRAM_SKYWARS = Arrays.asList("&2Sky Wars", "&6{players} Conectados");
    private static final List<String> HOLOGRAM_BEDWARS = Arrays.asList("&2Bed Wars", "&6{players} Conectados");
    
    // Itens na mão dos NPCs
    private static final String HAND_SKYWARS = "EYE_OF_ENDER : 1";
    private static final String HAND_BEDWARS = "BED : 1";
    
    // Skins dos NPCs (valores e assinaturas)
    private static final String SKIN_VALUE_SKYWARS = "ewogICJ0aW1lc3RhbXAiIDogMTcxMjg1NzIyODI3NywKICAicHJvZmlsZUlkIiA6ICIyZGM2MGUzM2QyZWQ0MTVmYjczYjgxYjllZDExMmZhNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdW5hb2thbWlfQmFpemkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTMxZjg5NTg5ZDc5YmI4ZWNmMDM4MTE3ZTQ2Y2M1NzM1ODQ1N2UyNzI1MWY0YWE4MTVkOThmNDMxMmE5YjRkNiIKICAgIH0KICB9Cn0=";
    private static final String SKIN_SIGNATURE_SKYWARS = "bJ77L2wLpDLfzFlMX8A58on1av/iBBbxz6HCPHJbBqiS3Jr45iWX5/0AsjpFu5l23OsyXgqs184FBslbvXSgatYZZS7U7A0AYI2Mv+9KPGoDrvMLt6n3L+Pb/7Fr8LXMYRR/D6H1OCnMQVFaiU3ieD9WIC20M/f36LE7sToCr4oDrI8xoBhwEI0xsSWi2BCql4Ce40i/Jq7/hHWLNOyVrbyV4F4fKCKOfB1Nxn8KXj0JtCyYUWyDbS81lG97SutNVINvARweGNcaW1Atl+iCPzLnRq+hn8PCl/J3hYmmeOWGJR5emvYbrEYGrQfqt15xXXxFm2QZirgLPP+y0lNYBEr9uhSztYWPtVPmG5c+cbHb8gPX1JCIjmRQtl2Ib3G6kBcgb37zkPRLZvRfa8fUsaWLJ7IJ59NmJ5ypNP6CeQGLfvZtoEM7aEKvavrrSpCBUcCt77CSOSF81n3NjBx3Rm4+1SW1O5zbvLUFxSNFBjyCUYEloyMiOO02rl/yfrHQ5G1enr2euhObBvDEKVWwvri705srC3iTbOaQD2hB/cPbDxq7fFzFhYZ7mhZep1DYRcd1Bl5HMBwBRFy0WEdE8JU8WMim+pQ+TD+zWZwldK4ymNFno0Hr3EtGW/m28JStakoZ6UMeBWhDH4r0JRYwcmcitHwR55cYHchxWqJew10=";

    private static final String SKIN_VALUE_BEDWARS = "ewogICJ0aW1lc3RhbXAiIDogMTc0MTU1NTA5OTA0MywKICAicHJvZmlsZUlkIiA6ICJlZmI1ZWQ2YjVjOTU0ODBlYWFmMjAyZDIxOWVmNjBjNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaWtlSHdhazAwMSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zZTVlM2UwNDNkZjY0ZmQ0NTQ2YWYxNDEzYTc2MGRkMzdkMTJmZjFmNDM5MjUyOWRjMzRkMWEzZjA2YTllZWY4IgogICAgfQogIH0KfQ==";
    private static final String SKIN_SIGNATURE_BEDWARS = "WlfD32J7cp8zBVnnW77cfodPE/Wwt1bqITouZOvoL6KjwNrsNZDOV28QZ5BgfUBFf45RiFsg7N3xQjbcjAN4sYU/Cy1cxYt7TIoJZDXjtwXo6AlfuG66ZSZlO0H+ljUBdMH+xE2uWpYC1o1n/+9kRECCrt4XsZksI0C7SObxLmLIqlD6IA0OAP39Ququ9nxMjJcZ6sUEte+t9fQoFJlrMb43nEli/zPwcgi7GjXCw6fOgkQKIr0TGIML4tGD5b9HcJnncE9rhFQ5kEi/WvU2G+WDPDw2vVi/VuIFFhDhlZO742lY0UwOv+uO8p10sqb2uEvcJ39PHiDjZ+6yoo7738BSTcQ5inIMTUd0J7hVFNuWb5eotLnQYb+/puyH6+bo4V4Sc/EpOn6Cfr0xuulgLtUEP49LqijFV17ufRCqfx81YEe8k61ywB78YbX0yD4aicdN6uvmMJ2hEQqKkkOXsEEqm80MlJOl2J26etH7G+fCodOjwQ5PUuMANScYUrosdNmpO5L1bm+Fr8+HZUR5W68+QHre504FEagrTg6u1UCxyRoDbKla1QL/QixRaxyKzlN82h9UnbZ122/A0FvDXO6wWeUhrDuSX/Os97UqANo9KG4A5T/89UwsEpR5FwILomhhK5E445nfNeyj1oqsMYHO1/KiqWj7TDHY8cBe614=";
    
    private static final Map<String, EntryNPC> ENTRIES = createEntries();
    
    /**
     * Cria o mapa de NPCs de entrada.
     * @return mapa com os NPCs configurados
     */
    private static Map<String, EntryNPC> createEntries() {
        Map<String, EntryNPC> entries = new HashMap<>();
        
        // Sky Wars
        entries.put(KEY_SKYWARS, new EntryNPC(
                KEY_SKYWARS,
                HOLOGRAM_SKYWARS,
                HAND_SKYWARS,
                SKIN_VALUE_SKYWARS,
                SKIN_SIGNATURE_SKYWARS
        ));

        // Bed Wars
        entries.put(KEY_BEDWARS, new EntryNPC(
                KEY_BEDWARS,
                HOLOGRAM_BEDWARS,
                HAND_BEDWARS,
                SKIN_VALUE_BEDWARS,
                SKIN_SIGNATURE_BEDWARS
        ));
        
        return Collections.unmodifiableMap(entries);
    }
    
    /**
     * Obtém o mapa de NPCs de entrada.
     * @return mapa imutável com os NPCs
     */
    public static Map<String, EntryNPC> getEntries() {
        return ENTRIES;
    }
    
    /**
     * Obtém um NPC específico.
     * @param key chave do NPC
     * @return NPC ou null se não encontrado
     */
    public static EntryNPC getEntry(String key) {
        return ENTRIES.get(key);
    }
    
    /**
     * Representa um NPC de entrada para um minigame.
     */
    public static final class EntryNPC {
        private final String key;
        private final List<String> holograms;
        private final String hand;
        private final String skinValue;
        private final String skinSignature;
        
        /**
         * Constrói um NPC de entrada.
         * @param key chave identificadora do minigame
         * @param holograms lista de hologramas exibidos
         * @param hand item na mão do NPC
         * @param skinValue valor da skin do NPC
         * @param skinSignature assinatura da skin do NPC
         */
        public EntryNPC(String key, List<String> holograms, String hand, String skinValue, String skinSignature) {
            this.key = key;
            this.holograms = Collections.unmodifiableList(holograms);
            this.hand = hand;
            this.skinValue = skinValue;
            this.skinSignature = skinSignature;
        }
        
        /**
         * Obtém a chave do minigame.
         * @return chave identificadora
         */
        public String getKey() {
            return key;
        }
        
        /**
         * Obtém os hologramas do NPC.
         * @return lista imutável de hologramas
         */
        public List<String> getHolograms() {
            return holograms;
        }
        
        /**
         * Obtém o item na mão do NPC.
         * @return string de serialização do item
         */
        public String getHand() {
            return hand;
        }
        
        /**
         * Obtém o valor da skin do NPC.
         * @return valor da skin (Base64)
         */
        public String getSkinValue() {
            return skinValue;
        }
        
        /**
         * Obtém a assinatura da skin do NPC.
         * @return assinatura da skin
         */
        public String getSkinSignature() {
            return skinSignature;
        }
        
        @Override
        public String toString() {
            return String.format("EntryNPC{key='%s', holograms=%s, hand='%s'}", key, holograms, hand);
        }
    }
}
