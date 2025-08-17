package dev.slickcollections.kiwizin.murder.cosmetics;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.slickcollections.kiwizin.murder.Main;
import dev.slickcollections.kiwizin.murder.container.CosmeticsContainer;
import dev.slickcollections.kiwizin.murder.cosmetics.types.*;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.slickcollections.kiwizin.murder.container.SelectedContainer;

@SuppressWarnings("unchecked")
public abstract class Cosmetic {

  private long id;
  private double coins;
  protected long cash;
  private String permission;
  private CosmeticType type;
  protected EnumRarity rarity;

  public Cosmetic(long id, CosmeticType type, double coins, String permission) {
    this.id = id;
    this.coins = coins;
    this.permission = permission;
    this.type = type;
    COSMETICS.add(this);
  }

  public void give(Profile profile) {
    profile.getAbstractContainer("kCoreMurder", "cosmetics", CosmeticsContainer.class).addCosmetic(this);
  }

  public boolean has(Profile profile) {
    return profile.getAbstractContainer("kCoreMurder", "cosmetics", CosmeticsContainer.class).hasCosmetic(this);
  }

  public boolean isSelected(Profile profile) {
    return profile.getAbstractContainer("kCoreMurder", "selected", SelectedContainer.class).isSelected(this);
  }

  public long getId() {
    return this.id;
  }

  public String getLootChestsID() {
    return "mm" + this.type.ordinal() + "-" + this.id;
  }

  public long getIndex() { return 1; }

  public EnumRarity getRarity() {
    return this.rarity;
  }

  public double getCoins() {
    return this.coins;
  }

  public long getCash() {
    return this.cash;
  }

  public String getPermission() {
    return this.permission;
  }

  public CosmeticType getType() {
    return this.type;
  }

  public boolean canBuy(Player player) {
    return this.permission.isEmpty() || player.hasPermission(this.permission);
  }

  public abstract String getName();

  public abstract ItemStack getIcon(Profile profile);

  private static final List<Cosmetic> COSMETICS = new ArrayList<>();

  public static void setupCosmetics() {
    Knife.setupKnifes();
    DeathCry.setupDeathCries();
    WinAnimation.setupAnimations();
    Hat.setupHats();
    DeathMessage.setupDeathMessages();
  }

  public static void removeCosmetic(Cosmetic cosmetic) {
    COSMETICS.remove(cosmetic);
  }

  public static <T extends Cosmetic> T findById(Class<T> cosmeticClass, long id) {
    return COSMETICS.stream()
      .filter(cosmetic -> (cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass)) && cosmetic.getId() == id)
      .map(cosmetic -> (T) cosmetic).findFirst().orElse(null);
  }

  public static Cosmetic findById(String lootChestID) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getLootChestsID().equals(lootChestID)).findFirst().orElse(null);
  }

  public static List<Cosmetic> listCosmetics() {
    return COSMETICS;
  }

  public static <T extends Cosmetic> List<T> listByType(Class<T> cosmeticClass) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass))
      .sorted(Comparator.comparingLong(Cosmetic::getId)).map(cosmetic -> (T) cosmetic).collect(Collectors.toList());
  }

  protected static Object getAbsentProperty(String file, String property) {
    InputStream stream = Main.getInstance().getResource(file + ".yml");
    if (stream == null) {
      return null;
    }

    return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8)).get(property);
  }
}
