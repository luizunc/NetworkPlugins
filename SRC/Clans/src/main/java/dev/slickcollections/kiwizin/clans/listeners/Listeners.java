package dev.slickcollections.kiwizin.clans.listeners;

import dev.slickcollections.kiwizin.clans.Main;
import dev.slickcollections.kiwizin.clans.api.ClanAPI;
import dev.slickcollections.kiwizin.clans.clan.Clan;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
  
  private static boolean reportCard;
  
  public static void setupListeners() {
    reportCard =
        Main.getInstance().getConfig().getBoolean("join-report-card", true);
    Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      DataContainer container = profile.getDataContainer("kCoreProfile", "clan");
      if (ClanAPI.getClanByPlayer(player) != null) {
        container.set((ClanAPI.getClanByPlayer(player).tagPermissionPlus ? "§6" : ClanAPI.getClanByPlayer(player).tagPermission ? "§7" : "§8") +
            "[" + ClanAPI.getClanByPlayer(player).getTag() + "]");
      } else {
        container.set("");
      }
    }
    
    if (reportCard) {
      Clan.boletimClanJoin(player);
    }
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    ChatHandler.INVITE.clearCreating(evt.getPlayer());
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChatC(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    
    if (ChatHandler.INVITE.isCreating(player)) {
      evt.setCancelled(true);
      if (evt.getMessage().equalsIgnoreCase("cancelar")) {
        ChatHandler.INVITE.clearCreating(player);
        player.sendMessage("§cOperação cancelada.");
        return;
      }
      
      String name = evt.getMessage();
      Player target = Bukkit.getPlayerExact(name);
      if (target == null) {
        player.sendMessage("§cEste usuário não está online.");
        player.sendMessage(
            " \n§aQuem você deseja convidar?\n§7Digite o nome do usuário ou §lCANCELAR §7para cancelar.\n ");
        return;
      }
      
      if (dev.slickcollections.kiwizin.clans.clan.Clan.getClan(target) != null) {
        player.sendMessage("§cEste usuário já possui um clan.");
        player.sendMessage(
            " \n§aQuem você deseja convidar?\n§7Digite o nome do usuário ou §lCANCELAR §7para cancelar.\n ");
        return;
      }
      
      ChatHandler.INVITE.clearCreating(player);
      player.performCommand("clan convidar " + name);
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (ChatHandler.INVITE.isCreating(evt.getPlayer())) {
      evt.setCancelled(true);
      evt.getPlayer().sendMessage(
          " \n§aQuem você deseja convidar?\n§7Digite o nome do usuário ou §lCANCELAR §7para cancelar.\n ");
    }
  }
  
  @EventHandler
  public void onPlayerQuitt(PlayerQuitEvent evt) {
    ChatHandler.CREATING.clearCreating(evt.getPlayer());
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChatt(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    
    if (ChatHandler.CREATING.isCreating(player)) {
      evt.setCancelled(true);
      if (evt.getMessage().equalsIgnoreCase("cancelar")) {
        ChatHandler.CREATING.clearCreating(player);
        player.sendMessage("§cOperação cancelada.");
        return;
      }
      
      String[] arr = ChatHandler.CREATING.getCreating(player);
      if (arr[0] == null) {
        String name = evt.getMessage();
        if (name.length() > 16 || name.length() < 4) {
          player.sendMessage("§cO nome do clan precisa conter de 4 a 16 caracteres.");
          return;
        }
        
        dev.slickcollections.kiwizin.clans.clan.Clan clan = dev.slickcollections.kiwizin.clans.clan.Clan.getByName(name);
        if (clan != null) {
          player.sendMessage("§cUm clan com este nome já existe.");
          player.sendMessage(
              " \n§aQual será o nome do clan?\n§7Responda no chat ou digite §lCANCELAR §7para cancelar a operação.\n ");
          return;
        }
        
        arr[0] = name;
        player.sendMessage(
            " \n§aQual será a tag do clan?\n§7Responda no chat ou digite §lCANCELAR §7para cancelar.\n ");
        return;
      }
      
      if (arr[1] == null) {
        String tag = evt.getMessage();
        if (tag.length() > 5 || tag.length() < 3) {
          player.sendMessage("§cA tag do clan precisa conter de 3 a 5 caracteres.");
          return;
        }
        
        dev.slickcollections.kiwizin.clans.clan.Clan clan = dev.slickcollections.kiwizin.clans.clan.Clan.getByTag(tag);
        if (clan != null) {
          player.sendMessage("§cUm clan com esta sigla já existe.");
          player.sendMessage(
              " \n§aQual será a tag do clan?\n§7Responda no chat ou digite §lCANCELAR §7para cancelar.\n ");
          return;
        }
        
        arr[1] = tag;
        player.sendMessage(" \n§aDeseja confirmar a criação do clan \"§f[" + tag + "] " + arr[0]
            + "§a\"?\n§7Responda SIM ou NAO no chat.\n ");
        return;
      }
      
      if (evt.getMessage().equalsIgnoreCase("sim")) {
        ChatHandler.CREATING.clearCreating(player);
        dev.slickcollections.kiwizin.clans.clan.Clan.createClan(player, arr[1], arr[0]);
      } else if (evt.getMessage().replace("ã", "a").equalsIgnoreCase("nao")) {
        ChatHandler.CREATING.clearCreating(player);
        player.sendMessage("§cOperação cancelada.");
      } else {
        player.sendMessage(" \n§aDeseja confirmar a criação do clan \"§f[" + arr[0] + "] " + arr[1]
            + "§a\"?\n§7Responda SIM ou NAO no chat.\n ");
      }
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocesss(PlayerCommandPreprocessEvent evt) {
    Player player = evt.getPlayer();
    if (ChatHandler.CREATING.isCreating(player)) {
      evt.setCancelled(true);
      String[] arr = ChatHandler.CREATING.getCreating(player);
      if (arr[0] == null) {
        player.sendMessage(
            " \n§aQual será o nome do clan?\n§7Responda no chat ou digite §lCANCELAR §7para cancelar a operação.\n ");
      } else if (arr[1] == null) {
        player.sendMessage(
            " \n§aQual será a tag do clan?\n§7Responda no chat ou digite §lCANCELAR §7para cancelar.\n ");
      } else {
        player.sendMessage(" \n§aDeseja confirmar a criação do clan \"§f[" + arr[0] + "] " + arr[1]
            + "§a\"?\n§7Responda SIM ou NAO no chat.\n ");
      }
    }
  }
}
