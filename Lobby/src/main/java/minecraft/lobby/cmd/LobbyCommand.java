package minecraft.lobby.cmd;

import minecraft.lobby.Main;
import minecraft.lobby.cmd.pl.BuildCommand;
import minecraft.lobby.cmd.pl.NPCPlayCommand;
import minecraft.lobby.cmd.pl.SetSpawnCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Comando principal do plugin Lobby.
 * Gerencia subcomandos e exibe ajuda para os usuários.
 */
public final class LobbyCommand extends Commands {
    
    private static final String COMMAND_NAME = "pl";
    private static final String PERMISSION = "lobby.cmd.lobby";
    private static final String PLAYER_ONLY_MESSAGE = "§cEsse comando pode ser utilizado apenas pelos jogadores.";
    private static final String PAGE_NOT_FOUND_MESSAGE = "§cPágina não encontrada.";
    private static final String HELP_HEADER_FORMAT = " \n§eAjuda - %d/%d\n \n";
    private static final String HELP_LINE_FORMAT = "§6/pl %s §f- §7%s\n";
    private static final String VERSION_MESSAGE_FORMAT = "§6Lobby §bv%s §7Criado por §6Nyskiwi§7.";
    
    private static final int COMMANDS_PER_PAGE = 7;
    
    private final List<SubCommand> commands = new ArrayList<>();
    
    /**
     * Constrói o comando principal e registra os subcomandos.
     */
    public LobbyCommand() {
        super(COMMAND_NAME);
        registerSubCommands();
    }
    
    /**
     * Registra todos os subcomandos disponíveis.
     */
    private void registerSubCommands() {
        commands.add(new SetSpawnCommand());
        commands.add(new BuildCommand());
        commands.add(new NPCPlayCommand());
    }
    
    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sendVersionMessage(sender);
            return;
        }
        
        if (args.length == 0) {
            sendHelp(sender, 1);
            return;
        }
        
        try {
            int page = Integer.parseInt(args[0]);
            sendHelp(sender, page);
        } catch (NumberFormatException ex) {
            executeSubCommand(sender, args);
        }
    }
    
    /**
     * Envia a mensagem de versão do plugin.
     * @param sender executor do comando
     */
    private void sendVersionMessage(CommandSender sender) {
        String version = Main.getInstance().getDescription().getVersion();
        sender.sendMessage(String.format(VERSION_MESSAGE_FORMAT, version));
    }
    
    /**
     * Executa um subcomando específico.
     * @param sender executor do comando
     * @param args argumentos do comando
     */
    private void executeSubCommand(CommandSender sender, String[] args) {
        String subCommandName = args[0];
        SubCommand subCommand = findSubCommand(subCommandName);
        
        if (subCommand == null) {
            sendHelp(sender, 1);
            return;
        }
        
        if (subCommand.onlyForPlayer() && !(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_MESSAGE);
            return;
        }
        
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        
        if (subCommand.onlyForPlayer()) {
            subCommand.perform((Player) sender, subArgs);
        } else {
            subCommand.perform(sender, subArgs);
        }
    }
    
    /**
     * Encontra um subcomando pelo nome.
     * @param name nome do subcomando
     * @return subcomando encontrado ou null
     */
    private SubCommand findSubCommand(String name) {
        return commands.stream()
                .filter(sc -> sc.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Envia a página de ajuda para o executor.
     * @param sender executor do comando
     * @param page número da página
     */
    private void sendHelp(CommandSender sender, int page) {
        List<SubCommand> availableCommands = getAvailableCommands(sender);
        Map<Integer, StringBuilder> pages = createHelpPages(availableCommands);
        
        StringBuilder pageContent = pages.get(page);
        if (pageContent == null) {
            sender.sendMessage(PAGE_NOT_FOUND_MESSAGE);
            return;
        }
        
        pageContent.append(" ");
        sender.sendMessage(pageContent.toString());
    }
    
    /**
     * Obtém os comandos disponíveis para o executor.
     * @param sender executor do comando
     * @return lista de comandos disponíveis
     */
    private List<SubCommand> getAvailableCommands(CommandSender sender) {
        return commands.stream()
                .filter(subcommand -> sender instanceof Player || !subcommand.onlyForPlayer())
                .collect(Collectors.toList());
    }
    
    /**
     * Cria as páginas de ajuda.
     * @param commands lista de comandos disponíveis
     * @return mapa de páginas de ajuda
     */
    private Map<Integer, StringBuilder> createHelpPages(List<SubCommand> commands) {
        Map<Integer, StringBuilder> pages = new HashMap<>();
        int pagesCount = calculatePagesCount(commands.size());
        
        for (int index = 0; index < commands.size(); index++) {
            int currentPage = (index + COMMANDS_PER_PAGE) / COMMANDS_PER_PAGE;
            
            pages.computeIfAbsent(currentPage, page -> 
                    new StringBuilder(String.format(HELP_HEADER_FORMAT, page, pagesCount)));
            
            SubCommand command = commands.get(index);
            pages.get(currentPage).append(String.format(HELP_LINE_FORMAT, 
                    command.getUsage(), command.getDescription()));
        }
        
        return pages;
    }
    
    /**
     * Calcula o número total de páginas.
     * @param commandsCount número de comandos
     * @return número de páginas
     */
    private int calculatePagesCount(int commandsCount) {
        return (commandsCount + COMMANDS_PER_PAGE - 1) / COMMANDS_PER_PAGE;
    }
}
