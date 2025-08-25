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

public class Crystal3D extends KillEffect {
  
  public Crystal3D(ConfigurationSection section) {
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
          if (tick >= 130) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do cristal (0-45 ticks)
          if (tick <= 45) {
            double height = (tick / 45.0) * 6.0;
            double width = (tick / 45.0) * 3.0;
            drawCrystal3D(location, width, height, tick, false);
            
            // Som de cristal se formando
            if (tick % 10 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.0f, 0.8f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Cristal brilhante (46-90 ticks)
          else if (tick <= 90) {
            double pulseHeight = 6.0 + Math.sin(Math.toRadians(tick * 5)) * 0.2;
            double pulseWidth = 3.0 + Math.sin(Math.toRadians(tick * 7)) * 0.1;
            drawCrystal3D(location, pulseWidth, pulseHeight, tick, false);
            
            // Partículas de energia nos vértices do cristal
            for (int i = 0; i < 6; i++) {
              double angle = i * 60 + (tick * 2);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 10 + i * 60)) * 0.8;
              Location vertexLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.1f, 6, vertexLoc, player);
                ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0, 0.3f, 0.08f, 4, vertexLoc, player);
              }
            }
            
            // Efeito de brilho no centro do cristal
            Location centerLoc = location.clone().add(0, 3.0, 0);
            for (Player player : Bukkit.getOnlinePlayers()) {
              ParticleEffect.ENCHANTMENT_TABLE.display(0.5f, 0, 0.5f, 0.12f, 8, centerLoc, player);
              ParticleEffect.SPELL_WITCH.display(0.3f, 0, 0.3f, 0.08f, 5, centerLoc, player);
            }
            
            // Som de cristal ativo
            if (tick % 20 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.3f, 1.0f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Explosão do cristal (91-130 ticks)
          else {
            if (tick == 91) {
              // Explosão inicial
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(1.0f, 0, 1.0f, 0.2f, 35, location, player);
              }
              location.getWorld().playSound(location, Sound.EXPLODE, 2.0f, 0.7f);
            }
            
            // Fragmentos de cristal se expandindo
            double expansionRadius = (tick - 91) * 0.5;
            for (int i = 0; i < 10; i++) {
              double angle = i * 36 + (tick * 2);
              double elevation = random.nextDouble() * Math.PI;
              double x = expansionRadius * Math.sin(elevation) * Math.cos(Math.toRadians(angle));
              double y = expansionRadius * Math.cos(elevation);
              double z = expansionRadius * Math.sin(elevation) * Math.sin(Math.toRadians(angle));
              Location fragmentLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.06f, 3, fragmentLoc, player);
                ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.04f, 2, fragmentLoc, player);
              }
            }
            
            // Ondas de energia cristalina
            if (tick % 8 == 0) {
              for (int i = 0; i < 6; i++) {
                double angle = i * 60 + (tick * 3);
                double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
                double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
                double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 60)) * 0.5;
                Location waveLoc = location.clone().add(x, y, z);
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                  ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.08f, 4, waveLoc, player);
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
          if (tick >= 130) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do cristal (0-45 ticks)
          if (tick <= 45) {
            double height = (tick / 45.0) * 6.0;
            double width = (tick / 45.0) * 3.0;
            drawCrystal3D(location, width, height, tick, true);
            
            // Som de cristal se formando
            if (tick % 10 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.0f, 0.8f + (tick * 0.01f));
            }
          }
          
          // Fase 2: Cristal brilhante (46-90 ticks)
          else if (tick <= 90) {
            double pulseHeight = 6.0 + Math.sin(Math.toRadians(tick * 5)) * 0.2;
            double pulseWidth = 3.0 + Math.sin(Math.toRadians(tick * 7)) * 0.1;
            drawCrystal3D(location, pulseWidth, pulseHeight, tick, true);
            
            // Partículas de energia nos vértices do cristal
            for (int i = 0; i < 6; i++) {
              double angle = i * 60 + (tick * 2);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 10 + i * 60)) * 0.8;
              Location vertexLoc = location.clone().add(x, y, z);
              
              ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.1f, 6, vertexLoc, viewer);
              ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0, 0.3f, 0.08f, 4, vertexLoc, viewer);
            }
            
            // Efeito de brilho no centro do cristal
            Location centerLoc = location.clone().add(0, 3.0, 0);
            ParticleEffect.ENCHANTMENT_TABLE.display(0.5f, 0, 0.5f, 0.12f, 8, centerLoc, viewer);
            ParticleEffect.SPELL_WITCH.display(0.3f, 0, 0.3f, 0.08f, 5, centerLoc, viewer);
            
            // Som de cristal ativo
            if (tick % 20 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.3f, 1.0f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Explosão do cristal (91-130 ticks)
          else {
            if (tick == 91) {
              // Explosão inicial
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, viewer);
              ParticleEffect.ENCHANTMENT_TABLE.display(1.0f, 0, 1.0f, 0.2f, 35, location, viewer);
              viewer.playSound(location, Sound.EXPLODE, 2.0f, 0.7f);
            }
            
            // Fragmentos de cristal se expandindo
            double expansionRadius = (tick - 91) * 0.5;
            for (int i = 0; i < 10; i++) {
              double angle = i * 36 + (tick * 2);
              double elevation = random.nextDouble() * Math.PI;
              double x = expansionRadius * Math.sin(elevation) * Math.cos(Math.toRadians(angle));
              double y = expansionRadius * Math.cos(elevation);
              double z = expansionRadius * Math.sin(elevation) * Math.sin(Math.toRadians(angle));
              Location fragmentLoc = location.clone().add(x, y, z);
              
              ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.06f, 3, fragmentLoc, viewer);
              ParticleEffect.FIREWORKS_SPARK.display(0.2f, 0, 0.2f, 0.04f, 2, fragmentLoc, viewer);
            }
            
            // Ondas de energia cristalina
            if (tick % 8 == 0) {
              for (int i = 0; i < 6; i++) {
                double angle = i * 60 + (tick * 3);
                double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
                double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
                double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 60)) * 0.5;
                Location waveLoc = location.clone().add(x, y, z);
                
                ParticleEffect.ENCHANTMENT_TABLE.display(0.4f, 0, 0.4f, 0.08f, 4, waveLoc, viewer);
              }
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
  }
  
  private void drawCrystal3D(Location center, double width, double height, int tick, boolean singleViewer) {
    // Desenha um cristal 3D hexagonal
    double halfWidth = width / 2.0;
    
    // Base do cristal (hexágono)
    for (int i = 0; i < 6; i++) {
      double angle1 = i * 60;
      double angle2 = (i + 1) * 60;
      double x1 = Math.cos(Math.toRadians(angle1)) * halfWidth;
      double z1 = Math.sin(Math.toRadians(angle1)) * halfWidth;
      double x2 = Math.cos(Math.toRadians(angle2)) * halfWidth;
      double z2 = Math.sin(Math.toRadians(angle2)) * halfWidth;
      
      Location start = center.clone().add(x1, 0, z1);
      Location end = center.clone().add(x2, 0, z2);
      drawLine3D(start, end, tick, singleViewer);
    }
    
    // Arestas do cristal (do centro do topo para os vértices da base)
    Location top = center.clone().add(0, height, 0);
    for (int i = 0; i < 6; i++) {
      double angle = i * 60;
      double x = Math.cos(Math.toRadians(angle)) * halfWidth;
      double z = Math.sin(Math.toRadians(angle)) * halfWidth;
      Location vertex = center.clone().add(x, 0, z);
      drawLine3D(top, vertex, tick, singleViewer);
    }
    
    // Arestas internas do cristal (do centro para o topo)
    Location centerTop = center.clone().add(0, height * 0.7, 0);
    drawLine3D(center, centerTop, tick, singleViewer);
    
    // Partículas no topo do cristal
    for (Player player : Bukkit.getOnlinePlayers()) {
      ParticleEffect.ENCHANTMENT_TABLE.display(0.3f, 0, 0.3f, 0.08f, 4, top, player);
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
        ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 0, 1, point, player);
        ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, point, player);
      }
    }
  }
} 