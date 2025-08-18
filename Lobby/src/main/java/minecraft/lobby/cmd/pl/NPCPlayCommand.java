package minecraft.lobby.cmd.pl;

import minecraft.lobby.cmd.SubCommand;
import minecraft.lobby.lobby.PlayNPC;
import minecraft.lobby.lobby.ServerEntry;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para gerenciar NPCs de jogo.
 * Permite adicionar e remover NPCs que permitem aos jogadores entrar em minigames.
 */
public final class NPCPlayCommand extends SubCommand {
    
    private static final String COMMAND_NAME = "npcjogar";
    private static final String COMMAND_USAGE = "npcjogar";
    private static final String COMMAND_DESCRIPTION = "Adicione/remova NPC de Jogar.";
    
    // Ações do comando
    private static final String ACTION_ADD = "adicionar";
    private static final String ACTION_REMOVE = "remover";
    
    // Mensagens de erro
    private static final String ERROR_ADD_USAGE = "§cUtilize /pl npcjogar adicionar [id] [entry]";
    private static final String ERROR_REMOVE_USAGE = "§cUtilize /pl npcjogar remover [id]";
    private static final String ERROR_ID_EXISTS = "§cJá existe um NPC de Jogar utilizando \"%s\" como ID.";
    private static final String ERROR_ID_NOT_FOUND = "§cNão existe um NPC de Jogar utilizando \"%s\" como ID.";
    
    // Mensagens de sucesso
    private static final String SUCCESS_ADD = "§aNPC de Jogar adicionado com sucesso.";
    private static final String SUCCESS_REMOVE = "§cNPC de Jogar removido com sucesso.";
    
    // Help
    private static final String HELP_MESSAGE = " \n§eAjuda\n \n§6/pl npcjogar adicionar [id] [entry] §f- §7Adicionar NPC.\n§6/pl npcjogar remover [id] §f- §7Remover NPC.\n ";
    
    /**
     * Constrói o comando de gerenciamento de NPCs.
     */
    public NPCPlayCommand() {
        super(COMMAND_NAME, COMMAND_USAGE, COMMAND_DESCRIPTION, true);
    }
    
    @Override
    public void perform(CommandSender sender, String[] args) {
        // Comando apenas para jogadores
    }
    
    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 0) {
            showHelp(player);
            return;
        }
        
        String action = args[0];
        
        switch (action.toLowerCase()) {
            case ACTION_ADD:
                handleAddAction(player, args);
                break;
            case ACTION_REMOVE:
                handleRemoveAction(player, args);
                break;
            default:
                showHelp(player);
                break;
        }
    }
    
    /**
     * Exibe a ajuda do comando.
     * @param player jogador para exibir a ajuda
     */
    private void showHelp(Player player) {
        player.sendMessage(HELP_MESSAGE);
    }
    
    /**
     * Processa a ação de adicionar NPC.
     * @param player jogador executando o comando
     * @param args argumentos do comando
     */
    private void handleAddAction(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(ERROR_ADD_USAGE);
            return;
        }
        
        String id = args[1];
        String entryKey = args[2];
        
        if (isNPCIdExists(id)) {
            player.sendMessage(String.format(ERROR_ID_EXISTS, id));
            return;
        }
        
        ServerEntry entry = ServerEntry.getByKey(entryKey);
        if (entry == null) {
            player.sendMessage(ERROR_ADD_USAGE);
            return;
        }
        
        Location location = createNPCLocation(player);
        PlayNPC.add(id, location, entry);
        player.sendMessage(SUCCESS_ADD);
    }
    
    /**
     * Processa a ação de remover NPC.
     * @param player jogador executando o comando
     * @param args argumentos do comando
     */
    private void handleRemoveAction(Player player, String[] args) {
        if (args.length <= 1) {
            player.sendMessage(ERROR_REMOVE_USAGE);
            return;
        }
        
        String id = args[1];
        PlayNPC npc = PlayNPC.getById(id);
        
        if (npc == null) {
            player.sendMessage(String.format(ERROR_ID_NOT_FOUND, id));
            return;
        }
        
        PlayNPC.remove(npc);
        player.sendMessage(SUCCESS_REMOVE);
    }
    
    /**
     * Verifica se já existe um NPC com o ID especificado.
     * @param id ID a ser verificado
     * @return true se existe, false caso contrário
     */
    private boolean isNPCIdExists(String id) {
        return PlayNPC.getById(id) != null;
    }
    
    /**
     * Cria a localização do NPC baseada na posição do jogador.
     * @param player jogador que define a posição
     * @return localização do NPC
     */
    private Location createNPCLocation(Player player) {
        Location playerLocation = player.getLocation();
        Location npcLocation = playerLocation.getBlock().getLocation().add(0.5, 0, 0.5);
        
        // Mantém a direção do jogador
        npcLocation.setYaw(playerLocation.getYaw());
        npcLocation.setPitch(playerLocation.getPitch());
        
        return npcLocation;
    }
}
