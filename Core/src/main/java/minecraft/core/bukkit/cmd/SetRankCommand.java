package minecraft.core.bukkit.cmd;

import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.rank.RankManager;
import minecraft.core.core.player.rank.RankPermissionUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para definir ranks de jogadores.
 * Permite que administradores definam ranks para outros jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class SetRankCommand extends Commands {

    // Permissões
    private static final String PERMISSION_SETRANK = "rank.admin";
    
    // Mensagens
    private static final String MSG_NO_PERMISSION = "§cVocê não tem permissão para usar este comando!";
    private static final String MSG_USAGE = "§cUso: /setrank <rank> <jogador>";
    private static final String MSG_RANK_NOT_FOUND = "§cRank não encontrado!";
    private static final String MSG_PLAYER_NOT_FOUND = "§cJogador não encontrado!";
    private static final String MSG_RANK_SET = "§aRank definido com sucesso!";
    private static final String MSG_RANK_REMOVED = "§aRank removido com sucesso!";

    public SetRankCommand() {
        super("setrank", "setarrank", "definirrank");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        // Verificar permissão de administrador ou superior
        if (!RankPermissionUtils.hasAdminOrHigher(sender)) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return;
        }

        // Verificar argumentos
        if (args.length == 0) {
            sender.sendMessage(MSG_USAGE);
            return;
        }

        // Verificar se o rank existe
        Rank rank = Rank.getRoleByName(args[0]);
        if (rank == null) {
            sender.sendMessage(MSG_RANK_NOT_FOUND);
            return;
        }

        // Verificar se o jogador foi especificado
        if (args.length < 2) {
            sender.sendMessage(MSG_USAGE);
            return;
        }

        String targetPlayerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer != null) {
            // Jogador online
            setRankOnline(sender, targetPlayer, rank);
        } else {
            // Jogador offline
            setRankOffline(sender, targetPlayerName, rank);
        }
    }

    /**
     * Define o rank para um jogador online.
     */
    private void setRankOnline(CommandSender sender, Player targetPlayer, Rank rank) {
        try {
            // Obter o profile do jogador
            Profile profile = Profile.createOrLoadProfile(targetPlayer.getName());
            
            // Definir o rank no profile (sem cores para salvar no banco)
            String cleanRankName = StringUtils.stripColors(rank.getName());
            profile.getDataContainer("account", "rank").set(cleanRankName);
            
            // Aplicar o rank usando o RankManager (inclui permissões)
            RankManager.applyRank(targetPlayer, rank);
            
            // IMPORTANTE: Aplicar automaticamente a tag visual correspondente ao rank
            // Isso simula o comando /tag <rank> sendo executado
            applyTagVisual(targetPlayer, rank);
            
            // Salvar o profile no banco de dados para persistir o rank
            profile.save();
            
            // Enviar mensagens de confirmação
            sender.sendMessage(MSG_RANK_SET + " §7" + targetPlayer.getName() + " §7agora virou " + rank.getName());
            targetPlayer.sendMessage("§aSeu rank foi alterado para " + rank.getName() + " §apor um administrador!");

            // Atualizar o jogador se estiver online
            if (targetPlayer.isOnline()) {
                // Forçar atualização do scoreboard e outros sistemas
                targetPlayer.updateInventory();
            }
            
        } catch (Exception e) {
            sender.sendMessage("§cErro ao definir rank: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Define o rank para um jogador offline.
     */
    private void setRankOffline(CommandSender sender, String playerName, Rank rank) {
        try {
            // Obter o profile do jogador offline
            Profile profile = Profile.createOrLoadProfile(playerName);
            
            // Definir o rank no profile (sem cores para salvar no banco)
            String cleanRankName = StringUtils.stripColors(rank.getName());
            profile.getDataContainer("account", "rank").set(cleanRankName);
            
            // IMPORTANTE: Aplicar automaticamente a tag visual correspondente ao rank
            // Para jogadores offline, salvamos tanto o rank quanto a tag
            profile.getDataContainer("account", "tag").set(cleanRankName);
            
            // Atualizar o cache de tags (para jogadores offline, salvamos no profile)
            TagCache.setCache(playerName, cleanRankName, playerName);
            
            // Salvar o profile no banco de dados para persistir o rank
            profile.save();
            
            // Enviar mensagem de confirmação
            sender.sendMessage(MSG_RANK_SET + " §7" + playerName + " §7agora virou " + rank.getName() + " §7(offline)");

        } catch (Exception e) {
            sender.sendMessage("§cErro ao definir rank: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aplica automaticamente a tag visual correspondente ao rank definido.
     * Simula o comando /tag sendo executado automaticamente.
     */
    private void applyTagVisual(Player player, Rank rank) {
        try {
            // Obter o profile do jogador
            Profile profile = Profile.createOrLoadProfile(player.getName());
            
            // IMPORTANTE: Salvar APENAS na coluna tag (NUNCA na coluna rank)
            String cleanTagName = StringUtils.stripColors(rank.getName());
            profile.getDataContainer("account", "tag").set(cleanTagName);
            
            // Atualizar o cache de tags (apenas para referência visual)
            TagCache.setCache(player.getName(), cleanTagName, player.getName());
            
            // Aplicar APENAS a tag visual usando TagUtils (sem permissões)
            TagUtils.setTag(player, rank);
            
            // Salvar o profile para persistir a tag selecionada
            profile.save();
            
        } catch (Exception e) {
            // Log do erro mas não interromper o processo
            System.err.println("Erro ao aplicar tag visual automaticamente: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 