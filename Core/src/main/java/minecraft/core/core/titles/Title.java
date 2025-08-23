package minecraft.core.core.titles;

import me.clip.placeholderapi.PlaceholderAPI;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Title {

    private static final List<Title> TITLES = new ArrayList<>();
    private final String id;
    private final String icon;
    private final String title;

    public Title(String id, String title, String desc) {
        this.id = id;
        this.icon = "%material%:%durability% : 1 : esconder>tudo : nome>%name% : desc>&fTítulo: " + title + "\n \n" + desc + "\n \n%action%";
        this.title = title;
    }

    public static void setupTitles() {
        //STATS MINIGAMES
        TITLES.add(new Title("killsw", "§7Kills no Sky Wars: &b%Core_SkyWars_kills%", "&7\n&8Pode ser desbloqueado através do\n&8Desafio \"Assassino das Pontes\"&8."));
        TITLES.add(new Title("winsw", "§7Wins no Sky Wars: &b%Core_SkyWars_wins%", "&7\n&8Pode ser desbloqueado através do\n&8Desafio \"Glorioso sobre Pontes\"&8."));
        //TITLES.add(new Title("kstreaksw", "§bKillStreak Sky Wars", "&7KillStreak no SkyWars: &b%skywars_killstreak%\n&8Pode ser desbloqueado através do\n&8Desafio \"Maestria em Pontuação\"&8."));
        //TITLES.add(new Title("wstreaksw", "§bWinstreak Sky Wars", "&7WinStreak no SkyWars: &b%Core_SkyWars_solokills%\n&8Pode ser desbloqueado através do\n&8Desafio \"Freddy Krueger\"&8."));

        TITLES.add(new Title("killbw", "§7Kills no Bed Wars: &b%Core_BedWars_kills%", "&7\n&8Pode ser desbloqueado através do\n&8Desafio \"Assasino a espreita\"&8."));
        TITLES.add(new Title("winsbw", "§7Wins no Bed Wars: &b%Core_BedWars_wins%", "&7\n&8Pode ser desbloqueado através do\n&8Desafio \"Protetor de Camas\"&8."));
        //TITLES.add(new Title("kstreakbw", "§bKillStreak Bed Wars", "&7KillStreak no BedWars: &b%bedwars_killstreak%\n&8Pode ser desbloqueado através do\n&8Desafio \"Maestria em Pontuação\"&8."));
        //TITLES.add(new Title("wstreakbw", "§bWinstreak Bed Wars", "&7WinStreak no BedWars: &b%bedwars_winstreak%\n&8Pode ser desbloqueado através do\n&8Desafio \"Freddy Krueger\"&8."));

    }

    public static Title getById(String id) {
        return TITLES.stream().filter(title -> title.getId().equals(id)).findFirst().orElse(null);
    }

    public static Collection<Title> listTitles() {
        return TITLES;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * Obtém o título processado com as estatísticas do jogador.
     *
     * @param profile Perfil do jogador
     * @return Título com placeholders processados
     */
    public String getProcessedTitle(Profile profile) {
        String processedTitle = this.title;

        // Processa placeholders específicas
        if (this.id.equals("killsw")) {
            long skywarsKills = profile.getStats("skywars", "solokills", "duokills", "rankedkills");
            processedTitle = processedTitle.replace("%Core_SkyWars_kills%", StringUtils.formatNumber(skywarsKills));
        } else if (this.id.equals("winsw")) {
            long skywarsWins = profile.getStats("skywars", "solowins", "duowins", "rankedwins");
            processedTitle = processedTitle.replace("%Core_SkyWars_wins%", StringUtils.formatNumber(skywarsWins));
        } else if (this.id.equals("killbw")) {
            long bedwarsKills = profile.getStats("bedwars", "solokills", "duokills", "quadkills");
            processedTitle = processedTitle.replace("%Core_BedWars_kills%", StringUtils.formatNumber(bedwarsKills));
        } else if (this.id.equals("winsbw")) {
            long bedwarsWins = profile.getStats("bedwars", "solowins", "duowins", "quadwins");
            processedTitle = processedTitle.replace("%Core_BedWars_wins%", StringUtils.formatNumber(bedwarsWins));
        }

        // Processa placeholders do PlaceholderAPI
        if (profile.getPlayer() != null) {
            processedTitle = PlaceholderAPI.setPlaceholders(profile.getPlayer(), processedTitle);
        }

        return processedTitle;
    }

    public void give(Profile profile) {
        // Se o jogador tem rank Iron ou superior, dá o título automaticamente
        if (hasIronOrHigherRank(profile) && !profile.getTitlesContainer().has(this)) {
            profile.getTitlesContainer().add(this);
        } else if (!this.has(profile)) {
            // Para outros casos, verifica se já possui
            profile.getTitlesContainer().add(this);
        }
    }

    public boolean has(Profile profile) {
        // Se o jogador tem rank Iron ou superior, possui todos os títulos
        if (hasIronOrHigherRank(profile)) {
            return true;
        }

        // Caso contrário, verifica se possui o título normalmente
        return profile.getTitlesContainer().has(this);
    }

    /**
     * Verifica se o jogador tem rank Iron ou superior.
     *
     * @param profile Perfil do jogador
     * @return true se tem rank Iron ou superior
     */
    private boolean hasIronOrHigherRank(Profile profile) {
        if (profile.getPlayer() == null) {
            return false;
        }

        // Ranks em ordem (do mais baixo para o mais alto)
        String[] rankOrder = {"membro", "iron", "gold", "emerald", "creator", "builder", "staff", "trial", "mod", "admin"};

        // Obtém o rank atual do jogador
        minecraft.core.core.player.role.Rank currentRank = minecraft.core.core.player.role.Rank.getPlayerRank(profile.getPlayer(), true);
        String currentRankName = minecraft.core.core.utils.StringUtils.stripColors(currentRank.getName()).toLowerCase();

        // Encontra a posição do rank atual
        int currentRankIndex = -1;
        for (int i = 0; i < rankOrder.length; i++) {
            if (rankOrder[i].equals(currentRankName)) {
                currentRankIndex = i;
                break;
            }
        }

        // Se não encontrou o rank, assume que é membro (mais baixo)
        if (currentRankIndex == -1) {
            currentRankIndex = 0; // membro
        }

        // Encontra a posição do rank Iron
        int ironRankIndex = -1;
        for (int i = 0; i < rankOrder.length; i++) {
            if (rankOrder[i].equals("iron")) {
                ironRankIndex = i;
                break;
            }
        }

        // Retorna true se o rank atual é igual ou superior ao Iron
        return currentRankIndex >= ironRankIndex;
    }

    public ItemStack getIcon(Profile profile) {
        boolean has = this.has(profile);
        boolean hasIronPlus = hasIronOrHigherRank(profile);
        Title selected = profile.getSelectedContainer().getTitle();

        String actionText;
        if (has) {
            actionText = (selected != null && selected.equals(this)) ? "&eClique para remover!" : "&eClique para selecionar!";
        } else if (hasIronPlus) {
            actionText = "&aClique para obter este título!";
        } else {
            actionText = "&cVocê não possui este título.";
        }

        String processedIcon = this.icon.replace("%material%", has ? (selected != null && selected.equals(this)) ? "ENCHANTED_BOOK" : "BOOK" : "STAINED_GLASS_PANE").replace("%durability%", has ? "0" : "14")
                .replace("%name%", (has ? "&a" : (hasIronPlus ? "&6" : "&c")) + StringUtils.stripColors(this.title))
                .replace("%action%", actionText);

        // Processa placeholders específicas
        if (this.id.equals("killsw")) {
            long skywarsKills = profile.getStats("skywars", "solokills", "duokills", "rankedkills");
            processedIcon = processedIcon.replace("%skywars_kills%", StringUtils.formatNumber(skywarsKills));
        } else if (this.id.equals("winsw")) {
            long skywarsWins = profile.getStats("skywars", "solowins", "duowins", "rankedwins");
            processedIcon = processedIcon.replace("%skywars_wins%", StringUtils.formatNumber(skywarsWins));
        } else if (this.id.equals("kstreaksw")) {
            long skywarsKillStreak = profile.getStats("skywars", "solokillstreak", "duokillstreak", "rankedkillstreak");
            processedIcon = processedIcon.replace("%skywars_killstreak%", StringUtils.formatNumber(skywarsKillStreak));
        } else if (this.id.equals("wstreaksw")) {
            long skywarsWinStreak = profile.getStats("skywars", "solowinstreak", "duowinstreak", "rankedwinstreak");
            processedIcon = processedIcon.replace("%skywars_winstreak%", StringUtils.formatNumber(skywarsWinStreak));
        } else if (this.id.equals("killbw")) {
            long bedwarsKills = profile.getStats("bedwars", "solokills", "duokills", "quadkills");
            processedIcon = processedIcon.replace("%bedwars_kills%", StringUtils.formatNumber(bedwarsKills));
        } else if (this.id.equals("winsbw")) {
            long bedwarsWins = profile.getStats("bedwars", "solowins", "duowins", "quadwins");
            processedIcon = processedIcon.replace("%bedwars_wins%", StringUtils.formatNumber(bedwarsWins));
        } else if (this.id.equals("kstreakbw")) {
            long bedwarsKillStreak = profile.getStats("bedwars", "solokillstreak", "duokillstreak", "quadkillstreak");
            processedIcon = processedIcon.replace("%bedwars_killstreak%", StringUtils.formatNumber(bedwarsKillStreak));
        } else if (this.id.equals("wstreakbw")) {
            long bedwarsWinStreak = profile.getStats("bedwars", "solowinstreak", "duowinstreak", "quadwinstreak");
            processedIcon = processedIcon.replace("%bedwars_winstreak%", StringUtils.formatNumber(bedwarsWinStreak));
        }

        // Processa placeholders do PlaceholderAPI
        if (profile.getPlayer() != null) {
            processedIcon = PlaceholderAPI.setPlaceholders(profile.getPlayer(), processedIcon);
        }

        return BukkitUtils.deserializeItemStack(processedIcon);
    }
}
