package dev.slickcollections.kiwizin.collectibles.cosmetics.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes.*;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ClothesCosmetic extends Cosmetic {
  
  protected List<ItemStack> items;
  private final ItemStack icon;
  
  public ClothesCosmetic(String name, EnumRarity rarity, String icon) {
    super(name, rarity, CosmeticType.CLOTHES);
    this.icon = BukkitUtils.deserializeItemStack(icon);
    Cosmetic.addCosmetic(this);
  }
  
  public static void setupClothes() {
    new LeatherClothes();
    new ChainmailClothes();
    new IronClothes();
    new GoldClothes();
    new DiamondClothes();
    new LuigiClothes();
    new MarioClothes();
    new SpidermanClothes();
    new VenomClothes();
    new MonkeyClothes();
    new MichelangeloClothes();
    new RaphaelClothes();
    new LeonardoClothes();
    new DonatelloClothes();
    new PigClothes();
    new EndermanClothes();
    new EnderdragonClothes();
    new HerobrineClothes();
  }
  
  @Override
  public void onSelect(CUser user) {
    user.handleClothes();
  }
  
  public ItemStack getPiece(int index) {
    return this.items.get(index);
  }
  
  public ItemStack getIcon(String color, String... loreList) {
    return this.getIcon(color, Arrays.asList(loreList));
  }
  
  public ItemStack getIcon(String color, List<String> loreList) {
    ItemStack icon = this.icon.clone();
    ItemMeta meta = icon.getItemMeta();
    meta.setDisplayName(color + meta.getDisplayName());
    List<String> currentLore = meta.getLore();
    currentLore.add("");
    currentLore.add("§fRaridade: " + this.getRarity().getName());
    currentLore.addAll(loreList);
    meta.setLore(currentLore);
    icon.setItemMeta(meta);
    return icon;
  }
}
