package dev.slickcollections.kiwizin.clans.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Bungee extends Plugin implements Listener {
  
  @Override
  public void onEnable() {
    this.getProxy().registerChannel("kClans");
    this.getProxy().getPluginManager().registerListener(this, this);
    
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @EventHandler
  public void onPluginMessage(PluginMessageEvent evt) {
    if (evt.getTag().equals("kClans")) {
      if (evt.getSender() instanceof Server && evt.getReceiver() instanceof ProxiedPlayer) {
        Server server = (Server) evt.getSender();
        ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
        String subChannel = in.readUTF();
        String tag = in.readUTF();
        
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        out.writeUTF(tag);
        if (subChannel.equals("Broadcast")) {
          out.writeUTF(in.readUTF());
          out.writeUTF(in.readUTF());
        }
        for (ServerInfo info : Bungee.this.getProxy().getServers().values()) {
          if (!info.getName().equals(server.getInfo().getName())) {
            info.sendData("kClans", out.toByteArray());
          }
        }
      }
    }
  }
}
