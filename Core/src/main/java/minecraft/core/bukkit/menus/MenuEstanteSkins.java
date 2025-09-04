package minecraft.core.bukkit.menus;

import minecraft.core.bukkit.Core;
import minecraft.core.bukkit.config.Skins;
import minecraft.core.bukkit.listeners.UpdateSkin;
import minecraft.core.core.database.data.container.SkinsContainer;
import minecraft.core.core.libraries.menu.PagedPlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.skin.SkinCooldown;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Menu da Estante de Skins.
 * Exibe a biblioteca de skins disponíveis em 4 linhas com paginação.
 * 
 * @author Luiz
 * @version 1.0
 */
public class MenuEstanteSkins extends PagedPlayerMenu {
    
    private Map<ItemStack, String> skins = new HashMap<>();
    
    /**
     * Construtor do menu da estante de skins.
     * 
     * @param player Jogador que abrirá o menu
     * @param profile Perfil do jogador
     */
    public MenuEstanteSkins(Player player, Profile profile) {
        super(player, "Estante de Skins", Skins.getLibrary().size() / 7 + 4);
        
        // Configuração da paginação
        this.previousPage = this.rows * 9 - 9;
        this.nextPage = this.rows * 9 - 1;
        
        // Define os slots disponíveis para as skins (4 linhas centrais)
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
        
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
        SkinsContainer skinsContainer = profile.getSkinsContainer();
        String selectedSkin = skinsContainer == null ? "none" : skinsContainer.getSkin();
        
        ArrayList<ItemStack> items = new ArrayList<>();
        
        // Cria os itens das skins
        Skins.getLibrary().forEach((key, value) -> {
            String[] parts = value.split(":");
            if (parts.length >= 3) {
                String skinName = parts[0];  // Nome da skin
                String skinValue = parts[1]; // Value da skin
                String skinSignature = parts[2]; // Signature da skin
                
                // Verifica se é a skin atual
                boolean isCurrentSkin = selectedSkin != null && selectedSkin.equals(skinName);
                String statusText = isCurrentSkin ? "§aSkin Selecionada" : "§eClique para selecionar.";
                
                ItemStack icon = BukkitUtils.deserializeItemStack(
                    "SKULL_ITEM:3 : 1 : skin>" + skinValue + " : nome>&a" + key + " : desc>&7Altere sua skin para " + key + ".\n \n" + statusText
                );
                
                items.add(icon);
                this.skins.put(icon, key);
            }
        });
        
        // Adiciona botão de voltar
        this.removeSlotsWith(
            BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&a← Voltar : desc>&7Retorna ao menu de skins"), 
            this.rows * 9 - 5
        );
        
        // Define os itens no menu
        this.setItems(items);
        items.clear();
    }
    
    /**
     * Manipula o clique no inventário.
     * 
     * @param evt Evento de clique
     */
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
                
