package tk.kiwicollections.kiwizin.utils.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import tk.kiwicollections.kiwizin.utils.bungee.cmd.Commands;
import tk.kiwicollections.kiwizin.utils.bungee.listeners.Listeners;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Bungee extends Plugin {

    public static Map<ProxiedPlayer, ProxiedPlayer> baianice = new HashMap<>();
    private static Bungee instance;

    public Bungee() {
        instance = this;
    }

    public static Bungee getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Commands.setupCommands();
        getProxy().getPluginManager().registerListener(this, new Listeners());
        this.getLogger().info("O plugin foi ativado.");
    }
}
