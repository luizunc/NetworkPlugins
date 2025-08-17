package dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets;

import dev.slickcollections.kiwizin.collectibles.utils.enums.MHorseColor;
import dev.slickcollections.kiwizin.collectibles.utils.enums.MHorseType;

public interface PetHorse {
  
  MHorseColor getColor();
  
  void setColor(MHorseColor color);
  
  MHorseType getType();
  
  void setType(MHorseType type);
}