                ItemStack item;
                if (evt.getClickedInventory() != null && 
                    evt.getClickedInventory().equals(this.getCurrentInventory()) && 
                    (item = evt.getCurrentItem()) != null && 
                    item.getType() != Material.AIR) {
                    
                    String skin = this.skins.get(item);
                    
                    if (evt.getSlot() == this.previousPage) {
                        // Página anterior
                        this.player.playSound(this.player.getLocation(), Sound.CLICK, 0.5f, 2.0f);
                        this.openPrevious();
                    } else if (evt.getSlot() == this.nextPage) {
                        // Próxima página
                        this.player.playSound(this.player.getLocation(), Sound.CLICK, 0.5f, 2.0f);
                        this.openNext();
                    } else if (evt.getSlot() == this.rows * 9 - 5) {
                        // Botão voltar
                        this.player.playSound(this.player.getLocation(), Sound.CLICK, 0.5f, 2.0f);
                        HandlerList.unregisterAll(this);
                        new MenuSkins(this.player, profile);
                    } else if (skin != null) {
                        // Aplicar skin
                        applySkin(profile, skin);
                    }
                }
            }
        }
    }
    
    /**
     * Aplica uma skin ao jogador usando os dados da biblioteca.
     * 
     * @param profile Perfil do jogador
     * @param skinName Nome da skin
     */
    private void applySkin(Profile profile, String skinName) {
        // Verifica cooldown
        if (SkinCooldown.hasCooldown(this.player.getName())) {
            this.player.sendMessage("§cVocê precisa aguardar " + SkinCooldown.getRemainingTime(this.player.getName()) + " para trocar de skin novamente!");
            this.player.playSound(this.player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return;
        }
        
        SkinsContainer container = profile.getSkinsContainer();
        String skinData = Skins.getSkinData(skinName);
        
        if (skinData == null) {
            this.player.sendMessage("§cSkin não encontrada!");
            return;
        }
        
        String[] data = skinData.split(":");
        if (data.length < 3) {
            this.player.sendMessage("§cDados da skin inválidos!");
            return;
        }
        
        // Novo formato: "NomeSkin:Value:Signature"
        String skinNameToApply = data[0];  // Nome da skin
        String value = data[1];            // Value da skin
        String signature = data[2];        // Signature da skin
        
        // Verifica se já é a skin atual
        if (container.getSkin() != null && container.getSkin().equals(skinNameToApply)) {
            this.player.sendMessage("§eVocê já está utilizando essa skin!");
            this.player.playSound(this.player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return;
        }
        
        try {
            // Verifica se os dados são válidos
            if (value == null || signature == null || value.isEmpty() || signature.isEmpty()) {
                this.player.sendMessage("§cDados da skin não encontrados!");
                this.player.playSound(this.player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
                return;
            }
            
            // Aplica a skin usando método personalizado que não atualiza o container
            boolean success = updateSkinFromLibrary(this.player, value, signature);
            
            if (success) {
                // Atualiza o container com os dados da skin da biblioteca
                container.setSkin(skinNameToApply, value, signature);
                
                // Salva o perfil
                profile.save();
                
                // Adiciona cooldown
                SkinCooldown.addCooldown(this.player.getName());
                
                this.player.sendMessage("§aSkin aplicada com sucesso!");
                this.player.playSound(this.player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                this.player.closeInventory();
            } else {
                this.player.sendMessage("§cErro ao aplicar a skin!");
                this.player.playSound(this.player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            }
            
        } catch (Exception e) {
            this.player.sendMessage("§cOcorreu um erro ao tentar aplicar a skin!");
            this.player.playSound(this.player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            e.printStackTrace();
        }
    }
    
    /**
     * Aplica a skin sem atualizar o container (versão personalizada do UpdateSkin).
     * 
     * @param player Jogador
     * @param value Valor da skin
     * @param signature Assinatura da skin
     * @return true se aplicada com sucesso
     */
    private boolean updateSkinFromLibrary(Player player, String value, String signature) {
        if (value == null || signature == null) {
            return false;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile profile = entityPlayer.getProfile();

        Property currentSkin = profile.getProperties().get("textures").stream().findFirst().orElse(null);

        if (currentSkin != null && currentSkin.getValue().equals(value) && currentSkin.getSignature().equals(signature)) {
            player.sendMessage("§eVocê já está utilizando essa skin.");
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
            UpdateSkin.updatePlayerHeadItems(player);
            
        }, 2L);
        
        return true;
    }
    
    /**
     * Cancela o menu e limpa os recursos.
     */
    public void cancel() {
        HandlerList.unregisterAll(this);
        this.skins.clear();
        this.skins = null;
    }
    
    /**
     * Manipula o jogador saindo do servidor.
     * 
     * @param evt Evento de saída
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }
    
    /**
     * Manipula o fechamento do inventário.
     * 
     * @param evt Evento de fechamento
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
            this.cancel();
        }
    }
}