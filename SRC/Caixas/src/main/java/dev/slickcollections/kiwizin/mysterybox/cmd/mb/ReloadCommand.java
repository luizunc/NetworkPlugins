package dev.slickcollections.kiwizin.mysterybox.cmd.mb;

import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.cmd.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
  public ReloadCommand() {
    super("reload", "reload", "Recarrega os prêmios da DB do kMysteryBox.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    BoxContent.setupRewards();
    sender.sendMessage("§aOs conteúdos das Cápsulas Mágicas foram recarregados!");
  }
}