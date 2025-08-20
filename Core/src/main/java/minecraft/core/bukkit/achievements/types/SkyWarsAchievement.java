package minecraft.core.bukkit.achievements.types;

import minecraft.core.bukkit.achievements.Achievement;
import minecraft.core.core.player.Profile;
import minecraft.core.core.titles.Title;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class SkyWarsAchievement extends Achievement {
  
  protected SkyWarsReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public SkyWarsAchievement(SkyWarsReward reward, String id, String name, String desc, int reach, String... stats) {
    super("sw-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : nome>%name% : desc>" + desc + "\n \n&fProgresso: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "1k1", "Assasino (Solo)", "&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins",
            50, "solokills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "1k2", "Assasino Mestre (Solo)", "&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins",
            250, "solokills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "1w1", "Vitorioso (Solo)", "&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins",
            50, "solowins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "1w2", "Vitorioso Mestre (Solo)", "&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61.000 Coins",
            200, "solowins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "1a1", "Assistente (Solo)", "&7Dê assistência em um total de %reach%\n&7abates para receber:\n \n &8• &6250 Coins",
            100, "soloassists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "1a2", "Assistente Mestre (Solo)", "&7Dê assistência em um total de %reach%\n&7abates para receber:\n \n &8• &61.000 Coins",
            500, "soloassists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "1g1", "Persistente (Solo)", "&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins", 250, "sologames"));
    
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "2k1", "Assasino (Duplas)", "&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6100 Coins",
            50, "duokills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "2k2", "Assasino Mestre (Duplas)", "&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &6500 Coins",
            250, "duokills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "2w1", "Vitorioso (Duplas)", "&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins",
            50, "duowins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "2w2", "Vitorioso Mestre (Duplas)", "&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &61.000 Coins",
            200, "duowins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "2a1", "Assistente (Duplas)", "&7Dê assistência em um total de %reach%\n&7abates para receber:\n \n &8• &6250 Coins",
            100, "duoassists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "2a2", "Assistente Mestre (Duplas)", "&7Dê assistência em um total de %reach%\n&7abates para receber:\n \n &8• &61.000 Coins",
            500, "duoassists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "2g1", "Persistente (Duplas)", "&7Jogue um total de %reach%\n&7partidas para receber:\n \n &8• &6250 Coins", 250, "duogames"));
    
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "tk", "Assasino Total", "&7Abata um total de %reach%\n&7jogadores para receber:\n \n &8• &61.000 Coins",
            500, "solokills", "duokills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(2000), "tw", "Vitorioso Total", "&7Vença um total de %reach%\n&7partidas para receber:\n \n &8• &62.000 Coins",
            500, "solowins", "duowins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "ta", "Assistente Total", "&7Dê assistência em um total de %reach%\n&7abates para receber:\n \n &8• &61.000 Coins",
            500, "soloassists", "duoassists"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("skywars", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("skywars", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface SkyWarsReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements SkyWarsReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("skywars", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements SkyWarsReward {
    private final String titleId;
    
    public TitleReward(String titleId) {
      this.titleId = titleId;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getTitlesContainer().add(Title.getById(this.titleId));
    }
  }
}
