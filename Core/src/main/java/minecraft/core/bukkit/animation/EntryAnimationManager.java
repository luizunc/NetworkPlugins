package minecraft.core.bukkit.animation;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gerenciador de animações de chegada dos jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class EntryAnimationManager {
  
           private static final Random random = new Random();
  
  /**
   * Executa a animação de chegada baseada na animação selecionada pelo jogador.
   * 
   * @param player Jogador que entrou
   * @param profile Perfil do jogador
   */
  public static void playEntryAnimation(Player player, Profile profile) {
    String selectedAnimation = profile.getDataContainer("account", "entryanimation").getAsString();
    
    if (selectedAnimation.equals("[]") || selectedAnimation.isEmpty()) {
      return; // Nenhuma animação selecionada
    }
    
    try {
      int animationId = Integer.parseInt(selectedAnimation);
      switch (animationId) {
        case 1:
          playAquamanAnimation(player);
          break;
        case 2:
          playChefaoAnimation(player);
          break;
        case 3:
          playEstrelaAnimation(player);
          break;

        case 4:
          playThorAnimation(player);
          break;
      }
    } catch (NumberFormatException e) {
      // Animação inválida, ignora
    }
  }
  
  /**
   * Animação Aquaman - Partículas de água volumosas.
   * 
   * @param player Jogador
   */
  private static void playAquamanAnimation(Player player) {
    Location playerLoc = player.getLocation();
    
    new BukkitRunnable() {
      int tick = 0;
      
      @Override
      public void run() {
        if (tick >= 60) { // 3 segundos
          cancel();
          return;
        }
        
        // Cria partículas de água em círculo ao redor do jogador
        for (int i = 0; i < 8; i++) {
          double angle = (i * Math.PI * 2) / 8;
          double radius = 3.0 + (tick * 0.1);
          
          double x = playerLoc.getX() + Math.cos(angle) * radius;
          double z = playerLoc.getZ() + Math.sin(angle) * radius;
          
          Location particleLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY(), z);
          
          // Partículas de água
          try {
            ParticleEffect.WATER_SPLASH.display(0.5f, 0.5f, 0.5f, 0.1f, 20, particleLoc, 50);
            ParticleEffect.WATER_BUBBLE.display(0.3f, 0.3f, 0.3f, 0.05f, 15, particleLoc, 50);
          } catch (Exception e) {
            // Ignora erros de partículas
          }
        }
        
        // Partículas no chão
        for (int i = 0; i < 5; i++) {
          double x = playerLoc.getX() + (random.nextDouble() - 0.5) * 6;
          double z = playerLoc.getZ() + (random.nextDouble() - 0.5) * 6;
          Location groundLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY(), z);
          
          try {
            ParticleEffect.WATER_WAKE.display(0.2f, 0.2f, 0.2f, 0.05f, 10, groundLoc, 50);
          } catch (Exception e) {
            // Ignora erros de partículas
          }
        }
        
        tick++;
      }
    }.runTaskTimer(Core.getInstance(), 0L, 1L);
  }
  
     /**
    * Animação Chefão - 2 Withers circulando o jogador (apenas visual).
    * 
    * @param player Jogador
    */
   private static void playChefaoAnimation(Player player) {
     Location playerLoc = player.getLocation();
     List<Wither> withers = new ArrayList<>();
     
     // Spawn dos 2 Withers em localização invisível para outros jogadores
     for (int i = 0; i < 2; i++) {
       double angle = (i * Math.PI);
       double x = playerLoc.getX() + Math.cos(angle) * 5;
       double z = playerLoc.getZ() + Math.sin(angle) * 5;
       
       Location witherLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY() + 2, z);
       Wither wither = (Wither) playerLoc.getWorld().spawnEntity(witherLoc, EntityType.WITHER);
       
       // Configura o Wither para não causar dano
       wither.setCustomName("§cChefão");
       wither.setCustomNameVisible(true);
       // Remove a IA e torna invulnerável usando reflection
       try {
         Object nmsEntity = wither.getClass().getMethod("getHandle").invoke(wither);
         nmsEntity.getClass().getMethod("setInvulnerable", boolean.class).invoke(nmsEntity, true);
         nmsEntity.getClass().getMethod("setAI", boolean.class).invoke(nmsEntity, false);
       } catch (Exception e) {
         // Ignora erros de reflection
       }
       
               // Esconde o Wither de outros jogadores (usando reflection para compatibilidade)
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          if (!onlinePlayer.equals(player)) {
            try {
              // Tenta usar o método hideEntity se disponível
              onlinePlayer.getClass().getMethod("hideEntity", org.bukkit.entity.Entity.class).invoke(onlinePlayer, wither);
            } catch (Exception e) {
              // Se não disponível, apenas ignora
            }
          }
        }
       
       withers.add(wither);
     }
     
     new BukkitRunnable() {
       int tick = 0;
       double angle = 0;
       
       @Override
       public void run() {
         if (tick >= 120) { // 6 segundos
           // Remove os Withers
           for (Wither wither : withers) {
             wither.remove();
           }
           cancel();
           return;
         }
         
         // Faz os Withers circularem
         for (int i = 0; i < withers.size(); i++) {
           Wither wither = withers.get(i);
           if (wither.isValid()) {
             double currentAngle = angle + (i * Math.PI);
             double radius = 5.0;
             
             double x = playerLoc.getX() + Math.cos(currentAngle) * radius;
             double z = playerLoc.getZ() + Math.sin(currentAngle) * radius;
             
             Location newLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY() + 2, z);
             wither.teleport(newLoc);
           }
         }
         
         angle += 0.1;
         tick++;
       }
     }.runTaskTimer(Core.getInstance(), 0L, 1L);
   }
  
           /**
     * Animação Estrela - Tapete vermelho (apenas visual).
     * 
     * @param player Jogador
     */
    private static void playEstrelaAnimation(Player player) {
      Location playerLoc = player.getLocation();
      
      // Cria partículas de tapete vermelho
      new BukkitRunnable() {
        int tick = 0;
        
        @Override
        public void run() {
          if (tick >= 200) { // 10 segundos
            cancel();
            return;
          }
          
          // Cria partículas de tapete vermelho em linha reta
          for (int x = -1; x <= 1; x++) {
            for (int z = 0; z < 10; z++) {
              Location particleLoc = playerLoc.clone().add(x, -0.5, z);
              
                             try {
                 // Partículas vermelhas para simular tapete
                 ParticleEffect.REDSTONE.display(0.3f, 0.3f, 0.3f, 0.05f, 5, particleLoc, 50);
                 ParticleEffect.EXPLOSION_NORMAL.display(0.2f, 0.2f, 0.2f, 0.02f, 3, particleLoc, 50);
               } catch (Exception e) {
                 // Ignora erros de partículas
               }
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Core.getInstance(), 0L, 2L); // A cada 2 ticks
    }
  

  
     
  
  /**
   * Animação Thor - Raios caindo sobre o jogador.
   * 
   * @param player Jogador
   */
  private static void playThorAnimation(Player player) {
    Location playerLoc = player.getLocation();
    
    new BukkitRunnable() {
      int tick = 0;
      
      @Override
      public void run() {
        if (tick >= 40) { // 2 segundos
          cancel();
          return;
        }
        
        // Cria raios aleatórios ao redor do jogador
        for (int i = 0; i < 3; i++) {
          double x = playerLoc.getX() + (random.nextDouble() - 0.5) * 4;
          double z = playerLoc.getZ() + (random.nextDouble() - 0.5) * 4;
          
          Location lightningLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY() + 10, z);
          
          // Efeito de raio
          playerLoc.getWorld().strikeLightningEffect(lightningLoc);
          
          // Partículas de raio
          try {
            ParticleEffect.CRIT_MAGIC.display(0.5f, 0.5f, 0.5f, 0.1f, 30, lightningLoc, 50);
            ParticleEffect.EXPLOSION_LARGE.display(0.3f, 0.3f, 0.3f, 0.05f, 10, lightningLoc, 50);
          } catch (Exception e) {
            // Ignora erros de partículas
          }
        }
        
        // Raios diretos no jogador
        if (tick % 10 == 0) {
          Location directLightning = playerLoc.clone().add(0, 10, 0);
          playerLoc.getWorld().strikeLightningEffect(directLightning);
          
          // Partículas especiais
          try {
            ParticleEffect.FIREWORKS_SPARK.display(1.0f, 1.0f, 1.0f, 0.2f, 50, playerLoc, 50);
          } catch (Exception e) {
            // Ignora erros de partículas
          }
        }
        
        tick++;
      }
    }.runTaskTimer(Core.getInstance(), 0L, 1L);
  }
}
