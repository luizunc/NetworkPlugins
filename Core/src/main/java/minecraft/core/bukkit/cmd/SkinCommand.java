package minecraft.core.bukkit.cmd;

import minecraft.core.bukkit.listeners.UpdateSkin;
import minecraft.core.bukkit.menus.MenuSkins;
import minecraft.core.core.api.profile.Mojang;
import minecraft.core.core.database.data.container.SkinsContainer;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.RankPermissionUtils;
import minecraft.core.core.player.skin.SkinCooldown;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkinCommand extends Commands {

    public SkinCommand() {
        super("skin");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
            return;
        }

        Player player = (Player) sender;
        if (!RankPermissionUtils.hasIronOrHigher(player)) {
            player.sendMessage("§cVocê precisa do VIP para usar este comando.");
            return;
        }

        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            player.sendMessage("§cSeu perfil não foi carregado corretamente.");
            return;
        }

        if (args.length == 0) {
            // Abre o menu de skins
            new MenuSkins(player, profile);
            return;
        }

        if (SkinCooldown.hasCooldown(player.getName())) {
            player.sendMessage("§cVocê precisa aguardar " + SkinCooldown.getRemainingTime(player.getName()) + " para trocar de skin novamente!");
            return;
        }

        String targetName = args[0];
        try {
            String id = Mojang.getUUID(targetName);
            if (id == null) {
                player.sendMessage("§cJogador não encontrado!");
                return;
            }

            String textures = Mojang.getSkinProperty(id);
            if (textures == null) {
                player.sendMessage("§cNão foi possível obter a skin deste jogador!");
                return;
            }

            String[] textureData = textures.split(" : ");
            if (textureData.length < 3) {
                player.sendMessage("§cNão foi possível obter a skin deste jogador!");
                return;
            }

            String value = textureData[1];
            String signature = textureData[2];

            SkinsContainer container = profile.getSkinsContainer();
            container.setOriginalSkin(targetName);
            UpdateSkin.updateSkin(player, value, signature);
            profile.save();
            SkinCooldown.addCooldown(player.getName());
        } catch (Exception e) {
            player.sendMessage("§cOcorreu um erro ao tentar obter a skin deste jogador!");
        }
    }
} 