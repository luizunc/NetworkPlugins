package dev.slickcollections.kiwizin.murder.hook;

import com.comphenix.protocol.ProtocolLibrary;
import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.achievements.Achievement;
import dev.slickcollections.kiwizin.achievements.types.MurderAchievement;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.game.MurderTeam;
import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;
import dev.slickcollections.kiwizin.murder.game.enums.MurderRole;
import dev.slickcollections.kiwizin.murder.game.types.AssassinsMurder;
import dev.slickcollections.kiwizin.murder.game.types.ClassicMurder;
import dev.slickcollections.kiwizin.murder.hook.hotbar.MMHotbarActionType;
import dev.slickcollections.kiwizin.murder.hook.protocollib.HologramAdapter;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.hotbar.HotbarAction;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;
import dev.slickcollections.kiwizin.player.hotbar.HotbarButton;
import dev.slickcollections.kiwizin.player.scoreboard.KScoreboard;
import dev.slickcollections.kiwizin.player.scoreboard.scroller.ScoreboardScroller;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

public class MMCoreHook {

  public static void setupHook() {
    Core.minigame = "Murder";

    setupHotbars();
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (profile.getScoreboard() != null) {
            profile.getScoreboard().scroll();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, Language.scoreboards$scroller$every_tick);

    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (!profile.playingGame() && profile.getScoreboard() != null) {
            profile.update();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
  }

  public static void checkAchievements(final Profile profile) {
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
      Achievement.listAchievements(MurderAchievement.class).stream().filter(ma -> ma.canComplete(profile)).forEach(ma -> {
        ma.complete(profile);
        profile.getPlayer().sendMessage(Language.lobby$achievement.replace("{name}", ma.getName()));
      });
    });
  }

  private static final SimpleDateFormat SDF = new SimpleDateFormat("mm:ss");

  public static void reloadScoreboard(Profile profile) {
    if (!profile.playingGame()) {
      checkAchievements(profile);
    }
    Player player = profile.getPlayer();
    Murder game = profile.getGame(Murder.class);
    List<String> lines = game == null ? Language.scoreboards$lobby : game.getState().canJoin() ? Language.scoreboards$waiting : game.getMode() == MurderMode.CLASSIC ? Language.scoreboards$classic : Language.scoreboards$assassins;
    profile.setScoreboard(new KScoreboard() {
      @Override
      public void update() {
        for (int index = 0; index < Math.min(lines.size(), 15); index++) {
          String line = lines.get(index);

          if (game != null) {
            if (game instanceof ClassicMurder) {
              line =
                line
                  .replace("{innocents}", StringUtils.formatNumber(((ClassicMurder) game).getInnocents()))
                  .replace("{detective}", game.listTeams().stream().anyMatch(mt -> mt.getRole() == MurderRole.DETECTIVE && mt.isAlive()) ? "§aVivo" : "§cMorto");
            } else {
              MurderTeam team = game.getTeam(player);
              String contract = ((AssassinsMurder) game).getContract(player);
              line = line
                .replace("{bounty}", "§7" + (contract.isEmpty() ? "Nenhum" : contract))
                .replace("{kills}", StringUtils.formatNumber(team != null ? team.getKills() : 0));
            }
            line = line.replace("{timeLeft}", SDF.format(game.getTimer() * 1000))
              .replace("{role}", game.getTeam(player) == null ? "§7Morto" : game.getTeam(player).getRole().getName())
              .replace("{map}", game.getMapName())
              .replace("{server}", game.getGameName())
              .replace("{mode}", game.getMode().getName())
              .replace("{players}", StringUtils.formatNumber(game.getOnline()))
              .replace("{max_players}", StringUtils.formatNumber(game.getMaxPlayers()))
              .replace("{time}", game.getTimer() == 46 ? Language.scoreboards$time$waiting : Language.scoreboards$time$starting.replace("{time}", StringUtils.formatNumber(game.getTimer())));
          } else {
            line = PlaceholderAPI.setPlaceholders(player, line);
          }

          this.add(15 - index, line);
        }
      }
    }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles)).to(player).build());
    profile.update();
    profile.getScoreboard().scroll();
  }

  private static void setupHotbars() {
    HotbarActionType.addActionType("murder", new MMHotbarActionType());

    KConfig config = Main.getInstance().getConfig("hotbar");
    for (String id : new String[] {"lobby", "waiting", "spectator"}) {
      Hotbar hotbar = new Hotbar(id);

      ConfigurationSection hb = config.getSection(id);
      for (String button : hb.getKeys(false)) {
        try {
          hotbar.getButtons().add(new HotbarButton(hb.getInt(button + ".slot"), new HotbarAction(hb.getString(button + ".execute")), hb.getString(button + ".icon")));
        } catch (Exception ex) {
          Main.getInstance().getLogger().log(Level.WARNING, "Falha ao carregar o botao \"" + button + "\" da hotbar \"" + id + "\": ", ex);
        }
      }

      Hotbar.addHotbar(hotbar);
    }
  }
}
