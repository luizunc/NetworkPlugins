package dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.ClothesCosmetic;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Arrays;

public class IronClothes extends ClothesCosmetic {
  
  public IronClothes() {
    super("Roupa de Ferro", EnumRarity.COMUM, "IRON_HELMET : 1 : nome>Roupa de Ferro : desc>&7Caminhe com estilo em nossos\n&7lobbies com a roupa de Ferro.");
    this.items = Arrays.asList(
        BukkitUtils.deserializeItemStack("IRON_HELMET : 1 : nome>&aCapacete de Ferro"),
        BukkitUtils.deserializeItemStack("IRON_CHESTPLATE : 1 : nome>&aPeitoral de Ferro"),
        BukkitUtils.deserializeItemStack("IRON_LEGGINGS : 1 : nome>&aCalças de Ferro"),
        BukkitUtils.deserializeItemStack("IRON_BOOTS : 1 : nome>&aBotas de Ferro"));
  }
}
