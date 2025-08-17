package dev.slickcollections.kiwizin.murder.cmd.mm;

import dev.slickcollections.kiwizin.murder.cmd.SubCommand;
import dev.slickcollections.kiwizin.murder.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.murder.hook.mysterybox.MysteryBoxesHook;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MysteryBoxesCommand extends SubCommand {

    public MysteryBoxesCommand() {
        super("mb", "mb sync", "Sincronizar cosméticos com o kMysteryBox.", false);
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUtilize /mm " + this.getUsage());
            return;
        }

        String action = args[0];
        if (action.equalsIgnoreCase("sync")) {
            sender.sendMessage("§aSincronizando cosméticos...");
            MysteryBoxesHook.sync(sender);
        } else if ((!(sender instanceof Player)) && action.equalsIgnoreCase("give")) {
            if (args.length <= 2) {
                return;
            }

            Profile profile = Profile.getProfile(args[1]);
            if (profile == null) {
                return;
            }

            Cosmetic cosmetic = Cosmetic.findById(args[2]);
            if (cosmetic.has(profile)) {
                double coins = cosmetic.getCoins();
                if (coins <= 0) {
                    coins = 100;
                }
                coins /= 10;
                if (coins < 100) {
                    coins += 100;
                }
                profile.addStats("kMysteryBox", 50, "mystery_frags");
                profile.addCoins("kCoreMurder", coins);
                profile.getPlayer().sendMessage("§aVocê recebeu §6" + coins + " Coins §apor já possuir " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + "§a!");
            } else {
                cosmetic.give(profile);
                profile.getPlayer().sendMessage("§aVocê recebeu " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + " §aatravés de uma Cápsula Mágica!");
            }
        } else {
            sender.sendMessage("§cUtilize /mm " + this.getUsage());
        }
    }

}