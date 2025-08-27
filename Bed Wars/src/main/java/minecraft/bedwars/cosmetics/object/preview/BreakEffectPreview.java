package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.types.BreakEffect;
import minecraft.bedwars.cmd.pl.BuildCommand;
import minecraft.bedwars.menus.cosmetics.MenuCosmetics;
import minecraft.core.bukkit.Core;
import minecraft.core.core.game.FakeGame;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.Hotbar;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Preview para efeitos de quebra de cama
 * Permite que o jogador teste animações de quebra de cama em um ambiente controlado
 */
public class BreakEffectPreview extends AbstractPreview<BreakEffect> implements Listener {

    // Constantes para configuração
    private static final String BED_MATERIAL_PATTERN = "BED";
    private static final String RESTRICTED_BREAK_MESSAGE = "§cVocê só pode quebrar a cama neste preview!";
    private static final int BED_OFFSET_Z = 2;
    
    // Constantes para duração das animações (em ticks)
    private static final int FLAME_ANIMATION_DURATION = 80;    // 4 segundos
    private static final int VILLAGER_ANIMATION_DURATION = 50; // 2.5 segundos
    private static final int DEFAULT_ANIMATION_DURATION = 60;  // 3 segundos
    
    // Localizações estáticas
    private static final Location[] PREVIEW_LOCATIONS = new Location[1];
    
    // Lista de jogadores em preview de quebra de cama
    private static final java.util.Set<String> PLAYERS_IN_PREVIEW = new java.util.HashSet<>();

    static {
        initializePreviewLocations();
    }

    // Campos da instância
    private Location originalLocation;
    private Location bedLocation;
    private boolean isActive;
    private Profile profile;
    private Material originalBedMaterial;
    private boolean bedWasBroken;

    /**
     * Construtor do preview de efeito de quebra de cama
     * @param profile Perfil do jogador
     * @param cosmetic Efeito cosmético a ser testado
     */
    public BreakEffectPreview(Profile profile, BreakEffect cosmetic) {
        super(profile, cosmetic);
        this.profile = profile;
        this.isActive = true;
        this.bedWasBroken = false;
        addPlayerToPreview(this.player);
        setupPreview();
    }

    /**
     * Inicializa as localizações de preview a partir da configuração
     */
    private static void initializePreviewLocations() {
        if (CONFIG.contains("cama")) {
            String locationValue = CONFIG.getString("cama.1");
            if (locationValue != null) {
                PREVIEW_LOCATIONS[0] = BukkitUtils.deserializeLocation(locationValue);
            }
        }
    }

    /**
     * Método público para manter compatibilidade com outros arquivos
     * @deprecated Use initializePreviewLocations() instead
     */
    public static void createLocations() {
        initializePreviewLocations();
    }

    /**
     * Verifica se um jogador está em preview de quebra de cama
     */
    public static boolean isPlayerInPreview(Player player) {
        return PLAYERS_IN_PREVIEW.contains(player.getName());
    }

    /**
     * Adiciona um jogador à lista de preview
     */
    private static void addPlayerToPreview(Player player) {
        PLAYERS_IN_PREVIEW.add(player.getName());
    }

    /**
     * Remove um jogador da lista de preview
     */
    private static void removePlayerFromPreview(Player player) {
        PLAYERS_IN_PREVIEW.remove(player.getName());
    }

    /**
     * Configura o ambiente de preview
     */
    private void setupPreview() {
        saveOriginalState();
        configurePlayerForPreview();
        setupPreviewWorld();
        registerEventListeners();
    }

    /**
     * Salva o estado original do jogador
     */
    private void saveOriginalState() {
        this.originalLocation = this.player.getLocation();
    }

    /**
     * Configura o jogador para o preview
     */
    private void configurePlayerForPreview() {
        // Configurar perfil para jogo fake
        this.profile.setGame(FakeGame.FAKE_GAME);
        
        // Limpar inventário e hotbar
        clearPlayerInventory();
        
        // Configurar modo de jogo
        this.player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        
        // Garantir que o jogador possa quebrar blocos
        ensurePlayerCanBreakBlocks();
        
        // Ocultar jogador dos outros
        hidePlayerFromOthers();
        
        // Teleportar para área de preview
        this.player.teleport(PREVIEW_LOCATIONS[0]);
    }

