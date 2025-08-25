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

public class Tornado3D extends KillEffect {
  
  public Tornado3D(ConfigurationSection section) {
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
          if (tick >= 160) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do tornado (0-60 ticks)
          if (tick <= 60) {
            double height = (tick / 60.0) * 8.0;
            double radius = (tick / 60.0) * 2.5;
            drawTornado3D(location, radius, height, tick, false);
            
            // Som de vento se formando
            if (tick % 15 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.0f, 0.5f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Tornado ativo (61-120 ticks)
          else if (tick <= 120) {
            double pulseHeight = 8.0 + Math.sin(Math.toRadians(tick * 4)) * 0.5;
            double pulseRadius = 2.5 + Math.sin(Math.toRadians(tick * 6)) * 0.3;
            drawTornado3D(location, pulseRadius, pulseHeight, tick, false);
            
            // Partículas de detritos no tornado
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double heightOffset = (tick % 20) * 0.4;
              double radius = 1.5 + Math.sin(Math.toRadians(tick * 8 + i * 45)) * 0.5;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = heightOffset + Math.sin(Math.toRadians(tick * 12 + i * 45)) * 0.3;
              Location debrisLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.08f, 4, debrisLoc, player);
                ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.05f, 3, debrisLoc, player);
              }
            }
            
            // Efeito de vento na base
            for (int i = 0; i < 6; i++) {
              double angle = i * 60 + (tick * 2);
              double radius = 3.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              Location windLoc = location.clone().add(x, 0.5, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.SMOKE_NORMAL.display(0.4f, 0, 0.4f, 0.1f, 5, windLoc, player);
              }
            }
            
            // Som de tornado ativo
            if (tick % 30 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.4f, 0.6f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Dissipação do tornado (121-160 ticks)
          else {
            double dissipationHeight = 8.0 * (1.0 - ((tick - 121) / 40.0));
            double dissipationRadius = 2.5 * (1.0 - ((tick - 121) / 40.0));
            drawTornado3D(location, dissipationRadius, dissipationHeight, tick, false);
            
            // Partículas de dissipação
            if (tick % 5 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * 4.0;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * dissipationHeight;
                Location dissipateLoc = location.clone().add(x, y, z);
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                  ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, dissipateLoc, player);
                  ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.03f, 2, dissipateLoc, player);
                }
              }
            }
            
            // Som final de dissipação
            if (tick == 121) {
              location.getWorld().playSound(location, Sound.EXPLODE, 1.5f, 0.5f);
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
          if (tick >= 160) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do tornado (0-60 ticks)
          if (tick <= 60) {
            double height = (tick / 60.0) * 8.0;
            double radius = (tick / 60.0) * 2.5;
            drawTornado3D(location, radius, height, tick, true);
            
            // Som de vento se formando
            if (tick % 15 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.0f, 0.5f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Tornado ativo (61-120 ticks)
          else if (tick <= 120) {
            double pulseHeight = 8.0 + Math.sin(Math.toRadians(tick * 4)) * 0.5;
            double pulseRadius = 2.5 + Math.sin(Math.toRadians(tick * 6)) * 0.3;
            drawTornado3D(location, pulseRadius, pulseHeight, tick, true);
            
            // Partículas de detritos no tornado
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double heightOffset = (tick % 20) * 0.4;
              double radius = 1.5 + Math.sin(Math.toRadians(tick * 8 + i * 45)) * 0.5;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = heightOffset + Math.sin(Math.toRadians(tick * 12 + i * 45)) * 0.3;
              Location debrisLoc = location.clone().add(x, y, z);
              
              ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.08f, 4, debrisLoc, viewer);
              ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.05f, 3, debrisLoc, viewer);
            }
            
            // Efeito de vento na base
            for (int i = 0; i < 6; i++) {
              double angle = i * 60 + (tick * 2);
              double radius = 3.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              Location windLoc = location.clone().add(x, 0.5, z);
              
              ParticleEffect.SMOKE_NORMAL.display(0.4f, 0, 0.4f, 0.1f, 5, windLoc, viewer);
            }
            
            // Som de tornado ativo
            if (tick % 30 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.4f, 0.6f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Dissipação do tornado (121-160 ticks)
          else {
            double dissipationHeight = 8.0 * (1.0 - ((tick - 121) / 40.0));
            double dissipationRadius = 2.5 * (1.0 - ((tick - 121) / 40.0));
            drawTornado3D(location, dissipationRadius, dissipationHeight, tick, true);
            
            // Partículas de dissipação
            if (tick % 5 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * 4.0;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * dissipationHeight;
                Location dissipateLoc = location.clone().add(x, y, z);
                
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, dissipateLoc, viewer);
                ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.03f, 2, dissipateLoc, viewer);
              }
            }
            
            // Som final de dissipação
            if (tick == 121) {
              viewer.playSound(location, Sound.EXPLODE, 1.5f, 0.5f);
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
  }
  
  private void drawTornado3D(Location center, double radius, double height, int tick, boolean singleViewer) {
    // Desenha um tornado 3D usando hélices espirais
    int rings = 16;
    int pointsPerRing = 8;
    
    for (int ring = 0; ring <= rings; ring++) {
      double ringHeight = (ring / (double) rings) * height;
      double ringRadius = radius * (1.0 - (ring / (double) rings) * 0.3); // Tornado mais fino no topo
      double rotation = tick * 2 + ring * 20; // Rotação progressiva
      
      for (int point = 0; point < pointsPerRing; point++) {
        double angle = (360.0 / pointsPerRing) * point + rotation;
        double x = Math.cos(Math.toRadians(angle)) * ringRadius;
        double z = Math.sin(Math.toRadians(angle)) * ringRadius;
        double y = ringHeight;
        
        Location tornadoPoint = center.clone().add(x, y, z);
        
                    for (Player player : Bukkit.getOnlinePlayers()) {
        ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0, 1, tornadoPoint, player);
        ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, tornadoPoint, player);
      }
      }
    }
    
    // Linha central do tornado
    for (int i = 0; i <= 20; i++) {
      double y = (i / 20.0) * height;
      Location centerLine = center.clone().add(0, y, 0);
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        ParticleEffect.SMOKE_NORMAL.display(0.1f, 0, 0.1f, 0.02f, 2, centerLine, player);
      }
    }
  }
} 