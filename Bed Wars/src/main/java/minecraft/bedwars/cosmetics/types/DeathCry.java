package minecraft.bedwars.cosmetics.types;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.role.Rank;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumRarity;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class DeathCry extends Cosmetic {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("DEATH_CRY");
  private String name;
  private String icon;
  private EnumSound sound;
  private float volume;
  private float speed;
  
  public DeathCry(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String icon, EnumSound sound, float volume, float speed) {
    super(id, CosmeticType.DEATH_CRY, coins, permission);
    this.name = name;
    this.icon = icon;
    this.sound = sound;
    this.volume = volume;
    this.speed = speed;
    this.rarity = rarity;
    this.cash = cash;
  }
  
  public static void setupDeathCries() {
    KConfig config = Main.getInstance().getConfig("cosmetics", "deathcries");
    
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      double coins = config.getDouble(key + ".coins");
      if (!config.contains(key + ".cash")) {
        config.set(key + ".cash", getAbsentProperty("deathcries", key + ".cash"));
      }
      long cash = config.getInt(key + ".cash", 0);
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
      if (!config.contains(key + ".rarity")) {
        config.set(key + ".rarity", getAbsentProperty("deathcries", key + ".rarity"));
      }
      EnumSound sound;
      try {
        sound = EnumSound.valueOf(config.getString(key + ".sound"));
      } catch (Exception ex) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> LOGGER.warning("O som \"" + config.getString(key + ".sound") + "\" nao foi encontrado."));
        continue;
      }
      float volume = (float) config.getDouble(key + ".volume");
      float speed = (float) config.getDouble(key + ".speed");
      
      new DeathCry(id, EnumRarity.fromName(config.getString(key + ".rarity")), coins, cash, permission, name, icon, sound, volume, speed);
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  public EnumSound getSound() {
    return this.sound;
  }
  
  public float getVolume() {
    return this.volume;
  }
  
  public float getSpeed() {
    return this.speed;
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
        Language.cosmetics$deathcry$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$deathcry$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$deathcry$icon$perm_desc$start
                .replace("{perm_desc_status}", (rank == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{rank}", rank.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : nome>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}
