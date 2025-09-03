package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.cmd.MedalsCommand;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.nick.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Classe abstrata base para comandos do sistema Core.
 * Fornece funcionalidades comuns de registro e execução de comandos.
 * 
 * @author Luiz
 * @version 1.0
 */
public abstract class Commands extends Command {

    // Constantes
    private static final String COMMAND_PREFIX = "core";
    private static final String ERROR_REGISTER = "Erro ao registrar comando: ";
    private static final String ERROR_EXECUTE = "Erro ao executar comando ";

    /**
     * Construtor que registra o comando automaticamente.
     * 
     * @param name Nome do comando
     * @param aliases Aliases do comando
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
            commandMap.register(this.getName(), COMMAND_PREFIX, this);
        } catch (ReflectiveOperationException ex) {
            Core.getInstance().getLogger().log(
                Level.SEVERE, 
                ERROR_REGISTER + this.getName(), 
                ex
            );
        }
    }

    /**
     * Obtém o mapa de comandos do servidor via reflection.
     * 
     * @return SimpleCommandMap do servidor
     * @throws ReflectiveOperationException Se houver erro na reflection
     */
    private SimpleCommandMap getCommandMap() throws ReflectiveOperationException {
        Method getCommandMapMethod = Bukkit.getServer()
            .getClass()
            .getDeclaredMethod("getCommandMap");
        
        return (SimpleCommandMap) getCommandMapMethod.invoke(Bukkit.getServer());
    }

    /**
     * Configura todos os comandos do sistema.
     * Registra comandos baseados nas funcionalidades habilitadas.
     */
    public static void setupCommands() {
        // Comandos básicos sempre disponíveis
        registerBasicCommands();
        
        // Comandos condicionais baseados em funcionalidades
        registerConditionalCommands();
    }

    /**
     * Registra os comandos básicos do sistema.
     */
    private static void registerBasicCommands() {
        new CoreCommand();
        new CoinsCommand();
        new StatsCommand();
        new TagCommand();
        new MedalsCommand();
        new SetRankCommand();
        new SkinCommand();
    }

    /**
     * Registra comandos condicionais baseados em funcionalidades habilitadas.
     */
    private static void registerConditionalCommands() {
        // Comando de cash se habilitado
        if (CashManager.CASH) {
            new CashCommand();
        }
        
        // Comandos de nick e party apenas no lado Bukkit
        if (!NickManager.isBungeeSide()) {
            new NickCommand();
            new PartyCommand();
        }
    }

    /**
     * Método abstrato que deve ser implementado pelas classes filhas.
     * Define a lógica de execução do comando.
     * 
     * @param sender Quem executou o comando
     * @param label Label usado para executar o comando
     * @param args Argumentos do comando
     */
    public abstract void perform(CommandSender sender, String label, String[] args);

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        try {
            this.perform(sender, commandLabel, args);
            return true;
        } catch (Exception e) {
            Core.getInstance().getLogger().log(
                Level.WARNING, 
                ERROR_EXECUTE + this.getName() + " para " + sender.getName(), 
                e
            );
            return false;
        }
    }
}