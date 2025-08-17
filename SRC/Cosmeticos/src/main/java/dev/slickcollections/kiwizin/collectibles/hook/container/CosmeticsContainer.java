package dev.slickcollections.kiwizin.collectibles.hook.container;

import dev.slickcollections.kiwizin.collectibles.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class CosmeticsContainer extends AbstractContainer {
  
  public CosmeticsContainer(DataContainer dataContainer) {
    super(dataContainer);
    
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    for (CosmeticType type : CosmeticType.values()) {
      if (!cosmetics.containsKey(type.name().toLowerCase())) {
        cosmetics.put(type.name().toLowerCase(), new JSONArray());
      }
    }
    
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public void add(CosmeticType type, long uniqueId) {
    if (!has(type, uniqueId)) {
      JSONObject cosmetics = this.dataContainer.getAsJsonObject();
      ((JSONArray) cosmetics.get(type.name().toLowerCase())).add(uniqueId);
      this.dataContainer.set(cosmetics.toString());
      cosmetics.clear();
    }
  }
  
  public boolean has(CosmeticType type, long uniqueId) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    boolean has = ((JSONArray) cosmetics.get(type.name().toLowerCase())).contains(uniqueId);
    cosmetics.clear();
    return has;
  }
}
