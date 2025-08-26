package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.types.ShopkeeperSkin;
import minecraft.bedwars.lobby.trait.NPCSkinTrait;
import minecraft.bedwars.menus.cosmetics.MenuCosmetics;
import minecraft.core.core.game.FakeGame;
import minecraft.core.core.libraries.npclib.NPCLibrary;
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
import org.bukkit.scheduler.BukkitTask;

public class ShopkeeperSkinPreview extends AbstractPreview<ShopkeeperSkin> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[2];

  static {
    createLocations();
  }
  
  private NPC shopkeeper;
  private Location oldLocation;
  private BukkitTask autoReturnTask;
  
  public ShopkeeperSkinPreview(Profile profile, ShopkeeperSkin cosmetic) {
    super(profile, cosmetic);
    
    // Criar NPC com a skin do cosmético
    this.shopkeeper = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] Vendedor");
    this.shopkeeper.data().set(NPC.PROTECTED_KEY, true);
    this.shopkeeper.addTrait(new NPCSkinTrait(this.shopkeeper, cosmetic.getValue(), cosmetic.getSignature()));
    this.shopkeeper.spawn(LOCATIONS[0]);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    
    // Limpar o inventário do jogador para remover itens da hotbar anterior
    this.player.getInventory().clear();
    
    // Configurar hotbar personalizada com apenas uma cama no slot 9
    profile.setHotbar(Hotbar.getHotbarById("preview_vendedor"));
    
    // Aplicar a nova hotbar imediatamente
    profile.refresh();
    
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    // Teleportar jogador para a área de preview
    this.player.teleport(LOCATIONS[1]);
    
    // Enviar mensagem informativa
    this.player.sendMessage("§aVocê entrou no preview da skin do vendedor!");
    this.player.sendMessage("§7Você será retornado automaticamente em 30 segundos.");
    this.player.sendMessage("§7Use a cama na sua hotbar para voltar manualmente.");
    
    // Iniciar timer de 30 segundos para voltar automaticamente
    this.autoReturnTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (this.player != null && this.player.isOnline()) {
        this.player.sendMessage("§cTempo do preview expirado! Retornando ao lobby...");
        this.returnToMenu();
        this.stop();
      }
    }, 600L); // 30 segundos = 600 ticks
    
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("vendedor")) {
      for (int index = 0; index < 2; index++) {
        String value = CONFIG.getString("vendedor." + (index + 1));
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
  
  public void stop() {
    // Cancelar o timer de retorno automático
    if (this.autoReturnTask != null) {
      this.autoReturnTask.cancel();
      this.autoReturnTask = null;
    }
    
    this.oldLocation = null;
    if (this.shopkeeper != null) {
      this.shopkeeper.destroy();
      this.shopkeeper = null;
    }
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      this.player.setAllowFlight(this.player.hasPermission("core.fly"));
      profile.setGame(null);
      
      // Limpar o inventário e aplicar a hotbar do lobby
      this.player.getInventory().clear();
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refresh();
      
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      // Retornar ao lobby do BedWars em vez de abrir o menu de cosméticos
    }
  }
  
  @Override
  public void run() {
    // Este método é necessário para BukkitRunnable, mas não é usado neste preview
  }
} 