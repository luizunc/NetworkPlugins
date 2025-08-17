package tk.kiwicollections.kiwizin.utils.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.cmd.utils.*;
import tk.kiwicollections.kiwizin.utils.items.cmd.ItemsCommand;
import tk.kiwicollections.kiwizin.utils.items.cmd.LobbyItemsCommand;
import tk.kiwicollections.kiwizin.utils.lobby.LobbyCommand;
import tk.kiwicollections.kiwizin.utils.roles.cmd.SetRoleCommand;
import tk.kiwicollections.kiwizin.utils.upgrades.npc.cmd.UpgradeNPCCommand;
import tk.slicecollections.maxteer.Core;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * @author Maxter
 */
public abstract class Commands extends Command {

    public Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));

        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "mcore", this);
        } catch (ReflectiveOperationException ex) {
            Core.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
        }
    }

    public abstract void perform(CommandSender sender, String label, String[] args);

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        this.perform(sender, commandLabel, args);
        return true;
    }

    public static void setupCommands() {
        new SetRoleCommand();
        new PingCommand();
        new OnlineCommand();
        new UpgradeNPCCommand();
        new FlyCommand();
       // new AdminCommand();
        new InvseeCommand();
        new LobbyItemsCommand();
        new GamemodeCommand();
        new LobbyCommand();
        if (!Language.options$bungeecord) {
            new WarningCommand();
            new StaffChatCommand();
            new TellCommand();
            new ReplyCommand();
        }
        new ItemsCommand();
    }
}