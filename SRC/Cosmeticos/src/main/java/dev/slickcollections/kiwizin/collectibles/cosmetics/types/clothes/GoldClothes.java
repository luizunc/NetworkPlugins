package dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.ClothesCosmetic;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Arrays;

public class GoldClothes extends ClothesCosmetic {
  
  public GoldClothes() {
    super("Roupa de Ouro", EnumRarity.COMUM, "GOLD_HELMET : 1 : nome>Roupa de Ouro : desc>&7Caminhe com estilo em nossos\n&7lobbies com a roupa de Ouro.");
    this.items = Arrays.asList(
        BukkitUtils.deserializeItemStack("GOLD_HELMET : 1 : nome>&aCapacete de Ouro"),
        BukkitUtils.deserializeItemStack("GOLD_CHESTPLATE : 1 : nome>&aPeitoral de Ouro"),
        BukkitUtils.deserializeItemStack("GOLD_LEGGINGS : 1 : nome>&aCalças de Ouro"),
        BukkitUtils.deserializeItemStack("GOLD_BOOTS : 1 : nome>&aBotas de Ouro"));
  }
}
