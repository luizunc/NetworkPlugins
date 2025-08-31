package minecraft.core.bungee.cmd;

import minecraft.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Classe abstrata base para comandos do sistema Core no BungeeCord.
 * Fornece funcionalidades comuns de registro e execução de comandos.
 * 
 * @author Luiz
 * @version 1.0
 */
public abstract class Commands extends Command {

    /**
     * Construtor que registra o comando automaticamente.
     * 
     * @param name Nome do comando
     * @param aliases Aliases do comando
     */
    public Commands(String name, String... aliases) {
        super(name, null, aliases);
        ProxyServer.getInstance()
            .getPluginManager()
            .registerCommand(Bungee.getInstance(), this);
    }

    /**
     * Configura todos os comandos do sistema BungeeCord.
     * Registra todos os comandos disponíveis no proxy.
     */
    public static void setupCommands() {
        // Comandos de nick
        new NickCommand();
        new NickResetCommand();
        new NickListCommand();
        
        // Comandos de party
        new PartyCommand();
        new PartyChatCommand();
    }

    /**
     * Método abstrato que deve ser implementado pelas classes filhas.
     * Define a lógica de execução do comando.
     * 
     * @param sender Quem executou o comando
     * @param args Argumentos do comando
     */
    public abstract void perform(CommandSender sender, String[] args);

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.perform(sender, args);
    }
}
