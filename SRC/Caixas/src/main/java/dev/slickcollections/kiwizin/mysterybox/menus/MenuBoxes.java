package dev.slickcollections.kiwizin.mysterybox.menus;


import dev.slickcollections.kiwizin.libraries.menu.UpdatablePlayerPagedMenu;
import dev.slickcollections.kiwizin.mysterybox.Language;
import dev.slickcollections.kiwizin.mysterybox.Main;
import dev.slickcollections.kiwizin.mysterybox.api.MysteryBoxAPI;
import dev.slickcollections.kiwizin.mysterybox.box.Box;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.box.loot.LootMenu;
import dev.slickcollections.kiwizin.mysterybox.cosmetics.types.Opener;
import dev.slickcollections.kiwizin.mysterybox.hook.container.MysteryBoxRewardedContainer;
import dev.slickcollections.kiwizin.mysterybox.lobby.BoxNPC;
import dev.slickcollections.kiwizin.mysterybox.menus.cosmetics.MenuCosmetics;
import dev.slickcollections.kiwizin.mysterybox.menus.fabricate.MenuFabricateBox;
import dev.slickcollections.kiwizin.mysterybox.menus.open.MenuConfirmOpen;
import dev.slickcollections.kiwizin.mysterybox.menus.open.MenuConfirmOpenMultiple;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

@SuppressWarnings("unchecked")
public class MenuBoxes extends UpdatablePlayerPagedMenu {
  
  private final BoxNPC npc;
  private Map<ItemStack, BoxContent> boxes;
  
  public MenuBoxes(Profile profile, BoxNPC npc) {
    super(profile.getPlayer(), "Cápsulas", (int) ((MysteryBoxAPI.getMysteryBoxes(profile) / 7) + 4));
    this.npc = npc;
    this.boxes = new HashMap<>();
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15,
        16, 19, 20, 21, 22, 23, 24,
        25, 28, 29, 30, 31, 32, 33, 34);
    
    this.update();
    this.register(Main.getInstance(), 20);
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(getCurrentInventory())) {
      evt.setCancelled(true);
      
      
      if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
        ItemStack item = evt.getCurrentItem();
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
          player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(evt.getInventory()) && item != null && item.getType() != Material.AIR) {
          BoxContent box = boxes.get(item);
          if (evt.getSlot() == this.previousPage) {
            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            this.openPrevious();
          } else if (evt.getSlot() == this.nextPage) {
            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            this.openNext();
          } else if (evt.getSlot() == (this.rows * 9) - 7) {
            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            new MenuCosmetics<>(profile, "Animações de Abertura", Opener.class, npc);
          } else if (evt.getSlot() == (this.rows * 9) - 6) {
            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            new MenuFabricateBox(profile, npc);
          } else if (evt.getSlot() == (this.rows * 9) - 3) {
            new LootMenu(player, Box.getFirst());
          } else if (evt.getSlot() == (this.rows * 9) - 4) {
            if (!player.hasPermission("kmysterybox.open.multiplesboxes") || MysteryBoxAPI.getMysteryBoxes(profile) < 2) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 1.0F, 1.0F);
              player.sendMessage("§c" + (!player.hasPermission("kmysterybox.open.multiplesboxes") ? "Você não possui permissão para isto." : "Você não possui caixas suficientes."));
              return;
            }
            new MenuConfirmOpenMultiple(player, box, npc);
          } else if (box != null) {
            new MenuConfirmOpen(player, box, npc);
          }
        }
      }
    }
    
  }
  
  @Override
  public void update() {
    Profile profile = Profile.getProfile(this.player.getName());
    
    List<ItemStack> items = new ArrayList<>();
    List<BoxContent> fakeBoxes = new ArrayList<>();
    
    for (int i = 0; i < MysteryBoxAPI.getMysteryBoxes(profile); i++) {
      if (BoxContent.getRandom() == null) {
        continue;
      }
      fakeBoxes.add(BoxContent.getRandom());
    }
    
    for (BoxContent box : fakeBoxes) {
      ItemStack icon = box.getIcon(false);
      items.add(icon);
      boxes.put(icon, box);
    }
    
    if (items.size() == 0) {
      this.removeSlotsWith(BukkitUtils.deserializeItemStack(Language.menus$boxes$no_have_boxes), (this.rows * 4) - 3);
    }
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("" +
        "REDSTONE_COMPARATOR : 1 : nome>&aCustomizar Animação : desc>&7Customize a sua animação\n&7de abertura de cápsula!\n \n&eClique para comprar ou selecionar!"), (this.rows * 9) - 7);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack
        (Language.menus$boxes$fabricate_box), (this.rows * 9) - 6);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack(Language.menus$boxes$info.replace("{boxes}",
        StringUtils.formatNumber(MysteryBoxAPI.getMysteryBoxes(profile))).
        replace("{last_rewards}", Profile.getProfile(player.getName()).getAbstractContainer("kMysteryBox", "lastRewards", MysteryBoxRewardedContainer.class).getLastItems()).replace("{frags}",
        profile.getFormatedStats("kMysteryBox", "mystery_frags"))), (this.rows * 9) - 5);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack(Language.menus$boxes$open_multiples_boxes
        .replace("{open_desc}", (!player.hasPermission("kmysterybox.open.multiplesboxes") ? "§cVocê não possui permissão para isto." : MysteryBoxAPI.getMysteryBoxes(profile) < 2 ? "§cVocê não possui caixas suficientes." : "§eClique para abrir"))), (this.rows * 9) - 4);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack(
        "SIGN : 1 : nome>&aMenu : desc>&7Um menu privado para abrir\n&7e comprar cápsulas mágicas.\n \n&eClique para abrir!"), (this.rows * 9) - 3);
    
    
    this.setItems(items);
  }
  
  public void cancel() {
    super.cancel();
    HandlerList.unregisterAll(this);
    this.boxes.clear();
    this.boxes = null;
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getCurrentInventory())) {
      this.cancel();
    }
  }
}