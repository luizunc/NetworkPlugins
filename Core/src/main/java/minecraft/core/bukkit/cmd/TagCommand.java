package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.menu.TagsCommandMenu;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.fake.FakeManager;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.TagUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TagCommand extends Commands {

    public TagCommand() {
        super("tag");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());

        if (FakeManager.isFake(player.getName())) {
            player.sendMessage("§cNão é possível executar este comando com o /nick ativado.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5f, 2.0f);
            return;
        }

        List<String> tags = Rank.listRoles().stream()
                .filter(role -> player.hasPermission(role.getPermission()) || role.isDefault())
                .map(Rank::getName)
                .collect(Collectors.toList());

        if (args.length == 0) {
            sendAvailableTagsMessage(player, tags);
            return;
        }

        String action = args[0];
        if (tags.stream().noneMatch(tag -> StringUtils.stripColors(tag).equalsIgnoreCase(action))) {
            player.sendMessage("§cTag não encontrada.");
            return;
        }

        String selectedTag = tags.stream()
                .filter(tag -> StringUtils.stripColors(tag).equalsIgnoreCase(action))
                .findFirst().get();

        Rank role = Rank.getRoleByName(StringUtils.stripColors(selectedTag));

        if (profile.getDataContainer("account", "tag").getAsString().equalsIgnoreCase(StringUtils.stripColors(role.getName()))) {
            player.sendMessage("§aVocê já está utilizando a tag " + role.getName() + "§c.");
        }

        player.sendMessage("§aVocê selecionou a tag " + role.getName() + "§a.");
        
        // Atualizar o cache
        minecraft.core.core.database.cache.TagCache.setCache(player.getName(), StringUtils.stripColors(role.getName()), player.getName());
        
        TagUtils.setTag(player, role);
        DataContainer container = profile.getDataContainer("account", "tag");
        container.set(StringUtils.stripColors(role.getName()));
        profile.save();

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
    }

    private void sendAvailableTagsMessage(Player player, List<String> tags) {
        new TagsCommandMenu(Profile.getProfile(player.getName()));
    }
}
