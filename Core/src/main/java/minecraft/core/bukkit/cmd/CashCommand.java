package minecraft.core.bukkit.cmd;

import minecraft.core.core.cash.CashException;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para gerenciar o sistema de Cash.
 * Permite visualizar, adicionar, remover e definir cash de jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class CashCommand extends Commands {

    // Constantes
    private static final String PERMISSION_CASH = "core.cmd.cash";
    private static final String ACTION_SET = "set";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_REMOVE = "remove";
    
    // Mensagens
    private static final String MSG_CASH_INFO = "§eCash: §b%s";
    private static final String MSG_HELP = " \n§6/cash set [jogador] [quantia] §f- §7Setar o cash do jogador.\n§6/cash add [jogador] [quantia] §f- §7Dar cash para um jogador.\n§6/cash remove [jogador] [quantia] §f- §7Remover o cash de um jogador.\n ";
    private static final String MSG_INVALID_ACTION = " \n§6/cash set [jogador] [quantia] §f- §7Setar o cash do jogador.\n§6/cash add [jogador] [quantia] §f- §7Dar cash para um jogador.\n§6/cash remove [jogador] [quantia] §f- §7Remover o cash de um jogador.\n ";
    private static final String MSG_USAGE = "§cUtilize /cash %s [jogador] [quantia]";
    private static final String MSG_INVALID_NUMBER = "§cUtilize números válidos e positivos.";
    private static final String MSG_USER_OFFLINE = "§cO usuário precisa estar conectado.";
    private static final String MSG_SET_SUCCESS = "§aVocê setou o Cash do(a) %s §apara §b%s§a.";
    private static final String MSG_ADD_SUCCESS = "§aVocê adicionou §b%s §ade Cash para o(a) %s§a.";
    private static final String MSG_REMOVE_SUCCESS = "§aVocê removeu §b%s §ade Cash do(a) %s§a.";

    public CashCommand() {
        super("cash");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        // Sem argumentos - mostra cash do jogador ou ajuda
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(String.format(MSG_CASH_INFO, 
                    StringUtils.formatNumber(CashManager.getCash(sender.getName()))));
                return;
            }
            sender.sendMessage(MSG_HELP);
            return;
        }

        // Verifica permissão para comandos administrativos
        if (!sender.hasPermission(PERMISSION_CASH)) {
            sender.sendMessage(String.format(MSG_CASH_INFO, 
                StringUtils.formatNumber(CashManager.getCash(sender.getName()))));
            return;
        }

        String action = args[0];
        
        // Valida ação
        if (!isValidAction(action)) {
            sender.sendMessage(MSG_INVALID_ACTION);
            return;
        }

        // Verifica argumentos mínimos
        if (args.length <= 2) {
            sender.sendMessage(String.format(MSG_USAGE, action));
            return;
        }

        // Processa o comando
        processCashAction(sender, action, args);
    }

    /**
     * Verifica se a ação é válida.
     * 
     * @param action Ação a ser verificada
     * @return true se a ação for válida
     */
    private boolean isValidAction(String action) {
        return action.equalsIgnoreCase(ACTION_SET) || 
               action.equalsIgnoreCase(ACTION_ADD) || 
               action.equalsIgnoreCase(ACTION_REMOVE);
    }

    /**
     * Processa a ação de cash solicitada.
     * 
     * @param sender Quem executou o comando
     * @param action Ação a ser executada
     * @param args Argumentos do comando
     */
    private void processCashAction(CommandSender sender, String action, String[] args) {
        // Valida e converte o valor
        long amount = parseAmount(args[2]);
        if (amount == -1) {
            sender.sendMessage(MSG_INVALID_NUMBER);
            return;
        }

        String playerName = args[1];
        String formattedAmount = StringUtils.formatNumber(amount);
        String coloredPlayerName = Rank.getColored(playerName);

        try {
            switch (action.toLowerCase()) {
                case ACTION_SET:
                    CashManager.setCash(playerName, amount);
                    sender.sendMessage(String.format(MSG_SET_SUCCESS, 
                        coloredPlayerName, formattedAmount));
                    break;
                    
                case ACTION_ADD:
                    CashManager.addCash(playerName, amount);
                    sender.sendMessage(String.format(MSG_ADD_SUCCESS, 
                        formattedAmount, coloredPlayerName));
                    break;
                    
                case ACTION_REMOVE:
                    CashManager.removeCash(playerName, amount);
                    sender.sendMessage(String.format(MSG_REMOVE_SUCCESS, 
                        formattedAmount, coloredPlayerName));
                    break;
            }
        } catch (CashException ex) {
            sender.sendMessage(MSG_USER_OFFLINE);
        }
    }

    /**
     * Converte e valida o valor numérico.
     * 
     * @param value String contendo o valor
     * @return Valor convertido ou -1 se inválido
     */
    private long parseAmount(String value) {
        try {
            if (value.startsWith("-")) {
                return -1;
            }
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}