package dev.slickcollections.kiwizin.clans.clan;

import com.google.common.collect.ImmutableList;
import dev.slickcollections.kiwizin.clans.Main;
import dev.slickcollections.kiwizin.clans.database.Database;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.Validator;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Clan {
  
  private static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("CLANS");
  private static final Map<String, Clan> clans = new HashMap<>();
  public boolean tagPermission, tagPermissionPlus;
  private String tag;
  private String name;
  private String leader;
  private int slots, coins;
  private final long created;
  private long updatedBoletim;
  private List<String> members, officers;
  private List<String> invites;
  private List<ClanUser> users;
  private List<ClanChangelog> changelogs;
  
  public Clan(String tag, String name, int slots, String leader, long created, boolean tagPermission, boolean tagPermissionPlus) {
    this.tag = tag;
    this.name = name;
    this.created = created;
    this.leader = leader.split(":")[0];
    this.slots = slots;
    this.tagPermission = tagPermission;
    this.tagPermissionPlus = tagPermissionPlus;
    this.members = new ArrayList<>();
    this.officers = new ArrayList<>();
    this.invites = new ArrayList<>();
    this.users = new ArrayList<>();
    this.users.add(new ClanUser(this.leader, Long.parseLong(leader.split(":")[1]), Long.parseLong(leader.split(":")[2])));
    this.changelogs = new ArrayList<>();
  }
  
  public static void setupClans() {
    try {
      CachedRowSet rs = Database.getInstance().query("SELECT * FROM `kclans`");
      if (rs != null) {
        rs.beforeFirst();
        while (rs.next()) {
          String tag = rs.getString("tag");
          String name = rs.getString("name");
          String leader = rs.getString("leader");
          boolean tagPermission = rs.getBoolean("tagPermission");
          boolean tagPermissionPlus = rs.getBoolean("tagPermissionPlus");
          int slots = rs.getInt("slots");
          Clan clan = new Clan(tag, name, slots, leader, rs.getLong("created"), tagPermission, tagPermissionPlus);
          clan.updatedBoletim = rs.getLong("boletim");
          clan.coins = rs.getInt("coins");
          clans.put(tag, clan);
          for (String member : rs.getString("members").split(", ")) {
            if (!member.isEmpty()) {
              clan.members.add(member.split(":")[0]);
              clan.users.add(new ClanUser(member.split(":")[0], Long.parseLong(member.split(":")[1]),
                  Long.parseLong(member.split(":")[2])));
            }
          }
          for (String officer : rs.getString("officers").split(", ")) {
            if (!officer.isEmpty()) {
              clan.officers.add(officer.split(":")[0]);
              clan.users.add(new ClanUser(officer.split(":")[0],
                  Long.parseLong(officer.split(":")[1]), Long.parseLong(officer.split(":")[2])));
            }
          }
          for (String invite : rs.getString("invites").split(", ")) {
            if (!invite.isEmpty()) {
              clan.getInvites().add(invite);
            }
          }
          for (String changelog : rs.getString("changelog").split(" ,:, ")) {
            if (!changelog.isEmpty()) {
              clan.changelogs.add(new ClanChangelog(changelog.split(" ,;, ")[0],
                  Long.parseLong(changelog.split(" ,;, ")[1])));
            }
          }
        }
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Error while load clans: ", ex);
    }
  }
  
  public static void loadClan(String tag) {
    try {
      CachedRowSet rs = Database.getInstance().query("SELECT * FROM `kclans` WHERE `tag` = ?", tag);
      if (rs != null) {
        String name = rs.getString("name");
        String leader = rs.getString("leader");
        boolean tagPermission = rs.getBoolean("tagPermission");
        boolean tagPermissionPlus = rs.getBoolean("tagPermissionPlus");
        int slots = rs.getInt("slots");
        Clan clan = new Clan(tag, name, slots, leader, rs.getLong("created"), tagPermission, tagPermissionPlus);
        clan.updatedBoletim = rs.getLong("boletim");
        clan.addCoins(rs.getInt("coins"));
        clans.put(tag, clan);
        for (String member : rs.getString("members").split(", ")) {
          if (!member.isEmpty()) {
            clan.members.add(member.split(":")[0]);
            clan.users.add(new ClanUser(member.split(":")[0], Long.parseLong(member.split(":")[1]),
                Long.parseLong(member.split(":")[2])));
          }
        }
        for (String officer : rs.getString("officers").split(", ")) {
          if (!officer.isEmpty()) {
            clan.officers.add(officer.split(":")[0]);
            clan.users.add(new ClanUser(officer.split(":")[0], Long.parseLong(officer.split(":")[1]),
                Long.parseLong(officer.split(":")[2])));
          }
        }
        for (String invite : rs.getString("invites").split(", ")) {
          if (!invite.isEmpty()) {
            clan.getInvites().add(invite);
          }
        }
        for (String changelog : rs.getString("changelog").split(" ,:, ")) {
          if (!changelog.isEmpty()) {
            clan.changelogs.add(new ClanChangelog(changelog.split(" ,;, ")[0],
                Long.parseLong(changelog.split(" ,;, ")[1])));
          }
        }
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Error while load clan(" + tag + "): ", ex);
    }
  }
  
  public static void createClan(Player leader, String tag, String name) {
    tag = tag.toUpperCase();
    Clan clan = getClan(leader);
    if (clan != null) {
      leader.sendMessage("§cVocê já está em um clan.");
      return;
    }
    
    if (tag.length() > 5 || tag.length() < 3) {
      leader.sendMessage("§cA tag do clan precisa conter de 3 a 5 caracteres.");
      return;
    }
    
    if (name.length() > 16 || name.length() < 4) {
      leader.sendMessage("§cO nome do clan precisa conter de 4 a 16 caracteres.");
      return;
    }
    
    if (dev.slickcollections.kiwizin.clans.clan.Clan.getByTag(tag) != null) {
      leader.sendMessage("§cUm clan com esta tag já existe.");
      return;
    }
    
    if (dev.slickcollections.kiwizin.clans.clan.Clan.getByName(name) != null) {
      leader.sendMessage("§cUm clan com este nome já existe.");
      return;
    }
    
    if (!Validator.isValidUsername(tag)) {
      leader.sendMessage("§cA tag não pode conter caracteres especiais.");
      return;
    }
    
    clan = new Clan(tag, name, 5,
        leader.getName() + ":" + System.currentTimeMillis() + ":" + System.currentTimeMillis(),
        System.currentTimeMillis(), false, false);
    clans.put(tag, clan);
    
    String date =
        "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(clan.getCreated()) + "] §r";
    clan.changelogs
        .add(new ClanChangelog(date + leader.getName() + " criou o clan.", clan.getCreated()));
    Database.getInstance().execute(
        "INSERT INTO `kclans` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", tag, name,
        leader.getName() + ":" + clan.getCreated() + ":" + clan.getCreated(), "", "", "",
        StringUtils.join(Collections.singletonList(clan.changelogs.get(0).toString()), " ,:, "),
        clan.getCreated(), 5, false, false, 0, "", clan.getCreated());
    leader.sendMessage("§aClan §f[{tag}] {name}§a criado com sucesso.".replace("{tag}", tag).replace("{name}", name));
    Main.sendBungee(leader, "Create", tag);
  }
  
  public static void inviteClan(Player player, String name) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (player.getName().equals(name)) {
      return;
    }
    
    if (!clan.getLeader().equals(player.getName()) && !clan.officers.contains(player.getName())) {
      player.sendMessage("§cSomente o líder ou oficiais podem convidar novos membros.");
      return;
    }
    
    Player target = Bukkit.getPlayerExact(name);
    if (target == null) {
      player.sendMessage("§cUsuário não encontrado.");
      return;
    }
    
    Clan clan2 = getClan(target);
    if (clan2 != null) {
      player.sendMessage("§c" + target.getName() + " §cjá faz parte de um clan.");
      return;
    }
    
    if (clan.getSlots() <= clan.getAll().size()) {
      player.sendMessage("§cO clan já atingiu o seu limite máximo de membros.");
      return;
    }
    
    if (clan.getInvites().contains(target.getName())) {
      player.sendMessage("§cO jogador " + target.getName() + " §cjá foi convidado para o clan.");
      return;
    }
    
    clan.getInvites().add(name);
    clan.save();
    List<BaseComponent> list = new ArrayList<>(Arrays.asList(TextComponent.fromLegacyText(" \n" + Role.getColored(player.getName()) + " §aquer que você participe do clan ["
        + clan.getTag() + "] " + clan.getName() + "!\n")));
    Collections.addAll(list, TextComponent.fromLegacyText("§aClique "));
    TextComponent accept = new TextComponent("§a§lAQUI");
    accept.setHoverEvent(
        new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, TextComponent
            .fromLegacyText("§eClique aqui para aceitar.\n§7/clan aceitar " + clan.getTag())));
    accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan aceitar " + clan.getTag()));
    list.add(accept);
    list.add(new TextComponent(" §apara aceitar e "));
    TextComponent reject = new TextComponent("§c§lAQUI");
    reject.setHoverEvent(
        new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, TextComponent
            .fromLegacyText("§eClique aqui para recusar.\n§7/clan recusar " + clan.getTag())));
    reject.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan recusar " + clan.getTag()));
    list.add(reject);
    list.add(new TextComponent(" §apara negar.\n"));
    target.spigot().sendMessage(list.toArray(new BaseComponent[0]));
    player.sendMessage("\n" + Role.getColored(target.getName()) + " §afoi convidado para o clan.\n \n");
  }
  
  public static void chatClan(Player player, String message) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de um clan.");
      return;
    }
    
    clan.broadcast("§7[Clan] " + Role.getPrefixed(player.getName()) + "§f: " + StringUtils.formatColors(message), true);
  }
  
  public static void kickClan(Player player, String target) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de um clan.");
      return;
    }
    
    if (player.getName().equals(target)) {
      return;
    }
    
    if (!clan.getLeader().equals(player.getName()) && !clan.officers.contains(player.getName())) {
      player.sendMessage("§cSomente o líder ou oficiais podem expulsar membros.");
      return;
    }
    
    if (!clan.members.contains(target)) {
      player.sendMessage("§c" + target + " não é um membro do seu clan.");
      return;
    }
    
    clan.remove(target);
    clan.save();
    long created = System.currentTimeMillis();
    Player tplayer = Bukkit.getPlayerExact(target);
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    if (tplayer != null) {
      clan.addChangelog(
          date + tplayer.getName() + " foi expulso do clan por " + player.getName() + ".", created);
      clan.broadcast("\n" + Role.getColored(tplayer.getName()) + " §afoi expulso do clan por " + Role.getColored(player.getName()) + " §a.\n ", true);
      tplayer.sendMessage(
          "\n§aVocê foi expulso do clan §f[" + clan.getTag() + "] " + clan.getName() + " §apor " + player.getName() + "§a.\n ");
    } else {
      clan.addChangelog(date + target + " foi expulso do clan por " + player.getName() + ".",
          created);
      clan.broadcast("\n" + Role.getColored(target) + " §afoi expulso do clan por " + Role.getColored(player.getName()) + "§a.\n ", true);
    }
  }
  
  public static void acceptClan(Player player, String tag) {
    if (getClan(player) != null) {
      player.sendMessage("§cVocê já faz parte de um clan.");
      return;
    }
    
    Clan clan = getByTag(tag);
    if (clan == null) {
      player.sendMessage("§cClan não encontrado.");
      return;
    }
    
    if (!clan.getInvites().contains(player.getName())) {
      player.sendMessage("§cVocê não foi convidado por este clan.");
      return;
    }
    
    clan.getInvites().remove(player.getName());
    if (clan.getSlots() <= clan.getAll().size()) {
      clan.save();
      player.sendMessage("§cO clan já atingiu o seu limite máximo de membros.");
      return;
    }
    
    clans.values().stream().filter(c -> c.getInvites().contains(player.getName())).forEach(c -> {
      c.getInvites().remove(player.getName());
      c.save();
    });
    clan.add(player.getName());
    long created = System.currentTimeMillis();
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    clan.addChangelog(date + player.getName() + " entrou no clan.", created);
    clan.broadcast(" \n" + Role.getColored(player.getName()) + " §aentrou no clan.\n ", true);
  }
  
  public static void rejectClan(Player player, String tag) {
    if (getClan(player) != null) {
      player.sendMessage("§cVocê já faz parte de um clan.");
      return;
    }
    
    Clan clan = getByTag(tag);
    if (clan == null) {
      player.sendMessage("§cClan não encontrado.");
      return;
    }
    
    if (!clan.getInvites().contains(player.getName())) {
      player.sendMessage("§cVocê não foi convidado por este clan.");
      return;
    }
    
    clan.getInvites().remove(player.getName());
    clan.save();
    clan.sendLeader(" \n" + Role.getColored(player.getName()) + " §arecusou o convite de clan.\n ");
    player.sendMessage("§aVocê recusou o convite do clan §f[" + clan.getTag() + "] " + clan.getName() + "§a.");
  }
  
  public static void invitesClan(Player player) {
    Clan clan = getClan(player);
    if (clan == null || !clan.getLeader().equals(player.getName())) {
      // listar convite
      List<Clan> invites = new ArrayList<>();
      for (Clan c : clans.values()) {
        if (c.getInvites().contains(player.getName())) {
          invites.add(c);
        }
      }
      
      if (clan != null) {
        player.sendMessage("§cApenas o líder pode visualizar os convites enviados.");
      } else {
        if (invites.isEmpty()) {
          player.sendMessage("§cVocê não possui convites.");
        } else {
          List<BaseComponent> list = new ArrayList<>(Arrays.asList(TextComponent.fromLegacyText(" \n§fLista de convites:\n")));
          for (Clan c : invites) {
            for (BaseComponent comp : TextComponent.fromLegacyText("§6[" + c.getTag() + "]")) {
              comp.setHoverEvent(
                  new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                      TextComponent.fromLegacyText(
                          "§3Líder do Clan: §7" + Role.getPrefixed(c.getLeader()) + "\n§3Quantidade de Membros: §7"
                              + c.getAll().size() + "/" + c.getSlots())));
              list.add(comp);
            }
            list.add(new TextComponent(" "));
            TextComponent accept = new TextComponent("§a§lACEITAR");
            accept
                .setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText("§eClique para aceitar!")));
            accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan aceitar " + c.getTag()));
            list.add(accept);
            list.add(new TextComponent(" §eou "));
            TextComponent reject = new TextComponent("§c§lRECUSAR");
            reject
                .setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText("§eClique para recusar!")));
            reject.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan recusar " + c.getTag()));
            list.add(reject);
            list.add(new TextComponent("\n"));
          }
          
          player.spigot().sendMessage(list.toArray(new BaseComponent[0]));
        }
      }
    } else {
      // listar convidados
      if (clan.getInvites().isEmpty()) {
        player.sendMessage("§cNão existe nenhum convite ativo.");
      } else {
        List<BaseComponent> list = new ArrayList<>();
        Collections.addAll(list, TextComponent
            .fromLegacyText(" \n§fLista de convites ativos:\n"));
        for (String invited : clan.getInvites()) {
          list.add(new TextComponent(" §f* §7" + invited + "\n"));
        }
        
        player.spigot().sendMessage(list.toArray(new BaseComponent[0]));
      }
    }
  }
  
  public static void boletimClan(Player player) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    List<ClanChangelog> list = clan.getChangelogs();
    if (list.isEmpty()) {
      player.sendMessage("§cNão foi encontrada nenhuma notícia.");
      return;
    }
    
    player.sendMessage("");
    player.sendMessage("§eBoletim ");
    player.sendMessage("");
    for (ClanChangelog ccl : list) {
      player.sendMessage(ccl.getMessage());
    }
    player.sendMessage("");
  }
  
  public static void boletimClanJoin(Player player) {
    Clan clan = getClan(player);
    if (clan == null) {
      return;
    }
    
    if (clan.getUser(player.getName()).getLastBoletim() >= clan.getUpdatedBoletim()) {
      return;
    }
    
    List<ClanChangelog> list = clan.getChangelogs();
    if (list.isEmpty()) {
      return;
    }
    
    player.sendMessage("");
    player.sendMessage("§eBoletim ");
    player.sendMessage("");
    for (ClanChangelog ccl : list) {
      player.sendMessage(ccl.getMessage());
    }
    clan.getUser(player.getName()).update();
    clan.save();
    player.sendMessage("");
  }
  
  public static void leaveClan(Player player) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (clan.getLeader().equals(player.getName())) {
      player.sendMessage("§cVocê é o líder, exclua o clan caso deseje sair.");
      return;
    }
    
    clan.remove(player.getName());
    long created = System.currentTimeMillis();
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    clan.addChangelog(date + player.getName() + " saiu do clan.", created);
    clan.broadcast(" \n" + Role.getColored(player.getName()) + " §asaiu do clan.\n ", true);
    player.sendMessage(" \n§cVocê saiu do clan [" + clan.getTag() + "] " + clan.getName() + "\n ");
  }
  
  public static void deleteClan(Player player) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (!clan.getLeader().equals(player.getName())) {
      player.sendMessage("§cApenas o líder pode excluir o clan.");
      return;
    }
    
    player.sendMessage(" \n§cVocê excluiu o clan [" + clan.getTag() + "] " + clan.getName() + " \n ");
    clan.destroy();
  }
  
  public static void promoteClan(Player player, String target) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (player.getName().equals(target)) {
      return;
    }
    
    if (!clan.getLeader().equals(player.getName())) {
      player.sendMessage("§cApenas o líder pode promover alguém.");
      return;
    }
    
    if (!clan.getMembers().contains(target)) {
      player.sendMessage("§cEste jogador não é um membro do clan.");
      return;
    }
    
    clan.setOfficer(target);
    long created = System.currentTimeMillis();
    //   Player tplayer = Bukkit.getPlayerExact(target);
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    clan.addChangelog(date + target + " foi promovido a oficial do clan.", created);
    clan.broadcast(" \n" + Role.getColored(target) + " §afoi promovido a oficial do clan por " + Role.getColored(player.getName()) + "§a.\n ", true);
  }
  
  public static void demoteClan(Player player, String target) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (player.getName().equals(target)) {
      return;
    }
    
    if (!clan.getLeader().equals(player.getName())) {
      player.sendMessage("§cApenas o líder pode demotar alguém.");
      return;
    }
    
    if (!clan.officers.contains(target)) {
      player.sendMessage(Role.getColored(target) + " §cnão é um oficial.");
      return;
    }
    
    clan.officers.remove(target);
    clan.members.add(target);
    clan.save();
    long created = System.currentTimeMillis();
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    clan.addChangelog(date + target + " foi rebaixado a membro do clan.", created);
    clan.broadcast(" \n" + Role.getColored(target) + " §afoi rebaixado a membro do clan por " + Role.getColored(player.getName()) + "§a.\n ", true);
  }
  
  public static void promoteLeader(Player player, String target) {
    Clan clan = getClan(player);
    if (clan == null) {
      player.sendMessage("§cVocê não faz parte de nenhum clan.");
      return;
    }
    
    if (player.getName().equals(target)) {
      return;
    }
    
    if (!clan.getLeader().equals(player.getName())) {
      player.sendMessage("§cApenas o líder pode transferir o clan.");
      return;
    }
    
    if (!clan.getAll().contains(target)) {
      player.sendMessage("§c" + target + " não é um membro do clan.");
      return;
    }
    
    clan.setLeader(target);
    long created = System.currentTimeMillis();
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    clan.addChangelog(date + player.getName() + " transferiu a liderença para " + target + ".",
        created);
    clan.broadcast(" \n" + Role.getColored(target) + " §atransferiu a liderança do clan para " + Role.getColored(player.getName()) + "§a.\n ", true);
  }
  
  public static Clan getByTag(String tag) {
    return clans.get(tag);
  }
  
  public static Clan getByName(String name) {
    for (Clan clan : clans.values()) {
      if (clan.getName().equals(name)) {
        return clan;
      }
    }
    
    return null;
  }
  
  public static Clan getByUserName(String name) {
    for (Clan clan : clans.values()) {
      if (clan.contains(name)) {
        return clan;
      }
    }
    
    return null;
  }
  
  public static Clan getClan(Player player) {
    for (Clan clan : clans.values()) {
      if (clan.contains(player.getName())) {
        return clan;
      }
    }
    
    return null;
  }
  
  public static List<Clan> listClans() {
    return ImmutableList.copyOf(clans.values());
  }
  
  public void addCoins(int coins) {
    this.coins += coins;
    this.save();
  }
  
  public void removeCoins(int coins) {
    this.coins -= coins;
    this.save();
  }
  
  public void addTagColor(boolean upgrade) {
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    this.addChangelog(date + (upgrade ? "Tag no chat aprimorada" : "Tag no chat adicionada"),
        System.currentTimeMillis());
    if (upgrade) {
      tagPermissionPlus = true;
    } else {
      tagPermission = true;
    }
  }
  
  public void addSlots() {
    this.slots += 5;
    String date = "§7[" + new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(created) + "] §r";
    this.addChangelog(date + "Limite de Membros ampliado para " + slots,
        System.currentTimeMillis());
  }
  
  public void addChangelog(String message, long created) {
    if (changelogs.size() == 8) {
      changelogs.remove(0);
    }
    
    changelogs.add(new ClanChangelog(message, created));
    this.updatedBoletim = System.currentTimeMillis();
    this.save();
  }
  
  public void setOfficer(String player) {
    this.members.remove(player);
    this.officers.add(player);
    this.save();
  }
  
  public void add(String name) {
    members.add(name);
    users.add(new ClanUser(name, System.currentTimeMillis(), System.currentTimeMillis()));
  }
  
  public void remove(String name) {
    officers.remove(name);
    members.remove(name);
    users.remove(getUser(name));
  }
  
  public String getRole(String name) {
    return members.contains(name) ? "Membro" : officers.contains(name) ? "Oficial" : "Líder";
  }
  
  public boolean contains(String name) {
    return leader.equals(name) || members.contains(name) || officers.contains(name);
  }
  
  public ClanUser getUser(String name) {
    for (ClanUser user : users) {
      if (user.getName().equals(name)) {
        return user;
      }
    }
    
    return null;
  }
  
  public List<ClanChangelog> getChangelogs() {
    return changelogs;
  }
  
  public void reload() {
    try {
      CachedRowSet rs = Database.getInstance().query("SELECT * FROM `kclans` WHERE `tag` = ?", tag);
      if (rs != null) {
        this.coins = rs.getInt("coins");
        this.tagPermission = rs.getBoolean("tagPermission");
        this.tagPermissionPlus = rs.getBoolean("tagPermissionPlus");
        this.slots = rs.getInt("slots");
        this.users.clear();
        this.leader = rs.getString("leader").split(":")[0];
        this.users.add(new ClanUser(leader, Long.parseLong(rs.getString("leader").split(":")[1]),
            Long.parseLong(rs.getString("leader").split(":")[2])));
        
        // RELOAD MEMBERS
        this.members.clear();
        for (String member : rs.getString("members").split(", ")) {
          if (!member.isEmpty()) {
            this.members.add(member.split(":")[0]);
            this.users.add(new ClanUser(member.split(":")[0], Long.parseLong(member.split(":")[1]),
                Long.parseLong(member.split(":")[2])));
          }
        }
        
        // RELOAD OFFICERS
        this.officers.clear();
        for (String officer : rs.getString("officers").split(", ")) {
          if (!officer.isEmpty()) {
            this.officers.add(officer.split(":")[0]);
            this.users.add(new ClanUser(officer.split(":")[0], Long.parseLong(officer.split(":")[1]),
                Long.parseLong(officer.split(":")[2])));
          }
        }
        
        // RELOAD INVITES
        this.invites.clear();
        for (String invite : rs.getString("invites").split(", ")) {
          if (!invite.isEmpty()) {
            invites.add(invite);
          }
        }
        
        // RELOAD CHANGELOGS
        this.changelogs.clear();
        for (String changelog : rs.getString("changelog").split(" ,:, ")) {
          if (!changelog.isEmpty()) {
            this.changelogs.add(new ClanChangelog(changelog.split(" ,;, ")[0], Long.parseLong(changelog.split(" ,;, ")[1])));
          }
        }
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Error while reloading clan (" + tag + "): ", ex);
    }
  }
  
  public void destroy() {
    destroy(true);
  }
  
  public void destroy(boolean executeDb) {
    clans.remove(this.tag);
    if (executeDb) {
      Main.sendBungee(Bukkit.getOnlinePlayers().iterator().next(), "Destroy", tag);
      this.broadcast("\n§aO seu clan §7[" + tag + "] " + name + " §afoi excluído!\n", false);
      Database.getInstance().execute("DELETE FROM `kclans` WHERE `tag` = ?", tag);
    }
    this.tag = null;
    this.name = null;
    this.leader = null;
    this.slots = 0;
    this.members.clear();
    this.members = null;
    this.officers.clear();
    this.officers = null;
    this.users.clear();
    this.users = null;
    this.changelogs.clear();
    this.changelogs = null;
    this.invites.clear();
    this.invites = null;
  }
  
  public void sendLeader(String message) {
    Player target = null;
    if ((target = Bukkit.getPlayerExact(leader)) != null) {
      target.sendMessage(message);
    }
  }
  
  public void broadcast(String message, boolean leader, String... blockBungee) {
    Player target = null;
    if (leader) {
      sendLeader(message);
    }
    
    List<String> m = new ArrayList<>();
    m.addAll(members);
    m.addAll(officers);
    for (String member : m) {
      if ((target = Bukkit.getPlayerExact(member)) != null) {
        target.sendMessage(message);
      }
    }
    
    if (blockBungee.length == 0) {
      Main.sendBungee(Bukkit.getOnlinePlayers().iterator().next(), "Broadcast", tag, message, String.valueOf(leader));
    }
  }
  
  public void save() {
    StringBuilder joined = new StringBuilder();
    for (int slot = 0; slot < members.size(); slot++) {
      joined.append(members.get(slot)).append(":").append(getUser(members.get(slot)).getJoined()).append(":").append(getUser(members.get(slot)).getLastBoletim()).append(slot + 1 == members.size() ? "" : ", ");
    }
    
    StringBuilder joined2 = new StringBuilder();
    for (int slot = 0; slot < officers.size(); slot++) {
      joined2.append(officers.get(slot)).append(":").append(getUser(officers.get(slot)).getJoined()).append(":").append(getUser(officers.get(slot)).getLastBoletim()).append(slot + 1 == officers.size() ? "" : ", ");
    }
    
    Database.getInstance().execute(
        "UPDATE `kclans` SET `members` = ?, `officers` = ?, `invites` = ?, `changelog` = ?, `boletim` = ?, `leader` = ?, `coins` = ?, `slots` = ?, `tagPermission` = ?, `tagPermissionPlus` = ? WHERE `tag` = ?",
        joined.toString(), joined2.toString(), StringUtils.join(invites, ", "),
        StringUtils.join(changelogs, " ,:, "), updatedBoletim,
        leader + ":" + getUser(leader).getJoined() + ":" + getUser(leader).getLastBoletim(), coins,
        slots, tagPermission, tagPermissionPlus, tag);
    
    Main.sendBungee(Bukkit.getOnlinePlayers().iterator().next(), "Update", tag);
  }
  
  public String getTag() {
    return tag;
  }
  
  public String getName() {
    return name;
  }
  
  public String getLeader() {
    return leader;
  }
  
  public void setLeader(String player) {
    this.members.add(leader);
    this.officers.remove(player);
    this.members.remove(player);
    this.leader = player;
    this.save();
  }
  
  public long getCreated() {
    return created;
  }
  
  public long getUpdatedBoletim() {
    return updatedBoletim;
  }
  
  public int getSlots() {
    return slots;
  }
  
  public int getCoins() {
    return coins;
  }
  
  public List<String> getInvites() {
    return invites;
  }
  
  public List<String> getMembers() {
    return members;
  }
  
  public List<String> getOfficers() {
    return officers;
  }
  
  public List<String> getTaggedMembers() {
    return members.stream().map(Role::getColored).collect(Collectors.toList());
  }
  
  public List<String> getTaggedOfficers() {
    return officers.stream().map(Role::getColored).collect(Collectors.toList());
  }
  
  public List<String> getPrefixedMembers() {
    return members.stream().map(Role::getPrefixed).collect(Collectors.toList());
  }
  
  public List<String> getPrefixedOfficers() {
    return officers.stream().map(Role::getPrefixed).collect(Collectors.toList());
  }
  
  public List<String> getAll() {
    List<String> all = new ArrayList<>();
    all.addAll(members);
    all.addAll(officers);
    return all;
  }
}
