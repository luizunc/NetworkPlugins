package minecraft.lobby.cmd;

import minecraft.core.bukkit.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Classe base abstrata para comandos do plugin.
 * Fornece funcionalidade comum para registro e execução de comandos.
 */
public abstract class Commands extends Command {
    
    private static final String COMMAND_MAP_METHOD = "getCommandMap";
    private static final String PLUGIN_NAME = "lobby";
    private static final String COMMAND_REGISTRATION_ERROR = "Erro ao registrar comando: ";
    
    /**
     * Constrói um comando com nome e aliases.
     * @param name nome do comando
     * @param aliases aliases do comando
     */
    public Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));
        registerCommand();
    }
    
    /**
     * Registra o comando no sistema do Bukkit.
     */
    private void registerCommand() {
        try {
            SimpleCommandMap commandMap = getCommandMap();
            commandMap.register(this.getName(), PLUGIN_NAME, this);
        } catch (ReflectiveOperationException ex) {
            Core.getInstance().getLogger().log(Level.SEVERE, COMMAND_REGISTRATION_ERROR, ex);
        }
    }
    
    /**
     * Obtém o mapa de comandos do servidor via reflexão.
     * @return SimpleCommandMap do servidor
     * @throws ReflectiveOperationException se houver erro de reflexão
     */
    private SimpleCommandMap getCommandMap() throws ReflectiveOperationException {
        Method getCommandMapMethod = Bukkit.getServer().getClass().getDeclaredMethod(COMMAND_MAP_METHOD);
        return (SimpleCommandMap) getCommandMapMethod.invoke(Bukkit.getServer());
    }
    
    /**
     * Configura todos os comandos do plugin.
     */
    public static void setupCommands() {
        new LobbyCommand();
    }
    
    /**
     * Executa a lógica específica do comando.
     * @param sender executor do comando
     * @param label label usado para executar o comando
     * @param args argumentos do comando
     */
    public abstract void perform(CommandSender sender, String label, String[] args);
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        this.perform(sender, commandLabel, args);
        return true;
    }
}
