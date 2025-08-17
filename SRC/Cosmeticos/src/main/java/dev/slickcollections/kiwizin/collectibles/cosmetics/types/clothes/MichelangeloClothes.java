package dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.ClothesCosmetic;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Arrays;

public class MichelangeloClothes extends ClothesCosmetic {
  
  public MichelangeloClothes() {
    super("Roupa do Michelangelo", EnumRarity.EPICO, "SKULL_ITEM:3 : 1 : nome>Roupa do Michelangelo : desc>&7Caminhe com estilo em nossos\n&7lobbies com a roupa do Michelangelo. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJlYjVlNTRiZTU2YzE0NGNkZjQzNGUyOWMxNTdiMTk4Zjk0YTkxNzZiYWU4OGRkNjVmMDgxODA1MzQyIn19fQ==");
    this.items = Arrays.asList(
        BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&aCapacete do Michelangelo : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJlYjVlNTRiZTU2YzE0NGNkZjQzNGUyOWMxNTdiMTk4Zjk0YTkxNzZiYWU4OGRkNjVmMDgxODA1MzQyIn19fQ=="),
        BukkitUtils.deserializeItemStack("LEATHER_CHESTPLATE : 1 : nome>&aPeitoral do Michelangelo : pintar>YELLOW"),
        BukkitUtils.deserializeItemStack("LEATHER_LEGGINGS : 1 : nome>&aCalças do Michelangelo : pintar>GREEN"),
        BukkitUtils.deserializeItemStack("LEATHER_BOOTS : 1 : nome>&aBotas do Michelangelo : pintar>GREEN"));
  }
}
