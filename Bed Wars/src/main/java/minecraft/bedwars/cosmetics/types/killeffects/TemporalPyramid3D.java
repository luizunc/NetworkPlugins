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

public class TemporalPyramid3D extends KillEffect {
  
  public TemporalPyramid3D(ConfigurationSection section) {
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
          if (tick >= 140) {
            cancel();
            return;
          }
          
          // Fase 1: Formação da pirâmide (0-50 ticks)
          if (tick <= 50) {
            double height = (tick / 50.0) * 5.0;
            double baseSize = (tick / 50.0) * 4.0;
            drawPyramid3D(location, baseSize, height, tick, false);
            
            // Som de tempo se acumulando
            if (tick % 12 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.0f, 0.6f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Pirâmide temporal ativa (51-100 ticks)
          else if (tick <= 100) {
            double pulseHeight = 5.0 + Math.sin(Math.toRadians(tick * 6)) * 0.3;
            double pulseBase = 4.0 + Math.sin(Math.toRadians(tick * 8)) * 0.2;
            drawPyramid3D(location, pulseBase, pulseHeight, tick, false);
            
            // Partículas de tempo nos vértices
            for (int i = 0; i < 4; i++) {
              double angle = i * 90 + (tick * 2);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 90)) * 0.5;
              Location vertexLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.PORTAL.display(0.4f, 0, 0.4f, 0.1f, 8, vertexLoc, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 5, vertexLoc, player);
              }
            }
            
            // Efeito de distorção temporal no topo
            Location topLoc = location.clone().add(0, 5.0, 0);
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.PORTAL.display(0.6f, 0, 0.6f, 0.15f, 12, topLoc, player);
              ParticleEffect.SPELL_WITCH.display(0.4f, 0, 0.4f, 0.1f, 6, topLoc, player);
            }
            
            // Som de pirâmide ativa
            if (tick % 25 == 0) {
              location.getWorld().playSound(location, Sound.PORTAL, 1.2f, 0.8f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Colapso temporal (101-140 ticks)
          else {
            if (tick == 101) {
              // Colapso inicial
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
                ParticleEffect.PORTAL.display(1.0f, 0, 1.0f, 0.2f, 30, location, player);
              }
              location.getWorld().playSound(location, Sound.EXPLODE, 2.0f, 0.6f);
            }
            
            // Ondas de tempo se expandindo
            double expansionRadius = (tick - 101) * 0.6;
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 20 + i * 45)) * 0.8;
              Location waveLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.PORTAL.display(0.5f, 0, 0.5f, 0.1f, 6, waveLoc, player);
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, waveLoc, player);
              }
            }
            
            // Partículas de tempo residual
            if (tick % 4 == 0) {
              for (int i = 0; i < 3; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * expansionRadius;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * 3.0;
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
          if (tick >= 140) {
            cancel();
            return;
          }
          
          // Fase 1: Formação da pirâmide (0-50 ticks)
          if (tick <= 50) {
            double height = (tick / 50.0) * 5.0;
            double baseSize = (tick / 50.0) * 4.0;
            drawPyramid3D(location, baseSize, height, tick, true);
            
            // Som de tempo se acumulando
            if (tick % 12 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.0f, 0.6f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Pirâmide temporal ativa (51-100 ticks)
          else if (tick <= 100) {
            double pulseHeight = 5.0 + Math.sin(Math.toRadians(tick * 6)) * 0.3;
            double pulseBase = 4.0 + Math.sin(Math.toRadians(tick * 8)) * 0.2;
            drawPyramid3D(location, pulseBase, pulseHeight, tick, true);
            
            // Partículas de tempo nos vértices
            for (int i = 0; i < 4; i++) {
              double angle = i * 90 + (tick * 2);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 90)) * 0.5;
              Location vertexLoc = location.clone().add(x, y, z);
              
              ParticleEffect.PORTAL.display(0.4f, 0, 0.4f, 0.1f, 8, vertexLoc, viewer);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 5, vertexLoc, viewer);
            }
            
            // Efeito de distorção temporal no topo
            Location topLoc = location.clone().add(0, 5.0, 0);
            ParticleEffect.PORTAL.display(0.6f, 0, 0.6f, 0.15f, 12, topLoc, viewer);
            ParticleEffect.SPELL_WITCH.display(0.4f, 0, 0.4f, 0.1f, 6, topLoc, viewer);
            
            // Som de pirâmide ativa
            if (tick % 25 == 0) {
              viewer.playSound(location, Sound.PORTAL, 1.2f, 0.8f + (random.nextFloat() * 0.4f));
            }
          }
          
          // Fase 3: Colapso temporal (101-140 ticks)
          else {
            if (tick == 101) {
              // Colapso inicial
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, viewer);
              ParticleEffect.PORTAL.display(1.0f, 0, 1.0f, 0.2f, 30, location, viewer);
              viewer.playSound(location, Sound.EXPLODE, 2.0f, 0.6f);
            }
            
            // Ondas de tempo se expandindo
            double expansionRadius = (tick - 101) * 0.6;
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 20 + i * 45)) * 0.8;
              Location waveLoc = location.clone().add(x, y, z);
              
              ParticleEffect.PORTAL.display(0.5f, 0, 0.5f, 0.1f, 6, waveLoc, viewer);
              ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 3, waveLoc, viewer);
            }
            
            // Partículas de tempo residual
            if (tick % 4 == 0) {
              for (int i = 0; i < 3; i++) {
                double angle = random.nextDouble() * 360;
                double radius = random.nextDouble() * expansionRadius;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = random.nextDouble() * 3.0;
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
  
  private void drawPyramid3D(Location center, double baseSize, double height, int tick, boolean singleViewer) {
    // Desenha uma pirâmide 3D
    double halfBase = baseSize / 2.0;
    
    // Base da pirâmide (quadrado)
    drawLine3D(center.clone().add(-halfBase, 0, -halfBase), center.clone().add(halfBase, 0, -halfBase), tick, singleViewer);
    drawLine3D(center.clone().add(-halfBase, 0, halfBase), center.clone().add(halfBase, 0, halfBase), tick, singleViewer);
    drawLine3D(center.clone().add(-halfBase, 0, -halfBase), center.clone().add(-halfBase, 0, halfBase), tick, singleViewer);
    drawLine3D(center.clone().add(halfBase, 0, -halfBase), center.clone().add(halfBase, 0, halfBase), tick, singleViewer);
    
    // Arestas da pirâmide (do topo para os vértices da base)
    Location top = center.clone().add(0, height, 0);
    drawLine3D(top, center.clone().add(-halfBase, 0, -halfBase), tick, singleViewer);
    drawLine3D(top, center.clone().add(halfBase, 0, -halfBase), tick, singleViewer);
    drawLine3D(top, center.clone().add(halfBase, 0, halfBase), tick, singleViewer);
    drawLine3D(top, center.clone().add(-halfBase, 0, halfBase), tick, singleViewer);
    
    // Partículas no topo da pirâmide
    for (Player player : Bukkit.getOnlinePlayers()) {
      ParticleEffect.PORTAL.display(0.3f, 0, 0.3f, 0.08f, 4, top, player);
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