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
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Balloon extends Cosmetic {
  
  protected String name;
  protected String icon;
  protected List<String> textures;
  
  public Balloon(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String icon, List<String> textures) {
    super(id, CosmeticType.BALLOON, coins, permission);
    this.name = name;
    this.icon = icon;
    this.textures = textures;
    this.rarity = rarity;
    this.cash = cash;
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  public List<String> getTextures() {
    return this.textures;
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
        Language.cosmetics$balloon$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$balloon$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$balloon$icon$perm_desc$start
                .replace("{perm_desc_status}", (rank == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{rank}", rank.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : nome>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("BALLOONS");
  
  public static void setupBalloons() {
    KConfig config = Main.getInstance().getConfig("cosmetics", "balloons");
    
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      double coins = config.getDouble(key + ".coins");
      if (!config.contains(key + ".cash")) {
        config.set(key + ".cash", getAbsentProperty("balloons", key + ".cash"));
      }
      long cash = config.getInt(key + ".cash");
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
      if (!config.contains(key + ".rarity")) {
        config.set(key + ".rarity", getAbsentProperty("balloons", key + ".rarity"));
      }
      List<String> textures = config.getStringList(key + ".textures");
      if (textures.isEmpty()) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
            LOGGER.warning("O balao \"" + key + "\" nao possui texturas."));
        continue;
      }
      
      new Balloon(id, EnumRarity.fromName(config.getString(key + ".rarity")), coins, cash, permission, name, icon, textures);
    }
  }
}
