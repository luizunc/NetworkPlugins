package minecraft.core.core.player.rank;

import minecraft.core.Manager;
import minecraft.core.core.database.Database;
import minecraft.core.core.database.cache.TagCache;
import minecraft.core.core.player.Profile;
import minecraft.core.core.player.enums.Medal;
import minecraft.core.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Rank {

    private final int id;
    private final String name;
    private final String prefix;
    private final String permission;

    private final boolean alwaysVisible;

    public Rank(String name, String prefix, String permission, boolean alwaysVisible) {
        this.id = ROLES.size();
        this.name = StringUtils.formatColors(name);
        this.prefix = StringUtils.formatColors(prefix);
        this.permission = permission;
        this.alwaysVisible = alwaysVisible;

        ROLES.add(this);
    }

    public static void loadRanks() {
        new Rank("§4Admin", "§4§lADMIN §4", "rank.admin", false);
        new Rank("§5Mod+", "§5§lMOD+§5 ", "rank.mod+", false);
        new Rank("§5Mod", "§5§lMOD§5 ", "rank.mod", false);
        new Rank("§9Helper", "§9§lHELPER§9 ", "rank.helper", false);
        new Rank("§2Builder", "§2§lBUILDER§2 ", "rank.builder", false);
        new Rank("§1Beta", "§1§lBETA§1 ", "rank.beta", false);
        new Rank("§3Partner+", "§3§lPARTNER+§3 ", "rank.partner+", false);
        new Rank("§bPartner", "§b§lPARTNER§b ", "rank.partner", false);
        new Rank("§cNatal", "§c§lNATAL§c ", "tag.natal", false);
        new Rank("§6Halloween", "§6§lHALLOWEEN§6 ", "tag.halloween", false);
        new Rank("§aFérias", "§a§lFÉRIAS§a ", "tag.ferias", false);
        new Rank("§6Carnaval", "§6§lCARNAVAL§6 ", "tag.carnaval", false);
        new Rank("§2Emerald", "§2§lEMERALD §2", "rank.emerald", false);
        new Rank("§6Gold", "§6§lGOLD §6", "rank.gold", false);
        new Rank("§fIron", "§f§lIRON §f", "rank.iron", false);
        new Rank("§9Apoiador", "§9§lAPOIADOR §9", "rank.apoiador", false);
        new Rank("§7Membro", "§7", "", false);
    }

    public boolean isDefault() {
        return this.permission.isEmpty();
    }

    public boolean has(Object player) {
        return this.isDefault() || Manager.hasPermission(player, this.permission);
    }
    private static final List<Rank> ROLES = new ArrayList<>();
    public static String getPrefixed(String name) {
        return getPrefixed(name, false);
    }
    public static String getColored(String name) {
        return getColored(name, false);
    }
    public static String getPrefixed(String name, boolean removeNick) {
        return getTaggedName(name, false, removeNick);
    }
    public static String getColored(String name, boolean removeNick) {
        return getTaggedName(name, true, removeNick);
    }

    static String getTaggedName(String name, boolean onlyColor, boolean removeNick) {
        String prefix = "§7";

        if (!removeNick && Manager.isNick(name)) {
            prefix = Manager.getNickRole(name).getPrefix();
            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
            }

            return prefix + name;
        }

        Object target = Manager.getPlayer(name);
        if (target != null) {
            // Verificar cache primeiro (como no aCore)
            String currentTag = TagCache.isPresent(Manager.getName(target)) ? TagCache.get(Manager.getName(target)) : null;
            if (currentTag != null) {
                Rank role = getRoleByName(currentTag.split(" : ")[0]);
                if (role != null) {
                    prefix = role.getPrefix();
                }
            } else {
                // Se não tem no cache, verificar profile
                Profile profile = Profile.getProfile(name);
                if (profile != null) {
                    String selectedTag = profile.getDataContainer("account", "tag").getAsString();
                    if (selectedTag != null && !selectedTag.isEmpty()) {
                        Rank role = getRoleByName(selectedTag);
                        if (role != null && role.has(target)) {
                            prefix = role.getPrefix();
                        } else {
                            // Se a tag selecionada não é válida, usar o rank mais alto
                            Rank highestRank = getRank(target, removeNick);
                            if (highestRank != null) {
                                prefix = highestRank.getPrefix();
                            }
                        }
                    } else {
                        // Se não tem tag selecionada, usar o rank mais alto
                        Rank highestRank = getRank(target, removeNick);
                        if (highestRank != null) {
                            prefix = highestRank.getPrefix();
                        }
                    }
                } else {
                    // Se não tem profile, usar o rank mais alto
                    Rank highestRank = getRank(target, removeNick);
                    if (highestRank != null) {
                        prefix = highestRank.getPrefix();
                    }
                }
            }
            
            // Adicionar medalha se houver
            String suffix = "";
            Profile profile = Profile.getProfile(name);
            if (profile != null) {
                String selectedMedal = profile.getDataContainer("account", "medalha").getAsString();
                if (selectedMedal != null && !selectedMedal.isEmpty()) {
                    Medal medal = Medal.getMedalByName(selectedMedal);
                    if (medal != null) {
                        suffix = medal.getSuffix();
                    }
                }
            }
            
            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
                // Para onlyColor, não mostrar a medalha (só a cor)
                return prefix + name;
            }
            return prefix + name + suffix;
        }

        // Para jogadores offline, verificar cache primeiro (como no aCore)
        String rs = TagCache.isPresent(name) ? TagCache.get(name) : Database.getInstance().getTagAndName(name);
        if (rs != null) {
            String[] parts = rs.split(" : ");
            if (parts.length >= 2) {
                Rank role = getRoleByName(parts[0]);
                if (role != null) {
                    prefix = role.getPrefix();
                }
                name = parts[1];
            }
            if (onlyColor) {
                prefix = StringUtils.getLastColor(prefix);
            }
            if (!removeNick && Manager.isNick(name)) {
                name = Manager.getNick(name);
            }
            
            // Adicionar medalha se houver (para jogadores offline, não temos como verificar)
            // A medalha será aplicada quando o jogador entrar online
            return prefix + name;
        }
        
        // Se não tem tag selecionada, usar o rank padrão
        if (onlyColor) {
            prefix = StringUtils.getLastColor(prefix);
        }
        return prefix + name;
    }

    public static Rank getRoleByName(String name) {
        for (Rank role : ROLES) {
            if (StringUtils.stripColors(role.getName()).equalsIgnoreCase(name)) {
                return role;
            }
        }

        return getLastRole();
    }

    public static Rank getRoleByPermission(String permission) {
        for (Rank role : ROLES) {
            if (role.getPermission().equals(permission)) {
                return role;
            }
        }

        return null;
    }

    public static Rank getRank(Object player) {
        return getRank(player, false);
    }
    public static Rank getRank(Object player, boolean removeNick) {
        if (!removeNick && Manager.isNick(Manager.getName(player))) {
            return Manager.getNickRole(Manager.getName(player));
        }

        // Verificar cache primeiro (como no aCore)
        String currentTag = TagCache.isPresent(Manager.getName(player)) ? TagCache.get(Manager.getName(player)) : null;
        if (currentTag != null) {
            return getRoleByName(currentTag.split(" : ")[0]);
        }

        // Para rank, sempre retorna a tag mais alta baseada nas permissões
        for (Rank role : ROLES) {
            if (role.has(player)) {
                return role;
            }
        }

        return getLastRole();
    }

    public static Rank getLastRole() {
        if (ROLES.isEmpty()) return null;
        return ROLES.get(ROLES.size() - 1);
    }

    public static List<Rank> listRoles() {
        return ROLES;
    }
    
    // Métodos adicionais para compatibilidade com o Core
    public static Rank getRankByName(String name) {
        return getRoleByName(name);
    }
    
    public static Rank getPlayerRank(Object player) {
        return getRank(player, false);
    }
    
    public static Rank getPlayerRank(Object player, boolean removeNick) {
        return getRank(player, removeNick);
    }
    
    public static Rank getLastRank() {
        return getLastRole();
    }
    
    public static List<Rank> listRanks() {
        return listRoles();
    }
    
    /**
     * Obtém a tag selecionada pelo jogador (para chat/scoreboard)
     */
    public static Rank getSelectedTag(Object player) {
        if (Manager.isNick(Manager.getName(player))) {
            return Manager.getNickRole(Manager.getName(player));
        }

        // Verificar cache primeiro (como no aCore)
        String currentTag = TagCache.isPresent(Manager.getName(player)) ? TagCache.get(Manager.getName(player)) : null;
        if (currentTag != null) {
            Rank role = getRoleByName(currentTag.split(" : ")[0]);
            if (role != null) {
                return role;
            }
        }

        // Buscar a tag selecionada diretamente do Profile
        Profile profile = Profile.getProfile(Manager.getName(player));
        if (profile != null) {
            String selectedTag = profile.getDataContainer("account", "tag").getAsString();
            if (selectedTag != null && !selectedTag.isEmpty()) {
                Rank role = getRoleByName(selectedTag);
                if (role != null) {
                    return role;
                }
            }
        }

        // Se não tem tag selecionada, retorna o rank mais alto
        return getRank(player, true);
    }
    
    // Getters
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public boolean isAlwaysVisible() {
        return this.alwaysVisible;
    }
    
    public boolean isBroadcast() {
        return this.alwaysVisible;
    }
}
