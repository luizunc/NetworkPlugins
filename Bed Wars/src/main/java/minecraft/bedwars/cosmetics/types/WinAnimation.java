package minecraft.bedwars.cosmetics.types;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.types.winanimations.*;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.bedwars.cosmetics.object.AbstractExecutor;
import minecraft.bedwars.cosmetics.types.winanimations.*;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.role.Rank;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumRarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class WinAnimation extends Cosmetic {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "winanimations");
  private String name;
  private String icon;
  
  public WinAnimation(long id, String key, double coins, String permission, String name, String icon) {
    super(id, CosmeticType.WIN_ANIMATION, coins, permission);
    this.name = name;
    this.icon = icon;
    if (id != 0) {
      this.rarity = this.getRarity(key);
      this.cash = this.getCash(key);
    } else {
      this.rarity = EnumRarity.COMUM;
    }
  }
  
  public static void setupAnimations() {
    checkIfAbsent("fireworks");
    checkIfAbsent("ender_dragon");
    checkIfAbsent("thor");
    checkIfAbsent("wither");
  
    new Fireworks(CONFIG.getSection("fireworks"));
    new EnderDragon(CONFIG.getSection("ender_dragon"));
    new Thor(CONFIG.getSection("thor"));
    new Wither(CONFIG.getSection("wither"));
  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    try {
      FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("winanimations.yml"), StandardCharsets.UTF_8));
      
      // Verificar se a seção existe antes de tentar acessá-la
      if (!config.contains(key)) {
        return;
      }
      
      ConfigurationSection section = config.getConfigurationSection(key);
      if (section == null) {
        return;
      }
      
      for (String dataKey : section.getKeys(false)) {
        Object value = config.get(key + "." + dataKey);
        if (value != null) {
          CONFIG.set(key + "." + dataKey, value);
        }
      }
    } catch (Exception e) {
      // Log do erro mas não interromper a execução
      Main.getInstance().getLogger().warning("Erro ao carregar configuração para " + key + ": " + e.getMessage());
    }
  }
  
  protected long getCash(String key) {
    try {
      if (!CONFIG.contains(key + ".cash")) {
        Object value = getAbsentProperty("winanimations", key + ".cash");
        if (value != null) {
          CONFIG.set(key + ".cash", value);
        } else {
          CONFIG.set(key + ".cash", 0);
        }
      }
      
      return (long) CONFIG.getInt(key + ".cash");
    } catch (Exception e) {
      return 0;
    }
  }
  
  protected EnumRarity getRarity(String key) {
    try {
      if (!CONFIG.contains(key + ".rarity")) {
        Object value = getAbsentProperty("winanimations", key + ".rarity");
        if (value != null) {
          CONFIG.set(key + ".rarity", value);
        } else {
          CONFIG.set(key + ".rarity", "COMUM");
        }
      }
      
      String rarityStr = CONFIG.getString(key + ".rarity");
      if (rarityStr != null) {
        return EnumRarity.fromName(rarityStr);
      } else {
        return EnumRarity.COMUM;
      }
    } catch (Exception e) {
      return EnumRarity.COMUM;
    }
  }
  
  public abstract AbstractExecutor execute(Player player);
  
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
    String color = has ? (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked)
        : (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$win_animation$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$win_animation$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$win_animation$icon$perm_desc$start
                .replace("{perm_desc_status}", (rank == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{rank}", rank.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + desc + " : nome>" + (color + this.name));
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}