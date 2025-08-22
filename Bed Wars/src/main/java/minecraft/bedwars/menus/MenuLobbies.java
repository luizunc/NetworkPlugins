package minecraft.bedwars.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.core.libraries.menu.UpdatablePlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.bedwars.Main;
import minecraft.bedwars.config.LobbiesConfig;
import minecraft.bedwars.lobby.Lobby;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static minecraft.bedwars.lobby.Lobby.listLobbies;

/**
 * Menu de seleção de lobbies.
 * Permite aos jogadores escolher entre diferentes servidores de lobby.
 */
public final class MenuLobbies extends UpdatablePlayerMenu {

    private static final int UPDATE_INTERVAL = 20;
    private static final String ICON_ERROR_MESSAGE = "Erro ao criar ícone para lobby %s: %s";

    /**
     * Constrói o menu de lobbies.
     * @param profile perfil do jogador
     */
    public MenuLobbies(Profile profile) {
        super(profile.getPlayer(), LobbiesConfig.TITLE, LobbiesConfig.ROWS);

        this.update();
        this.register(Core.getInstance(), UPDATE_INTERVAL);
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (!isValidClick(evt)) {
            return;
        }

        evt.setCancelled(true);

        if (evt.getWhoClicked().equals(this.player)) {
            handleInventoryClick(evt);
        }
    }

    /**
     * Verifica se o clique é válido para processamento.
     * @param evt evento de clique
     * @return true se válido, false caso contrário
     */
    private boolean isValidClick(InventoryClickEvent evt) {
        return evt.getInventory().equals(this.getInventory());
    }

    /**
     * Processa o clique no inventário.
     * @param evt evento de clique
     */
    private void handleInventoryClick(InventoryClickEvent evt) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
            this.player.closeInventory();
            return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
            ItemStack item = evt.getCurrentItem();

            if (item != null && item.getType() != Material.AIR) {
                handleLobbySelection(evt.getSlot(), profile);
            }
        }
    }

    /**
     * Processa a seleção de um lobby.
     * @param slot slot clicado
     * @param profile perfil do jogador
     */
    private void handleLobbySelection(int slot, Profile profile) {
        Lobby lobby = findLobbyBySlot(slot);
        if (lobby != null && canConnectToLobby(lobby)) {
            Core.sendServer(profile, lobby.getServerName());
            this.player.closeInventory();
        }
    }

    /**
     * Encontra um lobby pelo slot.
     * @param slot slot do lobby
     * @return lobby encontrado ou null
     */
    private Lobby findLobbyBySlot(int slot) {
        return listLobbies().stream()
                .filter(s -> s.getSlot() == slot)
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica se o jogador pode conectar ao lobby.
     * @param lobby lobby a ser verificado
     * @return true se pode conectar, false caso contrário
     */
    private boolean canConnectToLobby(Lobby lobby) {
        return !Main.currentServerName.contentEquals(lobby.getServerName())
                && lobby.getPlayers() < lobby.getMaxPlayers();
    }

    @Override
    public void update() {
        clearInventory();

        for (Lobby lobby : listLobbies()) {
            createLobbyItem(lobby);
        }
    }

    /**
     * Limpa o inventário antes de atualizar.
     */
    private void clearInventory() {
        this.getInventory().clear();
    }

    /**
     * Cria o item de um lobby no menu.
     * @param lobby lobby para criar o item
     */
    private void createLobbyItem(Lobby lobby) {
        String description = getLobbyDescription(lobby);
        String iconString = createIconString(lobby);
        String finalIconString = replaceVariables(iconString, lobby, description);

        try {
            ItemStack icon = BukkitUtils.deserializeItemStack(finalIconString);

            if (isCurrentLobby(lobby)) {
                BukkitUtils.putGlowEnchantment(icon);
            }

            this.setItem(lobby.getSlot(), icon);
        } catch (Exception e) {
            logIconError(lobby, e);
        }
    }

    /**
     * Obtém a descrição do lobby.
     * @param lobby lobby para obter a descrição
     * @return descrição do lobby
     */
    private String getLobbyDescription(Lobby lobby) {
        return isCurrentLobby(lobby)
                ? LobbiesConfig.Messages.CURRENT
                : LobbiesConfig.Messages.CONNECT;
    }

    /**
     * Cria a string do ícone baseada no lobby.
     * @param lobby lobby para criar o ícone
     * @return string do ícone
     */
    private String createIconString(Lobby lobby) {
        // Extrai o número do lobby (ex: "lobby1" -> "1")
        String lobbyNumberStr = lobby.getServerName().replace("lobby", "");
        int lobbyNumber = Integer.parseInt(lobbyNumberStr);
        
        // Verifica se é o lobby atual
        boolean isCurrent = isCurrentLobby(lobby);
        
        // Obtém o ícone apropriado
        return LobbiesConfig.getLobbyIcon(lobbyNumber, isCurrent);
    }

    /**
     * Substitui as variáveis na string do ícone.
     * @param iconString string do ícone
     * @param lobby lobby para substituir variáveis
     * @param description descrição do lobby
     * @return string final do ícone
     */
    private String replaceVariables(String iconString, Lobby lobby, String description) {
        return iconString
                .replace("{players}", StringUtils.formatNumber(lobby.getPlayers()))
                .replace("{maxplayers}", StringUtils.formatNumber(lobby.getMaxPlayers()))
                .replace("{description}", StringUtils.formatColors(description));
    }

    /**
     * Verifica se o lobby é o atual.
     * @param lobby lobby a ser verificado
     * @return true se é o lobby atual, false caso contrário
     */
    private boolean isCurrentLobby(Lobby lobby) {
        return Main.currentServerName.equals(lobby.getServerName());
    }

    /**
     * Registra erro na criação do ícone.
     * @param lobby lobby com erro
     * @param e exceção ocorrida
     */
    private void logIconError(Lobby lobby, Exception e) {
        Main.getInstance().getLogger().warning(
                String.format(ICON_ERROR_MESSAGE, lobby.getServerName(), e.getMessage()));
    }

    /**
     * Cancela o menu e remove os listeners.
     */
    public void cancel() {
        super.cancel();
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
}
