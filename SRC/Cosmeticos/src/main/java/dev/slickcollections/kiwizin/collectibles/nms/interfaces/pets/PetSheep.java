package dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets;

import dev.slickcollections.kiwizin.collectibles.utils.enums.MWoolColor;

public interface PetSheep {
  
  void setRainbow(boolean rainbow);
  
  MWoolColor getColor();
  
  void setColor(MWoolColor color);
}
