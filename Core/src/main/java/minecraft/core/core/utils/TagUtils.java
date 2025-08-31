package minecraft.core.core.utils;

import minecraft.core.core.player.nick.NickManager;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.Medal;
import minecraft.core.core.utils.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Utilitário para gerenciar tags e times de jogadores.
 * Permite aplicar e remover tags personalizadas.
 * 
 * @author Luiz
 * @version 1.0
 */
public class TagUtils {
      private static final Map<String, NickTeam> TEAMS = new HashMap<>();
  private static final Map<String, NickTeam> CACHED_NICK_TEAMS = new HashMap<>();

    /**
     * Aplica uma tag personalizada a um jogador.
     * 
     * @param player Jogador que receberá a tag
     * @param prefix Prefixo da tag
     * @param suffix Sufixo da tag
     * @param priority Prioridade da tag
     */
    public static void setTag(Player player, String prefix, String suffix, int priority) {
        if (NickManager.isNick(player.getName())) {
            Rank nickRole = NickManager.getRole(player.getName());
            setTag(player.getName(), nickRole.getPrefix(), "", nickRole.getId());
        }
        
        // Verificar se há tag selecionada no cache primeiro
        String currentTag = TagCache.isPresent(player.getName()) ? TagCache.get(player.getName()) : null;
        Rank role;
        String medalSuffix = "";
        
        // Pegar a medalha selecionada
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            String selectedMedal = profile.getDataContainer("account", "medalha").getAsString();
            if (selectedMedal != null && !selectedMedal.isEmpty()) {
                Medal medal = Medal.getMedalByName(selectedMedal);
                if (medal != null) {
                    medalSuffix = medal.getSuffix();
                }
            }
        }
        
        if (currentTag != null) {
            // Se tem no cache, usar a tag do cache
            role = Rank.getRoleByName(currentTag.split(" : ")[0]);
        } else {
            // IMPORTANTE: Verificar APENAS a tag selecionada (coluna tag)
            // NUNCA usar rank ou permissões para determinar a tag visual
            if (profile != null) {
                String selectedTag = profile.getDataContainer("account", "tag").getAsString();
                if (selectedTag != null && !selectedTag.isEmpty()) {
                    role = Rank.getRoleByName(selectedTag);
                    // IMPORTANTE: NÃO verificar permissões - tags são apenas visuais
                } else {
                    // Se não tem tag selecionada, usar rank padrão (Membro) apenas para aparência
                    role = Rank.getLastRole();
                }
            } else {
                // Se não tem profile, usar rank padrão (Membro) apenas para aparência
                role = Rank.getLastRole();
            }
        }
        
        if (role == null) {
            role = Rank.getLastRole(); // Fallback para Membro (apenas visual)
        }
        
