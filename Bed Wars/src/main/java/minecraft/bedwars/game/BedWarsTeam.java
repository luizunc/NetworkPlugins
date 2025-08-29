package minecraft.bedwars.game;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import minecraft.bedwars.Language;
import minecraft.bedwars.hook.container.SelectedContainer;
import minecraft.bedwars.cosmetics.CosmeticType;


import minecraft.bedwars.game.enums.BedWarsMode;
import minecraft.bedwars.game.improvements.UpgradeType;
import minecraft.bedwars.game.improvements.traps.Trap;
import minecraft.bedwars.game.object.BedWarsEquipment;
import minecraft.bedwars.game.object.BedWarsTeamGenerator;
import minecraft.bedwars.game.object.ShopkeeperNPC;
import minecraft.bedwars.utils.PlayerUtils;
import minecraft.core.core.game.GameState;
import minecraft.core.core.game.GameTeam;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.CubeID;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static minecraft.bedwars.game.improvements.UpgradeType.*;
import static minecraft.bedwars.hook.BWCoreHook.reloadScoreboard;

public class BedWarsTeam extends GameTeam {
  
  protected int size;
  protected int index;
  
  protected String name;
  protected String named;
  protected BedWarsTeamGenerator generator;
  protected String bedLocation;
  protected ShopkeeperNPC itemShop;
  protected ShopkeeperNPC upgradeShop;
  
  protected Map<UpgradeType, Integer> upgrades = new HashMap<>();
  protected Map<UUID, BedWarsEquipment> equipments = new HashMap<>();
  
  public CubeID cubeId;
  protected String color;

  protected boolean bedBreaked = false;
  
  protected List<Trap> traps = new ArrayList<>();
  protected Player lastTrapped;
  protected long lastTrappedTime;
  protected JsonObject objectTeam;
  
  public BedWarsTeam(BedWars game, String serialized, int size) {
    super(game, new JsonParser().parse(serialized).getAsJsonObject().get("spawn").getAsString(), size);
    this.index = game.listTeams().size();
    JsonObject team = new JsonParser().parse(serialized).getAsJsonObject();
    this.cubeId = new CubeID(team.get("cubeId").getAsString());
    this.objectTeam = team;
    this.generator = new BedWarsTeamGenerator(this, team.get("generator").getAsString());
    this.bedLocation = team.get("bed").getAsString();
    
    this.size = game.getMode().getSize();
    this.name = ids[this.index];
    this.named = names[this.index];
    
    // Equipment Color.
    int color = this.index;
    if (color == 0) {
      this.color = "255:85:85"; // Vermelho
    } else if (color == 1) {
      this.color = "85:85:255"; // Azul
    } else if (color == 2) {
      this.color = "85:255:85"; // Verde
    } else if (color == 3) {
      this.color = "255:255:85"; // Amarelo
    } else if (color == 4) {
      this.color = "85:255:255"; // Ciano
    } else if (color == 5) {
      this.color = "255:255:255"; // Branco
    } else if (color == 6) {
      this.color = "255:85:255"; // Rosa
    } else {
      this.color = "170:170:170"; // Cinza
    }
  }
  
