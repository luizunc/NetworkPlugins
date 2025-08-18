package minecraft.lobby.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração dos lobbies disponíveis no menu de seleção.
 * Define os lobbies que aparecem no menu de seleção de servidores.
 */
public final class LobbiesConfig {
    
    // Configurações do menu
    public static final String TITLE = "Lobbies";
    public static final int ROWS = 3;
    
    // Slots dos lobbies no menu
    private static final int SLOT_LOBBY_1 = 11;
    private static final int SLOT_LOBBY_2 = 12;
    private static final int SLOT_LOBBY_3 = 13;
    private static final int SLOT_LOBBY_4 = 14;
    private static final int SLOT_LOBBY_5 = 15;
    
    // Nomes dos servidores
    private static final String SERVER_LOBBY_1 = "lobby1";
    private static final String SERVER_LOBBY_2 = "lobby2";
    private static final String SERVER_LOBBY_3 = "lobby3";
    private static final String SERVER_LOBBY_4 = "lobby4";
    private static final String SERVER_LOBBY_5 = "lobby5";
    
    // Endereços dos servidores
    private static final String ADDRESS_LOBBY_1 = "127.0.0.1:25565";
    private static final String ADDRESS_LOBBY_2 = "127.0.0.1:25566";
    private static final String ADDRESS_LOBBY_3 = "127.0.0.1:25567";
    private static final String ADDRESS_LOBBY_4 = "127.0.0.1:25568";
    private static final String ADDRESS_LOBBY_5 = "127.0.0.1:25569";
    
    // Capacidade máxima de jogadores
    private static final int MAX_PLAYERS = 50;
    
    // Template do ícone base
    private static final String ICON_TEMPLATE = "INK_SACK:10 : 1 : nome>&aLobby %d : desc>&fJogadores: &7{players}/{maxplayers}\n \n{description}";
    
    private static final Map<String, LobbyItem> LOBBY_ITEMS = createLobbyItems();
    
    /**
     * Cria o mapa de itens dos lobbies.
     * @return mapa com os lobbies configurados
     */
    private static Map<String, LobbyItem> createLobbyItems() {
        Map<String, LobbyItem> items = new HashMap<>();
        
        // Lobby 1
        items.put(SERVER_LOBBY_1, new LobbyItem(
                SLOT_LOBBY_1,
                String.format(ICON_TEMPLATE, 1),
                MAX_PLAYERS,
                String.format("%s ; %s", ADDRESS_LOBBY_1, SERVER_LOBBY_1)
        ));
        
        // Lobby 2
        items.put(SERVER_LOBBY_2, new LobbyItem(
                SLOT_LOBBY_2,
                String.format(ICON_TEMPLATE, 2),
                MAX_PLAYERS,
                String.format("%s ; %s", ADDRESS_LOBBY_2, SERVER_LOBBY_2)
        ));
        
        // Lobby 3
        items.put(SERVER_LOBBY_3, new LobbyItem(
                SLOT_LOBBY_3,
                String.format(ICON_TEMPLATE, 3),
                MAX_PLAYERS,
                String.format("%s ; %s", ADDRESS_LOBBY_3, SERVER_LOBBY_3)
        ));
        
        // Lobby 4
        items.put(SERVER_LOBBY_4, new LobbyItem(
                SLOT_LOBBY_4,
                String.format(ICON_TEMPLATE, 4),
                MAX_PLAYERS,
                String.format("%s ; %s", ADDRESS_LOBBY_4, SERVER_LOBBY_4)
        ));
        
        // Lobby 5
        items.put(SERVER_LOBBY_5, new LobbyItem(
                SLOT_LOBBY_5,
                String.format(ICON_TEMPLATE, 5),
                MAX_PLAYERS,
                String.format("%s ; %s", ADDRESS_LOBBY_5, SERVER_LOBBY_5)
        ));
        
        return Collections.unmodifiableMap(items);
    }
    
    /**
     * Obtém o mapa de itens dos lobbies.
     * @return mapa imutável com os lobbies
     */
    public static Map<String, LobbyItem> getLobbyItems() {
        return LOBBY_ITEMS;
    }
    
    /**
     * Obtém um lobby específico.
     * @param serverName nome do servidor
     * @return item do lobby ou null se não encontrado
     */
    public static LobbyItem getLobby(String serverName) {
        return LOBBY_ITEMS.get(serverName);
    }
    
    /**
     * Mensagens utilizadas no menu de lobbies.
     */
    public static final class Messages {
        public static final String CURRENT = "&aVocê está aqui.";
        public static final String CONNECT = "&8Clique para entrar!";
        
        private Messages() {
            // Classe utilitária - não deve ser instanciada
        }
    }
    
    /**
     * Representa um item de lobby no menu.
     */
    public static final class LobbyItem {
        private final int slot;
        private final String icon;
        private final int maxPlayers;
        private final String serverName;
        
        /**
         * Constrói um item de lobby.
         * @param slot posição no menu
         * @param icon string de serialização do ícone
         * @param maxPlayers capacidade máxima de jogadores
         * @param serverName nome do servidor (formato: "endereço ; nome")
         */
        public LobbyItem(int slot, String icon, int maxPlayers, String serverName) {
            this.slot = slot;
            this.icon = icon;
            this.maxPlayers = maxPlayers;
            this.serverName = serverName;
        }
        
        /**
         * Obtém a posição no menu.
         * @return slot do item
         */
        public int getSlot() {
            return slot;
        }
        
        /**
         * Obtém a string de serialização do ícone.
         * @return string do ícone
         */
        public String getIcon() {
            return icon;
        }
        
        /**
         * Obtém a capacidade máxima de jogadores.
         * @return número máximo de jogadores
         */
        public int getMaxPlayers() {
            return maxPlayers;
        }
        
        /**
         * Obtém o nome do servidor.
         * @return nome do servidor
         */
        public String getServerName() {
            return serverName;
        }
        
        /**
         * Obtém apenas o nome do servidor (sem endereço).
         * @return nome do servidor
         */
        public String getServerNameOnly() {
            return serverName.split(" ; ")[1];
        }
        
        @Override
        public String toString() {
            return String.format("LobbyItem{slot=%d, icon='%s', maxPlayers=%d, serverName='%s'}", 
                    slot, icon, maxPlayers, serverName);
        }
    }
}
