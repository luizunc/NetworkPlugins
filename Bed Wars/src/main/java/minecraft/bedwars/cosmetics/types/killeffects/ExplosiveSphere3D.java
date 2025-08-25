package minecraft.bedwars.cosmetics.types.killeffects;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.types.KillEffect;
import minecraft.core.core.utils.enums.EnumRarity;
import minecraft.core.core.utils.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ExplosiveSphere3D extends KillEffect {
  
  public ExplosiveSphere3D(ConfigurationSection section) {
    super(section.getLong("id"), EnumRarity.fromName(section.getString("rarity")), section.getDouble("coins"), (long) section.getInt("cash"), section.getString("permission"),
        section.getString("name"), section.getString("icon"));
  }
  
  @Override
  public void execute(Player viewer, Location location) {
    if (viewer == null) {
      // Efeito para todos os jogadores
      new BukkitRunnable() {
        int tick = 0;
        Random random = new Random();
        
        @Override
        public void run() {
          if (tick >= 120) {
            cancel();
            return;
          }
          
          // Fase 1: Formação da esfera (0-40 ticks)
          if (tick <= 40) {
            double radius = (tick / 40.0) * 4.0;
            drawSphere3D(location, radius, tick, false);
            
            // Som de energia se acumulando
            if (tick % 10 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.0f, 0.7f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Esfera pulsante (41-80 ticks)
          else if (tick <= 80) {
            double pulseRadius = 4.0 + Math.sin(Math.toRadians(tick * 9)) * 0.5;
            drawSphere3D(location, pulseRadius, tick, false);
            
            // Partículas de energia na superfície
            for (int i = 0; i < 6; i++) {
              double phi = Math.PI * 2 * i / 6;
              double theta = Math.PI * (tick % 20) / 20;
              double x = pulseRadius * Math.sin(theta) * Math.cos(phi);
              double y = pulseRadius * Math.cos(theta);
              double z = pulseRadius * Math.sin(theta) * Math.sin(phi);
              Location surfaceLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.FIREWORKS_SPARK.display(0.4f, 0, 0.4f, 0.1f, 6, surfaceLoc, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 4, surfaceLoc, player);
              }
            }
            
            // Som de esfera ativa
            if (tick % 20 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.3f, 1.0f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Explosão da esfera (81-120 ticks)
          else {
            if (tick == 81) {
              // Explosão inicial
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
                ParticleEffect.FIREWORKS_SPARK.display(1.2f, 0, 1.2f, 0.3f, 50, location, player);
              }
              location.getWorld().playSound(location, Sound.EXPLODE, 2.5f, 0.7f);
            }
            
            // Ondas de choque esféricas
            double explosionRadius = (tick - 81) * 0.5;
            drawExplosionSphere3D(location, explosionRadius, tick);
            
            // Partículas de escombros
            if (tick % 3 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double elevation = random.nextDouble() * Math.PI;
                double x = explosionRadius * Math.sin(elevation) * Math.cos(angle);
                double y = explosionRadius * Math.cos(elevation);
                double z = explosionRadius * Math.sin(elevation) * Math.sin(angle);
                Location debrisLoc = location.clone().add(x, y, z);
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                  ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, debrisLoc, player);
                  ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.03f, 2, debrisLoc, player);
                }
              }
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
      
    } else {
      // Efeito apenas para o viewer
      new BukkitRunnable() {
        int tick = 0;
        Random random = new Random();
        
        @Override
        public void run() {
          if (tick >= 120) {
            cancel();
            return;
          }
          
          // Fase 1: Formação da esfera (0-40 ticks)
          if (tick <= 40) {
            double radius = (tick / 40.0) * 4.0;
            drawSphere3D(location, radius, tick, true);
            
            // Som de energia se acumulando
            if (tick % 10 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.0f, 0.7f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Esfera pulsante (41-80 ticks)
          else if (tick <= 80) {
            double pulseRadius = 4.0 + Math.sin(Math.toRadians(tick * 9)) * 0.5;
            drawSphere3D(location, pulseRadius, tick, true);
            
            // Partículas de energia na superfície
            for (int i = 0; i < 6; i++) {
              double phi = Math.PI * 2 * i / 6;
              double theta = Math.PI * (tick % 20) / 20;
              double x = pulseRadius * Math.sin(theta) * Math.cos(phi);
              double y = pulseRadius * Math.cos(theta);
              double z = pulseRadius * Math.sin(theta) * Math.sin(phi);
              Location surfaceLoc = location.clone().add(x, y, z);
              
              ParticleEffect.FIREWORKS_SPARK.display(0.4f, 0, 0.4f, 0.1f, 6, surfaceLoc, viewer);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 4, surfaceLoc, viewer);
            }
            
            // Som de esfera ativa
            if (tick % 20 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.3f, 1.0f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Explosão da esfera (81-120 ticks)
          else {
            if (tick == 81) {
              // Explosão inicial
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, viewer);
              ParticleEffect.FIREWORKS_SPARK.display(1.2f, 0, 1.2f, 0.3f, 50, location, viewer);
              viewer.playSound(location, Sound.EXPLODE, 2.5f, 0.7f);
            }
            
            // Ondas de choque esféricas
            double explosionRadius = (tick - 81) * 0.5;
            drawExplosionSphere3D(location, explosionRadius, tick);
            
            // Partículas de escombros
            if (tick % 3 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double elevation = random.nextDouble() * Math.PI;
                double x = explosionRadius * Math.sin(elevation) * Math.cos(angle);
                double y = explosionRadius * Math.cos(elevation);
                double z = explosionRadius * Math.sin(elevation) * Math.sin(angle);
                Location debrisLoc = location.clone().add(x, y, z);
                
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, debrisLoc, viewer);
                ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.03f, 2, debrisLoc, viewer);
              }
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
  }
  
  private void drawSphere3D(Location center, double radius, int tick, boolean singleViewer) {
    // Desenha uma esfera 3D usando coordenadas esféricas
    int rings = 8;
    int pointsPerRing = 12;
    
    for (int ring = 0; ring <= rings; ring++) {
      double theta = Math.PI * ring / rings;
      double y = radius * Math.cos(theta);
      double ringRadius = radius * Math.sin(theta);
      
      for (int point = 0; point < pointsPerRing; point++) {
        double phi = Math.PI * 2 * point / pointsPerRing;
        double x = ringRadius * Math.cos(phi);
        double z = ringRadius * Math.sin(phi);
        
        Location spherePoint = center.clone().add(x, y, z);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
          ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, spherePoint, player);
          ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 0, 1, spherePoint, player);
        }
      }
    }
  }
  
  private void drawExplosionSphere3D(Location center, double radius, int tick) {
    // Desenha uma esfera de explosão com menos densidade
    int rings = 6;
    int pointsPerRing = 8;
    
    for (int ring = 0; ring <= rings; ring++) {
      double theta = Math.PI * ring / rings;
      double y = radius * Math.cos(theta);
      double ringRadius = radius * Math.sin(theta);
      
      for (int point = 0; point < pointsPerRing; point++) {
        double phi = Math.PI * 2 * point / pointsPerRing;
        double x = ringRadius * Math.cos(phi);
        double z = ringRadius * Math.sin(phi);
        
        Location explosionPoint = center.clone().add(x, y, z);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
          ParticleEffect.SMOKE_NORMAL.display(0.2f, 0, 0.2f, 0.03f, 2, explosionPoint, player);
          ParticleEffect.FIREWORKS_SPARK.display(0.1f, 0, 0.1f, 0.02f, 1, explosionPoint, player);
        }
      }
    }
  }
} 