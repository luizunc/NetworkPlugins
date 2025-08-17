package dev.slickcollections.kiwizin.bedwars.cmd.bw;

import dev.slickcollections.kiwizin.bedwars.Main;
import dev.slickcollections.kiwizin.bedwars.cmd.SubCommand;
import dev.slickcollections.kiwizin.bedwars.game.BedWars;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CloneCommand extends SubCommand {
  
  public CloneCommand() {
    super("clonar", "clonar [mundo] [novoMundo]", "Clonar uma sala.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage("§cUtilize /bw " + this.getUsage());
      return;
    }
    
    BedWars game = BedWars.getByWorldName(args[0]);
    if (game == null) {
      sender.sendMessage("§cNão existe uma sala neste mundo.");
      return;
    }
    
    String newWorld = args[1];
    if (BedWars.getByWorldName(newWorld) != null) {
      sender.sendMessage("§cJá existe uma sala no mundo \"" + newWorld + "\".");
      return;
    }
    
    sender.sendMessage("§aRealizando processo de clonação...");
    KConfig config = Main.getInstance().getConfig("arenas", newWorld);
    for (String key : game.getConfig().getConfig().getKeys(false)) {
      Object value = game.getConfig().getConfig().get(key);
      if (value instanceof String) {
        value = ((String) value).replace(game.getGameName(), newWorld);
      } else if (value instanceof List) {
        List<String> list = new ArrayList<>();
        for (String v : (List<String>) value) {
          list.add(v.replace(game.getGameName(), newWorld));
        }
        value = list;
      }
      
      config.set(key, value);
    }
    
    Main.getInstance().getFileUtils().copyFiles(new File("plugins/kBedWars/mundos", args[0]), new File("plugins/kBedWars/mundos", newWorld));
    BedWars.load(config.getFile(), () -> sender.sendMessage("§aA sala foi clonada."));
  }
}
