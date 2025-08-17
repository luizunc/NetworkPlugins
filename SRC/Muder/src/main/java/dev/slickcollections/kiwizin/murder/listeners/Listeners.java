package dev.slickcollections.kiwizin.murder.listeners;

import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.listeners.entity.EntityListener;
import dev.slickcollections.kiwizin.murder.listeners.server.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import dev.slickcollections.kiwizin.murder.listeners.player.AsyncPlayerChatListener;
import dev.slickcollections.kiwizin.murder.listeners.player.InventoryClickListener;
import dev.slickcollections.kiwizin.murder.listeners.player.PlayerDeathListener;
import dev.slickcollections.kiwizin.murder.listeners.player.PlayerInteractListener;
import dev.slickcollections.kiwizin.murder.listeners.player.PlayerJoinListener;
import dev.slickcollections.kiwizin.murder.listeners.player.PlayerQuitListener;
import dev.slickcollections.kiwizin.murder.listeners.player.PlayerRestListener;

public class Listeners {

  public static void setupListeners() {
    try {
      PluginManager pm = Bukkit.getPluginManager();

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new EntityListener(), Main.getInstance());

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new AsyncPlayerChatListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new InventoryClickListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerDeathListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerInteractListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerJoinListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerQuitListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerRestListener(), Main.getInstance());

      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new ServerListener(), Main.getInstance());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
