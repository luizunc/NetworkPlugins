package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Menu de estatísticas do jogador.
 * Permite visualizar estatísticas de Sky Wars, Bed Wars e outros minigames.
 *
 * @author Luiz
 * @version 1.0
 */
public class MenuStatistics extends PlayerMenu {

    // Constantes
    private static final String MENU_TITLE = "Estatísticas";
    private static final int MENU_ROWS = 4;

    // Slots dos itens
    private static final int SLOT_SKYWARS = 11;
    private static final int SLOT_BEDWARS = 12;
    private static final int SLOT_VOLTAR = 31;

    // Jogador alvo (dono das estatísticas)
    private final Player targetPlayer;
    private final Profile targetProfile;
    private final boolean openedViaCommand;

    /**
     * Construtor do menu de estatísticas.
     *
     * @param profile Perfil do jogador
     */
    public MenuStatistics(Profile profile) {
        this(profile.getPlayer(), profile, false);
    }

    /**
     * Construtor do menu de estatísticas com jogador alvo específico.
     *
     * @param viewer Jogador que está visualizando o menu
     * @param targetProfile Perfil do jogador alvo (dono das estatísticas)
     */
    public MenuStatistics(Player viewer, Profile targetProfile) {
        this(viewer, targetProfile, true);
    }

    /**
     * Construtor do menu de estatísticas com controle de origem.
     *
     * @param viewer Jogador que está visualizando o menu
     * @param targetProfile Perfil do jogador alvo (dono das estatísticas)
     * @param openedViaCommand Se o menu foi aberto via comando /stats
     */
    public MenuStatistics(Player viewer, Profile targetProfile, boolean openedViaCommand) {
        super(viewer, MENU_TITLE, MENU_ROWS);
        this.targetPlayer = targetProfile.getPlayer();
        this.targetProfile = targetProfile;
        this.openedViaCommand = openedViaCommand;

        setupItems();
        register(Core.getInstance());
        open();
    }

    /**
     * Configura os itens do menu.
     */
    private void setupItems() {
        setItem(SLOT_SKYWARS, createSkyWarsItem());
        setItem(SLOT_BEDWARS, createBedWarsItem());
        setItem(SLOT_VOLTAR, createVoltarItem());
    }

    /**
     * Cria o item de estatísticas do Sky Wars.
     *
     * @return ItemStack do item
     */
    private ItemStack createSkyWarsItem() {
        String desc = "&eSolo:\n" +
                " &8▪ &fAbates: &7%Core_SkyWars_solokills%\n" +
                " &8▪ &fMortes: &7%Core_SkyWars_solodeaths%\n" +
                " &8▪ &fVitórias: &7%Core_SkyWars_solowins%\n" +
                " &8▪ &fPartidas: &7%Core_SkyWars_sologames%\n" +
                " &8▪ &fAssistências: &7%Core_SkyWars_soloassists%\n" +
                " \n" +
                "&eRanked:\n" +
                " &8▪ &fAbates: &7%Core_SkyWars_rankedkills%\n" +
                " &8▪ &fMortes: &7%Core_SkyWars_rankeddeaths%\n" +
                " &8▪ &fVitórias: &7%Core_SkyWars_rankedwins%\n" +
                " &8▪ &fPartidas: &7%Core_SkyWars_rankedgames%\n" +
                " &8▪ &fPontos: &7%Core_SkyWars_rankedpoints%\n" +
                " \n" +
                "&fCoins: &6%Core_SkyWars_coins%";

        return BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(targetPlayer,
                "EYE_OF_ENDER : 1 : nome>&aSky Wars : desc>" + desc));
    }

    /**
     * Cria o item de estatísticas do Bed Wars.
     *
     * @return ItemStack do item
     */
    private ItemStack createBedWarsItem() {
        String desc = "&eGeral:\n" +
                " &8▪ &fPartidas: &7%Core_BedWars_games%\n" +
                " &8▪ &fAbates: &7%Core_BedWars_kills%\n" +
                " &8▪ &fMortes: &7%Core_BedWars_deaths%\n" +
                " &8▪ &fAbates Finais: &7%Core_BedWars_finalkills%\n" +
                " &8▪ &fMortes Finais: &7%Core_BedWars_finaldeaths%\n" +
                " &8▪ &fVitórias: &7%Core_BedWars_wins%\n" +
                " &8▪ &fCamas destruídas: &7%Core_BedWars_beds%\n" +
                " &8▪ &fCamas perdidas: &7%Core_BedWars_bedslosteds%\n" +
                " \n" +
                "&fCoins: &6%Core_BedWars_coins%";

        return BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(targetPlayer,
                "BED : 1 : nome>&aBed Wars : desc>" + desc));
    }

    /**
     * Cria o item de voltar.
     *
     * @return ItemStack do item
     */
    private ItemStack createVoltarItem() {
        return BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar");
    }

    /**
     * Manipula o clique no inventário.
     *
     * @param evt Evento de clique
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (!evt.getInventory().equals(getInventory()) || !evt.getWhoClicked().equals(player)) {
            return;
        }

        evt.setCancelled(true);

        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            player.closeInventory();
            return;
        }

        if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(getInventory())) {
            return;
        }

        ItemStack item = evt.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        handleItemClick(evt.getSlot(), profile);
    }

    /**
     * Manipula o clique em itens específicos.
     *
     * @param slot Slot clicado
     * @param profile Perfil do jogador
     */
    private void handleItemClick(int slot, Profile profile) {
        switch (slot) {
            case SLOT_SKYWARS:
            case SLOT_BEDWARS:
                EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
                break;

            case SLOT_VOLTAR:
                EnumSound.CLICK.play(player, 0.5F, 2.0F);
                if (openedViaCommand) {
                    // Se foi aberto via comando /stats, fecha o menu
                    player.closeInventory();
                } else {
                    // Se foi aberto via menu de perfil, volta para o menu de perfil
                    new MenuProfile(profile);
                }
                break;
        }
    }

    /**
     * Cancela o registro de eventos.
     */
    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    /**
     * Manipula o evento de saída do jogador.
     *
     * @param evt Evento de saída
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(player)) {
            cancel();
        }
    }

    /**
     * Manipula o evento de fechamento do inventário.
     *
     * @param evt Evento de fechamento
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(player) && evt.getInventory().equals(getInventory())) {
            cancel();
        }
    }
}