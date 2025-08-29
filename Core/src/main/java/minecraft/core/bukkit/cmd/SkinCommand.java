package minecraft.core.bukkit.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.core.core.player.Profile;
import minecraft.core.core.database.data.container.SkinsContainer;
import minecraft.core.bukkit.listeners.UpdateSkin;
import minecraft.core.core.player.skin.SkinCooldown;
import minecraft.core.core.api.profile.Mojang;

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
        if (!player.hasPermission("rank.vip")) {
            player.sendMessage("§cVocê precisa do VIP para usar este comando.");
            return;
        }

        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            player.sendMessage("§cSeu perfil não foi carregado corretamente.");
            return;
        }

        if (args.length == 0) {
            player.sendMessage("§cUtilize /skin [jogador]");
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
            player.sendMessage("§aSua skin foi atualizada para a skin de " + targetName + "!");
        } catch (Exception e) {
            player.sendMessage("§cOcorreu um erro ao tentar obter a skin deste jogador!");
        }
    }
}
