package minecraft.bedwars.cosmetics.types.killeffects;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.types.KillEffect;
import minecraft.core.core.utils.enums.EnumRarity;
import minecraft.core.core.utils.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SpiderWeb3D extends KillEffect {
  
  public SpiderWeb3D(ConfigurationSection section) {
    super(section.getLong("id"), EnumRarity.fromName(section.getString("rarity")), section.getDouble("coins"), (long) section.getInt("cash"), section.getString("permission"),
        section.getString("name"), section.getString("icon"));
  }
  
  @Override
  public void execute(Player viewer, Location location) {
    new BukkitRunnable() {
      int tick = 0;
      Random random = new Random();
      
      @Override
      public void run() {
        if (tick >= 170) {
          cancel();
          return;
        }
        
        // Fase 1: Spider aparecendo (0-20 ticks)
        if (tick <= 20) {
          // Spider descendo do teto
          double descent = (tick / 20.0) * 3.0;
          Location spiderLoc = location.clone().add(0, 4.0 - descent, 0);
          
          // Corpo da spider
          for (int i = 0; i < 8; i++) {
            double angle = i * 45 + (tick * 2);
            double radius = 0.8;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            Location bodyLoc = spiderLoc.clone().add(x, 0, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.08f, 4, bodyLoc, player);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 2, bodyLoc, player);
            }
          }
          
          // Olhos vermelhos da spider
          Location leftEye = spiderLoc.clone().add(-0.3, 0.5, 0.3);
          Location rightEye = spiderLoc.clone().add(0.3, 0.5, 0.3);
          
          for (Player player : Bukkit.getOnlinePlayers()) {
            ParticleEffect.FLAME.display(0.3f, 0, 0.3f, 0.08f, 4, leftEye, player);
            ParticleEffect.FLAME.display(0.3f, 0, 0.3f, 0.08f, 4, rightEye, player);
          }
          
          // Linha de teia
          for (int i = 0; i <= 10; i++) {
            double t = (double) i / 10;
            Location webLine = location.clone().add(0, 4.0 - (t * descent), 0);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.ENCHANTMENT_TABLE.display(0.1f, 0, 0.1f, 0.02f, 1, webLine, player);
            }
          }
          
          if (tick % 8 == 0) {
            location.getWorld().playSound(location, Sound.SPIDER_IDLE, 1.2f, 0.8f);
          }
        }
        
        // Fase 2: Spider tecendo a teia (21-70 ticks)
        else if (tick <= 70) {
          Location spiderLoc = location.clone().add(0, 1.0, 0);
          
          // Spider se movendo
          double movement = Math.sin(Math.toRadians(tick * 3)) * 0.5;
          spiderLoc.add(movement, 0, 0);
          
          // Corpo da spider
          for (int i = 0; i < 10; i++) {
            double angle = i * 36 + (tick * 2);
            double radius = 0.8;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            Location bodyLoc = spiderLoc.clone().add(x, 0, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.SMOKE_NORMAL.display(0.4f, 0, 0.4f, 0.1f, 5, bodyLoc, player);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 3, bodyLoc, player);
            }
          }
          
          // Tecendo a teia
          double webProgress = (tick - 21) / 50.0;
          int webRings = 5;
          for (int ring = 0; ring < webRings; ring++) {
            double ringRadius = (ring + 1) * 1.5 * webProgress;
            int points = 8 + ring * 2;
            
            for (int point = 0; point < points; point++) {
              double angle = (360.0 / points) * point + (tick * 1.5);
              double x = Math.cos(Math.toRadians(angle)) * ringRadius;
              double z = Math.sin(Math.toRadians(angle)) * ringRadius;
              double y = 0.5 + Math.sin(Math.toRadians(tick * 4 + point * 30)) * 0.3;
              Location webPoint = spiderLoc.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 3, webPoint, player);
                if (random.nextBoolean()) {
                  ParticleEffect.SMOKE_NORMAL.display(0.2f, 0, 0.2f, 0.05f, 2, webPoint, player);
                }
              }
            }
          }
          
          // Linhas radiais da teia
          for (int i = 0; i < 8; i++) {
            double angle = i * 45 + (tick * 1.5);
            double radius = 6.0 * webProgress;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = 0.5 + Math.sin(Math.toRadians(tick * 3 + i * 45)) * 0.2;
            Location radialLine = spiderLoc.clone().add(x, y, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 2, radialLine, player);
            }
          }
          
          // Olhos pulsantes
          Location leftEye = spiderLoc.clone().add(-0.3, 0.5, 0.3);
          Location rightEye = spiderLoc.clone().add(0.3, 0.5, 0.3);
          
          for (Player player : Bukkit.getOnlinePlayers()) {
            ParticleEffect.FLAME.display(0.4f, 0, 0.4f, 0.1f, 5, leftEye, player);
            ParticleEffect.FLAME.display(0.4f, 0, 0.4f, 0.1f, 5, rightEye, player);
          }
          
          if (tick % 15 == 0) {
            location.getWorld().playSound(location, Sound.SPIDER_IDLE, 1.3f, 0.7f);
          }
        }
        
        // Fase 3: Ataque com Veneno (71-120 ticks)
        else if (tick <= 120) {
          if (tick == 71) {
            location.getWorld().playSound(location, Sound.SPIDER_IDLE, 2.0f, 0.6f);
          }
          
          Location spiderLoc = location.clone().add(0, 1.0, 0);
          
          // Spider atacando
          double attackProgress = (tick - 71) / 50.0;
          double attackMovement = Math.sin(attackProgress * Math.PI) * 2.0;
          spiderLoc.add(attackMovement, 0, 0);
          
          // Corpo da spider atacando
          for (int i = 0; i < 12; i++) {
            double angle = i * 30 + (tick * 3);
            double radius = 0.8 + Math.sin(attackProgress * Math.PI) * 0.3;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            Location bodyLoc = spiderLoc.clone().add(x, 0, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.SMOKE_NORMAL.display(0.5f, 0, 0.5f, 0.12f, 6, bodyLoc, player);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.1f, 4, bodyLoc, player);
            }
          }
          
          // Veneno sendo lançado
          for (int i = 0; i < 6; i++) {
            double angle = i * 60 + (tick * 2);
            double distance = attackProgress * 5.0;
            double x = Math.cos(Math.toRadians(angle)) * distance;
            double z = Math.sin(Math.toRadians(angle)) * distance;
            double y = 0.5 + Math.sin(attackProgress * Math.PI * 2) * 0.5;
            Location poisonLoc = spiderLoc.clone().add(x, y, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.SPELL_WITCH.display(0.4f, 0, 0.4f, 0.1f, 4, poisonLoc, player);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 3, poisonLoc, player);
            }
          }
          
          // Teia se contraindo
          if (tick % 8 == 0) {
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 2);
              double radius = 4.0 - attackProgress * 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 0.5 + Math.sin(Math.toRadians(tick * 5 + i * 45)) * 0.3;
              Location webLoc = spiderLoc.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.1f, 4, webLoc, player);
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.08f, 3, webLoc, player);
              }
            }
          }
          
          // Olhos intensos
          Location leftEye = spiderLoc.clone().add(-0.3, 0.5, 0.3);
          Location rightEye = spiderLoc.clone().add(0.3, 0.5, 0.3);
          
          for (Player player : Bukkit.getOnlinePlayers()) {
            ParticleEffect.FLAME.display(0.5f, 0, 0.5f, 0.12f, 6, leftEye, player);
            ParticleEffect.FLAME.display(0.5f, 0, 0.5f, 0.12f, 6, rightEye, player);
            ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 3, leftEye, player);
            ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 3, rightEye, player);
          }
        }
        
        // Fase 4: Fatality Final (121-170 ticks)
        else {
          if (tick == 121) {
            // Explosão final
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
              ParticleEffect.SPELL_WITCH.display(1.2f, 0, 1.2f, 0.25f, 50, location, player);
            }
            location.getWorld().playSound(location, Sound.EXPLODE, 2.0f, 0.6f);
          }
          
          // Spider se desintegrando
          double disintegration = (tick - 121) / 50.0;
          Location spiderLoc = location.clone().add(0, 1.0, 0);
          
          // Partículas de desintegração
          for (int i = 0; i < 15; i++) {
            double angle = random.nextDouble() * 360;
            double elevation = random.nextDouble() * Math.PI;
            double radius = disintegration * 5.0;
            double x = radius * Math.sin(elevation) * Math.cos(Math.toRadians(angle));
            double y = radius * Math.cos(elevation);
            double z = radius * Math.sin(elevation) * Math.sin(Math.toRadians(angle));
            Location disintegrateLoc = spiderLoc.clone().add(x, y, z);
            
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.SMOKE_NORMAL.display(0.4f, 0, 0.4f, 0.1f, 4, disintegrateLoc, player);
              ParticleEffect.SPELL_WITCH.display(0.3f, 0, 0.3f, 0.08f, 3, disintegrateLoc, player);
            }
          }
          
          // Teia se expandindo
          if (tick % 8 == 0) {
            for (int i = 0; i < 6; i++) {
              double angle = i * 60 + (tick * 2);
              double x = Math.cos(Math.toRadians(angle)) * (disintegration * 6.0);
              double z = Math.sin(Math.toRadians(angle)) * (disintegration * 6.0);
              double y = 0.5 + Math.sin(Math.toRadians(tick * 6 + i * 60)) * 0.5;
              Location webLoc = spiderLoc.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.1f, 4, webLoc, player);
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.08f, 3, webLoc, player);
              }
            }
          }
          
          // Veneno residual
          if (tick % 5 == 0) {
            for (int i = 0; i < 4; i++) {
              double angle = random.nextDouble() * 360;
              double radius = random.nextDouble() * (disintegration * 4.0);
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = random.nextDouble() * 2.0;
              Location poisonLoc = spiderLoc.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.SPELL_WITCH.display(0.3f, 0, 0.3f, 0.08f, 3, poisonLoc, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 2, poisonLoc, player);
              }
            }
          }
        }
        
        tick++;
      }
    }.runTaskTimer(Main.getInstance(), 0L, 1L);
  }
} 