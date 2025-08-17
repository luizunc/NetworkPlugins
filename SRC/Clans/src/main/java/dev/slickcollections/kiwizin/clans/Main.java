package dev.slickcollections.kiwizin.clans;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slickcollections.kiwizin.clans.clan.Clan;
import dev.slickcollections.kiwizin.clans.cmd.Commands;
import dev.slickcollections.kiwizin.clans.database.Database;
import dev.slickcollections.kiwizin.clans.listeners.Listeners;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Main extends KPlugin {
  
  public static boolean validInit;
  private static Main instance;
  
  public static Main getInstance() {
    return instance;
  }
  
  public static void sendBungee(Player player, String subChannel, String tag, String... strings) {
    if (getInstance().getConfig().getBoolean("bungeecord")) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF(subChannel);
      out.writeUTF(tag);
      for (String string : strings) {
        out.writeUTF(string);
      }
      player.sendPluginMessage(getInstance(), "kClans", out.toByteArray());
    }
  }
  
  public void start() {
    instance = this;
  }
  
  public void load() {}
  
  public void enable() {
    saveDefaultConfig();
    
    Database.setupDatabase();
    
    Commands.setupCommands();
    Listeners.setupListeners();
    
    if (this.getConfig().getBoolean("bungeecord")) {
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "kClans");
      Bukkit.getMessenger().registerIncomingPluginChannel(this, "kClans", (channel, arg1, msg) -> {
        if (channel.equals("kClans")) {
          ByteArrayDataInput in = ByteStreams.newDataInput(msg);
          
          String subChannel = in.readUTF();
          switch (subChannel) {
            case "Create":
              Clan.loadClan(in.readUTF());
              break;
            case "Update": {
              Clan clan = Clan.getByTag(in.readUTF());
              if (clan != null) {
                clan.reload();
              }
              break;
            }
            case "Destroy": {
              Clan clan = Clan.getByTag(in.readUTF());
              if (clan != null) {
                clan.destroy(false);
              }
              break;
            }
            case "Broadcast": {
              Clan clan = Clan.getByTag(in.readUTF());
              if (clan != null) {
                clan.broadcast(in.readUTF(), Boolean.parseBoolean(in.readUTF()), "blockBungee");
              }
              break;
            }
          }
        }
      });
    }
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  public void disable() {
    
    this.getLogger().info("O plugin foi desativado.");
  }
}
