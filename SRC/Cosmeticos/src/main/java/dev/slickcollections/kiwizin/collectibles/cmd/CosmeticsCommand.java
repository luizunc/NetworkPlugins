package dev.slickcollections.kiwizin.collectibles.cmd;

import dev.slickcollections.kiwizin.collectibles.Language;
import dev.slickcollections.kiwizin.collectibles.Main;
import dev.slickcollections.kiwizin.collectibles.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.collectibles.hook.Users;
import dev.slickcollections.kiwizin.collectibles.hook.mysteryboxes.MysteryBoxesHook;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.collectibles.nms.NMS;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmeticsCommand extends Commands {
  
  protected CosmeticsCommand() {
    super("kcosmetics", "kcs");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!sender.hasPermission("kcosmetics.cmd.kcs")) {
      sender.sendMessage("§6kCosmetics §bv" + Main.getInstance().getDescription().getVersion() + " §7Criado por §6Nyskiwi§7.");
      return;
    }
    
    if (args.length == 0) {
      this.sendHelp(sender);
      return;
    }
    
    String subCommand = args[0];
    if (sender instanceof Player && subCommand.equalsIgnoreCase("atualizar")) {
      Player player = (Player) sender;
      player.sendMessage("§aO plugin já se encontra em sua última versão.");
    } else if (subCommand.equalsIgnoreCase("mb")) {
      if (args.length == 1) {
        sender.sendMessage("§cUtilize /kcs mb sync");
        return;
      }
      
      String action = args[1];
      if (action.equalsIgnoreCase("sync")) {
        sender.sendMessage("§aSincronizando cosméticos...");
        MysteryBoxesHook.sync(sender);
      } else if ((!(sender instanceof Player)) && action.equalsIgnoreCase("give")) {
        if (args.length <= 3) {
          return;
        }
        
        CUser user = Users.getByName(args[2]);
        if (user == null) {
          return;
        }
        
        Cosmetic cosmetic = Cosmetic.findById(args[3]);
        if (user.hasCosmetic(cosmetic)) {
          user.getProfile().addCoins("kCoreTheBridge", Language.settings$coins$duplicated);
          user.getProfile().addCoins("kCoreSkyWars", Language.settings$coins$duplicated);
          user.getProfile().addCoins("kCoreBedWars", Language.settings$coins$duplicated);
          user.getProfile().addCoins("kCoreMurder", Language.settings$coins$duplicated);
          user.getProfile().addStats("kMysteryBox", 50, "mystery_frags");
          user.getPlayer().sendMessage(
              "§aVocê recebeu §6" + Language.settings$coins$duplicated + " Coins §apor já possuir " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + "§a!");
        } else {
          user.addCosmetic(cosmetic);
          user.getPlayer().sendMessage("§aVocê recebeu " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + " §aatravés de uma Cápsula Mágica!");
        }
      } else {
        sender.sendMessage("§cUtilize /kcs mb sync");
      }
    } else if (subCommand.equalsIgnoreCase("ids")) {
      CosmeticType type;
      try {
        if (args.length == 1) {
          throw new Exception();
        }
        type = CosmeticType.valueOf(args[1].toUpperCase());
      } catch (Exception ex) {
        sender.sendMessage("§cUtilize /kcs ids [" + StringUtils.join(CosmeticType.values(), "/") + "]");
        return;
      }
      
      StringBuilder sb = new StringBuilder(" \n§7Lista de IDs:\n");
      for (Cosmetic cosmetic : Cosmetic.listCosmetics(type)) {
        sb.append("§7ID: §f").append(cosmetic.getUniqueId()).append(" §f| §7Nome: §f").append(cosmetic.getName()).append("\n");
      }
      sb.append(" ");
      sender.sendMessage(sb.toString());
    } else if (subCommand.equalsIgnoreCase("give")) {
      Player target;
      CUser user;
      int id;
      CosmeticType type = null;
      try {
        if (args.length <= 3) {
          throw new Exception();
        }
        target = Bukkit.getPlayerExact(args[1]);
        if (target == null || (user = Users.getByName(target.getName())) == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        type = CosmeticType.valueOf(args[2].toUpperCase());
        id = Integer.parseInt(args[3]);
      } catch (Exception ex) {
        sender.sendMessage("§cUtilize /kcs give [jogador] [" + StringUtils.join(CosmeticType.values(), "/") + " [id]");
        return;
      }
      
      int copy = id;
      Cosmetic cosmetic = Cosmetic.listCosmetics(type).stream().filter(c -> c.getUniqueId() == copy).findFirst().orElse(null);
      if (cosmetic != null) {
        if (user.hasCosmetic(cosmetic)) {
          sender.sendMessage("§aO Jogador já possui este cosmético.");
        } else {
          user.addCosmetic(cosmetic);
          sender.sendMessage("§aO cosmético §f\"" + cosmetic.getName() + "\" §afoi dado ao Jogador.");
        }
      } else {
        if (id == 0) {
          Cosmetic.listCosmetics(type).forEach(user::addCosmetic);
          sender.sendMessage("§aTodos os cosméticos desta categoria foram dados ao Jogador.");
        } else {
          sender.sendMessage("§cCosmético não encontrado.");
        }
      }
    }
  }
  
  private void sendHelp(CommandSender sender) {
    sender.sendMessage(" " + (sender instanceof Player ?
        "\n§6/kcs atualizar §f- §7Atualizar o kCosmetics." :
        "") + "\n§6/kcs mb sync §f- §7Sincronizar cosméticos com o kMysteryBox.\n§6/kcs ids [tipo de cosmético] §f- §7Lista os IDs de cada cosmético da categoria.\n§6/kcs give [jogador] [tipo de cosmético] [id] §f- §7Autoriza um cosmético ao usuário.\n ");
  }
}
