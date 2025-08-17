package dev.slickcollections.kiwizin.murder.menus.cosmetics;

import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.container.HotbarContainer;
import dev.slickcollections.kiwizin.murder.menus.MenuShop;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class MenuHotbarConfigSelect extends PlayerMenu {

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getInventory())) {
            evt.setCancelled(true);

            if (evt.getWhoClicked().equals(this.player)) {
                Profile profile = Profile.getProfile(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        String name = names.get(evt.getSlot());
                        if (evt.getSlot() == 31) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuShop(profile);
                        } else if (name != null) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuHotbarConfig(profile, name.equals("Assassinos") ? "murder" : name.toLowerCase());
                        }
                    }
                }
            }
        }
    }

    private Map<Integer, String> names = new HashMap<>();

    public MenuHotbarConfigSelect(Profile profile) {
        super(profile.getPlayer(), "Configurar qual função?", 4);

        int[] slots = new int[] {11, 13, 15};
        String[] icons = new String[]{"DIAMOND_SWORD", "BOW", "GOLD_INGOT"};
        String[] roles = new String[]{"Assassinos", "Detetive", "Inocente"};

        for (int i = 0; i < 3; i++) {
            this.setItem(slots[i], BukkitUtils.deserializeItemStack(
                    icons[i] + " : 1 : esconder>tudo : nome>&a" + roles[i] + " : desc>&7Configurar os itens da\n&7função " + roles[i] + "\n \n&eClique para configurar!"));
            names.put(slots[i], roles[i]);
        }

        this.setItem(31, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar : desc>&7Para a Loja."));

        this.register(Main.getInstance());
        this.open();
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }

    protected String formatToT(String type) {
        return (type.equalsIgnoreCase("inocente") ? "innocent" : type.equals("murder") ? "murder" : "detective") + "Hotbar";
    }
}
