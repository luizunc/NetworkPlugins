package minecraft.bedwars.cosmetics.object.preview;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.object.AbstractPreview;
import minecraft.bedwars.cosmetics.types.KillEffect;
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
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class KillEffectPreview extends AbstractPreview<KillEffect> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[2];

  static {
    createLocations();
  }

  private NPC target;
  private Location oldLocation;
  private BukkitTask animationMonitorTask;
  private boolean animationFinished = false;
  
  public KillEffectPreview(Profile profile, KillEffect cosmetic) {
    super(profile, cosmetic);
    
    // Criar NPC alvo
    this.target = NPCLibrary.createNPC(EntityType.PLAYER, "§cAlvo");
    this.target.data().set(NPC.ATTACHED_PLAYER, profile.getName());
    
    // Garantir que o NPC não tenha proteção
    this.target.data().set(NPC.PROTECTED_KEY, false);
    
    this.target.addTrait(new NPCSkinTrait(this.target,
        "eyJ0aW1lc3RhbXAiOjE1ODY5MzIzODcyNTQsInByb2ZpbGVJZCI6ImJjNzAxYzk1NTViNzQ5YmJhNDdkZWEzZTlmZDgwMDFkIiwicHJvZmlsZU5hbWUiOiIweDQ1MiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk5NDc3OWM2ODBlMzhiNzU3ZGUyMTZkYmQ0OTVlZjljYmFmYzg3MjllYTlhYjAyNTNkMTNlZDU5N2Y1MTQ0MiJ9fX0=",
        "ppEIxcszLINYQw4BeE0jftmrBpvKgt5MUGRm21QYJBil60QWagrMZlovFv/0+sHwJJ7eJHLNjhvthY+i41vNljVY3/O21DbTXygyGkHkhAAyihCBroy5IxV87vXsylZNGZ5gfd7t3OLxpyOI6TUizWT10XzsrmhYo3hCZETkdVhIll+T6ENZxCHB/8Sl1mUhWko4hnf8pgmoS0rsixXM9WbccGFO00pNYYZ2Jai133QE0slMkbyCf/7t4Q2ATzjXGx+8avSu3LpHxSff2tUDWH54qanQ5x6eey9Rc846V24TUiy3kiKWmTchGiegZ6kAj3sO1wO/ohvQySzbeAsnz94rGdwzjpUF3pWgH5mJ6vHEstsH1hoYUBKwFAxaKhp2iI7CzgHOO+BMVbIF1Fm66OuDJ3+4am2mvCGRsGG0ufPxHM6O3TZHpkw3rUGWbx13KQLSFWvLzZjzl/EIcO8Kt6XTKgl8qciCG9nFM97EkCjpNHMoedKnphwFVu/K1O3hGz6QxI4/8PdsYZnLOlvPlG1nHDzlkDGjDtkLXTgWKHVTNFD7R10jXNbWoJyt712D2c7otceOOpu1s70JRbHMKKxMZtSMOt3MQMuCw6LJJEdbtyC4d8D6mE8lM1HZmMa3tcb7cBFHryy1eEwMStJh7A2O3GP6SPssgCI2TTtoLEs="));
    this.target.spawn(LOCATIONS[0]);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    
    // Limpar a hotbar do jogador
    this.player.getInventory().clear();
    profile.setHotbar(null);
    
    // Dar uma espada de ferro no slot 1 (indestrutível)
    ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
    
    // Adicionar enchantment de durabilidade infinita
    ironSword.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 10);
    
    // Definir metadados para tornar a espada indestrutível
    org.bukkit.metadata.FixedMetadataValue metadata = new org.bukkit.metadata.FixedMetadataValue(Main.getInstance(), "indestructible");
    ironSword.setItemMeta(ironSword.getItemMeta());
    
    this.player.getInventory().setItem(0, ironSword);
    this.player.updateInventory();
    
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    // Teleportar jogador para a área de preview
    this.player.teleport(LOCATIONS[1]);
    
    // Enviar mensagem informativa
    this.player.sendMessage("§aVocê entrou no preview do efeito de abate!");
    this.player.sendMessage("§7Use a espada de ferro para atacar o NPC alvo.");
    
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
  
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
    // Verificar se o jogador está atacando o NPC alvo
    if (evt.getDamager().equals(this.player) && evt.getEntity().equals(this.target.getEntity())) {
      // Debug: enviar mensagem para o jogador
      this.player.sendMessage("§aVocê atingiu o NPC alvo! Executando efeito de abate...");
      
      // Cancelar o evento original e aplicar nosso próprio dano
      evt.setCancelled(true);
      
      // Forçar a morte do NPC
      this.target.getEntity().remove();
      
      // Executar o efeito de abate imediatamente
      this.cosmetic.execute(this.player, this.target.getCurrentLocation());
      
      // Iniciar monitoramento da animação
      startAnimationMonitoring();
    } else if (evt.getDamager().equals(this.player) && NPCLibrary.isNPC(evt.getEntity())) {
      // Verificação adicional para qualquer NPC
      this.player.sendMessage("§eDebug: Você atingiu um NPC, mas não é o alvo correto.");
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerItemDamage(org.bukkit.event.player.PlayerItemDamageEvent evt) {
    // Prevenir que a espada se desgaste
    if (evt.getPlayer().equals(this.player) && evt.getItem().getType() == Material.IRON_SWORD) {
      evt.setCancelled(true);
    }
  }
  
  private void startAnimationMonitoring() {
    // Cancelar qualquer monitoramento anterior
    if (this.animationMonitorTask != null) {
      this.animationMonitorTask.cancel();
    }
    
    // Determinar o tempo máximo da animação baseado no tipo de efeito
    int maxAnimationTicks = getMaxAnimationTicks();
    
    this.player.sendMessage("§7Aguardando o fim da animação... (máximo " + (maxAnimationTicks / 20) + " segundos)");
    
    // Aguardar exatamente o tempo da animação
    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (!this.animationFinished) {
        this.animationFinished = true;
        this.player.sendMessage("§aAnimação do efeito finalizada! Retornando ao lobby...");
        this.returnToLobby();
        this.stop();
      }
    }, maxAnimationTicks + 20L); // +20 ticks (1 segundo) para garantir que a animação terminou
  }
  
  private int getMaxAnimationTicks() {
    // Determinar o tempo máximo baseado no nome do efeito
    String effectName = this.cosmetic.getName().toLowerCase();
    
    if (effectName.contains("portal")) {
      return 150; // Portal3D: 150 ticks = 7.5 segundos
    } else if (effectName.contains("crystal")) {
      return 130; // Crystal3D: 130 ticks = 6.5 segundos
    } else if (effectName.contains("tornado")) {
      return 160; // Tornado3D: 160 ticks = 8 segundos
    } else if (effectName.contains("explosive") || effectName.contains("sphere")) {
      return 120; // ExplosiveSphere3D: 120 ticks = 6 segundos
    } else if (effectName.contains("temporal") || effectName.contains("pyramid")) {
      return 140; // TemporalPyramid3D: 140 ticks = 7 segundos
    } else {
      // Tempo padrão para efeitos desconhecidos
      return 150; // 7.5 segundos
    }
  }
  
  public void stop() {
    // Cancelar o monitoramento da animação
    if (this.animationMonitorTask != null) {
      this.animationMonitorTask.cancel();
      this.animationMonitorTask = null;
    }
    
    this.oldLocation = null;
    if (this.target != null) {
      this.target.destroy();
      this.target = null;
    }
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void returnToMenu() {
    // Este método não é mais usado, mantido para compatibilidade
    this.returnToLobby();
  }
  
  private void returnToLobby() {
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
      
      // Enviar mensagem de confirmação
      this.player.sendMessage("§aPreview finalizado! Você foi retornado ao lobby.");
    }
  }
  
  @Override
  public void run() {
    // Este método é necessário para BukkitRunnable, mas não é usado neste preview
  }
}
