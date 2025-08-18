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
    private static final List<String> HOLOGRAM_SKYWARS = Arrays.asList("&bSky Wars", "&a{players} Jogadores");
    private static final List<String> HOLOGRAM_BEDWARS = Arrays.asList("&bBed Wars", "&a{players} Jogadores");
    
    // Itens na mão dos NPCs
    private static final String HAND_SKYWARS = "GRASS : 1";
    private static final String HAND_BEDWARS = "BED : 1";
    
    // Skins dos NPCs (valores e assinaturas)
    private static final String SKIN_VALUE_SKYWARS = "eyJ0aW1lc3RhbXAiOjE1ODYzMDgwODEyNTgsInByb2ZpbGVJZCI6IjIxMWNhN2E4ZWFkYzQ5ZTVhYjBhZjMzMTBlODY0M2NjIiwicHJvZmlsZU5hbWUiOiJ3aGVyZWlzbXl3YWlmdSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QwYjg0MjJiZmJhZTVkYzk5ZGU5NTZlNzNlYWFlZmY0NTFlMGNjNjUyMGExNWQ3ZDQ5ODM1ZTNiNmZkYWZjNyJ9fX0=";
    private static final String SKIN_SIGNATURE_SKYWARS = "JI1eDa4c2Mrqb1wW/jYgZSUU8omqZntI7V+wBZz6wJwDae8MrI+reSWSucWX8Ak6dZQ+0/E3U5cFcDJmhzvnxBOIQdt6EXlw4u4u9aoIAmMNPi3HppFgViUIox6ncokeOX1tl9PJQl/pwqMTlQB24xpaySgJn0mGBHpgujaZY1/VbBnPpKUQpsOTA+iCU+JyrFOffFlLKPwbzeCtGN1k1vhJ48Y6WDdq6SPBuDHR3eS1VWWZajuC4zwcjoYDHcoGvUZSxFZV0QT6VGCNtFit0gPmtP71R+T1YREDoOYI8gSWoSRCQcCSf1JPH18BzsQW9nfS5TvkanHD0KIHs4LOKFmP090z3v44L8CjClrTRkST86zB4k64UAkGVl3BFDbxmxXdWvwfmIThoo55iC5Rbez/25JiAiKLdZLqAwOikSm24ZG6CCtg/QZs+3VnYA8wpGmbBfYTPh/mFYwAm85GRO0fdWRpVDbLphYrRAWg7brEz1of2dqRmdGFADQcfyi+Vv5IOkrOlkKQcD7jQluXVC0imtlnexzDzfmkAsutbQJQp0oQE+3hOKgrj6m+NuJUzqMTF26bHBEcG0hxrvfAGp5gN8q5+jDuGc8BkdiwKk8fr4IH/8fFk7DVGuYBAbaTCniXprq5Q5yQ32iqfk9kSutNny+LF1179FMvT7VJ0Xg=";

    private static final String SKIN_VALUE_BEDWARS = "eyJ0aW1lc3RhbXAiOjE1NzIxMjY4MDA4NjksInByb2ZpbGVJZCI6IjIxMWNhN2E4ZWFkYzQ5ZTVhYjBhZjMzMTBlODY0M2NjIiwicHJvZmlsZU5hbWUiOiJNYXh0ZWVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lZjQxZTQyMDJiOGExYTEzM2YwYmI1ZWVhYTNiMmUwNjZiYmVmMzk2ZTU5MTE1ZDg4Njk5YmUxOTU5ZGNjNzE3In19fQ==";
    private static final String SKIN_SIGNATURE_BEDWARS = "JM882jmGoqpBJ2UErWFO4GE21F1SEmfX1LJAP/xpzivl0NhERQSF3bAg/pltx71P5CccT6ue9i6ppuhyBLwugmcO5biB24W1Y3gOzPXobDJh9IzX9uV8y1glVG1VzE4n8nkUvWvPWSQQGIuWtgcWg16RGbzoOGx8vA8qhvL7nxKHrborEaqXNWGxZ2l0+Oy3NUgowAIv1D6MFHaZZWq9Cy2EUs8zm/sjREKTU+UGGvqZUvfstqAe+/2jsDNqpqgHjjKvzHBbESRjy8DaYmZc96Oi/R6SDVrlkH2lDACg+zxvrlaXnCVJiDlazA020ig64j+pAgCZv2Z6/5qjuMzwzWMNkzDmHCVOyvrNX9RTWDdiXAskEHbqUT/oBQJf4ORaX5MuOBUCbGkb2+iZk2rDdDUHNVsQazoij8vGlcMk9I3cl4uspd9MltsUBXWKmVsuc6IJWoS46CEtBB8nP/OQmM9Br8pnQ+IsXniZDXHa9UDMvgD5OHLHQoWwlSrFnVWeuYNP88UcIiNjNI93wdRRER6FumMpAOFWfDPelj4lU5t/tnW2wv7cc69pntQyco0t6diIeJ1rPydeU+iftmASVBJgapyUO684XBJIC8SvqAFbkJ6ufdDjtLqPL2OSdZRhOsXkwGtEz+RsWCOAyU9XoUevexEr+tZ0V5WTC4DdzTA=";
    
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
