package dev.slickcollections.kiwizin.mysterybox;

import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.plugin.config.KWriter;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("language");
  public static List<String> lobby$mysterybox$holograms = Arrays.asList(
      "{boxes}", "§bCápsula Mágica", "§eClique para abrir!");
  public static String lobby$mysterybox$mysteryboxes = "§e§l{boxes} CAIXA{s}";
  public static String lobby$mysterybox$mysteryboxes_replace = "S";
  public static String menus$boxes$info =
      "BOOK : 1 : nome>&aInformações : desc>&fFragmentos Misteriosos: &7{frags}\n&fCápsulas Mágicas: &7{boxes}\n \n&fAdquira Cash acessando:\n&aloja.redeslick.com\n \n&aSeus últimos 5 itens encontrados: {last_rewards}";
  public static String menus$boxes$no_have_boxes =
      "STAINED_GLASS_PANE:14 : 1 : nome>&cOps, você não possui Cápsulas Mágicas. : desc>&bCápsulas Mágicas &7podem ser adquiridas utilizando Cash.\n \n&fAdquira Cash acessando\n&eloja.redeslick.com";
  public static String menus$boxes$open_multiples_boxes =
      "NETHER_STAR : 1 : nome>&aAbrir Múltiplas Cápsulas Mágicas : desc>&7Clique aqui para abrir várias\n&bCápsulas Mágicas&7 ao mesmo tempo\n \n{open_desc}";
  public static String menus$boxes$fabricate_box = "ANVIL : 1 : nome>&aFabricar Cápsulas Mágicas : desc>&7Ao receber itens repitidos em uma cápsula, você\n&7recebe &bFragmentos Misteriosos &7que podem ser\n&7usados para fabricar cápsulas.\n \n&eClique para fabricar!";
  
  public static String cosmetics$color$locked = "§a";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§6";
  
  public static String cosmetics$icon$perm_desc$common = "§cVocê não possui permissão.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusivo para {role} §7ou superior.";
  public static String cosmetics$icon$buy_desc$enough = "§cVocê não possui saldo suficiente.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClique para comprar!";
  public static String cosmetics$icon$has_desc$select = "§eClique para selecionar!";
  public static String cosmetics$icon$has_desc$selected = "§eClique para deselecionar!";
  
  public static String cosmetics$opener$icon$perm_desc$start =
      "§7Troca a animação da Cápsula\n§7para {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$opener$icon$buy_desc$start =
      "§7Troca a animação da Cápsula\n§7para {name}.\n \n§fRaridade: {rarity}\n§fCusto: §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$opener$icon$has_desc$start =
      "§7Troca a animação da Cápsula\n§7para {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Main.getInstance().getWriter(CONFIG.getFile(),
        "kMysteryBox - Criado por Kiwizin\nVersão da configuração: " + Main.getInstance()
            .getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          KWriter.YamlEntryInfo entryInfo = field.getAnnotation(KWriter.YamlEntryInfo.class);
          
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
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(),
                    CONFIG.get(nativeName)}));
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
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(), value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }
    
    if (save) {
      writer.write();
      CONFIG.reload();
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
          () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
  
  
}
