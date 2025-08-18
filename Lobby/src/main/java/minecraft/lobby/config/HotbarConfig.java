package minecraft.lobby.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração dos itens da hotbar do lobby.
 * Define os itens que aparecem na barra de ferramentas dos jogadores.
 */
public final class HotbarConfig {
    
    // Slots da hotbar
    private static final int SLOT_BUSSOLA = 1;
    private static final int SLOT_PERFIL = 2;
    private static final int SLOT_JOGADORES = 8;
    private static final int SLOT_LOBBIES = 9;
    
    // Chaves dos itens
    private static final String KEY_BUSSOLA = "bussola";
    private static final String KEY_PERFIL = "perfil";
    private static final String KEY_JOGADORES = "jogadores";
    private static final String KEY_LOBBIES = "lobbies";
    
    // Comandos de execução
    private static final String CMD_JOGOS = "CORE>jogos";
    private static final String CMD_PERFIL = "CORE>perfil";
    private static final String CMD_JOGADORES = "CORE>jogadores";
    private static final String CMD_LOBBIES = "LOBBY>lobbies";
    
    // Ícones dos itens
    private static final String ICON_BUSSOLA = "COMPASS : 1 : nome>&eMinigames : desc>&7Sabemos que é uma difícil escolha...";
    private static final String ICON_PERFIL = "%perfil%SKULL_ITEM:3 : 1 : nome>&eMeu Perfil : desc>&7Clique para ver suas\n&7informações pessoais!";
    private static final String ICON_JOGADORES = "INK_SACK:%Core_status_jogadores_inksack% : 1 : nome>&fJogadores: %Core_status_jogadores_nome% : desc>&7Clique para que os jogadores\n&7desapareçam!";
    private static final String ICON_LOBBIES = "NETHER_STAR : 1 : nome>&eLobbies : desc>&7Clique para selecionar um lobby!";
    
    private static final Map<String, HotbarItem> LOBBY_ITEMS = createLobbyItems();
    
    /**
     * Cria o mapa de itens da hotbar.
     * @return mapa com os itens configurados
     */
    private static Map<String, HotbarItem> createLobbyItems() {
        Map<String, HotbarItem> items = new HashMap<>();
        
        // Bússola (Minigames)
        items.put(KEY_BUSSOLA, new HotbarItem(SLOT_BUSSOLA, CMD_JOGOS, ICON_BUSSOLA));
        
        // Perfil do jogador
        items.put(KEY_PERFIL, new HotbarItem(SLOT_PERFIL, CMD_PERFIL, ICON_PERFIL));
        
        // Visibilidade dos jogadores
        items.put(KEY_JOGADORES, new HotbarItem(SLOT_JOGADORES, CMD_JOGADORES, ICON_JOGADORES));
        
        // Menu de lobbies
        items.put(KEY_LOBBIES, new HotbarItem(SLOT_LOBBIES, CMD_LOBBIES, ICON_LOBBIES));
        
        return Collections.unmodifiableMap(items);
    }
    
    /**
     * Obtém o mapa de itens da hotbar.
     * @return mapa imutável com os itens da hotbar
     */
    public static Map<String, HotbarItem> getLobbyItems() {
        return LOBBY_ITEMS;
    }
    
    /**
     * Obtém um item específico da hotbar.
     * @param key chave do item
     * @return item da hotbar ou null se não encontrado
     */
    public static HotbarItem getItem(String key) {
        return LOBBY_ITEMS.get(key);
    }
    
    /**
     * Representa um item da hotbar.
     */
    public static final class HotbarItem {
        private final int slot;
        private final String execute;
        private final String icon;
        
        /**
         * Constrói um item da hotbar.
         * @param slot posição do item na hotbar (0-8)
         * @param execute comando a ser executado ao clicar
         * @param icon string de serialização do ícone
         */
        public HotbarItem(int slot, String execute, String icon) {
            this.slot = slot;
            this.execute = execute;
            this.icon = icon;
        }
        
        /**
         * Obtém a posição do item na hotbar.
         * @return slot do item (0-8)
         */
        public int getSlot() {
            return slot;
        }
        
        /**
         * Obtém o comando a ser executado.
         * @return comando de execução
         */
        public String getExecute() {
            return execute;
        }
        
        /**
         * Obtém a string de serialização do ícone.
         * @return string do ícone
         */
        public String getIcon() {
            return icon;
        }
        
        @Override
        public String toString() {
            return String.format("HotbarItem{slot=%d, execute='%s', icon='%s'}", slot, execute, icon);
        }
    }
}
