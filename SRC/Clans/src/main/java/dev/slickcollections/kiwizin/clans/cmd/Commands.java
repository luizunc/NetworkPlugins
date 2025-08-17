package dev.slickcollections.kiwizin.clans.cmd;

import dev.slickcollections.kiwizin.clans.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class Commands extends Command {
  
  public Commands(String name, String... aliases) {
    super(name);
    this.setAliases(Arrays.asList(aliases));
    
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      simpleCommandMap.register(this.getName(), "kclans", this);
    } catch (ReflectiveOperationException ex) {
      Main.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
    }
  }
  
  public static void setupCommands() {
    // TODO: Megumin
    new ClanCommand();
    new Commands("kcl", "kclans") {
      
      @Override
      public void perform(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
          Player player = (Player) sender;
          if (!player.hasPermission("kclans.cmd.kcl")) {
            player.sendMessage("§6kClans §bv" + Main.getInstance().getDescription().getVersion() + " §7Criado por §6Kiwizin§7.");
            return;
          }
          
          if (args.length == 0) {
            player.sendMessage(" \n§6/kcl atualizar §f- §7Atualizar o kClans.\n ");
            return;
          }
          
          String action = args[0];
          if (action.equalsIgnoreCase("atualizar")) {
            player.sendMessage("§aO plugin já se encontra em sua última versão.");
          } else {
            player.sendMessage(" \n§6/kcl atualizar §f- §7Atualizar o kClans.\n ");
          }
        } else {
          sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
        }
      }
    };
  }
  
  public abstract void perform(CommandSender sender, String label, String[] args);
  
  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    this.perform(sender, commandLabel, args);
    return true;
  }
}
