package minecraft.bedwars.listeners.player;

import minecraft.bedwars.game.BedWars;
import minecraft.core.core.game.GameState;
import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.holograms.api.Hologram;
import minecraft.core.core.player.Profile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestHitListener implements Listener {
    
    // Mapa para armazenar os itens usados para hitar chests por jogador
    private static final Map<UUID, ItemStack> playerHitItems = new HashMap<>();
    
    // Mapa para armazenar hologramas por localização
    private static final Map<String, Hologram> chestHolograms = new HashMap<>();
    
    // Mapa para armazenar os baús por localização
    private static final Map<String, org.bukkit.block.Chest> chestBlocks = new HashMap<>();
    
    // Mapa para controlar cooldown de hits por jogador
    private static final Map<UUID, Long> playerHitCooldown = new HashMap<>();
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent evt) {
        Player player = evt.getPlayer();
        Block block = evt.getBlock();
        
        // Verificar se é um chest ou enderchest
        if (isChestOrEnderChest(block.getType())) {
            Profile profile = Profile.getProfile(player.getName());
            if (profile != null) {
                BedWars game = profile.getGame(BedWars.class);
                if (game != null && game.getState() == GameState.EMJOGO && !game.isSpectator(player)) {
                    // Guardar o item usado para hitar
                    ItemStack hitItem = player.getItemInHand();
                    if (hitItem != null && hitItem.getType() != Material.AIR) {
                        playerHitItems.put(player.getUniqueId(), hitItem.clone());
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        Block block = evt.getClickedBlock();
        
        if (block != null && isChestOrEnderChest(block.getType())) {
            Profile profile = Profile.getProfile(player.getName());
            if (profile != null) {
                BedWars game = profile.getGame(BedWars.class);
                if (game != null && game.getState() == GameState.EMJOGO && !game.isSpectator(player)) {
                    // Criar holograma se não existir
                    createChestHologram(block.getLocation());
                    
                    // Se for um hit (LEFT_CLICK), guardar o item usado e adicionar ao baú
                    if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
                        // Verificar cooldown para evitar spam
                        long currentTime = System.currentTimeMillis();
                        Long lastHit = playerHitCooldown.get(player.getUniqueId());
                        if (lastHit != null && currentTime - lastHit < 1000) { // 1 segundo de cooldown
                            return;
                        }
                        playerHitCooldown.put(player.getUniqueId(), currentTime);
                        
                        ItemStack hitItem = player.getItemInHand();
                        if (hitItem != null && hitItem.getType() != Material.AIR) {
                            playerHitItems.put(player.getUniqueId(), hitItem.clone());

                            // Adicionar o item ao baú automaticamente
                            addItemToChest(block.getLocation(), hitItem.clone(), player);
                            player.sendMessage("§aAdicionado " + hitItem.getAmount() + "x " + getItemDisplayName(hitItem) + " ao baú");
                            
                            // Remover o item da mão do jogador (simular que guardou)
                            player.setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            // Se não tiver item na mão, registrar como hit com a mão
                            playerHitItems.put(player.getUniqueId(), new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent evt) {
        // Verificar se há chests/enderchests no chunk carregado
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = evt.getChunk().getBlock(x, y, z);
                    if (isChestOrEnderChestStatic(block.getType())) {
                        // Verificar se o jogo está em andamento
                        BedWars game = BedWars.getByWorldName(block.getWorld().getName());
                        if (game != null && game.getState() == GameState.EMJOGO) {
                            createChestHologramStatic(block.getLocation());
                        }
                    }
                }
            }
        }
    }
    
    private boolean isChestOrEnderChest(Material material) {
        return material == Material.CHEST || material == Material.ENDER_CHEST;
    }
    
    private void createChestHologram(Location location) {
        createChestHologramStatic(location);
    }
    
    public static ItemStack getPlayerHitItem(UUID playerUUID) {
        return playerHitItems.get(playerUUID);
    }
    
    public static void clearPlayerHitItem(UUID playerUUID) {
        playerHitItems.remove(playerUUID);
        playerHitCooldown.remove(playerUUID);
    }
    
    private void addItemToChest(Location location, ItemStack item, Player player) {
        String locationKey = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        
        // Verificar se é um chest
        if (location.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) location.getBlock().getState();
            chest.getInventory().addItem(item);
        }
        // Verificar se é um enderchest
        else if (location.getBlock().getType() == Material.ENDER_CHEST) {
            player.getEnderChest().addItem(item);
        }
    }
    
    private String getItemDisplayName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        } else {
            // Converter o nome do material para um formato mais legível
            String materialName = item.getType().name().toLowerCase().replace("_", " ");
            String[] words = materialName.split(" ");
            StringBuilder result = new StringBuilder();
            
            for (String word : words) {
                if (word.length() > 0) {
                    result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
                }
            }
            
            return result.toString().trim();
        }
    }
    
    public static void clearAllHolograms() {
        for (Hologram hologram : chestHolograms.values()) {
            HologramLibrary.removeHologram(hologram);
        }
        chestHolograms.clear();
        chestBlocks.clear();
        playerHitCooldown.clear();
    }
    
    public static void loadChestHolograms(BedWars game) {
        // Procurar por todos os chests/enderchests no mundo do jogo
        // Usar uma área menor para evitar sobrecarga
        Location center = game.getCubeId().getCenterLocation();
        int radius = 50; // Raio de busca
        
        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
                    Location loc = new Location(game.getWorld(), x, y, z);
                    Block block = loc.getBlock();
                    if (isChestOrEnderChestStatic(block.getType())) {
                        createChestHologramStatic(loc);
                    }
                }
            }
        }
    }
    
    private static boolean isChestOrEnderChestStatic(Material material) {
        return material == Material.CHEST || material == Material.ENDER_CHEST;
    }
    
    private static void createChestHologramStatic(Location location) {
        String locationKey = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        
        // Verificar se já existe um holograma para esta localização
        if (chestHolograms.containsKey(locationKey)) {
            return;
        }
        
        // Criar holograma bem perto do chest (muito baixo)
        Location hologramLocation = location.clone().add(0.5, 0.1, 0.5);
        Hologram hologram = HologramLibrary.createHologram(hologramLocation);
        hologram.withLine("§e§lHIT PARA GUARDAR ITEM");
        
        // Armazenar o holograma
        chestHolograms.put(locationKey, hologram);
        
        // Armazenar referência do baú se for um chest
        if (location.getBlock().getState() instanceof Chest) {
            chestBlocks.put(locationKey, (Chest) location.getBlock().getState());
        }
    }
} 