package dev.slickcollections.kiwizin.clans.menu;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.clans.clan.Clan;
import dev.slickcollections.kiwizin.libraries.menu.PagedPlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersMenu extends PagedPlayerMenu {
  
  private Map<ItemStack, String> members = new HashMap<>();
  
  public MembersMenu(Player player, Clan clan) {
    super(player, "Membros do Clan", (((int) ST(clan)) / 7) + 4);
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cFechar"), (this.rows * 9) - 5);
    
    List<ItemStack> items = new ArrayList<>();
    List<String> list = new ArrayList<>();
    list.add(clan.getLeader());
    list.addAll(clan.getOfficers());
    list.addAll(clan.getMembers());
    for (String member : list) {
      String item;
      if (player.getName().equals(clan.getLeader()) && !member.equals(player.getName())) {
        if (clan.getOfficers().contains(member)) {
          item = "SKULL_ITEM:3 : 1 : nome>&7" + Role.getColored(member) + " : desc>&fCargo: &7" + clan.getRole(member)
              + "\n \n&eAções:\n &8▪ &fShift + Esquerdo para expulsar o membro\n &8▪ &fShift + Direito para rebaixar para membro\n \n"
              + (Bukkit.getPlayerExact(member) != null ? "&aOnline" : "&7Offline");
        } else {
          item = "SKULL_ITEM:3 : 1 : nome>" + Role.getColored(member) + " : desc>&fCargo: &7" + clan.getRole(member)
              + "\n \n&eAções:\n &8▪ &fShift + Esquerdo para expulsar o membro\n &8▪ &fShift + Direito para promover para oficial\n \n"
              + (Bukkit.getPlayerExact(member) != null ? "&aOnline" : "&7Offline");
        }
      } else if (clan.getOfficers().contains(player.getName())
          && clan.getMembers().contains(member)) {
        item =
            "SKULL_ITEM:3 : 1 : nome>" + StringUtils.getFirstColor(Role.getColored(member))
                + member + " : desc>&fCargo: &7" + clan.getRole(member)
                + "\n \n&eAções:\n &8▪ &fShift + Esquerdo para expulsar o membro\n \n"
                + (Bukkit.getPlayerExact(member) != null ? "&aOnline" : "&7Offline");
      } else {
        item =
            "SKULL_ITEM:3 : 1 : nome>" + StringUtils.getFirstColor(Role.getColored(member))
                + member + " : desc>&fCargo: &7" + clan.getRole(member) + "\n \n"
                + (Bukkit.getPlayerExact(member) != null ? "&aOnline" : "&7Offline");
      }
      
      ItemStack icon;
      if (Bukkit.getPlayerExact(member) != null) {
        icon = BukkitUtils.putProfileOnSkull(Bukkit.getPlayerExact(member), BukkitUtils.deserializeItemStack(item));
      } else {
        if (Manager.getSkin(member, "value") != null) {
          icon = BukkitUtils.deserializeItemStack(item + " : skin>" + Manager.getSkin(member, "value"));
        } else {
          icon = BukkitUtils.deserializeItemStack(item);
        }
      }
      items.add(icon);
      this.members.put(items.get(items.size() - 1), member);
    }
    this.setItems(items);
    items.clear();
    list.clear();
    
    this.open();
    this.register(Core.getInstance());
  }
  
  public static double ST(Clan clan) {
    List<String> membersSize = new ArrayList<>();
    membersSize.add(clan.getLeader());
    membersSize.addAll(clan.getOfficers());
    membersSize.addAll(clan.getMembers());
    return membersSize.size();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        Clan clan = Clan.getClan(this.player);
        if (clan == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            } else if (evt.getSlot() == (this.rows * 9) - 5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.player.closeInventory();
            } else {
              if (clan.getLeader().equals(this.player.getName())) {
                String member = this.members.get(item);
                if (member != null) {
                  if (!member.equals(this.player.getName())) {
                    if (evt.getClick() == ClickType.SHIFT_LEFT) {
                      player.performCommand("clan expulsar " + member);
                      new MembersMenu(player, clan);
                    } else if (evt.getClick() == ClickType.SHIFT_RIGHT) {
                      if (clan.getOfficers().contains(member)) {
                        player.performCommand("clan rebaixar " + member);
                      } else {
                        player.performCommand("clan promover " + member);
                      }
                      new MembersMenu(player, clan);
                    }
                  }
                }
              } else if (clan.getOfficers().contains(player.getName())) {
                String member = this.members.get(item);
                if (member != null && !member.equals(this.player.getName()) && !clan.getOfficers().contains(member)) {
                  if (evt.getClick() == ClickType.SHIFT_LEFT) {
                    player.performCommand("clan expulsar " + member);
                    new MembersMenu(player, clan);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
    this.members.clear();
    this.members = null;
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}