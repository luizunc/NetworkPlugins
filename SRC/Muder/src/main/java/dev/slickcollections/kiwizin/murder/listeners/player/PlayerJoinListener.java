package dev.slickcollections.kiwizin.murder.listeners.player;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.murder.hook.mysterybox.MysteryBoxesHook;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.titles.TitleManager;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import dev.slickcollections.kiwizin.murder.Language;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.hook.MMCoreHook;
import dev.slickcollections.kiwizin.murder.tagger.TagUtils;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    evt.setJoinMessage(null);

    Player player = evt.getPlayer();
    TagUtils.sendTeams(player);

    Profile profile = Profile.getProfile(player.getName());
    MMCoreHook.reloadScoreboard(profile);
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();
  
    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> TitleManager.joinLobby(profile), 10);

    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      TagUtils.setTag(evt.getPlayer());
      
      if (Role.getPlayerRole(player).isBroadcast()) {
        String broadcast = Language.lobby$broadcast.replace("{player}", Role.getPrefixed(player.getName()));
        Profile.listProfiles().forEach(pf -> {
          if (!pf.playingGame()) {
            Player players = pf.getPlayer();
            if (players != null) {
              players.sendMessage(broadcast);
            }
          }
        });
      }
    }, 5);

    NMS.sendTitle(player, "", "", 0, 1, 0);
    if (Language.lobby$tab$enabled) {
      NMS.sendTabHeaderFooter(player, Language.lobby$tab$header, Language.lobby$tab$footer);
    }

    if (player.hasPermission("kmurder.cmd.murder")) {
      if (Main.kMysteryBox) {

        if (MysteryBoxesHook.isUnsynced()) {
          TextComponent component = new TextComponent("");
          for (BaseComponent components : TextComponent
            .fromLegacyText(" \n §6§lMYSTERY BOX\n \n §7O kMurder aparentemente não está sincronizado com o kMysteryBox, para prosseguir basta clicar ")) {
            component.addExtra(components);
          }
          TextComponent click = new TextComponent("AQUI");
          click.setColor(ChatColor.GREEN);
          click.setBold(true);
          click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mm mb sync"));
          click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique aqui para sincronizar o kMurder com o kMysteryBox.")));
          component.addExtra(click);
          for (BaseComponent components : TextComponent.fromLegacyText("§7.\n ")) {
            component.addExtra(components);
          }

          player.spigot().sendMessage(component);
          EnumSound.ORB_PICKUP.play(player, 1.0F, 1.0F);
        }


      }
    }
  }
}
