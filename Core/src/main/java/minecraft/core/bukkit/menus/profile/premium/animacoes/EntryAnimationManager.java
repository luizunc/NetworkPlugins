package minecraft.core.bukkit.menus.profile.premium.animacoes;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Gerenciador de animações de chegada dos jogadores.
 * 
 * @author Luiz
 * @version 1.0
 */
public class EntryAnimationManager {
  
            private static final Random random = new Random();
            private static final Map<UUID, BukkitRunnable> activeAnimations = new HashMap<>();
            private static final Map<UUID, List<Wither>> playerWithers = new HashMap<>();
  
  /**
   * Cancela a animação ativa de um jogador quando ele sai do servidor.
   * 
   * @param player Jogador que saiu
   */
  public static void cancelPlayerAnimation(Player player) {
    UUID playerId = player.getUniqueId();
    BukkitRunnable activeAnimation = activeAnimations.get(playerId);
    
    if (activeAnimation != null) {
      activeAnimation.cancel();
      activeAnimations.remove(playerId);
    }
    
    // Remove os Withers do jogador se existirem
    List<Wither> withers = playerWithers.get(playerId);
    if (withers != null) {
      for (Wither wither : withers) {
        if (wither.isValid()) {
          wither.remove();
        }
      }
      playerWithers.remove(playerId);
    }
  }
  
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
    BukkitRunnable animation = new BukkitRunnable() {
      int tick = 0;
      
      @Override
      public void run() {
        if (tick >= 60) { // 3 segundos
          cancel();
          activeAnimations.remove(player.getUniqueId());
          return;
        }
        
        // Obtém a posição atual do jogador
        Location playerLoc = player.getLocation();
        
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
    };
    
    activeAnimations.put(player.getUniqueId(), animation);
    animation.runTaskTimer(Core.getInstance(), 0L, 1L);
  }
  
           /**
   * Animação Chefão - 2 Withers circulando o jogador (apenas visual).
   * 
   * @param player Jogador
   */
  private static void playChefaoAnimation(Player player) {
    List<Wither> withers = new ArrayList<>();
    UUID playerId = player.getUniqueId();
    
    BukkitRunnable animation = new BukkitRunnable() {
        int tick = 0;
        double angle = 0;
        
        @Override
        public void run() {
                     if (tick >= 120) { // 6 segundos
             // Remove os Withers
             for (Wither wither : withers) {
               if (wither.isValid()) {
                 wither.remove();
               }
             }
             cancel();
             activeAnimations.remove(player.getUniqueId());
             playerWithers.remove(playerId);
             return;
           }
          
          // Obtém a posição atual do jogador
          Location playerLoc = player.getLocation();
          
          // Spawn dos Withers se ainda não existem
          if (withers.isEmpty()) {
            for (int i = 0; i < 2; i++) {
              double spawnAngle = (i * Math.PI);
              double x = playerLoc.getX() + Math.cos(spawnAngle) * 5;
              double z = playerLoc.getZ() + Math.sin(spawnAngle) * 5;
              
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
             
             // Armazena os Withers no mapa global
             playerWithers.put(playerId, withers);
           }
          
          // Faz os Withers circularem ao redor da posição atual do jogador
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
      };
      
      activeAnimations.put(player.getUniqueId(), animation);
      animation.runTaskTimer(Core.getInstance(), 0L, 1L);
    }
  
                                                                                                 /**
   * Animação Estrela - Tapete vermelho (apenas visual).
   * 
   * @param player Jogador
   */
  private static void playEstrelaAnimation(Player player) {
    Location playerLoc = player.getLocation();
    
    // Cria partículas de tapete vermelho
    BukkitRunnable animation = new BukkitRunnable() {
           int tick = 0;
           
           @Override
           public void run() {
                         if (tick >= 200) { // 10 segundos
              cancel();
              activeAnimations.remove(player.getUniqueId());
              return;
            }
             
             // Cria partículas de tapete vermelho em linha reta
             for (int x = -1; x <= 1; x++) {
               for (int z = 0; z < 10; z++) {
                 // Calcula a posição base
                 Location baseLoc = playerLoc.clone().add(x, 0, z);
                 
                  // Encontra o bloco mais alto naquela posição e coloca meio bloco acima
                  Location groundLoc = baseLoc.getWorld().getHighestBlockAt(baseLoc).getLocation().add(0.5, 0.5, 0.5);
                 
                 try {
                   // Partículas vermelhas para simular tapete (sempre no chão)
                   ParticleEffect.REDSTONE.display(0.3f, 0.3f, 0.3f, 0.05f, 5, groundLoc, 50);
                   ParticleEffect.EXPLOSION_NORMAL.display(0.2f, 0.2f, 0.2f, 0.02f, 3, groundLoc, 50);
                 } catch (Exception e) {
                   // Ignora erros de partículas
                 }
               }
             }
             
             tick++;
                       }
          };
          
          activeAnimations.put(player.getUniqueId(), animation);
          animation.runTaskTimer(Core.getInstance(), 0L, 2L); // A cada 2 ticks
        }
  

  
     
  
  /**
   * Animação Thor - Raios caindo sobre o jogador (apenas um raio com som).
   * 
   * @param player Jogador
   */
  private static void playThorAnimation(Player player) {
    BukkitRunnable animation = new BukkitRunnable() {
      int tick = 0;
      boolean soundPlayed = false; // Controla se o som já foi reproduzido
      
      @Override
      public void run() {
        if (tick >= 40) { // 2 segundos
          cancel();
          activeAnimations.remove(player.getUniqueId());
          return;
        }
        
        // Obtém a posição atual do jogador
        Location playerLoc = player.getLocation();
        Location headLoc = playerLoc.clone().add(0, 1.8, 0); // Posição da cabeça
        
        // Raios aleatórios ao redor do jogador (apenas visuais)
        for (int i = 0; i < 2; i++) {
          double x = playerLoc.getX() + (random.nextDouble() - 0.5) * 3;
          double z = playerLoc.getZ() + (random.nextDouble() - 0.5) * 3;
          
          Location lightningLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY() + 8, z);
          // Apenas efeito visual sem som
          playerLoc.getWorld().strikeLightning(lightningLoc);
        }
        
        // Raios diretos na cabeça do jogador (mais frequentes)
        if (tick % 5 == 0) {
          // Apenas efeito visual sem som
          playerLoc.getWorld().strikeLightning(headLoc);
        }
        
        // Raios extras na cabeça para mais impacto
        if (tick % 12 == 0) {
          // Raio duplo na cabeça (apenas visuais)
          playerLoc.getWorld().strikeLightning(headLoc);
          playerLoc.getWorld().strikeLightning(headLoc.clone().add(0.5, 0, 0.5));
        }
        
        // Apenas um raio com som (no primeiro raio)
        if (tick == 0 && !soundPlayed) {
          playerLoc.getWorld().strikeLightningEffect(headLoc);
          soundPlayed = true;
        }
        
        tick++;
              }
      };
      
      activeAnimations.put(player.getUniqueId(), animation);
      animation.runTaskTimer(Core.getInstance(), 0L, 1L);
    }
  }
