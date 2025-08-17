package dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.ClothesCosmetic;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Arrays;

public class MonkeyClothes extends ClothesCosmetic {
  
  public MonkeyClothes() {
    super("Roupa de Macaco", EnumRarity.RARO, "SKULL_ITEM:3 : 1 : nome>Roupa de Macaco : desc>&7Caminhe com estilo em nossos\n&7lobbies com a roupa de Macaco. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlOTkzMzdkZGMyYzhkMTRjZjlmNTI3ZTk3MjI4NTEzMzU1OGM5NTM3NjVkZTRkZDVkN2E5MTlhOTg4ODIifX19");
    this.items = Arrays.asList(
        BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&aCapacete de Macaco : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlOTkzMzdkZGMyYzhkMTRjZjlmNTI3ZTk3MjI4NTEzMzU1OGM5NTM3NjVkZTRkZDVkN2E5MTlhOTg4ODIifX19"),
        BukkitUtils.deserializeItemStack("LEATHER_CHESTPLATE : 1 : nome>&aPeitoral de Macaco : pintar>150:75:0"),
        BukkitUtils.deserializeItemStack("LEATHER_LEGGINGS : 1 : nome>&aCalças de Macaco : pintar>150:75:0"),
        BukkitUtils.deserializeItemStack("LEATHER_BOOTS : 1 : nome>&aBotas de Macaco : pintar>150:75:0"));
  }
}
