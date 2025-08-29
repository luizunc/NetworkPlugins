package minecraft.core.core.titles;

import minecraft.core.bukkit.Core;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TitleManager {
  
  private static final TitleManager TITLE_MANAGER = new TitleManager();
  private final Map<String, TitleController> controllers = new HashMap<>();
  
  public static void joinLobby(Profile profile) {
    TITLE_MANAGER.onJoinLobby(profile);
  }
  
  public static void leaveLobby(Profile profile) {
    TITLE_MANAGER.onLeaveLobby(profile);
  }
  
  public static void leaveServer(Profile profile) {
    TITLE_MANAGER.onLeaveServer(profile);
  }
  
  public static void show(Profile profile, Profile target) {
    TITLE_MANAGER.onLobbyShow(profile, target);
  }
  
  public static void hide(Profile profile, Profile target) {
    TITLE_MANAGER.onLobbyHide(profile, target);
  }
  
  public static void select(Profile profile, Title title) {
    TITLE_MANAGER.onSelectTitle(profile, title);
  }
  
  public static void deselect(Profile profile) {
    TITLE_MANAGER.onDeselectTitle(profile);
  }
  
  public void onJoinLobby(Profile profile) {
    if (profile.getName() == null) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(controller -> {
        if (controller.getOwner() != null && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      });
      
      // Envia mensagem de broadcast quando o jogador entra no lobby
      sendLobbyJoinBroadcast(profile);
    }
    
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      controller.enable();
      // Atualiza o título com as estatísticas mais recentes
      Title title = profile.getSelectedContainer().getTitle();
      if (title != null) {
        controller.setName(title.getProcessedTitle(profile));
      }
    } else {
      Title title = profile.getSelectedContainer().getTitle();
      if (title != null && !this.controllers.containsKey(profile.getName())) {
        this.onSelectTitle(profile, title);
      }
    }
  }
  
  /**
   * Envia mensagem de broadcast quando o jogador entra no lobby.
   * 
   * @param profile Perfil do jogador
   */
  private void sendLobbyJoinBroadcast(Profile profile) {
    try {
      // Verifica se o perfil tem os dados necessários
      if (profile == null || profile.getName() == null) {
        return;
      }
      
      // Tenta obter a mensagem personalizada, se falhar usa a padrão
      String entryMessageId = "1"; // Mensagem padrão
      try {
        entryMessageId = profile.getDataContainer("account", "entrymessage").getAsString();
        if (entryMessageId == null || entryMessageId.isEmpty() || entryMessageId.equals("0")) {
          entryMessageId = "1"; // Mensagem padrão
        }
      } catch (Exception e) {
        // Se não conseguir obter a mensagem personalizada, usa a padrão
        entryMessageId = "1";
      }
      
      int messageId = Integer.parseInt(entryMessageId);
      String message = getLobbyJoinMessage(messageId);
      
      if (message != null) {
        // Formata a mensagem com o nome do jogador e rank
        String formattedMessage = formatLobbyJoinMessage(profile, message);
        
        // Envia para todos os jogadores online
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.sendMessage(formattedMessage);
        }
      }
    } catch (Exception e) {
      // Se houver qualquer erro, envia mensagem padrão
      try {
        String defaultMessage = formatLobbyJoinMessage(profile, "§6entrou no lobby!");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.sendMessage(defaultMessage);
        }
      } catch (Exception ex) {
        // Se até a mensagem padrão falhar, apenas ignora
        Core.getInstance().getLogger().warning("Erro ao enviar mensagem de lobby para " + profile.getName() + ": " + ex.getMessage());
      }
    }
  }
  
  /**
   * Obtém a mensagem de entrada no lobby pelo ID.
   * 
   * @param messageId ID da mensagem
   * @return Mensagem formatada
   */
  private String getLobbyJoinMessage(int messageId) {
    String[] availableMessages = {
        "§6entrou no lobby!",                    // ID 1 - Padrão
        "§aentrou juntamente com brr brr patapim", // ID 2 - Braintrot
        "§4entrou pronto para batalhar",         // ID 3 - Gladiador
        "§2§kentrou no lobby",                   // ID 4 - Glitch
        "§despalhou doces no lobby",             // ID 5 - Doce
        "§6tudo deles e nada nosso"              // ID 6 - Regresso
    };
    
    if (messageId > 0 && messageId <= availableMessages.length) {
      return availableMessages[messageId - 1];
    }
    return availableMessages[0]; // Retorna mensagem padrão
  }
  
  /**
   * Formata a mensagem de entrada no lobby com o nome e rank do jogador.
   * 
   * @param profile Perfil do jogador
   * @param message Mensagem base
   * @return Mensagem formatada
   */
  private String formatLobbyJoinMessage(Profile profile, String message) {
    try {
      // Usa o sistema de ranks existente para formatar a mensagem
      String prefixedName = Rank.getPrefixed(profile.getName());
      return prefixedName + " " + message;
    } catch (Exception e) {
      // Se houver erro, usa formatação simples
      return "§e" + profile.getName() + " " + message;
    }
  }
  
  public void onLeaveLobby(Profile profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      controller.disable();
    }
    
    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(c -> {
        if (c.getOwner() != null && player.canSee(c.getOwner())) {
          c.hideToPlayer(player);
        }
      });
    }
  }
  
  public void onLeaveServer(Profile profile) {
    TitleController controller = this.controllers.remove(profile.getName());
    if (controller != null) {
      controller.destroy();
    }
  }
  
  public void onLobbyShow(Profile profile, Profile target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
        if (controller.getOwner() != null && player.isOnline() && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      }, 10);
    }
  }
  
  public void onLobbyHide(Profile profile, Profile target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      controller.hideToPlayer(player);
    }
  }
  
  public void onSelectTitle(Profile profile, Title title) {
    TitleController controller = this.getTitleController(profile);
    if (controller == null) {
      controller = new TitleController(profile.getPlayer(), title.getProcessedTitle(profile));
      controller.enable();
      this.controllers.put(profile.getName(), controller);
      return;
    }
    
    controller.setName(title.getProcessedTitle(profile));
  }
  
  public void onDeselectTitle(Profile profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller == null) {
      return;
    }
    
    controller.setName("disabled");
  }
  
  public TitleController getTitleController(Profile profile) {
    return this.controllers.get(profile.getName());
  }
  
  /**
   * Atualiza todos os títulos com as estatísticas mais recentes.
   */
  public static void updateAllTitles() {
    TITLE_MANAGER.controllers.forEach((playerName, controller) -> {
      Profile profile = Profile.getProfile(playerName);
      if (profile != null && profile.getPlayer() != null) {
        Title title = profile.getSelectedContainer().getTitle();
        if (title != null) {
          controller.setName(title.getProcessedTitle(profile));
        }
      }
    });
  }
  
  /**
   * Atualiza o título de um jogador específico.
   * 
   * @param profile Perfil do jogador
   */
  public static void updatePlayerTitle(Profile profile) {
    TITLE_MANAGER.onUpdatePlayerTitle(profile);
  }
  
  /**
   * Atualiza o título de um jogador específico.
   * 
   * @param profile Perfil do jogador
   */
  public void onUpdatePlayerTitle(Profile profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      Title title = profile.getSelectedContainer().getTitle();
      if (title != null) {
        controller.setName(title.getProcessedTitle(profile));
      }
    }
  }
}