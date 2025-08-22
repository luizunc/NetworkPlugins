package minecraft.bedwars.listeners.player;

import minecraft.core.bukkit.Core;
import minecraft.bedwars.Main;
import minecraft.bedwars.cmd.pl.*;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.enums.BedWarsMode;
import minecraft.bedwars.menus.MenuPlay;
import minecraft.bedwars.menus.MenuStatsNPC;
import minecraft.bedwars.menus.game.MenuItemShop;
import minecraft.bedwars.menus.game.MenuTrackerShop;
import minecraft.bedwars.menus.game.MenuUpgradeShop;
import minecraft.bedwars.nms.NMS;
import minecraft.bedwars.utils.PlayerUtils;
import minecraft.core.core.game.GameState;
import minecraft.core.core.libraries.npclib.api.event.NPCRightClickEvent;
import minecraft.core.core.libraries.npclib.api.npc.NPC;

import minecraft.core.core.player.Profile;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

import static minecraft.bedwars.cmd.pl.BalloonsCommand.BALLOONS;
import static minecraft.bedwars.cmd.pl.CreateCommand.CREATING;
import static minecraft.bedwars.cmd.pl.GeneratorCommand.GENERATOR;
import static minecraft.bedwars.cmd.pl.SpawnCommand.SPAWN;

public class PlayerInteractListener implements Listener {
  
  @EventHandler
  public void onNPCLeftClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      NPC npc = evt.getNPC();
      BedWars game = profile.getGame(BedWars.class);
      if (npc.data().has("play-npc")) {
        BedWarsMode mode = BedWarsMode.fromName(npc.data().get("play-npc"));
        if (mode != null) {
          new MenuPlay(profile, mode);
        }
      } else if (npc.data().has("stats-npc")) {
        new MenuStatsNPC(profile);
      } else if (game != null && !game.isSpectator(player)) {
        if (npc.data().has("shopkeeper-type")) {
          String type = npc.data().get("shopkeeper-type");
          if (type != null && type.equals("items")) {
            new MenuItemShop(profile, null);
          } else if (type != null && type.equals("upgrades")) {
            new MenuUpgradeShop(profile);
          }
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (evt.getAction().name().contains("RIGHT") && !player.isSneaking() && evt.getClickedBlock() != null && PlayerUtils.isBedBlock(evt.getClickedBlock())) {
      evt.setCancelled(true);
    }
    if (profile != null) {
      if (CREATING.containsKey(player) && CREATING.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          CreateCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else if (BALLOONS.containsKey(player) && BALLOONS.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          BalloonsCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else if (SPAWN.containsKey(player) && ((BedWars) SPAWN.get(player)[0]).getWorld().equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          SpawnCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else if (GENERATOR.containsKey(player) && GENERATOR.get(player).getWorld().equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          GeneratorCommand.handleClick(player, profile, item.getItemMeta().getDisplayName(), evt);
        }
      }
      
      BedWars game = profile.getGame(BedWars.class);
      ItemStack item = player.getItemInHand();
      if (game == null && !BuildCommand.hasBuilder(player)) {
        evt.setCancelled(true);
      } else {
        if (game != null && game.isSpectator(player) && evt.getClickedBlock() != null && evt.getClickedBlock().getType().name().contains("CHEST") && evt.getAction().name().contains("RIGHT")) {
          evt.setCancelled(true);
        } else if (game != null && game.getState() == GameState.EMJOGO && !game.isSpectator(player)) {
          if (evt.getAction().name().contains("RIGHT")) {
            if (item != null && item.getType().name().contains("WATER_BUCKET")) {
              if (evt.getClickedBlock() != null) {
                Block block = evt.getClickedBlock();
                if (!game.getConfig().getCubeId().contains(block.getLocation())
                    || !game.getConfig().getCubeId().contains(block.getRelative(BlockFace.UP).getLocation())) {
                  evt.setCancelled(true);
                  return;
                }
                
                boolean nearZone =
                    game.listGenerators().stream().anyMatch(generator -> generator.getLocation().getBlock().getLocation().distance(block.getLocation()) < 3);
                if (!nearZone) {
                  nearZone = game.listTeams().stream().anyMatch(team -> team.nearBlockedZone(block));
                }
                
                evt.setCancelled(nearZone);
                if (!nearZone) {
                  Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
                      PlayerUtils.removeItem(player.getInventory(), Material.matchMaterial("BUCKET"), 1), 1);
                }
              }
            } else if (item != null) {
              if (item.getType().name().contains("FIREBALL")) {
                Location newLocation = player.getLineOfSight((HashSet<Byte>) null, 2).get(1).getLocation().setDirection(player.getLocation().getDirection());
                Fireball fireball = player.getWorld().spawn(newLocation, Fireball.class);
                PlayerUtils.removeItem(player.getInventory(), item.getType(), 1);
                evt.setCancelled(true);
              } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().equals("§aLocalizador")) {
                  new MenuTrackerShop(profile);
                }
              } else if (evt.getClickedBlock() != null && evt.getAction() == Action.RIGHT_CLICK_BLOCK && item.getType() == Material.MONSTER_EGG) {
                if (game.getTeam(player) != null) {
                  NMS.createIronGolem(game, game.getTeam(player), evt.getClickedBlock().getLocation().add(0, 1, 0));
                  PlayerUtils.removeItem(player.getInventory(), item.getType(), 1);
                  evt.setCancelled(true);
                }
              } else if (evt.getClickedBlock() != null && evt.getAction() == Action.RIGHT_CLICK_BLOCK && item.getType() == Material.HOPPER) {
                if (game.getTeam(player) != null) {
                  NMS.createSilverfish(game, game.getTeam(player), evt.getClickedBlock().getLocation().add(0, 1, 0));
                  PlayerUtils.removeItem(player.getInventory(), item.getType(), 1);
                  evt.setCancelled(true);
                }
              }
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
        BedWars game = profile.getGame(BedWars.class);
        if (game == null) {
          player.teleport(Core.getLobby());
        } else {
          if (game.getState() != GameState.EMJOGO || game.isSpectator(player)) {
            player.teleport(game.getCubeId().getCenterLocation());
          } else {
            ((CraftPlayer) player).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, (float) player.getMaxHealth());
          }
        }
      }
    }
  }
}
