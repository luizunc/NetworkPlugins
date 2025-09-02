package minecraft.core.bukkit.cmd;

import minecraft.core.Manager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.nick.NickManager;
import minecraft.core.core.player.rank.RankPermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Comando para gerenciar nick no Bukkit.
 * Permite aplicar, remover e listar nicknames falsos.
 * 
 * @author Luiz
 * @version 1.0
 */
public class NickCommand extends Commands {

    // Constantes
    private static final String PERMISSION_NICK = "rank.partner";
    private static final String PERMISSION_NICKLIST = "rank.mod";
    private static final String PERMISSION_MOD = "rank.mod";
    private static final String DEFAULT_RANK = "Membro";
    
    // Labels dos comandos
    private static final String LABEL_NICK = "nick";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_PLAYING_GAME = "§cVocê não pode utilizar este comando no momento.";
    private static final String MSG_USAGE = "§cUso: /nick <nick>";
    private static final String MSG_NOT_NICK = "§cVocê não está utilizando um nick.";
    private static final String MSG_HEADER = " \n§eLista de nicks:\n \n";
    private static final String MSG_FOOTER = "\n ";
    private static final String MSG_NO_NICKS = "§cNão há usuários utilizando nicks.";
    private static final String MSG_NICK_FORMAT = "§c%s §f= §acorefakereal:%s";

    public NickCommand() {
        super("nick", new String[]{"reset", "list"});
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_PLAYERS_ONLY);
            return;
        }

        Player player = (Player) sender;
        
        // Verifica permissões
        if (!hasPermission(player, label) && !hasSpecialPermission(player, label, args)) {
            player.sendMessage(MSG_NO_PERMISSION);
            return;
        }

        Profile profile = Profile.getProfile(player.getName());
        
        // Processa comando baseado no label
        switch (label.toLowerCase()) {
            case LABEL_NICK:
                handleNickApply(player, profile, args);
                break;
        }
        
        // Trata comandos especiais
        if (label.equalsIgnoreCase("reset") && args.length > 0 && args[0].equalsIgnoreCase("nick")) {
            handleNickRemove(player, profile);
        } else if (label.equalsIgnoreCase("list") && args.length > 0 && args[0].equalsIgnoreCase("nicks")) {
            handleNickList(player);
        }
    }

    /**
     * Verifica se o jogador tem permissão para o comando.
     * 
     * @param player Jogador a ser verificado
     * @param label Label do comando
     * @return true se tem permissão
     */
    private boolean hasPermission(Player player, String label) {
        if (label.equalsIgnoreCase("list")) {
            return RankPermissionUtils.hasModOrHigher(player);
        }
        return RankPermissionUtils.hasRankOrHigher(player, PERMISSION_NICK);
    }
    
    /**
     * Verifica se o jogador tem permissão para comandos especiais.
     * 
     * @param player Jogador a ser verificado
     * @param label Label do comando
     * @param args Argumentos do comando
     * @return true se tem permissão
     */
    private boolean hasSpecialPermission(Player player, String label, String[] args) {
        if (label.equalsIgnoreCase("reset") && args.length > 0 && args[0].equalsIgnoreCase("nick")) {
            return RankPermissionUtils.hasRankOrHigher(player, PERMISSION_NICK);
        } else if (label.equalsIgnoreCase("list") && args.length > 0 && args[0].equalsIgnoreCase("nicks")) {
            return RankPermissionUtils.hasModOrHigher(player);
        }
        return false;
    }

    /**
     * Gerencia a aplicação de nick.
     * 
     * @param player Jogador que aplicará o nick
     * @param profile Perfil do jogador
     * @param args Argumentos do comando
     */
    private void handleNickApply(Player player, Profile profile, String[] args) {
        // Verifica se está jogando
        if (profile != null && profile.playingGame()) {
            player.sendMessage(MSG_PLAYING_GAME);
            return;
        }

        // Verifica argumentos
        if (args.length == 0) {
            player.sendMessage(MSG_USAGE);
            return;
        }

        String nickName = args[0];
        
        // Verifica disponibilidade do nick com feedback específico
        String availabilityCheck = NickManager.checkNicknameAvailability(nickName);
        if (availabilityCheck != null) {
            player.sendMessage(availabilityCheck);
            return;
        }

        // Aplica o nick
        String finalRankName = DEFAULT_RANK;
        String finalSkin = Manager.getSkin(nickName, "value") + ":" + Manager.getSkin(nickName, "signature");
        NickManager.applyNick(player, nickName, finalRankName, finalSkin);
    }

    /**
     * Gerencia a remoção de nick.
     * 
     * @param player Jogador que removerá o nick
     * @param profile Perfil do jogador
     */
    private void handleNickRemove(Player player, Profile profile) {
        // Verifica se está jogando
        if (profile != null && profile.playingGame()) {
            player.sendMessage(MSG_PLAYING_GAME);
            return;
        }

        // Verifica se está usando nick
        if (!NickManager.isNick(player.getName())) {
            player.sendMessage(MSG_NOT_NICK);
            return;
        }

        // Remove o nick
        NickManager.removeNick(player);
    }

    /**
     * Gerencia a listagem de nicks.
     * 
     * @param player Jogador que solicitou a lista
     */
    private void handleNickList(Player player) {
        List<String> nicked = NickManager.listNicked();
        StringBuilder sb = new StringBuilder();

        // Constrói a lista de nicks
        for (int index = 0; index < nicked.size(); index++) {
            String nickName = nicked.get(index);
            sb.append(String.format(MSG_NICK_FORMAT, nickName, nickName));
            
            // Adiciona quebra de linha se não for o último
            if (index + 1 < nicked.size()) {
                sb.append("\n");
            }
        }

        // Limpa a lista para liberar memória
        nicked.clear();
        
        // Verifica se há nicks para exibir
        if (sb.length() == 0) {
            sb.append(MSG_NO_NICKS);
        }

        // Envia a mensagem formatada
        String finalMessage = MSG_HEADER + sb.toString() + MSG_FOOTER;
        player.sendMessage(finalMessage);
    }
}