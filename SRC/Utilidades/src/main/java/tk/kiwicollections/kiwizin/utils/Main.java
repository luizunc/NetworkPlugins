package tk.kiwicollections.kiwizin.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import tk.kiwicollections.kiwizin.utils.cmd.Commands;
import tk.kiwicollections.kiwizin.utils.items.object.AbstractItem;
import tk.kiwicollections.kiwizin.utils.listeners.Listeners;
import tk.kiwicollections.kiwizin.utils.roles.object.Role;
import tk.kiwicollections.kiwizin.utils.upgrades.npc.NPCUpgrade;
import tk.kiwicollections.kiwizin.utils.upgrades.object.Upgrade;
import tk.slicecollections.maxteer.cash.CashManager;
import tk.slicecollections.maxteer.plugin.MPlugin;
import tk.slicecollections.maxteer.reflection.Accessors;
import tk.slicecollections.maxteer.reflection.acessors.FieldAccessor;

import java.util.HashMap;

public class Main extends MPlugin {

    public static HashMap<Player, Player> reply = new HashMap<>();
    private static Main instance;
    public static boolean validInit;

    @Override
    public void start() {
        instance = this;
    }

    @Override
    public void load() {}

    @Override
    public void enable() {

        saveDefaultConfig();

        if (!CashManager.CASH) {
            this.getLogger().warning("Ative o cash do mCore para que o plugin funcione.");
            System.exit(0);
        }

        AbstractItem.setupItems();

        NPCUpgrade.setupNPCs();
        Listeners.setupListeners();

        Language.setupLanguage();
        Commands.setupCommands();
        Role.setupRoles();

        validInit = true;
        this.getLogger().info("O plugin foi ativado.");
    }

    @Override
    public void disable() {
        if (validInit) {
            NPCUpgrade.listNPCs().forEach(NPCUpgrade::destroy);
        }

        this.getLogger().info("O plugin foi desativado.");
    }

    public static Main getInstance() {
        return instance;
    }

}
