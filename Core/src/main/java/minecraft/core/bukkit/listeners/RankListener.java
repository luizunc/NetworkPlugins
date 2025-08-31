package minecraft.core.bukkit.listeners;

import minecraft.core.core.player.Profile;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.player.rank.RankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener para gerenciar ranks quando jogadores entram e saem do servidor.
 * 
 * @author Luiz
 * @version 1.0
 */
public class RankListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            // Obter o profile do jogador
            Profile profile = Profile.createOrLoadProfile(event.getPlayer().getName());
            
            // Obter a tag selecionada
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            
            if (selectedTag != null && !selectedTag.isEmpty()) {
                // Verificar se a tag selecionada é válida
                Rank rank = Rank.getRoleByName(selectedTag);
                if (rank != null) {
                    // Aplicar o rank (inclui permissões)
                    RankManager.applyRank(event.getPlayer(), rank);
                }
            } else {
                // Se não tem tag selecionada, aplicar o rank mais alto baseado nas permissões
                Rank highestRank = Rank.getRank(event.getPlayer());
                if (highestRank != null) {
                    RankManager.applyRank(event.getPlayer(), highestRank);
                }
            }
            
        } catch (Exception e) {
            // Log do erro, mas não interrompe o login
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Limpar permissões quando o jogador sai
        RankManager.onPlayerQuit(event.getPlayer());
    }
} 