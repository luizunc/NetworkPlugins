package minecraft.bedwars.cosmetics.types;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.role.Rank;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumRarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.logging.Level;

@SuppressWarnings({"unchecked", "deprecated"})
public class Cage extends Cosmetic {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("CAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "cages");
  
  private String name;
  private String icon;
  private JSONArray blocks, soloBlocks;
  
  public Cage(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String icon, JSONArray blocks, JSONArray blocksS) {
    super(id, CosmeticType.CAGE, coins, permission);
    this.name = name;
    this.icon = icon;
    this.blocks = blocks;
    this.soloBlocks = blocksS;
    this.rarity = rarity;
    this.cash = cash;
  }
  
  public static void setupCages() {
    for (String key : CONFIG.getKeys(false)) {
      long id = CONFIG.getInt(key + ".id");
      double coins = CONFIG.getDouble(key + ".coins");
      if (!CONFIG.contains(key + ".cash")) {
        CONFIG.set(key + ".cash", getAbsentProperty("cages", key + ".cash"));
      }
      long cash = CONFIG.getInt(key + ".cash", 0);
      String permission = CONFIG.getString(key + ".permission");
      String name = CONFIG.getString(key + ".name");
      String icon = CONFIG.getString(key + ".icon");
      if (!CONFIG.contains(key + ".rarity")) {
        CONFIG.set(key + ".rarity", getAbsentProperty("cages", key + ".rarity"));
      }
      JSONArray blocks = null;
      try {
        blocks = (JSONArray) new JSONParser().parse(CONFIG.getString(key + ".blocks"));
      } catch (ParseException ex) {
        LOGGER.log(Level.WARNING, "Cage \"" + key + "\" invalida: ", ex);
        continue;
      }
      
      JSONArray blocksS = null;
      try {
        if (CONFIG.getString(key + ".blocksS") != null) {
          blocksS = (JSONArray) new JSONParser().parse(CONFIG.getString(key + ".blocksS"));
        }
      } catch (ParseException ex) {
        LOGGER.log(Level.WARNING, "Cage (Solo) \"" + key + "\" invalida: ", ex);
        continue;
      }
      
      new Cage(id, EnumRarity.fromName(CONFIG.getString(key + ".rarity")), coins, cash, permission, name, icon, blocks, blocksS);
    }
  }
  
  public static void setSolo(Player player, String name) {
    JSONArray blocks = new JSONArray();
    Location location = player.getLocation().getBlock().getLocation().add(0, -1, 0);
    runCage((x, y, z) -> {
      Block block = location.clone().add(x, y, z).getBlock();
      if (block.getType() != Material.AIR) {
        blocks.add(x + "; " + y + "; " + z + "; " + block.getType().name() + "; " + block.getData());
      }
    }, 5, 4, 2, 1);
    
    String key = name.replace(" ", "_");
    CONFIG.set(key + ".blocksS", blocks.toString());
    
    // Reiniciar cages.
    Cosmetic.listByType(Cage.class).forEach(Cosmetic::removeCosmetic);
    Cage.setupCages();
  }
  
  public static void createCage(Player player, String name) {
    long id = 1;
    while (Cosmetic.findById(Cage.class, id) != null) {
      id++;
    }
    
    JSONArray blocks = new JSONArray();
    Location location = player.getLocation().getBlock().getLocation().add(0, -1, 0);
    runCage((x, y, z) -> {
      Block block = location.clone().add(x, y, z).getBlock();
      if (block.getType() != Material.AIR) {
        blocks.add(x + "; " + y + "; " + z + "; " + block.getType().name() + "; " + block.getData());
      }
    }, 5, 4, 2, 1);
    
    String key = name.replace(" ", "_");
    CONFIG.set(key + ".id", id);
    CONFIG.set(key + ".coins", 10000.0);
    CONFIG.set(key + ".cash", 50);
    CONFIG.set(key + ".rarity", "COMUM");
    CONFIG.set(key + ".name", name);
    CONFIG.set(key + ".permission", "");
    CONFIG.set(key + ".icon", "GLASS : 1");
    CONFIG.set(key + ".blocks", blocks.toString());
    CONFIG.set(key + ".blocksS", new JSONArray());
    new Cage(id, EnumRarity.COMUM, 10000.0, 50, "", name, "GLASS : 1", blocks, null);
  }
  
  public static void removeCage(Cage cage) {
    for (String key : CONFIG.getKeys(false)) {
      if (CONFIG.getInt(key + ".id") == cage.getId()) {
        CONFIG.set(key, null);
      }
    }
    
    cage.destroy();
    Cosmetic.removeCosmetic(cage);
  }

  public static void applyCage(Location location, boolean big) {
    runCage((x, y, z) -> location.clone().add(x, y, z).getBlock().setType(Material.GLASS), 4, big ? 2 : 1);
  }
  
  public static void destroyCage(Location location) {
    runCage((x, y, z) -> location.clone().add(x, y, z).getBlock().setType(Material.AIR), 5, 4, 2, 1);
  }
  
  private static void runCage(CageRunnable cageRunnable, double height, double width) {
    runCage(cageRunnable, height, height, width, width);
  }
  
  private static void runCage(CageRunnable cageRunnable, double height, double heightIndex, double width, double widthInside) {
    for (double y = 0; y <= height; y++) {
      for (double x = -width; x <= width; x++) {
        for (double z = -width; z <= width; z++) {
          if (y > 0 && y < heightIndex) {
            if ((x > -widthInside && x < widthInside) && (z > -widthInside && z < widthInside)) {
              continue;
            }
          }
          
          cageRunnable.run(x, y, z);
        }
      }
    }
  }
  
  public void destroy() {
    this.name = null;
    this.icon = null;
    this.blocks.clear();
    this.soloBlocks.clear();
    this.soloBlocks = null;
    this.blocks = null;
  }
  
  public void apply(Location location, boolean size) {
    if (size) {
      for (Object object : this.blocks) {
        if (object instanceof String) {
          String offset = (String) object;
          double offsetX = Double.parseDouble(offset.split("; ")[0]);
          double offsetY = Double.parseDouble(offset.split("; ")[1]);
          double offsetZ = Double.parseDouble(offset.split("; ")[2]);
          Material blockMaterial = Material.matchMaterial(offset.split("; ")[3]);
          byte data = Byte.parseByte(offset.split("; ")[4]);
          
          Block block = location.clone().add(offsetX, offsetY, offsetZ).getBlock();
          block.setType(blockMaterial);
          BlockState state = block.getState();
          state.getData().setData(data);
          state.update(true);
        }
      }
    } else {
      if (this.soloBlocks == null) {
        LOGGER.warning("Blocos do Modo Solo nao foram encontrados para a jaula " + this.name);
        return;
      }
      
      for (Object object : this.soloBlocks) {
        if (object instanceof String) {
          String offset = (String) object;
          double offsetX = Double.parseDouble(offset.split("; ")[0]);
          double offsetY = Double.parseDouble(offset.split("; ")[1]);
          double offsetZ = Double.parseDouble(offset.split("; ")[2]);
          Material blockMaterial = Material.matchMaterial(offset.split("; ")[3]);
          byte data = Byte.parseByte(offset.split("; ")[4]);
          
          Block block = location.clone().add(offsetX, offsetY, offsetZ).getBlock();
          block.setType(blockMaterial);
          BlockState state = block.getState();
          state.getData().setData(data);
          state.update(true);
        }
      }
    }
  }
  
  public void preview(Player viewer, Location location, boolean destroy) {
    for (Object object : this.blocks) {
      if (object instanceof String) {
        String offset = (String) object;
        double offsetX = Double.parseDouble(offset.split("; ")[0]);
        double offsetY = Double.parseDouble(offset.split("; ")[1]);
        double offsetZ = Double.parseDouble(offset.split("; ")[2]);
        Material blockMaterial = Material.matchMaterial(offset.split("; ")[3]);
        byte data = Byte.parseByte(offset.split("; ")[4]);
        
        if (destroy) {
          viewer.sendBlockChange(location.clone().add(offsetX, offsetY, offsetZ), Material.AIR, (byte) 0);
        } else {
          viewer.sendBlockChange(location.clone().add(offsetX, offsetY, offsetZ), blockMaterial, data);
        }
      }
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    double coins = profile.getCoins("bedwars");
    long cash = profile.getStats("account", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("bedwars", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Rank rank = Rank.getRankByPermission(this.getPermission());
    String color = has ?
        (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked) :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$cage$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$cage$icon$buy_desc$start.replace("{buy_desc_status}",
                (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$cage$icon$perm_desc$start
                .replace("{perm_desc_status}", (rank == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{rank}", rank.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins()))
        .replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : nome>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
  
  private interface CageRunnable {
    void run(double x, double y, double z);
  }
}
