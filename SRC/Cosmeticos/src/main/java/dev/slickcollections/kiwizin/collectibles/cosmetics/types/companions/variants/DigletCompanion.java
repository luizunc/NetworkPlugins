package dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.variants;

import dev.slickcollections.kiwizin.collectibles.cosmetics.types.CompanionCosmetic;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.CompanionAnimation;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.companions.name.CompanionNames;
import dev.slickcollections.kiwizin.collectibles.utils.MathUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;

import java.util.Collections;

public class DigletCompanion extends CompanionCosmetic {
  
  public DigletCompanion() {
    super("Diglet", 5, EnumRarity.DIVINO,
        "SKULL_ITEM:3 : 1 : nome>Diglet : desc>&7Divirta-se pelos nossos lobbies\n&7acompanhado de um Diglet! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRkZTlmNzI3OTdkMTY5NTk4YzEwZGJmMzE5MmJhN2Q5OWZlZjg3YmRjZmM5OWViZTk3NjYyNDI0NWU5OTgifX19");
    
    this.frames.createKeyFrame(0, Collections.singletonList(new CompanionAnimation(MathUtils.EulerAngle(0, 0, 0), "buik1", CompanionAnimation.MovementType.HEAD)));
  }
  
  @Override
  public CompanionNames getNameEnum() {
    return CompanionNames.DIGLET;
  }
}
