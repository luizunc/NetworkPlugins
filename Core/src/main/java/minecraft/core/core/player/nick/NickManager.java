package minecraft.core.core.player.nick;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import minecraft.core.bukkit.plugin.config.UtilsConfig;
import minecraft.core.core.database.Database;
import minecraft.core.core.libraries.profile.Mojang;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.utils.BukkitUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NickManager {
  
  public static final String STEVE =
      "eyJ0aW1lc3RhbXAiOjE1ODcxNTAzMTc3MjAsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWRJOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIn19fQ==:syZ2Mt1vQeEjh/t8RGbv810mcfTrhQvnwEV7iLCd+5udVeroTa5NjoUehgswacTML3k/KxHZHaq4o6LmACHwsj/ivstW4PWc2RmVn+CcOoDKI3ytEm70LvGz0wAaTVKkrXHSw/RbEX/b7g7oQ8F67rzpiZ1+Z3TKaxbgZ9vgBQZQdwRJjVML2keI0669a9a1lWq3V/VIKFZc1rMJGzETMB2QL7JVTpQFOH/zXJGA+hJS5bRol+JG3LZTX93+DililM1e8KEjKDS496DYhMAr6AfTUfirLAN1Jv+WW70DzIpeKKXWR5ZeI+9qf48+IvjG8DhRBVFwwKP34DADbLhuebrolF/UyBIB9sABmozYdfit9uIywWW9+KYgpl2EtFXHG7CltIcNkbBbOdZy0Qzq62Tx6z/EK2acKn4oscFMqrobtioh5cA/BCRb9V4wh0fy5qx6DYHyRBdzLcQUfb6DkDx1uyNJ7R5mO44b79pSo8gdd9VvMryn/+KaJu2UvyCrMVUtOOzoIh4nCMc9wXOFW3jZ7ZTo4J6c28ouL98rVQSAImEd/P017uGvWIT+hgkdXnacVG895Y6ilXqJToyvf1JUQb4dgry0WTv6UTAjNgrm5a8mZx9OryLuI2obas97LCon1rydcNXnBtjUk0TUzdrvIa5zNstYZPchUb+FSnU=";
  public static final String ALEX =
      "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";
  
  private static final KConfig CONFIG = UtilsConfig.getConfig();
  private static final Pattern REAL_PATTERN = Pattern.compile("(?i)corefakereal:\\w*"), NOT_CHANGE_PATTERN = Pattern.compile("(?i)corenotchange:\\w*");
  
  // Configurações internas
      private static final List<String> NICK_RANKS_LIST = Arrays.asList("Membro", "VIP", "MVP");
  private static final String KICK_APPLY_MESSAGE = "§cVocê foi desconectado para aplicar o nick.";
  private static final String KICK_REMOVE_MESSAGE = "§cVocê foi desconectado para remover o nick.";
  
  public static Map<String, String> nickNames = new HashMap<>();
      public static Map<String, Rank> nickRanks = new HashMap<>();
  public static Map<String, String> nickSkins = new HashMap<>();
  
      private static TextComponent NICK_RANKS;
  private static TextComponent NICK_SKINS;
  
  private static List<String> randoms;
  private static Boolean bungeeSide;
  
  public static void setupNick() {
            NICK_RANKS = new TextComponent("");
    for (BaseComponent component : TextComponent.fromLegacyText("§5§lALTERAR NICKNAME\n \n§0Escolha o cargo que gostaria de utilizar enquanto está disfarçado:\n ")) {
      NICK_RANKS.addExtra(component);
    }
            for (String roleName : NICK_RANKS_LIST) {
              Rank rank = Rank.getRankByName(roleName);
        if (rank != null) {
            TextComponent component = new TextComponent("\n §0▪ " + rank.getName());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Seu nickname será exibido como '" + rank.getPrefix() + "Nickname'§7.")));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + roleName));
        NICK_RANKS.addExtra(component);
      }
    }
    NICK_SKINS = new TextComponent("");
    for (BaseComponent component : TextComponent.fromLegacyText("§5§lALTERAR NICKNAME\n \n§0Enquanto disfarçado, sua skin será alterada para ajudar a te camuflar.\n \n§0Escolha sua skin:\n ")) {
      NICK_SKINS.addExtra(component);
    }
    TextComponent STEVE = new TextComponent("\n §0▪ §7Steve");
    STEVE.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a aparência de Steve.")));
    STEVE.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick {rank} steve"));
    TextComponent ALEX = new TextComponent("\n §0▪ §7Alex");
    ALEX.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a aparência da Alex.")));
    ALEX.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick {rank} alex"));
    TextComponent YOU = new TextComponent("\n §0▪ §7Você");
    YOU.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Você irá obter a sua aparência.")));
    YOU.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick {rank} you"));
    NICK_SKINS.addExtra(STEVE);
    NICK_SKINS.addExtra(YOU);
    NICK_SKINS.addExtra(ALEX);
  }
  
  public static void sendRole(Player player) {
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();
    meta.setAuthor("Nys");
    meta.setTitle("Escolher cargo");
    book.setItemMeta(meta);
            book = BukkitUtils.setNBTList(book, "pages", Collections.singletonList(ComponentSerializer.toString(NICK_RANKS)));
    BukkitUtils.openBook(player, book);
  }
  
  public static void sendSkin(Player player, String role) {
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();
    meta.setAuthor("Nys");
    meta.setTitle("Escolher cargo");
    book.setItemMeta(meta);
    book = BukkitUtils.setNBTList(book, "pages", Collections.singletonList(ComponentSerializer.toString(NICK_SKINS).replace("{rank}", role)));
    BukkitUtils.openBook(player, book);
  }
  
  public static void applyNick(Player player, String nickName, String role, String skin) {
    if (!isBungeeSide()) {
      player.kickPlayer(KICK_APPLY_MESSAGE);
    }
    nickNames.put(player.getName(), nickName);
            nickRanks.put(player.getName(), Rank.getRankByName(role));
    nickSkins.put(player.getName(), skin);
  }
  
  public static void removeNick(Player player) {
    if (!isBungeeSide()) {
      player.kickPlayer(KICK_REMOVE_MESSAGE);
    }
    nickNames.remove(player.getName());
            nickRanks.remove(player.getName());
    nickSkins.remove(player.getName());
  }
  
  public static String getCurrent(String playerName) {
    return isNick(playerName) ? getNick(playerName) : playerName;
  }
  
  public static String getNick(String playerName) {
    return nickNames.get(playerName);
  }
  
      public static Rank getRank(String playerName) {
            return nickRanks.getOrDefault(playerName, Rank.getLastRole());
  }
  
  public static Rank getRole(String playerName) {
    return getRank(playerName);
  }
  
  public static String getSkin(String playerName) {
    return nickSkins.getOrDefault(playerName, STEVE);
  }
  
  public static boolean isNick(String playerName) {
    return nickNames.containsKey(playerName);
  }
  
  /**
   * Verifica se um nickname pode ser usado para nick.
   * 
   * @param name Nome a ser verificado
   * @return true se o nome pode ser usado
   */
  public static boolean isUsable(String name) {
    return checkNicknameAvailability(name) == null;
  }
  
  /**
   * Verifica a disponibilidade de um nickname para nick.
   * 
   * @param name Nome a ser verificado
   * @return Mensagem de erro ou null se disponível
   */
  public static String checkNicknameAvailability(String name) {
    // Verifica se já está sendo usado por outro nick
    if (nickNames.containsKey(name) || nickNames.containsValue(name)) {
      return "§cO nickname não está disponível para uso.";
    }
    
    // Verifica se está online no servidor
    if (Bukkit.getPlayer(name) != null) {
      return "§cO nickname não está disponível para uso.";
    }
    
    // Verifica se está registrado no servidor
    if (Database.getInstance().exists(name) != null) {
      return "§cO nickname não está disponível para uso.";
    }
    
    // Verifica se existe como conta real do Minecraft
    try {
      String uuid = Mojang.getUUID(name);
      if (uuid != null) {
        return "§cO nickname não está disponível para uso.";
      }
    } catch (Exception e) {
      // Se erro na API, considera como não existente (permite usar)
    }
    
    return null; // Nickname disponível
  }
  
  public static List<String> listNicked() {
    return new ArrayList<>(nickNames.keySet());
  }
  
  public static List<String> getRandomNicks() {
    if (randoms == null) {
      randoms = CONFIG.getStringList("nick.randoms");
    }
    
    return randoms;
  }
  
  public static boolean isNickRole(String roleName) {
            return NICK_RANKS_LIST.stream().anyMatch(rank -> rank.equalsIgnoreCase(roleName));
  }
  
  public static boolean isBungeeSide() {
    if (bungeeSide == null) {
      bungeeSide = CONFIG.getBoolean("bungeecord");
    }
    
    return bungeeSide;
  }
  
  public static String replaceNickedChanges(String original) {
    String replaced = original;
    for (String name : listNicked()) {
      Matcher matcher = Pattern.compile("(?i)" + name).matcher(replaced);
      
      while (matcher.find()) {
        replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement("corenotchange:" + name));
      }
    }
    
    return replaced;
  }
  
  public static String replaceNickedPlayers(String original, boolean toNick) {
    String replaced = original;
    List<String> backup = new ArrayList<>();
    for (String name : listNicked()) {
      Matcher matcher = NOT_CHANGE_PATTERN.matcher(replaced);
      while (matcher.find()) {
        String found = matcher.group();
        backup.add(found.replace("corenotchange:", ""));
                  replaced = replaced.replaceFirst(Pattern.quote(found), Matcher.quoteReplacement("corenotchange:" + (backup.size() - 1)));
      }
      
      matcher = Pattern.compile("(?i)" + (toNick ? name : getNick(name))).matcher(replaced);
      while (matcher.find()) {
        replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(toNick ? getNick(name) : name));
      }
    }
    
    Matcher matcher = REAL_PATTERN.matcher(replaced);
    while (matcher.find()) {
      String found = matcher.group();
      replaced = replaced.replaceFirst(Pattern.quote(found), Matcher.quoteReplacement(
          nickNames.entrySet().stream().filter(entry -> entry.getValue().equals(found.replace("corefakereal:", ""))).map(Map.Entry::getKey).findFirst().orElse("")));
    }
    
    matcher = NOT_CHANGE_PATTERN.matcher(replaced);
    while (matcher.find()) {
      String found = matcher.group();
      replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(backup.get(Integer.parseInt(found.replace("corenotchange:", "")))));
    }
    
    backup.clear();
    return replaced;
  }
  
  public static WrappedGameProfile cloneProfile(WrappedGameProfile profile) {
    WrappedGameProfile gameProfile = profile.withName(getNick(profile.getName()));
    gameProfile.getProperties().clear();
    
    try {
      String id = Mojang.getUUID(gameProfile.getName());
      if (id != null) {
        String textures = Mojang.getSkinProperty(id);
        if (textures != null) {
          gameProfile.getProperties().put("textures", new WrappedSignedProperty(textures.split(" : ")[0], textures.split(" : ")[1], textures.split(" : ")[2]));
        }
      }
    } catch (Exception ignore) {
    }
    
    return gameProfile;
  }
}