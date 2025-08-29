package minecraft.core.core.utils;

import minecraft.core.core.player.fake.FakeManager;
import minecraft.core.core.player.rank.Rank;
import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.Medal;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings("deprecation")
public class TagUtils {
    private static final Map<String, FakeTeam> TEAMS = new HashMap<>();
    private static final Map<String, FakeTeam> CACHED_FAKE_TEAMS = new HashMap<>();

    public static void setTag(Player player) {
        if (FakeManager.isFake(player.getName())) {
            Rank fakeRole = FakeManager.getRole(player.getName());
            setTag(player.getName(), fakeRole.getPrefix(), "", fakeRole.getId());
            return;
        }
        
        // Verificar se há tag selecionada no cache primeiro
        String currentTag = TagCache.isPresent(player.getName()) ? TagCache.get(player.getName()) : null;
        Rank role;
        String suffix = "";
        
        // Pegar a medalha selecionada
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
        
        if (currentTag != null) {
            // Se tem no cache, usar a tag do cache
            role = Rank.getRoleByName(currentTag.split(" : ")[0]);
        } else {
            // Se não tem no cache, verificar a tag selecionada no profile
            if (profile != null) {
                String selectedTag = profile.getDataContainer("account", "tag").getAsString();
                if (selectedTag != null && !selectedTag.isEmpty()) {
                    role = Rank.getRoleByName(selectedTag);
                    // Verificar se o jogador tem permissão para a tag
                    if (role != null && !role.has(player)) {
                        // Se não tem permissão, usar o rank mais alto
                        role = Rank.getRank(player);
                    }
                } else {
                    // Se não tem tag selecionada, usar o rank mais alto
                    role = Rank.getRank(player);
                }
            } else {
                // Se não tem profile, usar o rank mais alto
                role = Rank.getRank(player);
            }
        }
        
        if (role == null) {
            role = Rank.getLastRole(); // Fallback para Membro
        }
        
        setTag(player.getName(), role.getPrefix(), suffix, role.getId());
    }

    public static void setMedal(Player player, Medal medal) {
        Profile profile = Profile.getProfile(player.getName());
        String prefix = "";
        String suffix = medal.getSuffix();

        if (profile != null) {
            // Pegar a tag selecionada
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            if (selectedTag != null && !selectedTag.isEmpty()) {
                Rank role = Rank.getRoleByName(selectedTag);
                if (role != null) {
                    prefix = role.getPrefix();
                }
            } else {
                // Se não tem tag selecionada, usar o rank mais alto
                Rank highestRank = Rank.getRank(player);
                if (highestRank != null) {
                    prefix = highestRank.getPrefix();
                }
            }
        }

        setTag(player.getName(), prefix, suffix, -1);
    }

    public static void setTag(Player player, Rank role) {
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
        setTag(player.getName(), role.getPrefix(), suffix, role.getId());
    }

    public static void setTag(String player, String prefix, String suffix) {
        setTag(player, prefix, suffix, -1);
    }

    public static void setTag(String player, String prefix, String suffix, int sortPriority) {
        addPlayerToTeam(player, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
    }

    public static void sendTeams(Player player) {
        for (FakeTeam fakeTeam : TEAMS.values()) {
            (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers())).send(player);
        }
    }

    public static void reset() {
        for (FakeTeam fakeTeam : TEAMS.values()) {
            removePlayerFromTeamPackets(fakeTeam, fakeTeam.getMembers());
            removeTeamPackets(fakeTeam);
        }

        CACHED_FAKE_TEAMS.clear();
        TEAMS.clear();
    }

    public static FakeTeam reset(String player) {
        return reset(player, decache(player));
    }

    private static FakeTeam decache(String player) {
        return CACHED_FAKE_TEAMS.remove(player);
    }

    public static FakeTeam getFakeTeam(String player) {
        return CACHED_FAKE_TEAMS.get(player);
    }

    private static void cache(String player, FakeTeam fakeTeam) {
        CACHED_FAKE_TEAMS.put(player, fakeTeam);
    }

    private static FakeTeam reset(String player, FakeTeam fakeTeam) {
        if (fakeTeam != null && fakeTeam.getMembers().remove(player)) {
            Player removing = Bukkit.getPlayerExact(player);
            boolean delete;
            if (removing != null) {
                delete = removePlayerFromTeamPackets(fakeTeam, removing.getName());
            } else {
                OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
                delete = removePlayerFromTeamPackets(fakeTeam, toRemoveOffline.getName());
            }

            if (delete) {
                removeTeamPackets(fakeTeam);
                TEAMS.remove(fakeTeam.getName());
            }
        }

        return fakeTeam;
    }

    private static void addPlayerToTeam(String player, String prefix, String suffix, int sortPriority) {
        reset(player);
        FakeTeam joining = getTeam(prefix, suffix);
        if (joining != null) {
            joining.addMember(player);
        } else {
            joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
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
        FakeTeam fakeTeam = CACHED_FAKE_TEAMS.get(player.getName());
        if (fakeTeam != null) {
            String prefix = fakeTeam.getPrefix();

            for (Rank role : Rank.listRoles()) {
                if (role.getPrefix().equals(prefix)) {
                    return role;
                }
            }
        }

        return null;
    }

    private static FakeTeam getTeam(String prefix, String suffix) {
        Iterator<FakeTeam> var2 = TEAMS.values().iterator();

        FakeTeam team;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            team = (FakeTeam)var2.next();
        } while(!team.isSimilar(prefix, suffix));

        return team;
    }

    private static String getNameFromInput(int input) {
        return input < 0 ? "" : String.valueOf((char)(input + 65));
    }

    private static void removeTeamPackets(FakeTeam fakeTeam) {
        (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 1, new ArrayList<>())).send();
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, String... players) {
        return removePlayerFromTeamPackets(fakeTeam, Arrays.asList(players));
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, List<String> players) {
        (new Wrapper(fakeTeam.getName(), 4, players)).send();
        fakeTeam.getMembers().removeAll(players);
        return fakeTeam.getMembers().isEmpty();
    }

    private static void addTeamPackets(FakeTeam fakeTeam) {
        (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers())).send();
    }

    private static void addPlayerToTeamPackets(FakeTeam fakeTeam, String player) {
        (new Wrapper(fakeTeam.getName(), 3, Collections.singletonList(player))).send();
    }
}
