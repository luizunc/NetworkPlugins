package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.types.KillEffect;
import minecraft.bedwars.lobby.trait.NPCSkinTrait;
import minecraft.bedwars.menus.cosmetics.MenuCosmetics;
import minecraft.core.core.game.FakeGame;
import minecraft.core.core.libraries.npclib.NPCLibrary;
import minecraft.core.core.libraries.npclib.api.event.NPCDeathEvent;
import minecraft.core.core.libraries.npclib.api.npc.NPC;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.hotbar.Hotbar;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class KillEffectPreview extends AbstractPreview<KillEffect> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[2];

  static {
    createLocations();
  }

  private NPC target;
  private Location oldLocation;
  
  public KillEffectPreview(Profile profile, KillEffect cosmetic) {
    super(profile, cosmetic);
    
    // Criar NPC alvo
    this.target = NPCLibrary.createNPC(EntityType.PLAYER, "§cAlvo");
    this.target.data().set(NPC.ATTACHED_PLAYER, profile.getName());
    this.target.addTrait(new NPCSkinTrait(this.target,
        "eyJ0aW1lc3RhbXAiOjE1ODY5MzIzODcyNTQsInByb2ZpbGVJZCI6ImJjNzAxYzk1NTViNzQ5YmJhNDdkZWEzZTlmZDgwMDFkIiwicHJvZmlsZU5hbWUiOiIweDQ1MiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk5NDc3OWM2ODBlMzhiNzU3ZGUyMTZkYmQ0OTVlZjljYmFmYzg3MjllYTlhYjAyNTNkMTNlZDU5N2Y1MTQ0MiJ9fX0=",
        "ppEIxcszLINYQw4BeE0jftmrBpvKgt5MUGRm21QYJBil60QWagrMZlovFv/0+sHwJJ7eJHLNjhvthY+i41vNljVY3/O21DbTXygyGkHkhAAyihCBroy5IxV87vXsylZNGZ5gfd7t3OLxpyOI6TUizWT10XzsrmhYo3hCZETkdVhIll+T6ENZxCHB/8Sl1mUhWko4hnf8pgmoS0rsixXM9WbccGFO00pNYYZ2Jai133QE0slMkbyCf/7t4Q2ATzjXGx+8avSu3LpHxSff2tUDWH54qanQ5x6eey9Rc846V24TUiy3kiKWmTchGiegZ6kAj3sO1wO/ohvQySzbeAsnz94rGdwzjpUF3pWgH5mJ6vHEstsH1hoYUBKwFAxaKhp2iI7CzgHOO+BMVbIF1Fm66OuDJ3+4am2mvCGRsGG0ufPxHM6O3TZHpkw3rUGWbx13KQLSFWvLzZjzl/EIcO8Kt6XTKgl8qciCG9nFM97EkCjpNHMoedKnphwFVu/K1O3hGz6QxI4/8PdsYZnLOlvPlG1nHDzlkDGjDtkLXTgWKHVTNFD7R10jXNbWoJyt712D2c7otceOOpu1s70JRbHMKKxMZtSMOt3MQMuCw6LJJEdbtyC4d8D6mE8lM1HZmMa3tcb7cBFHryy1eEwMStJh7A2O3GP6SPssgCI2TTtoLEs="));
    this.target.spawn(LOCATIONS[0]);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    profile.setHotbar(null);
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    // Teleportar jogador para a área de preview
    this.player.teleport(LOCATIONS[1]);
    
    // Dar uma espada para o jogador
    this.player.getInventory().addItem(BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1"));
    this.player.updateInventory();
    
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("killeffect")) {
      for (int index = 0; index < 2; index++) {
        String value = CONFIG.getString("killeffect." + (index + 1));
        if (value != null) {
          LOCATIONS[index] = BukkitUtils.deserializeLocation(value);
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.stop();
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onNPCDeathEvent(NPCDeathEvent evt) {
    if (evt.getNPC().equals(this.target)) {
      // Executar o efeito de abate
      this.cosmetic.execute(this.player, this.target.getCurrentLocation());
      
      // Retornar ao menu após 3 segundos
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
        this.returnToMenu();
        this.stop();
      }, 60L); // 3 segundos = 60 ticks
    }
  }
  
  public void stop() {
    this.oldLocation = null;
    if (this.target != null) {
      this.target.destroy();
      this.target = null;
    }
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      this.player.setAllowFlight(this.player.hasPermission("core.fly"));
      profile.setGame(null);
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      this.player.getInventory().clear();
      new MenuCosmetics<>(profile, "Efeitos de Abate", KillEffect.class);
    }
  }
  
  @Override
  public void run() {
    // Este método é necessário para BukkitRunnable, mas não é usado neste preview
  }
}
