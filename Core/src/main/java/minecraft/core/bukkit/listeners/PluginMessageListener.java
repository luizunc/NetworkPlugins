package minecraft.core.bukkit.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import minecraft.core.bukkit.BukkitParty;
import minecraft.core.bukkit.BukkitPartyManager;
import minecraft.core.core.nms.NMS;
import minecraft.core.core.party.PartyPlayer;
import minecraft.core.core.player.Profile;

import minecraft.core.core.utils.enums.EnumSound;
import minecraft.core.core.player.nick.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static minecraft.core.core.party.PartyRole.MEMBER;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
  
  @Override
  public void onPluginMessageReceived(String channel, Player receiver, byte[] data) {
    if (channel.equals("Core")) {
      ByteArrayDataInput in = ByteStreams.newDataInput(data);
      
      String subChannel = in.readUTF();
      switch (subChannel) {
        case "NICK": {
          Player player = Bukkit.getPlayerExact(in.readUTF());
          if (player != null) {
            String nickName = in.readUTF();
            String roleName = in.readUTF();
            String skin = in.readUTF();
            // Aplica o nick
            NickManager.applyNick(player, nickName, roleName, skin);
            NMS.refreshPlayer(player);
          }
          break;
        }
        case "NICK_BOOK": {
          Player player = Bukkit.getPlayerExact(in.readUTF());
          if (player != null) {
            try {
              String sound = in.readUTF();
              EnumSound.valueOf(sound).play(player, 1.0F, sound.contains("VILL") ? 1.0F : 2.0F);
            } catch (Exception ignore) {
            }
            // Envia o livro de seleção de cargo
            NickManager.sendRole(player);
          }
          break;
        }
        case "SEND_PARTY": {
          Player player = Bukkit.getPlayerExact(in.readUTF());
          Player leader = Bukkit.getPlayerExact(in.readUTF());
          if (player != null && leader != null) {
            Profile profile = Profile.getProfile(player.getName());
            Profile pLeader = Profile.getProfile(leader.getName());
            if (pLeader.getGame() == null) {
              return;
            }
            if (profile.getGame() != null) {
              profile.getGame().leave(profile, null);
            }
            pLeader.getGame().join(profile);
          }
        }
        case "NICK_BOOK2": {
          Player player = Bukkit.getPlayerExact(in.readUTF());
          if (player != null) {
            String roleName = in.readUTF();
          String sound = in.readUTF();
          EnumSound.valueOf(sound).play(player, 1.0F, sound.contains("VILL") ? 1.0F : 2.0F);
          // Envia o livro de seleção de skin
          NickManager.sendSkin(player, roleName);
        }
        break;
      }
        case "PARTY":
          try {
            JSONObject changes = (JSONObject) new JSONParser().parse(in.readUTF());
            String leader = changes.get("leader").toString();
            boolean delete = changes.containsKey("delete");
            BukkitParty party = BukkitPartyManager.getLeaderParty(leader);
            if (party == null) {
              if (delete) {
                return;
              }
              party = BukkitPartyManager.createParty(leader, 0);
            }
            
            if (delete) {
              party.delete();
              return;
            }
            
            if (changes.containsKey("newLeader")) {
              party.transfer(changes.get("newLeader").toString());
            }
            
            if (changes.containsKey("remove")) {
              party.listMembers().removeIf(pp -> pp.getName().equalsIgnoreCase(changes.get("remove").toString()));
            }
            
            for (Object object : (JSONArray) changes.get("members")) {
              if (!party.isMember(object.toString())) {
                party.listMembers().add(new PartyPlayer(object.toString(), MEMBER));
              }
            }
          } catch (ParseException ignore) {
          }
          break;
      }
    }
  }
}