    /**
     * Garante que o jogador tenha as permissões necessárias para quebrar blocos
     */
    private void ensurePlayerCanBreakBlocks() {
        // Adicionar o jogador à lista de builders temporariamente
        if (!BuildCommand.hasBuilder(this.player)) {
            BuildCommand.addBuilder(this.player);
        }
        
        // Dar permissões temporárias para quebrar blocos (duração maior)
        this.player.addAttachment(Main.getInstance(), "bedwars.break", true, 300); // 300 ticks = 15 segundos
        
        // Garantir que o jogador não seja afetado por proteções de build
        this.player.addAttachment(Main.getInstance(), "bedwars.build", true, 300);
    }

    /**
     * Limpa o inventário do jogador e remove hotbar personalizada
     */
    private void clearPlayerInventory() {
        this.player.getInventory().clear();
        this.profile.setHotbar(null);
    }

    /**
     * Oculta o jogador dos outros jogadores online
     */
    private void hidePlayerFromOthers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
        }
    }

    /**
     * Configura o mundo de preview com a cama
     */
    private void setupPreviewWorld() {
        this.bedLocation = PREVIEW_LOCATIONS[0].clone().add(0, 0, BED_OFFSET_Z);
        
        // Salvar o material original da cama
        this.originalBedMaterial = this.bedLocation.getBlock().getType();
        
        // Garantir que a área esteja limpa
        clearBedArea();
        
        // Colocar a cama
        placeBed();
        
        // Verificar se a cama foi colocada corretamente
        verifyBedPlacement();
    }

    /**
     * Limpa a área onde a cama será colocada
     */
    private void clearBedArea() {
        // Limpar a área da cama e adjacente
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location loc = this.bedLocation.clone().add(x, 0, z);
                if (loc.getBlock().getType() != Material.AIR) {
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    /**
     * Coloca a cama no local correto
     */
    private void placeBed() {
        this.bedLocation.getBlock().setType(Material.BED);
        
        // Informar ao jogador sobre a cama
        this.player.sendMessage("§aCama colocada! Quebre-a para ver a animação.");
        this.player.sendMessage("§eA cama será restaurada automaticamente após a quebra.");
    }

    /**
     * Verifica se a cama foi colocada corretamente
     */
    private void verifyBedPlacement() {
        if (!this.bedLocation.getBlock().getType().name().contains("BED")) {
            this.player.sendMessage("§cErro ao colocar a cama. Tentando novamente...");
            placeBed();
            
            // Verificar novamente
            if (!this.bedLocation.getBlock().getType().name().contains("BED")) {
                this.player.sendMessage("§cErro persistente ao colocar a cama. Preview pode não funcionar corretamente.");
            }
        } else {
            this.player.sendMessage("§aCama colocada com sucesso!");
        }
    }

    /**
     * Registra os listeners de eventos
     */
    private void registerEventListeners() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    /**
     * Handler para quando o jogador sai do servidor
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(this.player)) {
            // Garantir que a cama seja restaurada mesmo se o jogador sair
            forceRestoreBed();
            cleanup();
        }
    }

    /**
     * Força a restauração da cama independentemente do estado
     */
    private void forceRestoreBed() {
        if (this.bedLocation != null) {
            // Sempre restaurar o material original
            this.bedLocation.getBlock().setType(this.originalBedMaterial);
            
            // Se era uma cama, restaurar também o bloco adjacente
            if (this.originalBedMaterial.name().contains("BED")) {
                restoreAdjacentBedBlock();
            }
        }
    }

    /**
     * Handler de prioridade baixa para garantir que a cama seja quebrável
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakLowest(BlockBreakEvent event) {
        if (isPreviewPlayer(event.getPlayer()) && isBedBlock(event.getBlock().getType())) {
            // Forçar que a cama seja quebrável, sobrescrevendo outros listeners
            event.setCancelled(false);
        }
    }

    /**
     * Handler de prioridade alta para garantir que a cama seja quebrável
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakHighest(BlockBreakEvent event) {
        if (isPreviewPlayer(event.getPlayer()) && isBedBlock(event.getBlock().getType())) {
            // Garantir que a cama seja quebrável mesmo se outros listeners cancelarem
            event.setCancelled(false);
        }
    }

    /**
     * Handler para detectar quando a cama é quebrada (mesmo se o evento for cancelado)
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreakMonitor(BlockBreakEvent event) {
        if (isPreviewPlayer(event.getPlayer()) && isBedBlock(event.getBlock().getType()) && isActive) {
            // Se a cama foi quebrada (mesmo que o evento tenha sido cancelado), executar animação
            if (event.getBlock().getType() == Material.AIR || event.isCancelled()) {
                executeBreakAnimation(event.getBlock().getLocation());
                scheduleReturnToMenu();
            }
        }
    }

    /**
     * Handler principal para quebra de blocos
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isPreviewPlayer(event.getPlayer()) || !isActive) {
            return;
        }

        if (isBedBlock(event.getBlock().getType())) {
            handleBedBreak(event);
        } else {
            handleOtherBlockBreak(event);
        }
    }

    /**
     * Verifica se o jogador é o jogador do preview
     */
    private boolean isPreviewPlayer(Player player) {
        return player.equals(this.player);
    }

    /**
     * Verifica se o material é uma cama
     */
    private boolean isBedBlock(Material material) {
        return material.name().contains(BED_MATERIAL_PATTERN);
    }

    /**
     * Processa a quebra da cama
     */
    private void handleBedBreak(BlockBreakEvent event) {
        // Permitir quebra da cama
        event.setCancelled(false);
        
        // Marcar que a cama foi quebrada
        this.bedWasBroken = true;
        
        // Executar animação
        executeBreakAnimation(event.getBlock().getLocation());
        
        // Restaurar a cama automaticamente após um pequeno delay
        restoreBedAfterBreak();
        
        // Agendar retorno ao menu
        scheduleReturnToMenu();
    }

    /**
     * Restaura a cama automaticamente após ser quebrada
     */
    private void restoreBedAfterBreak() {
        // Restaurar a cama após 2 ticks (0.1 segundos) para garantir que a animação seja vista
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if (this.bedLocation != null) {
                // Restaurar o material original
                this.bedLocation.getBlock().setType(this.originalBedMaterial);
                
                // Se era uma cama, restaurar também o bloco adjacente
                if (this.originalBedMaterial.name().contains("BED")) {
                    restoreAdjacentBedBlock();
                }
                
                this.player.sendMessage("§aCama restaurada automaticamente!");
            }
        }, 2L);
    }

    /**
     * Processa a tentativa de quebra de outros blocos
     */
    private void handleOtherBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
        this.player.sendMessage(RESTRICTED_BREAK_MESSAGE);
    }

    /**
     * Executa a animação de quebra de cama
     */
    private void executeBreakAnimation(Location bedLocation) {
        this.cosmetic.showIn(this.player, bedLocation);
        this.player.sendMessage("§aAnimação de quebra de cama executada!");
    }

    /**
     * Agenda o retorno ao menu após a animação
     */
    private void scheduleReturnToMenu() {
        int animationDuration = calculateAnimationDuration();
        
        // Desativar o preview imediatamente para evitar múltiplas execuções
        this.isActive = false;
        
        // Informar o jogador sobre o retorno
        this.player.sendMessage("§eRetornando ao lobby em " + (animationDuration / 20) + " segundos...");
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            // Limpar recursos
            cleanup();
            
            // Retornar ao lobby
            returnToLobby();
        }, animationDuration);
    }

    /**
     * Retorna o jogador ao lobby
     */
    private void returnToLobby() {
        Profile playerProfile = Profile.getProfile(this.player.getName());
        if (playerProfile == null) {
            return;
        }

        restorePlayerState(playerProfile);
        teleportToLobby(playerProfile);
    }

    /**
     * Teleporta o jogador para o lobby
     */
    private void teleportToLobby(Profile playerProfile) {
        // Teleportar para o lobby
        this.player.teleport(Core.getLobby());
        
        // Aplicar hotbar com um pequeno delay para garantir que funcione
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            // Limpar o inventário antes de aplicar a hotbar
            this.player.getInventory().clear();
            
            // Aplicar hotbar do lobby
            playerProfile.setHotbar(Hotbar.getHotbarById("lobby"));
            playerProfile.refresh();
            playerProfile.refreshPlayers();
            
            // Mensagem de confirmação
            this.player.sendMessage("§aVocê foi enviado de volta ao lobby!");
            
            // Abrir menu de cosméticos
            openCosmeticsMenu(playerProfile);
        }, 5L); // 5 ticks = 0.25 segundos
    }

    /**
     * Calcula a duração da animação baseada no tipo de efeito
     */
    private int calculateAnimationDuration() {
        String effectName = this.cosmetic.getName().toLowerCase();
        String particleName = this.cosmetic.getParticle().name().toLowerCase();

        if (isFlameEffect(particleName, effectName)) {
            return FLAME_ANIMATION_DURATION;
        } else if (isVillagerEffect(particleName, effectName)) {
            return VILLAGER_ANIMATION_DURATION;
        } else {
            return DEFAULT_ANIMATION_DURATION;
        }
    }

    /**
     * Verifica se é um efeito de chamas
     */
    private boolean isFlameEffect(String particleName, String effectName) {
        return particleName.contains("flame") || effectName.contains("chamas");
    }

    /**
     * Verifica se é um efeito de villager
     */
    private boolean isVillagerEffect(String particleName, String effectName) {
        return particleName.contains("villager") || 
               particleName.contains("happy") || 
               effectName.contains("villager");
    }

    /**
     * Limpa recursos e restaura o estado original
     */
    public void cleanup() {
        if (!isActive) {
            return;
        }
        
        // Garantir que a cama seja restaurada
        forceRestoreBed();
        
        resetFields();
        unregisterListeners();
        removeBuilderPermissions();
        this.isActive = false;
    }

    /**
     * Remove as permissões de builder do jogador
     */
    private void removeBuilderPermissions() {
        // Remover o jogador da lista de builders
        BuildCommand.remove(this.player);
        // Remover o jogador da lista de preview
        removePlayerFromPreview(this.player);
    }

    /**
     * Restaura a cama se ela foi quebrada
     */
    private void restoreBed() {
        if (this.bedLocation != null) {
            // Verificar se a cama foi quebrada ou se precisamos restaurar o material original
            if (this.bedWasBroken || this.bedLocation.getBlock().getType() != this.originalBedMaterial) {
                // Restaurar o material original
                this.bedLocation.getBlock().setType(this.originalBedMaterial);
                
                // Se era uma cama, restaurar também o bloco adjacente
                if (this.originalBedMaterial.name().contains("BED")) {
                    restoreAdjacentBedBlock();
                }
                
                // Log para debug
                this.player.sendMessage("§aCama restaurada ao seu estado original!");
            }
        }
    }

    /**
     * Restaura o bloco principal da cama
     */
    private void restoreBedBlock() {
        this.bedLocation.getBlock().setType(Material.BED);
    }

    /**
     * Restaura o bloco adjacente da cama se necessário
     */
    private void restoreAdjacentBedBlock() {
        // Verificar todas as direções possíveis para a cama adjacente
        int[][] directions = {
            {1, 0, 0},   // Leste
            {-1, 0, 0},  // Oeste
            {0, 0, 1},   // Sul
            {0, 0, -1}   // Norte
        };
        
        for (int[] direction : directions) {
            Location adjacentLocation = this.bedLocation.clone().add(direction[0], direction[1], direction[2]);
            if (adjacentLocation.getBlock().getType() == Material.AIR) {
                // Colocar a cama adjacente
                adjacentLocation.getBlock().setType(Material.BED);
                break; // Só precisamos de uma cama adjacente
            }
        }
    }

    /**
     * Reseta os campos da instância
     */
    private void resetFields() {
        this.originalLocation = null;
        this.bedLocation = null;
    }

    /**
     * Remove os listeners de eventos
     */
    private void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }

    /**
     * Retorna o jogador ao menu de cosméticos
     */
    @Override
    public void returnToMenu() {
        Profile playerProfile = Profile.getProfile(this.player.getName());
        if (playerProfile == null) {
            return;
        }

        restorePlayerState(playerProfile);
        openCosmeticsMenu(playerProfile);
    }

    /**
     * Restaura o estado original do jogador
     */
    private void restorePlayerState(Profile playerProfile) {
        // Remover permissões de builder
        removeBuilderPermissions();
        
        // Restaurar modo de jogo
        this.player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        
        // Restaurar permissões de voo
        this.player.setAllowFlight(this.player.hasPermission("core.fly"));
        
        // Restaurar perfil (sem jogo ativo)
        playerProfile.setGame(null);
    }

    /**
     * Abre o menu de cosméticos
     */
    private void openCosmeticsMenu(Profile playerProfile) {
        new MenuCosmetics<>(playerProfile, "Quebra de Cama", BreakEffect.class);
    }

    /**
     * Método necessário para BukkitRunnable (não utilizado neste preview)
     */
    @Override
    public void run() {
        // Não utilizado neste preview
    }

    /**
     * Método de parada (alias para cleanup)
     */
    public void stop() {
        // Garantir que a cama seja restaurada antes de parar
        forceRestoreBed();
        cleanup();
    }
}