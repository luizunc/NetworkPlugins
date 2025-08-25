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

public class Portal3D extends KillEffect {
  
  public Portal3D(ConfigurationSection section) {
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
          if (tick >= 150) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do portal (0-50 ticks)
          if (tick <= 50) {
            double height = (tick / 50.0) * 6.0;
            double width = (tick / 50.0) * 4.0;
            drawPortal3D(location, width, height, tick, false);
            
            // Som de portal se formando
            if (tick % 12 == 0) {
              location.getWorld().playSound(location, Sound.PORTAL_TRIGGER, 1.0f, 0.7f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Portal ativo (51-100 ticks)
          else if (tick <= 100) {
            double pulseHeight = 6.0 + Math.sin(Math.toRadians(tick * 4)) * 0.3;
            double pulseWidth = 4.0 + Math.sin(Math.toRadians(tick * 6)) * 0.2;
            drawPortal3D(location, pulseWidth, pulseHeight, tick, false);
            
            // Partículas de energia no portal
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 2);
              double radius = 2.5;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 8 + i * 45)) * 1.0;
              Location energyLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.PORTAL.display(0.4f, 0, 0.4f, 0.1f, 6, energyLoc, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 4, energyLoc, player);
              }
            }
            
            // Efeito de distorção no centro do portal
            Location centerLoc = location.clone().add(0, 3.0, 0);
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.PORTAL.display(0.6f, 0, 0.6f, 0.15f, 10, centerLoc, player);
              ParticleEffect.SPELL_WITCH.display(0.4f, 0, 0.4f, 0.1f, 6, centerLoc, player);
            }
            
            // Som de portal ativo
            if (tick % 25 == 0) {
              location.getWorld().playSound(location, Sound.PORTAL, 1.2f, 0.8f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Colapso do portal (101-150 ticks)
          else {
            if (tick == 101) {
              // Colapso inicial
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
                ParticleEffect.PORTAL.display(1.2f, 0, 1.2f, 0.25f, 40, location, player);
              }
              location.getWorld().playSound(location, Sound.EXPLODE, 2.0f, 0.6f);
            }
            
            // Ondas de energia se expandindo
            double expansionRadius = (tick - 101) * 0.7;
            for (int i = 0; i < 10; i++) {
              double angle = i * 36 + (tick * 2);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 12 + i * 36)) * 0.8;
              Location waveLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.PORTAL.display(0.5f, 0, 0.5f, 0.1f, 5, waveLoc, player);
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, waveLoc, player);
              }
            }
            
            // Partículas de energia residual
            if (tick % 6 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * expansionRadius;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * 4.0;
                Location residualLoc = location.clone().add(x, y, z);
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                  ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.03f, 2, residualLoc, player);
                  ParticleEffect.SPELL_WITCH.display(0.1f, 0, 0.1f, 0.02f, 1, residualLoc, player);
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
          if (tick >= 150) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do portal (0-50 ticks)
          if (tick <= 50) {
            double height = (tick / 50.0) * 6.0;
            double width = (tick / 50.0) * 4.0;
            drawPortal3D(location, width, height, tick, true);
            
            // Som de portal se formando
            if (tick % 12 == 0) {
              viewer.playSound(location, Sound.PORTAL_TRIGGER, 1.0f, 0.7f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Portal ativo (51-100 ticks)
          else if (tick <= 100) {
            double pulseHeight = 6.0 + Math.sin(Math.toRadians(tick * 4)) * 0.3;
            double pulseWidth = 4.0 + Math.sin(Math.toRadians(tick * 6)) * 0.2;
            drawPortal3D(location, pulseWidth, pulseHeight, tick, true);
            
            // Partículas de energia no portal
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 2);
              double radius = 2.5;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 8 + i * 45)) * 1.0;
              Location energyLoc = location.clone().add(x, y, z);
              
              ParticleEffect.PORTAL.display(0.4f, 0, 0.4f, 0.1f, 6, energyLoc, viewer);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 4, energyLoc, viewer);
            }
            
            // Efeito de distorção no centro do portal
            Location centerLoc = location.clone().add(0, 3.0, 0);
            ParticleEffect.PORTAL.display(0.6f, 0, 0.6f, 0.15f, 10, centerLoc, viewer);
            ParticleEffect.SPELL_WITCH.display(0.4f, 0, 0.4f, 0.1f, 6, centerLoc, viewer);
            
            // Som de portal ativo
            if (tick % 25 == 0) {
              viewer.playSound(location, Sound.PORTAL, 1.2f, 0.8f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Colapso do portal (101-150 ticks)
          else {
            if (tick == 101) {
              // Colapso inicial
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, viewer);
              ParticleEffect.PORTAL.display(1.2f, 0, 1.2f, 0.25f, 40, location, viewer);
              viewer.playSound(location, Sound.EXPLODE, 2.0f, 0.6f);
            }
            
            // Ondas de energia se expandindo
            double expansionRadius = (tick - 101) * 0.7;
            for (int i = 0; i < 10; i++) {
              double angle = i * 36 + (tick * 2);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 12 + i * 36)) * 0.8;
              Location waveLoc = location.clone().add(x, y, z);
              
              ParticleEffect.PORTAL.display(0.5f, 0, 0.5f, 0.1f, 5, waveLoc, viewer);
              ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, waveLoc, viewer);
            }
            
            // Partículas de energia residual
            if (tick % 6 == 0) {
              for (int i = 0; i < 4; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * expansionRadius;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * 4.0;
                Location residualLoc = location.clone().add(x, y, z);
                
                ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.03f, 2, residualLoc, viewer);
                ParticleEffect.SPELL_WITCH.display(0.1f, 0, 0.1f, 0.02f, 1, residualLoc, viewer);
              }
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
  }
  
  private void drawPortal3D(Location center, double width, double height, int tick, boolean singleViewer) {
    // Desenha um portal 3D retangular
    double halfWidth = width / 2.0;
    double halfHeight = height / 2.0;
    
    // Moldura do portal (retângulo)
    // Linhas horizontais
    drawLine3D(center.clone().add(-halfWidth, -halfHeight, 0), center.clone().add(halfWidth, -halfHeight, 0), tick, singleViewer);
    drawLine3D(center.clone().add(-halfWidth, halfHeight, 0), center.clone().add(halfWidth, halfHeight, 0), tick, singleViewer);
    
    // Linhas verticais
    drawLine3D(center.clone().add(-halfWidth, -halfHeight, 0), center.clone().add(-halfWidth, halfHeight, 0), tick, singleViewer);
    drawLine3D(center.clone().add(halfWidth, -halfHeight, 0), center.clone().add(halfWidth, halfHeight, 0), tick, singleViewer);
    
    // Efeito de preenchimento do portal
    for (int i = 0; i < 8; i++) {
      double y = -halfHeight + (i / 7.0) * height;
      for (int j = 0; j < 6; j++) {
        double x = -halfWidth + (j / 5.0) * width;
        Location portalPoint = center.clone().add(x, y, 0);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
          ParticleEffect.PORTAL.display(0, 0, 0, 0, 1, portalPoint, player);
        }
      }
    }
    
    // Partículas nos cantos do portal
    Location[] corners = {
      center.clone().add(-halfWidth, -halfHeight, 0),
      center.clone().add(halfWidth, -halfHeight, 0),
      center.clone().add(halfWidth, halfHeight, 0),
      center.clone().add(-halfWidth, halfHeight, 0)
    };
    
    for (Location corner : corners) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 3, corner, player);
      }
    }
  }
  
  private void drawLine3D(Location start, Location end, int tick, boolean singleViewer) {
    double distance = start.distance(end);
    int points = (int) (distance * 3);
    
    for (int i = 0; i <= points; i++) {
      double t = (double) i / points;
      Location point = start.clone().add(
        (end.getX() - start.getX()) * t,
        (end.getY() - start.getY()) * t,
        (end.getZ() - start.getZ()) * t
      );
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        ParticleEffect.PORTAL.display(0, 0, 0, 0, 1, point, player);
        ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 0, 1, point, player);
      }
    }
  }
} 