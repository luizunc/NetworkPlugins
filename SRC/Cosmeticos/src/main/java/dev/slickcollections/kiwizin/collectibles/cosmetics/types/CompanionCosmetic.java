package dev.slickcollections.kiwizin.collectibles.cosmetics.types;

import com.mojang.authlib.properties.Property;
import dev.slickcollections.kiwizin.collectibles.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Companion;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.CompanionAnimation;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.CompanionFrames;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.name.CompanionNames;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class CompanionCosmetic extends Cosmetic {
  
  protected CompanionFrames frames;
  private final ItemStack icon;
  
  public CompanionCosmetic(String name, int quality, EnumRarity rarity, String icon) {
    super(name, rarity, CosmeticType.COMPANION);
    this.icon = BukkitUtils.deserializeItemStack(icon);
    this.frames = new CompanionFrames(quality);
    Cosmetic.addCosmetic(this);
  }
  
  public static void setupCompanions() {
    CompanionNames.setupCompanions();
  }
  
  @Override
  public void onSelect(CUser user) {
    user.selectCosmetic(Cosmetic.NONE_PET);
    user.handlePet();
    user.handleCompanion();
  }
  
  public Map<Integer, List<CompanionAnimation>> getFrames() {
    return this.frames.getFrames();
  }
  
  public Map<Integer, List<CompanionAnimation>> getIdleFrames() {
    return this.frames.getIdleFrames();
  }
  
  public Map<Integer, List<CompanionAnimation>> getKeyFrames() {
    return this.frames.getKeyFrames();
  }
  
  public Map<Integer, List<CompanionAnimation>> getIdleKeyFrames() {
    return this.frames.getIdleKeyFrames();
  }
  
  public boolean hasIdleAnimation() {
    return !this.frames.getIdleFrames().isEmpty();
  }
  
  public Companion build(CUser user) {
    return new Companion(user, this);
  }
  
  public abstract CompanionNames getNameEnum();
  
  public String getSkinValue() {
    return SKULL_META_PROFILE.get(icon.getItemMeta()).getProperties().get("textures").stream().findFirst().orElse(new Property("textures", "")).getValue();
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
