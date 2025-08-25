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

public class EnergyCube3D extends KillEffect {
  
  public EnergyCube3D(ConfigurationSection section) {
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
          if (tick >= 100) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do cubo (0-30 ticks)
          if (tick <= 30) {
            double size = (tick / 30.0) * 3.0;
            drawCube3D(location, size, tick, false);
            
            // Som de energia se acumulando
            if (tick % 8 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.0f, 0.8f + (tick * 0.02f));
            }
          }
          
          // Fase 2: Cubo rotativo (31-70 ticks)
          else if (tick <= 70) {
            double rotationX = (tick - 31) * 6;
            double rotationY = (tick - 31) * 4;
            double rotationZ = (tick - 31) * 8;
            drawRotatingCube3D(location, 3.0, rotationX, rotationY, rotationZ, tick);
            
            // Partículas de energia nos vértices
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 10 + i * 45)) * 1.0;
              Location vertexLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0, 0.3f, 0.1f, 5, vertexLoc, player);
                ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 3, vertexLoc, player);
              }
            }
            
            // Som de cubo ativo
            if (tick % 15 == 0) {
              location.getWorld().playSound(location, Sound.NOTE_PLING, 1.2f, 1.0f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Explosão do cubo (71-100 ticks)
          else {
            if (tick == 71) {
              // Explosão inicial
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, player);
                ParticleEffect.FIREWORKS_SPARK.display(1.0f, 0, 1.0f, 0.2f, 40, location, player);
              }
              location.getWorld().playSound(location, Sound.EXPLODE, 2.0f, 0.8f);
            }
            
            // Ondas de energia se expandindo
            double expansionRadius = (tick - 71) * 0.4;
            for (int i = 0; i < 12; i++) {
              double angle = i * 30 + (tick * 5);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 30)) * 0.5;
              Location waveLoc = location.clone().add(x, y, z);
              
              for (Player player : Bukkit.getOnlinePlayers()) {
                ParticleEffect.FIREWORKS_SPARK.display(0.4f, 0, 0.4f, 0.1f, 4, waveLoc, player);
                ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 2, waveLoc, player);
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
          if (tick >= 100) {
            cancel();
            return;
          }
          
          // Fase 1: Formação do cubo (0-30 ticks)
          if (tick <= 30) {
            double size = (tick / 30.0) * 3.0;
            drawCube3D(location, size, tick, true);
            
            // Som de energia se acumulando
            if (tick % 8 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.0f, 0.8f + (tick * 0.02f));
            }
          }
          
          // Fase 2: Cubo rotativo (31-70 ticks)
          else if (tick <= 70) {
            double rotationX = (tick - 31) * 6;
            double rotationY = (tick - 31) * 4;
            double rotationZ = (tick - 31) * 8;
            drawRotatingCube3D(location, 3.0, rotationX, rotationY, rotationZ, tick);
            
            // Partículas de energia nos vértices
            for (int i = 0; i < 8; i++) {
              double angle = i * 45 + (tick * 3);
              double radius = 2.0;
              double x = Math.cos(Math.toRadians(angle)) * radius;
              double z = Math.sin(Math.toRadians(angle)) * radius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 10 + i * 45)) * 1.0;
              Location vertexLoc = location.clone().add(x, y, z);
              
              ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0, 0.3f, 0.1f, 5, vertexLoc, viewer);
              ParticleEffect.ENCHANTMENT_TABLE.display(0.2f, 0, 0.2f, 0.05f, 3, vertexLoc, viewer);
            }
            
            // Som de cubo ativo
            if (tick % 15 == 0) {
              viewer.playSound(location, Sound.NOTE_PLING, 1.2f, 1.0f + (random.nextFloat() * 0.3f));
            }
          }
          
          // Fase 3: Explosão do cubo (71-100 ticks)
          else {
            if (tick == 71) {
              // Explosão inicial
              ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, location, viewer);
              ParticleEffect.FIREWORKS_SPARK.display(1.0f, 0, 1.0f, 0.2f, 40, location, viewer);
              viewer.playSound(location, Sound.EXPLODE, 2.0f, 0.8f);
            }
            
            // Ondas de energia se expandindo
            double expansionRadius = (tick - 71) * 0.4;
            for (int i = 0; i < 12; i++) {
              double angle = i * 30 + (tick * 5);
              double x = Math.cos(Math.toRadians(angle)) * expansionRadius;
              double z = Math.sin(Math.toRadians(angle)) * expansionRadius;
              double y = 1.0 + Math.sin(Math.toRadians(tick * 15 + i * 30)) * 0.5;
              Location waveLoc = location.clone().add(x, y, z);
              
              ParticleEffect.FIREWORKS_SPARK.display(0.4f, 0, 0.4f, 0.1f, 4, waveLoc, viewer);
              ParticleEffect.SMOKE_NORMAL.display(0.3f, 0, 0.3f, 0.05f, 2, waveLoc, viewer);
            }
          }
          
          tick++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
  }
  
  private void drawCube3D(Location center, double size, int tick, boolean singleViewer) {
    // Desenha as 12 arestas do cubo
    double halfSize = size / 2.0;
    
    // Arestas horizontais (base)
    drawLine3D(center.clone().add(-halfSize, -halfSize, -halfSize), center.clone().add(halfSize, -halfSize, -halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(-halfSize, -halfSize, halfSize), center.clone().add(halfSize, -halfSize, halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(-halfSize, -halfSize, -halfSize), center.clone().add(-halfSize, -halfSize, halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(halfSize, -halfSize, -halfSize), center.clone().add(halfSize, -halfSize, halfSize), tick, singleViewer);
    
    // Arestas horizontais (topo)
    drawLine3D(center.clone().add(-halfSize, halfSize, -halfSize), center.clone().add(halfSize, halfSize, -halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(-halfSize, halfSize, halfSize), center.clone().add(halfSize, halfSize, halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(-halfSize, halfSize, -halfSize), center.clone().add(-halfSize, halfSize, halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(halfSize, halfSize, -halfSize), center.clone().add(halfSize, halfSize, halfSize), tick, singleViewer);
    
    // Arestas verticais
    drawLine3D(center.clone().add(-halfSize, -halfSize, -halfSize), center.clone().add(-halfSize, halfSize, -halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(halfSize, -halfSize, -halfSize), center.clone().add(halfSize, halfSize, -halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(-halfSize, -halfSize, halfSize), center.clone().add(-halfSize, halfSize, halfSize), tick, singleViewer);
    drawLine3D(center.clone().add(halfSize, -halfSize, halfSize), center.clone().add(halfSize, halfSize, halfSize), tick, singleViewer);
  }
  
  private void drawRotatingCube3D(Location center, double size, double rotX, double rotY, double rotZ, int tick) {
    double halfSize = size / 2.0;
    
    // Vértices do cubo
    double[][] vertices = {
      {-halfSize, -halfSize, -halfSize},
      {halfSize, -halfSize, -halfSize},
      {halfSize, -halfSize, halfSize},
      {-halfSize, -halfSize, halfSize},
      {-halfSize, halfSize, -halfSize},
      {halfSize, halfSize, -halfSize},
      {halfSize, halfSize, halfSize},
      {-halfSize, halfSize, halfSize}
    };
    
    // Rotaciona os vértices
    double[][] rotatedVertices = new double[8][3];
    for (int i = 0; i < 8; i++) {
      rotatedVertices[i] = rotatePoint3D(vertices[i], rotX, rotY, rotZ);
    }
    
    // Desenha as arestas do cubo rotacionado
    int[][] edges = {
      {0, 1}, {1, 2}, {2, 3}, {3, 0}, // base
      {4, 5}, {5, 6}, {6, 7}, {7, 4}, // topo
      {0, 4}, {1, 5}, {2, 6}, {3, 7}  // verticais
    };
    
    for (int[] edge : edges) {
      Location start = center.clone().add(rotatedVertices[edge[0]][0], rotatedVertices[edge[0]][1], rotatedVertices[edge[0]][2]);
      Location end = center.clone().add(rotatedVertices[edge[1]][0], rotatedVertices[edge[1]][1], rotatedVertices[edge[1]][2]);
      drawLine3D(start, end, tick, false);
    }
  }
  
  private double[] rotatePoint3D(double[] point, double rotX, double rotY, double rotZ) {
    double x = point[0];
    double y = point[1];
    double z = point[2];
    
    // Rotação em X
    double cosX = Math.cos(Math.toRadians(rotX));
    double sinX = Math.sin(Math.toRadians(rotX));
    double newY = y * cosX - z * sinX;
    double newZ = y * sinX + z * cosX;
    y = newY;
    z = newZ;
    
    // Rotação em Y
    double cosY = Math.cos(Math.toRadians(rotY));
    double sinY = Math.sin(Math.toRadians(rotY));
    double newX = x * cosY + z * sinY;
    newZ = -x * sinY + z * cosY;
    x = newX;
    z = newZ;
    
    // Rotação em Z
    double cosZ = Math.cos(Math.toRadians(rotZ));
    double sinZ = Math.sin(Math.toRadians(rotZ));
    newX = x * cosZ - y * sinZ;
    newY = x * sinZ + y * cosZ;
    x = newX;
    y = newY;
    
    return new double[]{x, y, z};
  }
  
  private void drawLine3D(Location start, Location end, int tick, boolean singleViewer) {
    double distance = start.distance(end);
    int points = (int) (distance * 4);
    
    for (int i = 0; i <= points; i++) {
      double t = (double) i / points;
      Location point = start.clone().add(
        (end.getX() - start.getX()) * t,
        (end.getY() - start.getY()) * t,
        (end.getZ() - start.getZ()) * t
      );
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, point, player);
        ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 0, 1, point, player);
      }
    }
  }
} 