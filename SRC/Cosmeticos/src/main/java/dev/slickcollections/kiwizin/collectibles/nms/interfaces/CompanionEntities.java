package dev.slickcollections.kiwizin.collectibles.nms.interfaces;

import dev.slickcollections.kiwizin.collectibles.Main;
import dev.slickcollections.kiwizin.collectibles.cosmetics.object.Companion;
import dev.slickcollections.kiwizin.collectibles.cosmetics.types.CompanionCosmetic;
import dev.slickcollections.kiwizin.collectibles.nms.interfaces.companions.CompanionEntity;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CompanionEntities {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("COMPANION_ENTITIES");
  private static final Map<Class<? extends CompanionCosmetic>, Class<? extends CompanionEntity>> TYPES = new HashMap<>();
  
  public static void setCompanionType(Class<? extends CompanionCosmetic> type, Class<? extends CompanionEntity> entity) {
    TYPES.put(type, entity);
  }
  
  public static CompanionEntity createForType(Class<? extends CompanionCosmetic> clazz, Companion companion) {
    Class<? extends CompanionEntity> entityClass = TYPES.get(clazz);
    if (entityClass == null) {
      throw new IllegalArgumentException("Tipo de companion desconhecido: " + clazz.getSimpleName());
    }
    
    try {
      return (CompanionEntity) entityClass.getConstructors()[0].newInstance(companion);
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "createForType(" + clazz.getSimpleName() + "): ", ex);
    }
    
    return null;
  }
  
  public static boolean existsForType(Class<? extends CompanionEntity> type) {
    return TYPES.containsKey(type);
  }
}
