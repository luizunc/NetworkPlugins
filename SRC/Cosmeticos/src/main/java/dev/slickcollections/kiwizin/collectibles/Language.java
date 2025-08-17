package dev.slickcollections.kiwizin.collectibles;

import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.plugin.config.KWriter;
import dev.slickcollections.kiwizin.plugin.config.KWriter.YamlEntry;
import dev.slickcollections.kiwizin.plugin.config.KWriter.YamlEntryInfo;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings({"rawtypes"})
public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("LANGUAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("language");
  
  @YamlEntryInfo(annotation = "Lista de mundos permitidos.")
  public static List<String> settings$mundos = Collections.singletonList("world");
  @YamlEntryInfo(annotation = "Ativar/desativar o item para abrir o menu de cosméticos.")
  public static boolean settings$item$usar = true;
  @YamlEntryInfo(annotation = "Ativar/desativar o comando /cosmetics|/cosmeticos para abrir o menu de cosméticos.")
  public static boolean settings$command$cosmetics = true;
  @YamlEntryInfo(annotation = "Quantos coins os jogadores recebem nos minigames caso seja um cosmético duplicado.")
  public static int settings$coins$duplicated = 500;
  @YamlEntryInfo(annotation = "Slot de 1 a 9.")
  public static int settings$item$slot = 4;
  @YamlEntryInfo(annotation = "Slot de 1 a 9.")
  public static int settings$gadget$slot = 5;
  @YamlEntryInfo(annotation = "ItemStack criada a partir dessa String.")
  public static String settings$item$stack = "RAW_FISH:3 : 1 : nome>&eCosméticos";
  
  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Main.getInstance().getWriter(CONFIG.getFile(), "kCosmetics - Criado por Kiwizin\nVersão da configuração: " + Main.getInstance().getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(YamlEntryInfo.class)) {
        YamlEntryInfo entryInfo = field.getAnnotation(YamlEntryInfo.class);
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          
          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            field.set(null, value);
            writer.set(nativeName, new YamlEntry(new Object[]{entryInfo.annotation(), CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            save = true;
            writer.set(nativeName, new YamlEntry(new Object[]{entryInfo.annotation(), value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }
    
    if (save) {
      writer.write();
      CONFIG.reload();
      
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
          LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
}
