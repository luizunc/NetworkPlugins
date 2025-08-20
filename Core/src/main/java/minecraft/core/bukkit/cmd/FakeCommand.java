package minecraft.core.bukkit.cmd;

import minecraft.core.Manager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.fake.FakeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Comando para gerenciar fake no Bukkit.
 * Permite aplicar, remover e listar nicknames falsos.
 * 
 * @author Luiz
 * @version 1.0
 */
public class FakeCommand extends Commands {

    // Constantes
    private static final String PERMISSION_FAKE = "core.cmd.fake";
    private static final String PERMISSION_FAKELIST = "core.cmd.fakelist";
    private static final String DEFAULT_RANK = "Membro";
    
    // Labels dos comandos
    private static final String LABEL_FAKE = "fake";
    private static final String LABEL_FAKER = "faker";
    private static final String LABEL_FAKEL = "fakel";
    
    // Mensagens
    private static final String MSG_PLAYERS_ONLY = "§cApenas jogadores podem utilizar este comando.";
    private static final String MSG_NO_PERMISSION = "§cVocê não possui permissão para utilizar este comando.";
    private static final String MSG_PLAYING_GAME = "§cVocê não pode utilizar este comando no momento.";
    private static final String MSG_USAGE = "§cUso: /fake <nick>";
    private static final String MSG_NOT_FAKE = "§cVocê não está utilizando um nickname falso.";
    private static final String MSG_HEADER = " \n§eLista de nicknames falsos:\n \n";
    private static final String MSG_FOOTER = "\n ";
    private static final String MSG_NO_FAKES = "§cNão há nenhum usuário utilizando um nickname falso.";
    private static final String MSG_FAKE_FORMAT = "§c%s §fé na verdade §acorefakereal:%s";

    public FakeCommand() {
        super("fake", new String[]{"faker", "fakel"});
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_PLAYERS_ONLY);
            return;
        }

        Player player = (Player) sender;
        
        // Verifica permissões
        if (!hasPermission(player, label)) {
            player.sendMessage(MSG_NO_PERMISSION);
            return;
        }

        Profile profile = Profile.getProfile(player.getName());
        
        // Processa comando baseado no label
        switch (label.toLowerCase()) {
            case LABEL_FAKE:
                handleFakeApply(player, profile, args);
                break;
                
            case LABEL_FAKER:
                handleFakeRemove(player, profile);
                break;
                
            case LABEL_FAKEL:
                handleFakeList(player);
                break;
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
        if (label.equalsIgnoreCase(LABEL_FAKEL)) {
            return player.hasPermission(PERMISSION_FAKE) && player.hasPermission(PERMISSION_FAKELIST);
        }
        return player.hasPermission(PERMISSION_FAKE);
    }

    /**
     * Gerencia a aplicação de fake.
     * 
     * @param player Jogador que aplicará o fake
     * @param profile Perfil do jogador
     * @param args Argumentos do comando
     */
    private void handleFakeApply(Player player, Profile profile, String[] args) {
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

        String fakeName = args[0];
        
        // Verifica disponibilidade do nick com feedback específico
        String availabilityCheck = FakeManager.checkNicknameAvailability(fakeName);
        if (availabilityCheck != null) {
            player.sendMessage(availabilityCheck);
            return;
        }

        // Aplica o fake
        String finalRankName = DEFAULT_RANK;
        String finalSkin = Manager.getSkin(fakeName, "value") + ":" + Manager.getSkin(fakeName, "signature");
        FakeManager.applyFake(player, fakeName, finalRankName, finalSkin);
    }

    /**
     * Gerencia a remoção de fake.
     * 
     * @param player Jogador que removerá o fake
     * @param profile Perfil do jogador
     */
    private void handleFakeRemove(Player player, Profile profile) {
        // Verifica se está jogando
        if (profile != null && profile.playingGame()) {
            player.sendMessage(MSG_PLAYING_GAME);
            return;
        }

        // Verifica se está usando fake
        if (!FakeManager.isFake(player.getName())) {
            player.sendMessage(MSG_NOT_FAKE);
            return;
        }

        // Remove o fake
        FakeManager.removeFake(player);
    }

    /**
     * Gerencia a listagem de fakes.
     * 
     * @param player Jogador que solicitou a lista
     */
    private void handleFakeList(Player player) {
        List<String> nicked = FakeManager.listNicked();
        StringBuilder sb = new StringBuilder();

        // Constrói a lista de fakes
        for (int index = 0; index < nicked.size(); index++) {
            String fakeName = nicked.get(index);
            sb.append(String.format(MSG_FAKE_FORMAT, fakeName, fakeName));
            
            // Adiciona quebra de linha se não for o último
            if (index + 1 < nicked.size()) {
                sb.append("\n");
            }
        }

        // Limpa a lista para liberar memória
        nicked.clear();
        
        // Verifica se há fakes para exibir
        if (sb.length() == 0) {
            sb.append(MSG_NO_FAKES);
        }

        // Envia a mensagem formatada
        String finalMessage = MSG_HEADER + sb.toString() + MSG_FOOTER;
        player.sendMessage(finalMessage);
    }
}