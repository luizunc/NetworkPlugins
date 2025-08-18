package minecraft.lobby;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.plugin.KPlugin;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.lobby.cmd.Commands;
import minecraft.lobby.hook.LCoreHook;
import minecraft.lobby.listeners.Listeners;
import minecraft.lobby.lobby.AlwaysDayTask;
import minecraft.lobby.lobby.Lobby;
import minecraft.lobby.lobby.PlayNPC;
import minecraft.lobby.utils.tagger.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Classe principal do plugin Lobby.
 * Gerencia o ciclo de vida do plugin e inicialização dos componentes.
 */
public final class Main extends KPlugin {
    
    private static final String CONFIG_LOBBY_KEY = "lobby";
    private static final String CONFIG_SPAWN_KEY = "spawn";
    private static final String ACTIVATION_MESSAGE = "O plugin foi ativado.";
    private static final String DEACTIVATION_MESSAGE = "O plugin foi desativado.";
    private static final long DAY_TIME = 6000L;
    
    private static Main instance;
    private static boolean isInitialized;
    
    // Nome do servidor atual - usado para identificar o lobby atual
    private static String currentServerName;
    
    /**
     * Obtém a instância singleton do plugin.
     * @return instância do plugin
     */
    public static Main getInstance() {
        return instance;
    }
    
    /**
     * Obtém o nome do servidor atual.
     * @return nome do servidor atual
     */
    public static String getCurrentServerName() {
        return currentServerName;
    }
    
    /**
     * Verifica se o plugin foi inicializado corretamente.
     * @return true se inicializado, false caso contrário
     */
    public static boolean isPluginInitialized() {
        return isInitialized;
    }
    
    @Override
    public void start() {
        instance = this;
    }
    
    @Override
    public void load() {
        // Método vazio - não há carregamento específico necessário
    }
    
    @Override
    public void enable() {
        initializePlugin();
        lockWorldsToDay();
        // Agenda task que mantém o mundo sempre no tick 6000 a cada tick
        new AlwaysDayTask().runTaskTimer(this, 1L, 1L);
        setupComponents();
        isInitialized = true;
        
        getLogger().info(ACTIVATION_MESSAGE);
    }
    
    @Override
    public void disable() {
        if (isPluginInitialized()) {
            cleanupResources();
        }
        
        getLogger().info(DEACTIVATION_MESSAGE);
    }
    
    /**
     * Inicializa as configurações básicas do plugin.
     */
    private void initializePlugin() {
        saveDefaultConfig();
        currentServerName = getConfig().getString(CONFIG_LOBBY_KEY);
        
        String spawnLocation = getConfig().getString(CONFIG_SPAWN_KEY);
        if (spawnLocation != null) {
            Core.setLobby(BukkitUtils.deserializeLocation(spawnLocation));
        }
    }
    
    /**
     * Trava todos os mundos em dia (tick 6000) e desativa o ciclo.
     */
    private void lockWorldsToDay() {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(DAY_TIME);
            world.setGameRuleValue("doDaylightCycle", "false");
        }
    }
    
    /**
     * Configura todos os componentes do plugin.
     */
    private void setupComponents() {
        LCoreHook.setupHook();
        Lobby.setupLobbies();
        Listeners.setupListeners();
        Language.setupLanguage();
        PlayNPC.setupNPCs();
        Commands.setupCommands();
    }
    
    /**
     * Limpa recursos ao desabilitar o plugin.
     */
    private void cleanupResources() {
        PlayNPC.listNPCs().forEach(PlayNPC::destroy);
        TagUtils.reset();
    }
}
