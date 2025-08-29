package minecraft.core.bukkit.menu;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.enums.Medal;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.TagUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedalsCommandMenu extends PagedPlayerMenu {
    private final Map<ItemStack, Medal> medals = new HashMap<>();

    public MedalsCommandMenu(Profile profile) {
        super(profile.getPlayer(), "Medalhas", 6);
        this.previousPage = 45;
        this.nextPage = 53;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        String currentMedal = profile.getDataContainer("account", "medalha").getAsString();
        List<ItemStack> items = new ArrayList<>();

        for (Medal medal : Medal.values()) {
            boolean hasPermission = medal.has(profile.getPlayer()) || profile.getPlayer().isOp();
            boolean isSelected = medal.getName().equals(currentMedal);
            Rank rank = Rank.getRoleByName(profile.getDataContainer("account", "tag").getAsString());
            if (rank == null) {
                rank = Rank.getRank(profile.getPlayer());
            }
            
            List<String> description = new ArrayList<>();
            description.add("&7↳ Informações:");
            description.add(" ");
            description.add("&7▪ Status: " + (isSelected ? "§aAtivada!" : "§cDesativada"));
            description.add("&7▪ Permissão: " + (hasPermission ? "§aSim" : "§cNão"));
            description.add("&7▪ Símbolo: " + medal.getSuffix());
            description.add(" ");
            description.add("§bExemplo:");
            description.add(medal.getSuffix() + " " + rank.getPrefix() + profile.getPlayer().getName());
            description.add(" ");
            
            if (hasPermission) {
                if (isSelected) {
                    description.add("&aMedalha selecionada");
                } else {
                    description.add("&eClique para ativar!");
                }
            } else {
                description.add("&cVocê não tem permissão para usar esta medalha!");
            }

            String glassData;
            if (isSelected) {
                glassData = "STAINED_GLASS:5"; // Azul - medalha ativa (igual ao tag selecionado)
            } else {
                glassData = hasPermission ? "STAINED_GLASS:0:1" : "INK_SACK:8"; // Branco - disponível, Cinza escuro - indisponível (igual ao tag)
            }
            String itemName = (hasPermission ? "§a" : "§c") + medal.getName();
            
            ItemStack icon = BukkitUtils.deserializeItemStack(glassData + " : 1 : nome>" + itemName + " : desc>" + String.join("\n", description));
            items.add(icon);
            this.medals.put(icon, medal);
        }

        this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"), 49);
        this.setItems(items);
        this.register(Core.getInstance());
        this.open();
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

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        if (evt.getSlot() == 49) {
                            this.player.closeInventory();
                            return;
                        }

                        if (evt.getSlot() == 45) {
                            this.openPrevious();
                            return;
                        }

                        if (evt.getSlot() == 53) {
                            this.openNext();
                            return;
                        }

                        Medal medal = this.medals.get(item);
                        if (medal != null) {
                            if (!medal.has(profile.getPlayer()) && !profile.getPlayer().isOp()) {
                                this.player.sendMessage("§cVocê não tem permissão para usar esta medalha!");
                                this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            }

                            String currentMedal = profile.getDataContainer("account", "medalha").getAsString();
                            if (medal.getName().equals(currentMedal)) {
                                // Se clicar na medalha que já está selecionada, não faz nada
                                this.player.sendMessage("§aMedalha selecionada");
                                this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            } else {
                                profile.getDataContainer("account", "medalha").set(StringUtils.stripColors(medal.getName()));
                                profile.getPlayer().sendMessage("§aVocê selecionou a medalha " + medal.getName() + "§a.");
                            }
                            profile.save();
                            
                            // Atualizar o cache com a medalha
                            String currentTag = profile.getDataContainer("account", "tag").getAsString();
                            if (currentTag != null && !currentTag.isEmpty()) {
                                minecraft.core.core.database.cache.TagCache.setCache(this.player.getName(), currentTag, this.player.getName());
                            }
                            
                            TagUtils.setMedal(this.player, medal);
                            this.player.playSound(this.player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                            new MedalsCommandMenu(profile);
                        }
                    }
                }
            }
        }
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
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
            this.cancel();
        }
    }
}
