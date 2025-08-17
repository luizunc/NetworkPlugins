package dev.slickcollections.kiwizin.collectibles.nms.interfaces.pets;

import dev.slickcollections.kiwizin.collectibles.utils.enums.MVillagerProfession;

public interface PetVillager {
  
  MVillagerProfession getProfession();
  
  void setProfession(MVillagerProfession profession);
}
