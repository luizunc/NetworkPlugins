package minecraft.core.bukkit.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import minecraft.core.bukkit.Core;

import minecraft.core.core.player.nick.NickManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Server.*;

@SuppressWarnings("unchecked")
public class NickAdapter extends PacketAdapter {
  
  public NickAdapter() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Client.CHAT, TAB_COMPLETE, PLAYER_INFO, CHAT, SCOREBOARD_OBJECTIVE, SCOREBOARD_SCORE, SCOREBOARD_TEAM));
  }
  
  public NickAdapter(Core plugin) {
    super(params().plugin(plugin).types(PacketType.Play.Client.CHAT, TAB_COMPLETE, PLAYER_INFO, CHAT, SCOREBOARD_OBJECTIVE, SCOREBOARD_SCORE, SCOREBOARD_TEAM));
  }
  
  @Override
  public void onPacketReceiving(PacketEvent evt) {
    PacketContainer packet = evt.getPacket();
    if (packet.getType() == PacketType.Play.Client.CHAT) {
      String command = packet.getStrings().read(0);
      if (command.startsWith("/")) {
        // Substitui nomes de jogadores com nick
        packet.getStrings().write(0, NickManager.replaceNickedPlayers(packet.getStrings().read(0), false));
      } else {
        // Substitui mudanças de nick
        packet.getStrings().write(0, NickManager.replaceNickedChanges(packet.getStrings().read(0)));
      }
    }
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
    PacketContainer packet = evt.getPacket();
    if (packet.getType() == TAB_COMPLETE) {
      List<String> list = new ArrayList<>();
      for (String complete : packet.getStringArrays().read(0)) {
        // Substitui nomes de jogadores com nick
        list.add(NickManager.replaceNickedPlayers(complete, true));
      }
      
      packet.getStringArrays().write(0, list.toArray(new String[0]));
    } else if (packet.getType() == PLAYER_INFO) {
      List<PlayerInfoData> infoDataList = new ArrayList<>();
      for (PlayerInfoData infoData : packet.getPlayerInfoDataLists().read(0)) {
        WrappedGameProfile profile = infoData.getProfile();
        if (NickManager.isNick(profile.getName())) {
          infoData = new PlayerInfoData(NickManager.cloneProfile(profile), infoData.getLatency(), infoData.getGameMode(), infoData.getDisplayName());
        }
        
        infoDataList.add(infoData);
      }
      
      packet.getPlayerInfoDataLists().write(0, infoDataList);
    } else if (packet.getType() == CHAT) {
      WrappedChatComponent component = packet.getChatComponents().read(0);
      if (component != null) {
        // Substitui nomes de jogadores com nick
        packet.getChatComponents().write(0, WrappedChatComponent.fromJson(NickManager.replaceNickedPlayers(component.getJson(), true)));
      }
      BaseComponent[] components = (BaseComponent[]) packet.getModifier().read(1);
      if (components != null) {
        List<BaseComponent> newComps = new ArrayList<>();
        for (BaseComponent comp : components) {
          TextComponent newComp = new TextComponent("");
          for (BaseComponent newTextComp : ComponentSerializer.parse(NickManager.replaceNickedPlayers(ComponentSerializer.toString(comp), true))) {
            newComp.addExtra(newTextComp);
          }
          newComps.add(newComp);
        }
        packet.getModifier().write(1, newComps.toArray(new BaseComponent[0]));
      }
    } else if (packet.getType() == SCOREBOARD_OBJECTIVE) {
      // Substitui nomes de jogadores com nick
      packet.getStrings().write(1, NickManager.replaceNickedPlayers(packet.getStrings().read(1), true));
    } else if (packet.getType() == SCOREBOARD_SCORE) {
      // Substitui nomes de jogadores com nick
      packet.getStrings().write(0, NickManager.replaceNickedPlayers(packet.getStrings().read(0), true));
    } else if (packet.getType() == SCOREBOARD_TEAM) {
      List<String> members = new ArrayList<>();
      for (String member : (Collection<String>) packet.getModifier().withType(Collection.class).read(0)) {
        if (NickManager.isNick(member)) {
          member = NickManager.getNick(member);
        }
        
        members.add(member);
      }
      
      packet.getModifier().withType(Collection.class).write(0, members);
    }
  }
}