        // Aplicar APENAS a tag visual (sem afetar permissões ou coluna rank)
        setTag(player.getName(), role.getPrefix(), medalSuffix, role.getId());
    }

    public static void setMedal(Player player, Medal medal) {
        // IMPORTANTE: Este método é APENAS para aparência visual
        // NUNCA deve interferir na coluna rank ou permissões
        
        Profile profile = Profile.getProfile(player.getName());
        String prefix = "";
        String suffix = medal.getSuffix();

        if (profile != null) {
            // IMPORTANTE: Pegar APENAS a tag selecionada (coluna tag)
            // NUNCA usar rank ou permissões para determinar a tag visual
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            if (selectedTag != null && !selectedTag.isEmpty()) {
                Rank role = Rank.getRoleByName(selectedTag);
                if (role != null) {
                    prefix = role.getPrefix();
                }
            } else {
                // Se não tem tag selecionada, usar rank padrão (Membro) apenas para aparência
                Rank defaultRank = Rank.getLastRole();
                if (defaultRank != null) {
                    prefix = defaultRank.getPrefix();
                }
            }
        }
        
        // Aplicar APENAS a medalha visual (sem afetar permissões ou coluna rank)
        setTag(player.getName(), prefix, suffix, -1);
    }

    public static void setTag(Player player, Rank role) {
        // IMPORTANTE: Este método é APENAS para aparência visual
        // NUNCA deve interferir na coluna rank ou permissões
        
        // Preservar a medalha selecionada
        String suffix = "";
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            String selectedMedal = profile.getDataContainer("account", "medalha").getAsString();
            if (selectedMedal != null && !selectedMedal.isEmpty()) {
                Medal medal = Medal.getMedalByName(selectedMedal);
                if (medal != null) {
                    suffix = medal.getSuffix();
                }
            }
        }
        
        // Aplicar APENAS a tag visual (sem afetar permissões ou coluna rank)
        setTag(player.getName(), role.getPrefix(), suffix, role.getId());
    }

    public static void setTag(String player, String prefix, String suffix) {
        setTag(player, prefix, suffix, -1);
    }

    public static void setTag(String player, String prefix, String suffix, int sortPriority) {
        addPlayerToTeam(player, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
    }

    public static void sendTeams(Player player) {
                for (NickTeam nickTeam : TEAMS.values()) {
          (new Wrapper(nickTeam.getName(), nickTeam.getPrefix(), nickTeam.getSuffix(), 0, nickTeam.getMembers())).send(player);
        }
    }

    public static void clearAllTeams() {
                for (NickTeam nickTeam : TEAMS.values()) {
          (new Wrapper(nickTeam.getName(), nickTeam.getPrefix(), nickTeam.getSuffix(), 0, nickTeam.getMembers())).send();
        }
        
                for (NickTeam nickTeam : TEAMS.values()) {
          removePlayerFromTeamPackets(nickTeam, nickTeam.getMembers());
          removeTeamPackets(nickTeam);
        }
        
        TEAMS.clear();
        CACHED_NICK_TEAMS.clear();
    }

    public static NickTeam reset(String player) {
        return reset(player, decache(player));
    }

    private static NickTeam decache(String player) {
        return CACHED_NICK_TEAMS.remove(player);
    }

    public static NickTeam getNickTeam(String player) {
        return CACHED_NICK_TEAMS.get(player);
    }

    private static void cache(String player, NickTeam nickTeam) {
        CACHED_NICK_TEAMS.put(player, nickTeam);
    }

    private static NickTeam reset(String player, NickTeam nickTeam) {
        if (nickTeam != null && nickTeam.getMembers().remove(player)) {
            Player removing = Bukkit.getPlayerExact(player);
            boolean delete;
            if (removing != null) {
                delete = removePlayerFromTeamPackets(nickTeam, removing.getName());
            } else {
                OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
                delete = removePlayerFromTeamPackets(nickTeam, toRemoveOffline.getName());
            }

            if (delete) {
                removeTeamPackets(nickTeam);
                TEAMS.remove(nickTeam.getName());
            }
        }

        return nickTeam;
    }

    private static void addPlayerToTeam(String player, String prefix, String suffix, int sortPriority) {
        reset(player);
        NickTeam joining = getTeam(prefix, suffix);
        if (joining != null) {
            joining.addMember(player);
        } else {
            joining = new NickTeam(prefix, suffix, getNameFromInput(sortPriority));
            joining.addMember(player);
            TEAMS.put(joining.getName(), joining);
            addTeamPackets(joining);
        }

        Player adding = Bukkit.getPlayerExact(player);
        if (adding != null) {
            addPlayerToTeamPackets(joining, adding.getName());
            cache(adding.getName(), joining);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
            addPlayerToTeamPackets(joining, offlinePlayer.getName());
            cache(offlinePlayer.getName(), joining);
        }
    }

    public static Rank getTag(Player player) {
        NickTeam nickTeam = CACHED_NICK_TEAMS.get(player.getName());
        if (nickTeam != null) {
            String prefix = nickTeam.getPrefix();

            for (Rank role : Rank.listRoles()) {
                if (role.getPrefix().equals(prefix)) {
                    return role;
                }
            }
        }

        return null;
    }

    private static NickTeam getTeam(String prefix, String suffix) {
        Iterator<NickTeam> var2 = TEAMS.values().iterator();

        NickTeam team;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            team = (NickTeam)var2.next();
        } while(!team.isSimilar(prefix, suffix));

        return team;
    }

    private static String getNameFromInput(int input) {
        return input < 0 ? "" : String.valueOf((char)(input + 65));
    }

    private static void removeTeamPackets(NickTeam nickTeam) {
        (new Wrapper(nickTeam.getName(), nickTeam.getPrefix(), nickTeam.getSuffix(), 1, new ArrayList<>())).send();
    }

    private static boolean removePlayerFromTeamPackets(NickTeam nickTeam, String... players) {
        return removePlayerFromTeamPackets(nickTeam, Arrays.asList(players));
    }

    private static boolean removePlayerFromTeamPackets(NickTeam nickTeam, List<String> players) {
        (new Wrapper(nickTeam.getName(), 4, players)).send();
        nickTeam.getMembers().removeAll(players);
        return nickTeam.getMembers().isEmpty();
    }

    private static void addTeamPackets(NickTeam nickTeam) {
        (new Wrapper(nickTeam.getName(), nickTeam.getPrefix(), nickTeam.getSuffix(), 0, nickTeam.getMembers())).send();
    }

    private static void addPlayerToTeamPackets(NickTeam nickTeam, String player) {
        (new Wrapper(nickTeam.getName(), 3, Collections.singletonList(player))).send();
    }

    public static void setTag(Player player) {
        // IMPORTANTE: Este método é APENAS para aparência visual
        // NUNCA deve interferir na coluna rank ou permissões
        
        Profile profile = Profile.getProfile(player.getName());
        Rank role = null;
        String medalSuffix = "";

        // Obter medalha selecionada (se houver)
        if (profile != null) {
            String selectedMedal = profile.getDataContainer("account", "medalha").getAsString();
            if (selectedMedal != null && !selectedMedal.isEmpty()) {
                Medal medal = Medal.getMedalByName(selectedMedal);
                if (medal != null) {
                    medalSuffix = medal.getSuffix();
                }
            }
        }
        
        // IMPORTANTE: Verificar APENAS a tag selecionada (coluna tag)
        // NUNCA usar rank ou permissões para determinar a tag visual
        if (profile != null) {
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            if (selectedTag != null && !selectedTag.isEmpty()) {
                role = Rank.getRoleByName(selectedTag);
            }
        }
        
        // Se não tem tag selecionada, usar rank padrão (Membro) apenas para aparência
        if (role == null) {
            role = Rank.getLastRole(); // Fallback para Membro (apenas visual)
        }
        
        // Aplicar APENAS a tag visual (sem afetar permissões ou coluna rank)
        setTag(player.getName(), role.getPrefix(), medalSuffix, role.getId());
    }
}
