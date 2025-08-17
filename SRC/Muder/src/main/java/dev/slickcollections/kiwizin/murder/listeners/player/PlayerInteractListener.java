package dev.slickcollections.kiwizin.murder.listeners.player;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.libraries.npclib.api.event.NPCLeftClickEvent;
import dev.slickcollections.kiwizin.libraries.npclib.api.event.NPCRightClickEvent;
import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import dev.slickcollections.kiwizin.menus.MenuDeliveries;
import dev.slickcollections.kiwizin.murder.cmd.mm.BuildCommand;
import dev.slickcollections.kiwizin.murder.cmd.mm.CreateCommand;
import dev.slickcollections.kiwizin.murder.container.SelectedContainer;
import dev.slickcollections.kiwizin.murder.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.murder.cosmetics.types.Knife;
import dev.slickcollections.kiwizin.murder.game.enums.MurderMode;
import dev.slickcollections.kiwizin.murder.game.types.AssassinsMurder;
import dev.slickcollections.kiwizin.murder.game.types.ClassicMurder;
import dev.slickcollections.kiwizin.player.Profile;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import dev.slickcollections.kiwizin.murder.game.Murder;
import dev.slickcollections.kiwizin.murder.menus.MenuPlay;
import dev.slickcollections.kiwizin.murder.menus.MenuStatsNPC;
import dev.slickcollections.kiwizin.murder.utils.Throwable;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteractListener implements Listener {

  @EventHandler
  public void onNPCLeftClick(NPCLeftClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        new MenuPlay(profile, MurderMode.fromName(npc.data().get("play-npc")));
      }
    }
  }

  @EventHandler
  public void onNPCRightClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        new MenuPlay(profile, MurderMode.fromName(npc.data().get("play-npc")));
      } else if (npc.data().has("delivery-npc")) {
        new MenuDeliveries(profile);
      } else if (npc.data().has("stats-npc")) {
        new MenuStatsNPC(profile);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked().hasMetadata("MURDER")) {
      evt.setCancelled(true);
    }
  }

  public static Player getMoreNearby(Player player, List<Player> targets) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < targets.size(); i++) {
      list.add(i + " : " + targets.get(i).getLocation().distance(player.getLocation()));
    }

    list.sort((o1, o2) -> {
      double i1 = Double.parseDouble(o1.split(" : ")[1]);
      double i2 = Double.parseDouble(o2.split(" : ")[1]);
      return Double.compare(i1, i2);
    });

    return targets.get(Integer.parseInt(list.get(0).split(" : ")[0]));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile != null) {
      if (CreateCommand.CREATING.containsKey(player) && CreateCommand.CREATING.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          CreateCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else {
        Murder murder = profile.getGame(Murder.class);
        if (murder == null && !BuildCommand.hasBuilder(player)) {
          evt.setCancelled(true);
        } else if (murder != null) {
          evt.setCancelled(true);
          if (murder.isSpectator(player)) {
            return;
          }

          if (murder.getMode() == MurderMode.ASSASSINS || ((ClassicMurder) murder).isKiller(player)) {
            ItemStack item = player.getItemInHand();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
              Knife knife = profile.getAbstractContainer("kCoreMurder", "selected", SelectedContainer.class).
                      getSelected(CosmeticType.KNIFE, Knife.class);
              if (item.getType() == Material.COMPASS) {
                if (murder instanceof AssassinsMurder) {
                  player.sendMessage("§aBussola apontando para " + ((AssassinsMurder) murder).getContract(player));
                } else if (murder instanceof ClassicMurder) {
                  List<Player> players = murder.listPlayers(false);
                  players.remove(player);
                  Player target = getMoreNearby(player, players);
                  if (target != null) {
                    player.setCompassTarget(target.getLocation());
                    player.sendMessage("§aBussola apontando para " + target.getName());
                  }
                }
              } else if (item.getType() == Material.DIAMOND_SWORD || knife != null && item.getType() == knife.getItem().getType()) {
                if (evt.getAction().name().contains("RIGHT")) {
                  if (murder instanceof ClassicMurder) {
                    player.getInventory().remove(item);
                    player.updateInventory();
                    Throwable.throwSword((ClassicMurder) murder, profile, item);
                  } else if (murder instanceof AssassinsMurder) {
                    AssassinsMurder am = (AssassinsMurder) murder;
                    if (am.getDelay(player) == null) {
                      am.setDelay(player);
                      Throwable.throwSword(am, profile, item);
                    }
                  }
                }
              }
            }
          } else if (evt.getAction().name().contains("RIGHT")) {
            ItemStack item = player.getItemInHand();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
              evt.setCancelled(false);
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent evt) {
    if (evt.getTo().getBlockY() != evt.getFrom().getBlockY() && evt.getTo().getBlockY() < 0) {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());

      if (profile != null) {
        Murder game = profile.getGame(Murder.class);
        if (game == null) {
          player.teleport(Core.getLobby());
        } else {
          if (game.getState() != GameState.EMJOGO || game.isSpectator(player)) {
            player.teleport(game.getConfig().getSpawnLocation());
          } else {
            ((CraftPlayer) player).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, (float) player.getMaxHealth());
          }
        }
      }
    }
  }
}
