package dev.slickcollections.kiwizin.collectibles.cosmetics.types.clothes;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.ClothesCosmetic;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Arrays;

public class ChainmailClothes extends ClothesCosmetic {
  
  public ChainmailClothes() {
    super("Roupa de Malha", EnumRarity.COMUM, "CHAINMAIL_HELMET : 1 : nome>Roupa de Malha : desc>&7Caminhe com estilo em nossos\n&7lobbies com a roupa de Malha.");
    this.items = Arrays.asList(
        BukkitUtils.deserializeItemStack("CHAINMAIL_HELMET : 1 : nome>&aCapacete de Malha"),
        BukkitUtils.deserializeItemStack("CHAINMAIL_CHESTPLATE : 1 : nome>&aPeitoral de Malha"),
        BukkitUtils.deserializeItemStack("CHAINMAIL_LEGGINGS : 1 : nome>&aCalças de Malha"),
        BukkitUtils.deserializeItemStack("CHAINMAIL_BOOTS : 1 : nome>&aBotas de Malha"));
  }
}
