package minecraft.core.core.api.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

public class Mojang {
    
    private static final ConcurrentHashMap<String, String> CACHED_UUID = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> CACHED_PROPERTY = new ConcurrentHashMap<>();
    
    public static String getUUID(String name) {
        String id = CACHED_UUID.get(name);
        if (id != null) {
            return id;
        }
        
        try {
            URLConnection conn = new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection();
            conn.setConnectTimeout(5000);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String read;
            while ((read = reader.readLine()) != null) {
                builder.append(read);
            }
            if (builder.length() > 0) {
                JsonObject json = new JsonParser().parse(builder.toString()).getAsJsonObject();
                id = json.get("id").getAsString();
                CACHED_UUID.put(name, id);
                return id;
            }
        } catch (Exception e) {
            // Fallback para API alternativa
            try {
                URLConnection conn = new URL("https://api.minetools.eu/uuid/" + name).openConnection();
                conn.setConnectTimeout(5000);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String read;
                while ((read = reader.readLine()) != null) {
                    builder.append(read);
                }
                if (builder.toString().contains("\"status\": \"OK\"")) {
                    JsonObject json = new JsonParser().parse(builder.toString()).getAsJsonObject();
                    id = json.get("id").getAsString();
                    CACHED_UUID.put(name, id);
                    return id;
                }
            } catch (Exception ex) {
                // Ignora erro
            }
        }
        
        return null;
    }

    public static String getSkinProperty(String id) {
        String property = CACHED_PROPERTY.get(id);
        if (property != null) {
            return property;
        }
        
        try {
            URLConnection conn = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false").openConnection();
            conn.setConnectTimeout(5000);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String read;
            while ((read = reader.readLine()) != null) {
                builder.append(read);
            }
            if (builder.length() > 0) {
                JsonObject json = new JsonParser().parse(builder.toString()).getAsJsonObject();
                JsonObject properties = json.getAsJsonArray("properties").get(0).getAsJsonObject();
                String value = properties.get("value").getAsString();
                String signature = properties.get("signature").getAsString();
                property = id + " : " + value + " : " + signature;
                CACHED_PROPERTY.put(id, property);
                return property;
            }
        } catch (Exception e) {
            // Ignora erro
        }
        
        return null;
    }
    
    public static UUID getOfflineUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
    }
    
    public static String parseUUID(String withoutDashes) {
        return withoutDashes.substring(0, 8) + '-' + withoutDashes.substring(8, 12) + '-' + withoutDashes.substring(12, 16) + '-' + withoutDashes.substring(16, 20) + '-' + withoutDashes.substring(20, 32);
    }
}
