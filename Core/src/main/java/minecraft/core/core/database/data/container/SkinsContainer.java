package minecraft.core.core.database.data.container;

import minecraft.core.Manager;
import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.data.interfaces.AbstractContainer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SkinsContainer extends AbstractContainer {
    public SkinsContainer(DataContainer dataContainer) {
        super(dataContainer);
    }

    public void setOriginalSkin(String name) {
        if (name != null && !name.isEmpty()) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            
            // Usa a mesma lógica do SkinCommand para obter value e signature
            try {
                String id = minecraft.core.core.api.profile.Mojang.getUUID(name);
                if (id == null) {
                    return;
                }
                
                String textures = minecraft.core.core.api.profile.Mojang.getSkinProperty(id);
                if (textures == null) {
                    return;
                }
                
                String[] textureData = textures.split(" : ");
                if (textureData.length < 3) {
                    return;
                }
                
                String value = textureData[1];
                String signature = textureData[2];
                
                if (value != null && signature != null) {
                    selected.put("name", name);
                    selected.put("value", value);
                    selected.put("signature", signature);
                    selected.put("appliedAt", System.currentTimeMillis());
                    
                    // Adiciona ao histórico
                    addToHistory(name, value, signature);
                    
                    this.dataContainer.set(selected.toString());
                }
            } catch (Exception e) {
                // Ignora erros silenciosamente
            }

            selected.clear();
        }
    }

    public void setSkin(String name, String value, String signature) {
        if (name != null && value != null && signature != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("name", name);
            selected.put("value", value);
            selected.put("signature", signature);
            
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    /**
     * Adiciona uma skin ao histórico (máximo 10 skins)
     */
    private void addToHistory(String name, String value, String signature) {
        JSONObject data = this.dataContainer.getAsJsonObject();
        if (data == null) {
            data = new JSONObject();
        }
        
        // Obtém o histórico atual
        List<SkinHistoryEntry> history = getHistoryList();
        
        // Remove a skin se já existe no histórico
        history.removeIf(entry -> entry.name.equals(name));
        
        // Adiciona a nova skin no início
        history.add(0, new SkinHistoryEntry(name, value, signature, System.currentTimeMillis()));
        
        // Mantém apenas as últimas 10 skins
        if (history.size() > 10) {
            history = history.subList(0, 10);
        }
        
        // Salva o histórico
        JSONObject historyObj = new JSONObject();
        for (int i = 0; i < history.size(); i++) {
            SkinHistoryEntry entry = history.get(i);
            historyObj.put("history_" + i, entry.toJsonString());
        }
        
        // Adiciona o histórico ao container principal
        data.put("history", historyObj);
        
        // Salva as alterações no dataContainer
        this.dataContainer.set(data.toString());
    }
    
    /**
     * Obtém a lista de skins do histórico
     */
    public List<SkinHistoryEntry> getHistoryList() {
        JSONObject data = this.dataContainer.getAsJsonObject();
        if (data == null) {
            return new ArrayList<>();
        }
        
        Object historyObj = data.get("history");
        if (historyObj == null || !(historyObj instanceof JSONObject)) {
            return new ArrayList<>();
        }
        
        JSONObject historyJson = (JSONObject) historyObj;
        List<SkinHistoryEntry> history = new ArrayList<>();
        
        for (Object key : historyJson.keySet()) {
            String historyKey = key.toString();
            if (historyKey.startsWith("history_")) {
                Object historyData = historyJson.get(historyKey);
                if (historyData instanceof String) {
                    SkinHistoryEntry entry = SkinHistoryEntry.fromJsonString((String) historyData);
                    if (entry != null) {
                        history.add(entry);
                    }
                }
            }
        }
        
        // Ordena por timestamp (mais recente primeiro)
        history.sort((e1, e2) -> Long.compare(e2.timestamp, e1.timestamp));
        
        return history;
    }

    public String getSkin() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("name") : null;
    }

    public String getValue() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("value") : null;
    }

    public void setValue(String value) {
        if (value != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("value", value);
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    public void setSignature(String signature) {
        if (signature != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("signature", signature);
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    public String getSignature() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("signature") : null;
    }
    
    public Long getAppliedAt() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (Long)json.get("appliedAt") : null;
    }

    public void addSkin(String name) {
        if (name != null) {
            String value = Manager.getSkin(name, "value");
            String signature = Manager.getSkin(name, "signature");
            if (value != null && signature != null) {
                JSONObject selected = this.dataContainer.getAsJsonObject();
                selected.put(name, System.currentTimeMillis() + ":" + value);
                this.dataContainer.set(selected.toString());
                selected.clear();
            }
        }
    }
    
    /**
     * Classe interna para representar uma entrada do histórico
     */
    public static class SkinHistoryEntry {
        public final String name;
        public final String value;
        public final String signature;
        public final long timestamp;
        
        public SkinHistoryEntry(String name, String value, String signature, long timestamp) {
            this.name = name;
            this.value = value;
            this.signature = signature;
            this.timestamp = timestamp;
        }
        
        public String toJsonString() {
            return name + ":" + value + ":" + signature + ":" + timestamp;
        }
        
        public static SkinHistoryEntry fromJsonString(String jsonString) {
            try {
                if (jsonString == null || jsonString.isEmpty()) {
                    return null;
                }
                
                String[] parts = jsonString.split(":");
                if (parts.length >= 4) {
                    String name = parts[0];
                    String value = parts[1];
                    String signature = parts[2];
                    long timestamp = Long.parseLong(parts[3]);
                    
                    return new SkinHistoryEntry(name, value, signature, timestamp);
                }
            } catch (Exception e) {
                // Ignora entradas inválidas silenciosamente
            }
            return null;
        }
    }
}
