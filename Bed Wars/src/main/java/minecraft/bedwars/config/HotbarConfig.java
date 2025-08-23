package minecraft.bedwars.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração dos itens da hotbar do BedWars.
 * Define os itens que aparecem na barra de ferramentas dos jogadores.
 */
public final class HotbarConfig {
    
    // Slots da hotbar - Lobby
    private static final int SLOT_BUSSOLA = 1;
    private static final int SLOT_PERFIL = 2;
    private static final int SLOT_LOJA = 5;
    private static final int SLOT_JOGADORES = 8;
    private static final int SLOT_LOBBIES = 9;
    
    // Slots da hotbar - Waiting
    private static final int SLOT_COSMETICOS = 1;
    private static final int SLOT_SAIR_WAITING = 9;
    
    // Slots da hotbar - Spectator
    private static final int SLOT_COMPASS = 5;
    private static final int SLOT_JOGAR = 1;
    private static final int SLOT_SAIR_SPECTATOR = 9;
    
    // Chaves dos itens - Lobby
    private static final String KEY_BUSSOLA = "bussola";
    private static final String KEY_PERFIL = "perfil";
    private static final String KEY_LOJA = "loja";
    private static final String KEY_JOGADORES = "jogadores";
    private static final String KEY_LOBBIES = "lobbies";
    
    // Chaves dos itens - Waiting
    private static final String KEY_COSMETICOS = "loja";
    private static final String KEY_SAIR_WAITING = "sair";
    
    // Chaves dos itens - Spectator
    private static final String KEY_COMPASS = "compass";
    private static final String KEY_JOGAR = "jogar";
    private static final String KEY_SAIR_SPECTATOR = "sair";
    
    // Comandos de execução
    private static final String CMD_JOGOS = "CORE>jogos";
    private static final String CMD_PERFIL = "CORE>perfil";
    private static final String CMD_LOJA = "BEDWARS>loja";
    private static final String CMD_JOGADORES = "CORE>jogadores";
    private static final String CMD_LOBBIES = "BEDWARS>lobbies";
    private static final String CMD_SAIR = "BEDWARS>sair";
    private static final String CMD_ESPECTAR = "BEDWARS>espectar";
    private static final String CMD_JOGAR = "BEDWARS>jogar";
    
    // Ícones dos itens - Lobby
    private static final String ICON_BUSSOLA = "COMPASS : 1 : nome>&aServidores";
    private static final String ICON_PERFIL = "%perfil%SKULL_ITEM:3 : 1 : nome>&aPerfil";
    private static final String ICON_LOJA = "EMERALD : 1 : nome>&aLoja";
    private static final String ICON_JOGADORES = "INK_SACK:%Core_status_jogadores_inksack% : 1 : nome>&fJogadores: %Core_status_jogadores_nome%";
    private static final String ICON_LOBBIES = "WATCH : 1 : nome>&aLobbies";
    
    // Ícones dos itens - Waiting
    private static final String ICON_COSMETICOS = "EMERALD : 1 : nome>&aCosméticos";
    private static final String ICON_SAIR_WAITING = "BED : 1 : nome>&cVoltar ao Lobby";
    
    // Ícones dos itens - Spectator
    private static final String ICON_COMPASS = "COMPASS : 1 : nome>&aAssistir Jogadores";
    private static final String ICON_JOGAR = "PAPER : 1 : nome>&bJogar Novamente";
    private static final String ICON_SAIR_SPECTATOR = "BED : 1 : nome>&cVoltar ao Lobby";
    
    private static final Map<String, Map<String, HotbarItem>> HOTBAR_ITEMS = createHotbarItems();
    
    /**
     * Cria o mapa de itens da hotbar para todos os tipos.
     * @return mapa com os itens configurados
     */
    private static Map<String, Map<String, HotbarItem>> createHotbarItems() {
        Map<String, Map<String, HotbarItem>> hotbars = new HashMap<>();
        
        // Hotbar do Lobby
        Map<String, HotbarItem> lobbyItems = new HashMap<>();
        lobbyItems.put(KEY_BUSSOLA, new HotbarItem(SLOT_BUSSOLA, CMD_JOGOS, ICON_BUSSOLA));
        lobbyItems.put(KEY_PERFIL, new HotbarItem(SLOT_PERFIL, CMD_PERFIL, ICON_PERFIL));
        lobbyItems.put(KEY_LOJA, new HotbarItem(SLOT_LOJA, CMD_LOJA, ICON_LOJA));
        lobbyItems.put(KEY_JOGADORES, new HotbarItem(SLOT_JOGADORES, CMD_JOGADORES, ICON_JOGADORES));
        lobbyItems.put(KEY_LOBBIES, new HotbarItem(SLOT_LOBBIES, CMD_LOBBIES, ICON_LOBBIES));
        hotbars.put("lobby", Collections.unmodifiableMap(lobbyItems));
        
        // Hotbar do Waiting
        Map<String, HotbarItem> waitingItems = new HashMap<>();
        waitingItems.put(KEY_LOJA, new HotbarItem(SLOT_COSMETICOS, CMD_LOJA, ICON_COSMETICOS));
        waitingItems.put(KEY_SAIR_WAITING, new HotbarItem(SLOT_SAIR_WAITING, CMD_SAIR, ICON_SAIR_WAITING));
        hotbars.put("waiting", Collections.unmodifiableMap(waitingItems));
        
        // Hotbar do Spectator
        Map<String, HotbarItem> spectatorItems = new HashMap<>();
        spectatorItems.put(KEY_COMPASS, new HotbarItem(SLOT_COMPASS, CMD_ESPECTAR, ICON_COMPASS));
        spectatorItems.put(KEY_JOGAR, new HotbarItem(SLOT_JOGAR, CMD_JOGAR, ICON_JOGAR));
        spectatorItems.put(KEY_SAIR_SPECTATOR, new HotbarItem(SLOT_SAIR_SPECTATOR, CMD_SAIR, ICON_SAIR_SPECTATOR));
        hotbars.put("spectator", Collections.unmodifiableMap(spectatorItems));
        
        return Collections.unmodifiableMap(hotbars);
    }
    
    /**
     * Obtém o mapa de itens de uma hotbar específica.
     * @param hotbarType tipo da hotbar (lobby, waiting, spectator)
     * @return mapa imutável com os itens da hotbar ou null se não encontrado
     */
    public static Map<String, HotbarItem> getHotbarItems(String hotbarType) {
        return HOTBAR_ITEMS.get(hotbarType);
    }
    
    /**
     * Obtém um item específico de uma hotbar.
     * @param hotbarType tipo da hotbar
     * @param key chave do item
     * @return item da hotbar ou null se não encontrado
     */
    public static HotbarItem getItem(String hotbarType, String key) {
        Map<String, HotbarItem> items = HOTBAR_ITEMS.get(hotbarType);
        return items != null ? items.get(key) : null;
    }
    
    /**
     * Obtém todos os tipos de hotbar disponíveis.
     * @return conjunto de tipos de hotbar
     */
    public static java.util.Set<String> getHotbarTypes() {
        return HOTBAR_ITEMS.keySet();
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