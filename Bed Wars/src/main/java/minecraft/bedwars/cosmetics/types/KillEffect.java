package minecraft.bedwars.cosmetics.types;

import minecraft.bedwars.Language;
import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.CosmeticType;
import minecraft.bedwars.cosmetics.types.killeffects.ExplosiveSphere3D;
import minecraft.bedwars.cosmetics.types.killeffects.TemporalPyramid3D;
import minecraft.bedwars.cosmetics.types.killeffects.Tornado3D;
import minecraft.bedwars.cosmetics.types.killeffects.Crystal3D;
import minecraft.bedwars.cosmetics.types.killeffects.Portal3D;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.core.cash.CashManager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import minecraft.core.core.utils.enums.EnumRarity;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class KillEffect extends Cosmetic {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "killeffects");
  private String name;
  private String icon;
  
  public KillEffect(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String icon) {
    super(id, CosmeticType.KILL_EFFECT, coins, permission);
    this.name = name;
    this.icon = icon;
    this.rarity = rarity;
    this.cash = cash;
  }
  
  public static void setupEffects() {
    checkIfAbsent("explosive_sphere_3d");
    checkIfAbsent("temporal_pyramid_3d");
    checkIfAbsent("tornado_3d");
    checkIfAbsent("crystal_3d");
    checkIfAbsent("portal_3d");
    
    new ExplosiveSphere3D(CONFIG.getSection("explosive_sphere_3d"));
    new TemporalPyramid3D(CONFIG.getSection("temporal_pyramid_3d"));
    new Tornado3D(CONFIG.getSection("tornado_3d"));
    new Crystal3D(CONFIG.getSection("crystal_3d"));
    new Portal3D(CONFIG.getSection("portal_3d"));
  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("killeffects.yml"), StandardCharsets.UTF_8));
    for (String dataKey : config.getConfigurationSection(key).getKeys(false)) {
      CONFIG.set(key + "." + dataKey, config.get(key + "." + dataKey));
    }
  }
  
  public void execute(Location location) {
    this.execute(null, location);
  }
  
  public abstract void execute(Player viewer, Location location);
  
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
    
    Rank rank = Rank.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked) :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$kill_effect$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$kill_effect$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$kill_effect$icon$perm_desc$start
                .replace("{perm_desc_status}", (rank == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{rank}", rank.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    
    // Verificação de segurança para evitar NullPointerException
    if (this.icon == null || this.icon.isEmpty()) {
      // Fallback para um ícone padrão se o ícone estiver null ou vazio
      ItemStack item = BukkitUtils.deserializeItemStack("STONE : 1 : nome>" + (color + this.name) + " : desc>&cErro: Ícone não encontrado");
      if (isSelected) {
        BukkitUtils.putGlowEnchantment(item);
      }
      return item;
    }
    
    try {
      ItemStack item = BukkitUtils.deserializeItemStack(this.icon + desc + " : nome>" + (color + this.name));
      if (isSelected) {
        BukkitUtils.putGlowEnchantment(item);
      }
      return item;
    } catch (Exception e) {
      // Fallback em caso de erro na deserialização
      ItemStack item = BukkitUtils.deserializeItemStack("STONE : 1 : nome>" + (color + this.name) + " : desc>&cErro: Ícone inválido");
      if (isSelected) {
        BukkitUtils.putGlowEnchantment(item);
      }
      return item;
    }
  }
}
