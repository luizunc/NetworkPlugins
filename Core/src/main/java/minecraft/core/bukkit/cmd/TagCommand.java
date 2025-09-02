package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.menu.TagsCommandMenu;
import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.database.exception.ProfileLoadException;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.rank.RankPermissionUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.TagUtils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TagCommand extends Commands {

    public TagCommand() {
        super("tag", "tags", "etiqueta");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Abrir menu de tags
            try {
                Profile profile = Profile.createOrLoadProfile(player.getName());
                new TagsCommandMenu(profile);
            } catch (Exception e) {
                player.sendMessage("§cErro ao carregar perfil: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        if (args.length == 1) {
            String tagName = args[0];
            
            // Verificar se a tag existe
            Rank rank = Rank.getRoleByName(tagName);
            if (rank == null) {
                player.sendMessage("§cTag não encontrada!");
                return;
            }

            // Verificar se o jogador tem permissão para usar a tag (rank específico ou superior)
            // A tag "Membro" sempre deve estar disponível para todos
            // Tags especiais são liberadas para Mod+ ou superior
            if (!rank.isDefault() && 
                !RankPermissionUtils.hasRankOrHigher(player, rank.getPermission()) && 
                !isSpecialTagAvailable(player, rank)) {
                player.sendMessage("§cVocê não tem permissão para usar esta tag!");
                return;
            }

            // Aplicar apenas a tag visual (sem alterar permissões)
            applyTagVisual(player, rank);
            
        } else {
            player.sendMessage("§cUso: /tag <nome_da_tag>");
        }
    }

    /**
     * Aplica APENAS a tag visual ao jogador, SEM ALTERAR PERMISSÕES OU RANK.
     * IMPORTANTE: Este método NUNCA toca na coluna rank do MySQL.
     * As permissões sempre vêm da coluna rank e NUNCA são alteradas por tags.
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
            
            // Enviar mensagem de confirmação
            player.sendMessage("§aTag " + rank.getName() + " §aselecionada.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            
        } catch (ProfileLoadException e) {
            player.sendMessage("§cErro ao carregar perfil: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            player.sendMessage("§cErro ao alterar tag: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica se uma tag especial está disponível para o jogador.
     * Tags especiais (Férias, Halloween, Natal, Carnaval) são liberadas para Mod+ ou superior.
     * 
     * @param player Jogador
     * @param rank Rank da tag
     * @return true se a tag especial está disponível
     */
    private boolean isSpecialTagAvailable(Player player, Rank rank) {
        // Verificar se é uma tag especial
        String permission = rank.getPermission();
        if (permission != null && permission.startsWith("tag.")) {
            // Tags especiais são liberadas para Mod+ ou superior
            return RankPermissionUtils.hasModOrHigher(player);
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            // Retornar apenas tags que o jogador tem permissão para usar
            if (sender instanceof Player) {
                Player player = (Player) sender;
                return Rank.listRoles().stream()
                    .filter(rank -> rank.isDefault() || rank.has(player) || isSpecialTagAvailable(player, rank)) // Tag "Membro" sempre disponível + permissão baseada no rank mais alto + tags especiais para Mod+
                    .map(rank -> StringUtils.stripColors(rank.getName()))
                    .collect(Collectors.toList());
            }
        }
        return null;
    }
}
