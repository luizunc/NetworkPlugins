package dev.slickcollections.kiwizin.mysterybox.cosmetics.types;

import dev.slickcollections.kiwizin.cash.CashManager;
import dev.slickcollections.kiwizin.mysterybox.Language;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.box.animation.DefaultOpener;
import dev.slickcollections.kiwizin.mysterybox.box.animation.LootChestsOpener;
import dev.slickcollections.kiwizin.mysterybox.box.animation.VillagerOpener;
import dev.slickcollections.kiwizin.mysterybox.box.interfaces.OpeningCallback;
import dev.slickcollections.kiwizin.mysterybox.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.mysterybox.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.mysterybox.hook.container.SelectedContainer;
import dev.slickcollections.kiwizin.mysterybox.lobby.BoxNPC;
import dev.slickcollections.kiwizin.nms.interfaces.entity.IArmorStand;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class Opener extends Cosmetic {
  
  private final String name;
  private final String icon;
  
  public Opener(long id, CosmeticType type, String permission, String name, String icon, long cash, EnumRarity rarity) {
    super(id, type, permission);
    this.name = name;
    this.icon = icon;
    this.cash = cash;
    if (id == 0) {
      this.rarity = EnumRarity.COMUM;
    } else {
      this.rarity = rarity;
    }
  }
  
  public static void setupOpeners() {
    new VillagerOpener();
    new DefaultOpener();
    new LootChestsOpener();
  }
  
  public abstract void execute(BoxNPC npc, Block block, IArmorStand capsule, Profile profile, BoxContent box, Location location, OpeningCallback callback);
  
  public abstract void executeMultiples(IArmorStand capsule, Block block, Profile profile, Location location, OpeningCallback callback);
  
  @Override
  public ItemStack getIcon(Profile profile) {
    long cash = profile.getStats("kCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("kMysteryBox", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ? (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked)
        : ((CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$opener$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$opener$icon$buy_desc$start
                .replace("{buy_desc_status}", ((CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$opener$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : nome>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
  
  @Override
  public String getName() {
    return this.name;
  }
}
