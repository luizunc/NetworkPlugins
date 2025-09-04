package minecraft.core.bukkit.listeners;

import minecraft.core.bukkit.Core;
import minecraft.core.core.database.data.container.SkinsContainer;
import minecraft.core.core.player.Profile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.Map;

public class UpdateSkin {

    private static final Map<String, Long> lastUpdateTimestamps = new HashMap<>();
    private static final long COOLDOWN_TIME = 30 * 1000;

    public static boolean updateSkin(Player player, String value, String signature) {
        return updateSkin(player, null, value, signature);
    }
    
    public static boolean updateSkin(Player player, String skinName, String value, String signature) {
        if (value == null || signature == null) {
            return false;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile profile = entityPlayer.getProfile();
        Profile account = Profile.getProfile(player.getName());
        SkinsContainer container = account.getSkinsContainer();

        Property currentSkin = profile.getProperties().get("textures").stream().findFirst().orElse(null);

        if (currentSkin != null && currentSkin.getValue().equals(value) && currentSkin.getSignature().equals(signature)) {
            player.sendMessage("§cVocê já está utilizando essa skin.");
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return false;
        }

        long currentTime = System.currentTimeMillis();
        Long lastUpdate = lastUpdateTimestamps.get(player.getName());

        if (lastUpdate != null && currentTime - lastUpdate < COOLDOWN_TIME) {
            long timeRemaining = (COOLDOWN_TIME - (currentTime - lastUpdate)) / 1000;
            player.sendMessage("§cAguarde " + timeRemaining + "s para alterar novamente.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return false;
        }

        Location location = player.getLocation();
        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        int heldItemSlot = player.getInventory().getHeldItemSlot();
        GameMode gameMode = player.getGameMode();
        float exp = player.getExp();
        int level = player.getLevel();
        double health = player.getHealth();
        int foodLevel = player.getFoodLevel();
        
        profile.getProperties().clear();
        profile.getProperties().put("textures", new Property("textures", value, signature));

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            
            onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        });

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
            });

            entityPlayer.playerConnection.sendPacket(new PacketPlayOutRespawn(
                    entityPlayer.getWorld().worldProvider.getDimension(),
                    entityPlayer.getWorld().getDifficulty(),
                    entityPlayer.getWorld().worldData.getType(),
                    entityPlayer.playerInteractManager.getGameMode()));

            player.teleport(location);
            player.getInventory().setContents(inventory);
            player.getInventory().setArmorContents(armor);
            player.getInventory().setHeldItemSlot(heldItemSlot);
            player.setGameMode(gameMode);
            player.setExp(exp);
            player.setLevel(level);
            player.setHealth(health);
            player.setFoodLevel(foodLevel);
            player.updateInventory();

            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> !onlinePlayer.equals(player))
                    .forEach(onlinePlayer -> {
                        EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                        onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                        onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                    });

            entityPlayer.updateAbilities();
            
            // Força atualização de head items em inventários abertos
            updatePlayerHeadItems(player);
            
        }, 2L);

        // Se foi fornecido nome da skin, adiciona ao histórico
        if (skinName != null && !skinName.isEmpty()) {
            container.setSkinWithHistory(skinName, value, signature);
        } else {
            // Apenas atualiza a skin atual sem adicionar ao histórico
            container.setSkin(container.getSkin(), value, signature);
        }
        account.save();

        player.sendMessage("§aSkin aplicada com sucesso!");
        lastUpdateTimestamps.put(player.getName(), currentTime);
        
        return true;
    }
    
    /**
     * Atualiza head items do jogador em inventários abertos.
     * Este método força a atualização visual de skull items que representam o jogador.
     * 
     * @param player Jogador cuja skin foi alterada
     */
    public static void updatePlayerHeadItems(Player player) {
        // Força atualização apenas para o jogador que mudou a skin
        if (player.getOpenInventory() != null && player.getOpenInventory().getTitle() != null) {
            String title = player.getOpenInventory().getTitle();
            
            // Se for o menu principal de skins, força fechamento e reabertura
            if ("Skins".equals(title)) {
                Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                    if (player.getOpenInventory() != null && "Skins".equals(player.getOpenInventory().getTitle())) {
                        player.closeInventory();
                        
                        // Reabre o menu após garantir que a skin foi aplicada
                        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                            Profile profile = Profile.getProfile(player.getName());
                            if (profile != null) {
                                new minecraft.core.bukkit.menus.MenuSkins(player, profile);
                            }
                        }, 2L);
                    }
                }, 1L);
            }
        }
    }
    
    /**
     * Verifica se um inventário contém head items que possam representar o jogador.
     * 
     * @param inventory Inventário a ser verificado
     * @param playerName Nome do jogador
     * @return true se contém head items relacionados ao jogador
     */
    private static boolean hasPlayerHeadItems(org.bukkit.inventory.Inventory inventory, String playerName) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.SKULL_ITEM) {
                if (item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta) {
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    // Verifica se é uma cabeça do jogador ou se pode ser uma head item relacionada
                    if (skullMeta.hasOwner() && playerName.equals(skullMeta.getOwner())) {
                        return true;
                    }
                    // Também verifica head items que podem usar o perfil do jogador
                    // (como em menus de skin onde aparece "Minha Skin")
                    if (skullMeta.hasDisplayName() && 
                        (skullMeta.getDisplayName().contains("Minha Skin") || 
                         skullMeta.getDisplayName().contains("Skin Atual") ||
                         skullMeta.getDisplayName().contains("Credenciais"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
} 