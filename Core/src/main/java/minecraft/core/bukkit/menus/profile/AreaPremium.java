package minecraft.core.bukkit.menus.profile;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.menus.MenuProfile;
import minecraft.core.bukkit.menus.profile.premium.MenuAnimacoes;
import minecraft.core.bukkit.menus.profile.premium.MenuMensagens;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


/**
 * Menu do setor premium do jogador.
 * Permite acessar funcionalidades premium como Animações de Chegada e Mensagens de Chegada.
 *
 * @author Luiz
 * @version 1.0
 */
public class AreaPremium extends PlayerMenu {

    // Constantes
    private static final String MENU_TITLE = "Setor Premium";
    private static final int MENU_ROWS = 4;

    // Slots dos itens
    private static final int SLOT_ANIMACOES = 11;
    private static final int SLOT_MENSAGENS = 12;
    private static final int SLOT_VOLTAR = 31;

    /**
     * Construtor do menu premium.
     *
     * @param profile Perfil do jogador
     */
    public AreaPremium(Profile profile) {
        super(profile.getPlayer(), MENU_TITLE, MENU_ROWS);

        // Verificação adicional de segurança
        if (!hasIronOrHigherRank(profile)) {
            profile.getPlayer().sendMessage("§cVocê não tem permissão para acessar o Setor Premium!");
            profile.getPlayer().closeInventory();
            return;
        }

        setupItems(profile);
        register(Core.getInstance());
        open();
    }

    /**
     * Configura os itens do menu.
     *
     * @param profile Perfil do jogador
     */
    private void setupItems(Profile profile) {
        setItem(SLOT_ANIMACOES, createAnimacoesItem(profile));
        setItem(SLOT_MENSAGENS, createMensagensItem(profile));
        setItem(SLOT_VOLTAR, createVoltarItem());
    }

    /**
     * Cria o item de Animações de Chegada.
     *
     * @param profile Perfil do jogador
     * @return ItemStack do item
     */
    private ItemStack createAnimacoesItem(Profile profile) {
        return BukkitUtils.deserializeItemStack(
                "SKULL_ITEM:1 : 1 : nome>&aAnimações de Chegada : desc>&7Personalize suas animações\n" +
                        "&7de chegada no servidor!\n" +
                        " \n" +
                        "&eClique para visualizar!");
    }

    /**
     * Cria o item de Mensagens de Chegada.
     *
     * @param profile Perfil do jogador
     * @return ItemStack do item
     */
    private ItemStack createMensagensItem(Profile profile) {
        return BukkitUtils.deserializeItemStack(
                "BOOK : 1 : nome>&aMensagens de Chegada : desc>&7Personalize suas mensagens\n" +
                        "&7de chegada no servidor!\n" +
                        " \n" +
                        "&eClique para visualizar!");
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
            case SLOT_ANIMACOES:
                EnumSound.CLICK.play(player, 0.5F, 2.0F);
                new MenuAnimacoes(profile);
                break;

            case SLOT_MENSAGENS:
                EnumSound.CLICK.play(player, 0.5F, 2.0F);
                new MenuMensagens(profile);
                break;

            case SLOT_VOLTAR:
                EnumSound.CLICK.play(player, 0.5F, 2.0F);
                new MenuProfile(profile);
                break;
        }
    }

    /**
     * Verifica se o jogador tem rank Iron ou superior.
     *
     * @param profile Perfil do jogador
     * @return true se tem rank Iron ou superior
     */
    private boolean hasIronOrHigherRank(Profile profile) {
        if (profile.getPlayer() == null) {
            return false;
        }
        
        // Ranks em ordem (do mais baixo para o mais alto) conforme Rank.java
        String[] rankOrder = {"membro", "apoiador", "iron", "gold", "emerald", "partner", "partner+", "beta", "builder", "helper", "mod", "mod+", "admin"};
        
        // Obtém o rank atual do jogador
        minecraft.core.core.player.rank.Rank currentRank = minecraft.core.core.player.rank.Rank.getRank(profile.getPlayer(), true);
        String currentRankName = minecraft.core.core.utils.StringUtils.stripColors(currentRank.getName()).toLowerCase();
        
        // Encontra a posição do rank atual
        int currentRankIndex = -1;
        for (int i = 0; i < rankOrder.length; i++) {
            if (rankOrder[i].equals(currentRankName)) {
                currentRankIndex = i;
                break;
            }
        }
        
        // Se não encontrou o rank, assume que é membro (mais baixo)
        if (currentRankIndex == -1) {
            currentRankIndex = 0; // membro
        }
        
        // Encontra a posição do rank Iron
        int ironRankIndex = -1;
        for (int i = 0; i < rankOrder.length; i++) {
            if (rankOrder[i].equals("iron")) {
                ironRankIndex = i;
                break;
            }
        }
        
        // Retorna true se o rank atual é igual ou superior ao Iron
        return currentRankIndex >= ironRankIndex;
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
