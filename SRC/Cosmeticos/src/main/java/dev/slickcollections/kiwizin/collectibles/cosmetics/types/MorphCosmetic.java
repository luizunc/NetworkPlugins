package dev.slickcollections.kiwizin.collectibles.cosmetics.types;

import dev.slickcollections.kiwizin.collectibles.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.morphs.MorphType;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.pets.PetType;
import dev.slickcollections.kiwizin.collectibles.hook.player.CUser;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MorphCosmetic extends Cosmetic {
  
  public String texture;
  private final String icon;
  private final MorphType type;
  
  public MorphCosmetic(String name, EnumRarity rarity, MorphType type, String icon, String texture) {
    super(name, rarity, CosmeticType.MORPH);
    this.type = type;
    this.icon = icon + " : skin>" + texture;
    
    this.texture = texture;
    Cosmetic.addCosmetic(this);
  }
  
  public static void setupMorphs() {
    new MorphCosmetic("Iron Golem", EnumRarity.DIVINO, MorphType.IRON_GOLEM,
        "SKULL_ITEM:3 : 1 : nome>Iron Golem : desc>&7Passeie pelos nossos lobbies\n&7sendo um Iron Golem!", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19");
    new MorphCosmetic("Villager", EnumRarity.RARO, MorphType.VILLAGER,
        "SKULL_ITEM:3 : 1 : nome>Villager : desc>&7Passeie pelos nossos lobbies\n&7sendo um Villager!", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
    new MorphCosmetic("Enderman", EnumRarity.DIVINO, MorphType.ENDERMAN,
        "SKULL_ITEM:3 : 1 : nome>Enderman : desc>&7Passeie pelos nossos lobbies\n&7sendo um Enderman!", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=");
    new MorphCosmetic("Galinha", EnumRarity.COMUM, MorphType.CHICKEN,
        "SKULL_ITEM:3 : 1 : nome>Galinha : desc>&7Divirta-se pelos nossos lobbies\n&7sendo uma Galinha!", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ==");
  }
  
  @Override
  public void onSelect(CUser user) {
    user.handleMorph();
  }
  
  public MorphType getEntityType() {
    return this.type;
  }
  
  public ItemStack getIcon(String color, String... loreList) {
    return this.getIcon(color, Arrays.asList(loreList));
  }
  
  public ItemStack getIcon(String color, List<String> loreList) {
    ItemStack icon = BukkitUtils.deserializeItemStack(this.icon);
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
