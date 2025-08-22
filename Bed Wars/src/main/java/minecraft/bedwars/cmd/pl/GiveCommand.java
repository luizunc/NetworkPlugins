package minecraft.bedwars.cmd.pl;

import minecraft.bedwars.cmd.SubCommand;
import minecraft.core.core.player.Profile;
import org.bukkit.command.CommandSender;

public class GiveCommand extends SubCommand {
  
  public GiveCommand() {
    super("dar", "dar [jogador] coins", "Dar coins.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§6/pl dar [jogador] coins [quantia]\n ");
      return;
    }
    
    Profile target = Profile.getProfile(args[0]);
    if (target == null) {
      sender.sendMessage("§cUsuário não encontrado.");
      return;
    }
    
    String action = args[1];
    if (action.equalsIgnoreCase("coins")) {
      if (args.length < 3) {
        sender.sendMessage("§cUso: /pl dar [jogador] coins [quantia]");
        return;
      }
      
      try {
        double coins = Double.parseDouble(args[2]);
        if (coins < 1.0D) {
          throw new Exception();
        }
        
        target.addCoins("bedwars", coins);
        sender.sendMessage("§aCoins adicionados.");
      } catch (Exception ex) {
        sender.sendMessage("§cUtilize números válidos.");
      }
    } else {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§6/pl dar [jogador] coins [quantia]\n ");
    }
  }
}
