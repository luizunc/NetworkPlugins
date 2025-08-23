package minecraft.bedwars.lobby;

/**
 * Classe para gerenciar o modo de jogo global
 * Controla qual modo será exibido em todas as leaderboards
 */
public class GlobalGameMode {
    
    private static int currentMode = 0; // 0 = Geral, 1 = Solo, 2 = Duplas, 3 = Quartetos
    
    public static final int MODE_GERAL = 0;
    public static final int MODE_SOLO = 1;
    public static final int MODE_DUPLAS = 2;
    public static final int MODE_QUARTETOS = 3;
    
    /**
     * Obtém o modo atual
     */
    public static int getCurrentMode() {
        return currentMode;
    }
    
    /**
     * Define o modo atual
     */
    public static void setCurrentMode(int mode) {
        if (mode >= 0 && mode <= 3) {
            currentMode = mode;
        }
    }
    
    /**
     * Obtém o nome do modo atual
     */
    public static String getCurrentModeName() {
        switch (currentMode) {
            case MODE_GERAL:
                return "Geral";
            case MODE_SOLO:
                return "Solo";
            case MODE_DUPLAS:
                return "Duplas";
            case MODE_QUARTETOS:
                return "Quartetos";
            default:
                return "Geral";
        }
    }
    
    /**
     * Obtém o sufixo das estatísticas baseado no modo atual
     */
    public static String getStatsSuffix() {
        switch (currentMode) {
            case MODE_GERAL:
                return ""; // Sem sufixo para modo geral
            case MODE_SOLO:
                return "solo";
            case MODE_DUPLAS:
                return "duo";
            case MODE_QUARTETOS:
                return "quad";
            default:
                return "";
        }
    }
} 