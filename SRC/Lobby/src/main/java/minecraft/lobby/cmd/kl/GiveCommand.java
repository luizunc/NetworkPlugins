package minecraft.lobby.cmd.kl;

import minecraft.lobby.cmd.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {
  
  public GiveCommand() {
    super("dar", "dar [jogador]", "Comando removido.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    sender.sendMessage("§cEste comando foi removido.");
  }
  
  @Override
  public void perform(Player player, String[] args) {}
}
