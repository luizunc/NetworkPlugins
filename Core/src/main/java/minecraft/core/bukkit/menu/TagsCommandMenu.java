package minecraft.core.bukkit.menu;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.database.data.DataContainer;
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

public class TagsCommandMenu extends PagedPlayerMenu {
    private final Map<ItemStack, Rank> ranks = new HashMap<>();

    public TagsCommandMenu(Profile profile) {
        super(profile.getPlayer(), "Tags", 6);
        this.previousPage = 45;
        this.nextPage = 53;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        String currentTag = profile.getDataContainer("account", "tag").getAsString();
        List<ItemStack> items = new ArrayList<>();

        for (Rank rank : Rank.listRoles()) {
            boolean hasPermission = rank.has(profile.getPlayer()) || profile.getPlayer().isOp();
            boolean isSelected = rank.getName().equals(currentTag);
            Rank playerRank = Rank.getRoleByName(profile.getDataContainer("account", "tag").getAsString());

            List<String> description = new ArrayList<>();
            description.add("&7↳ Informações:");
            description.add(" ");
            description.add("&7▪ Status: " + (isSelected ? "§aAtivada!" : "§cDesativada"));
            description.add("&7▪ Permissão: " + (hasPermission ? "§aSim" : "§cNão"));
            description.add("&7▪ Prefixo: " + rank.getPrefix() + profile.getPlayer().getName());
            description.add(" ");

            if (hasPermission) {
                if (playerRank != null && playerRank.getName().equals(rank.getName())) {
                    description.add("&aTag Selecionada");
                } else {
                    description.add("&eClique para " + (isSelected ? "remover" : "selecionar"));
                }
            } else {
                description.add("&cVocê não tem permissão para usar esta tag!");
            }

            String glassData;
            if (playerRank != null && playerRank.getName().equals(rank.getName())) {
                glassData = "STAINED_GLASS:5";
            } else {
                glassData = hasPermission ? "STAINED_GLASS:0:1" : "INK_SACK:8 : 1";
            }
            
            String itemName = (hasPermission ? "§a" : "§c") + rank.getName();
            
            ItemStack icon = BukkitUtils.deserializeItemStack(glassData + " : 1 : nome>" + itemName + " : desc>" + String.join("\n", description));
            items.add(icon);
            this.ranks.put(icon, rank);
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

                        Rank rank = this.ranks.get(item);
                        if (rank != null) {
                            if (!rank.has(profile.getPlayer()) && !profile.getPlayer().isOp()) {
                                player.sendMessage("§cVocê não tem permissão para usar esta tag!");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            }

                            String currentTag = profile.getDataContainer("account", "tag").getAsString();
                            if (rank.getName().equals(currentTag)) {
                                // Se clicar na tag que já está selecionada, não faz nada
                                player.sendMessage("§aTag selecionada");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            } else {
                                // IMPORTANTE: Atualizar APENAS a tag visual (SEM ALTERAR COLUNA RANK)
                                minecraft.core.core.database.cache.TagCache.setCache(player.getName(), StringUtils.stripColors(rank.getName()), player.getName());
                                
                                // Aplicar APENAS a tag visual usando TagUtils (sem permissões)
                                TagUtils.setTag(player, rank);
                                
                                // IMPORTANTE: Salvar APENAS na coluna tag (NUNCA na coluna rank)
                                DataContainer container = profile.getDataContainer("account", "tag");
                                container.set(StringUtils.stripColors(rank.getName()));
                                profile.save();
                                
                                // Enviar mensagem de confirmação
                                player.sendMessage("§aTag " + rank.getName() + " §aselecionada.");
                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                                
                                // Fechar o menu
                                player.closeInventory();
                            }
                            // IMPORTANTE: NÃO fazer profile.save() duplicado
                            // O profile já foi salvo acima
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