  public void tick() {
    if (itemShop != null) this.itemShop.update();
    if (upgradeShop != null) this.upgradeShop.update();
    
    if (this.hasUpgrade(REGENERATION)) {
      for (int i = 0; i < 300; i++) {
        Location l = this.cubeId.getRandomLocation();
        l.getWorld().spigot().playEffect(l, Effect.HAPPY_VILLAGER);
      }
      
      this.listPlayers().forEach(player -> {
        if (this.cubeId.contains(player.getLocation())) {
          if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
          }
        } else if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
          player.removePotionEffect(PotionEffectType.REGENERATION);
        }
      });
    }
    
  }
  
  public Player getLastTrapped() {
    if (this.lastTrappedTime < System.currentTimeMillis()) {
      this.lastTrapped = null;
    }
    return this.lastTrapped;
  }
  
  public void setLastTrapped(Player player) {
    this.lastTrappedTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(90);
    this.lastTrapped = player;
  }
  
  public List<Trap> getTraps() {
    return ImmutableList.copyOf(this.traps);
  }
  
  public void addTrap(Trap trap) {
    this.traps.add(trap);
  }
  
  public void removeTrap(Trap trap) {
    this.traps.remove(trap);
  }
  
  public void equip() {
    this.listPlayers().forEach(this::equip);
  }
  
  public void equip(Player player) {
    this.getEquipment(player).refresh();
  }
  
  public void refresh() {
    this.listPlayers().forEach(this::refresh);
  }
  
  public void refresh(Player player) {
    if (this.hasUpgrade(MANIAC_MINER)) {
      player.removePotionEffect(PotionEffectType.FAST_DIGGING);
      player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, this.upgrades.get(MANIAC_MINER) - 1));
    }
    
    if (this.hasUpgrade(LIFE_POINTS)) {
      player.setMaxHealth(20.0D + this.upgrades.get(LIFE_POINTS) * 2);
      player.setHealth(player.getMaxHealth());
    }
    
    if (this.hasUpgrade(SHARPENED_SWORDS)) {
      for (int i = 0; i < player.getInventory().getSize(); i++) {
        ItemStack item = player.getInventory().getItem(i);
        if (item != null && (item.getType().name().contains("_SWORD") || item.getType().name().contains("_AXE"))) {
          if (item.containsEnchantment(Enchantment.DAMAGE_ALL)) {
            item.removeEnchantment(Enchantment.DAMAGE_ALL);
          }
          
          item.addEnchantment(Enchantment.DAMAGE_ALL, this.upgrades.get(SHARPENED_SWORDS));
          player.getInventory().setItem(i, item);
          player.updateInventory();
        }
      }
    }
    
    if (this.hasUpgrade(REINFORCED_ARMOR)) {
      ItemStack[] items = player.getInventory().getArmorContents();
      for (ItemStack item : items) {
        if (item != null && item.getType() != Material.AIR) {
          if (item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
          }
          
          item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, this.upgrades.get(REINFORCED_ARMOR));
        }
      }
    }
  }
  
  public void spawn() {
    this.listPlayers().forEach(player -> {
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        reloadScoreboard(profile);
        profile.setHotbar(null);
        profile.update();
        profile.refresh();
  
        this.equip(player);
        player.teleport(this.getLocation());
      }
    });

    this.itemShop = new ShopkeeperNPC("items", BukkitUtils.deserializeLocation(objectTeam.get("shop").getAsString()), this);
    this.upgradeShop = new ShopkeeperNPC("upgrades", BukkitUtils.deserializeLocation(objectTeam.get("upgrades").getAsString()), this);
    if (!isAlive()) {
      this.breakBed();
    }
  }
  
  @Override
  public void reset() {
    super.reset();

    this.bedBreaked = false;
    this.equipments.clear();
    this.traps.clear();
    this.generator.reset();
    if (itemShop != null) {
      this.itemShop.destroy();
    }
    if (this.upgradeShop != null) {
      this.upgradeShop.destroy();
    }
    this.upgrades.clear();
  }
  
  public void breakBed() {
    this.bedBreaked = true;
    
    Location bedLocation = BukkitUtils.deserializeLocation(this.bedLocation);
    bedLocation.getBlock().breakNaturally(new ItemStack(Material.AIR));
  }
  
  public boolean bed() {
    return bedBreaked;
  }
  
  @Override
  public void addMember(Player player) {
    super.addMember(player);
    this.equipments.put(player.getUniqueId(), new BedWarsEquipment(player, color, name));
  }
  
  @Override
  public void removeMember(Player player) {
    super.removeMember(player);
    BedWars bedwars = (BedWars) this.getGame();
    
    this.equipments.remove(player.getUniqueId()).destroy();
    if (bedwars.getState() == GameState.EMJOGO && !isAlive()) {
      if (!bed()) {
        this.breakBed();
      }
      
      this.getGame().listPlayers().forEach(co -> co.sendMessage(Language.ingame$broadcast$team_eliminated.replace("{team}", this.getName())));
    }
  }
  
  public String getColored(Player player) {
    return StringUtils.getFirstColor(this.named) + player.getName();
  }
  
  public void setUpgrade(UpgradeType type) {
    this.upgrades.put(type, this.getTier(type) + 1);
  }
  
  public String getId() {
    return this.name;
  }
  
  public boolean hasUpgrade(UpgradeType type) {
    return this.upgrades.containsKey(type);
  }
  
  public int getIndex() {
    return index;
  }
  
  public boolean nearBlockedZone(Block block) {
    if (this.getLocation().getBlock().getLocation().distance(block.getLocation()) < 5) {
      return true;
    }
    
    if (this.generator.getLocation().getBlock().getLocation().distance(block.getLocation()) < 3) {
      return true;
    }
    
    return this.itemShop.getLocation().getBlock().getLocation().distance(block.getLocation()) < 3
        || this.upgradeShop.getLocation().getBlock().getLocation().distance(block.getLocation()) < 3;
  }
  

  
  public int getTier(UpgradeType type) {
    return this.upgrades.getOrDefault(type, 0);
  }
  
  public BedWarsTeamGenerator getGenerator() {
    return generator;
  }
  
  public String getNameColor() {
    return StringUtils.getFirstColor(this.named);
  }
  
  public int getSize() {
    return this.size;
  }
  
  public String getName() {
    return this.named;
  }
  
  public String getRawName() {
    return StringUtils.stripColors(this.named);
  }
  
  public String getTag() {
    return StringUtils.getFirstColor(this.getName()) + "§l" + this.getName().charAt(2);
  }
  
  public boolean isBed(Block breakBlock) {
    Block teamBed = BukkitUtils.deserializeLocation(bedLocation).getBlock();
    Block neighbor = PlayerUtils.getBedNeighbor(teamBed);
    return neighbor.equals(breakBlock) || teamBed.equals(breakBlock);
  }
  
  public Location getBedLocation() {
    return BukkitUtils.deserializeLocation(this.bedLocation);
  }
  
  public BedWarsEquipment getEquipment(Player player) {
    return equipments.get(player.getUniqueId());
  }

  public String getSerializedSpawn() {
    return BukkitUtils.serializeLocation(this.getLocation());
  }
  
  public static final String[] names =
      {"§cVermelho", "§9Azul", "§aVerde", "§eAmarelo", "§bCiano", "§fBranco", "§dRosa", "§7Cinza"};
  public static final String[] ids = {"14", "11", "13", "4", "9", "0", "6", "7"};
}
