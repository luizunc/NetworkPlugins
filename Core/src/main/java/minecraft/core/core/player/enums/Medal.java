package minecraft.core.core.player.enums;

import minecraft.core.Manager;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.StringUtils;
import org.bukkit.entity.Player;

public enum Medal {

    G("G", "§4§lg", "medal.g"),
    PEACE_LOVE("PazEAmor", "✌", "medal.pazeamor"),
    HEART("Coração", "§4❤", "medal.heart"),
    HEART2("Heart2", "§c❥", "medal.heart2"),
    SORRISO("Sorriso", "§fツ", "medal.sorriso"),
    TOXICO("Tóxico", "§6☣", "medal.toxico"),
    RAIO("Raio", "§6⚡", "medal.raio"),
    MUSICA("Música", "§5♫", "medal.musico"),
    CAFE("Café", "§6☕", "medal.cafe"),
    RADIOATIVO("Radioativo", "§6☢", "medal.radioativo"),
    CRANIO("Crânio", "☠", "medal.cranio"),
    GUARDACHUVA("GuardaChuva", "§5☂", "medal.guardachuva"),
    CRUZ("Cruz", "§7✞", "medal.cruz"),
    CRUZDEMALTA("CruzDeMalta", "§c✠", "medal.cruzdemalta"),
    NEGATIVO("Negativo", "§c✘", "medal.negativado"),
    POSITIVO("Positivo", "§a✔", "medal.positivo"),
    YINYANG("YinYiang", "§f☯", "medal.yinyiang"),
    ESPADAS("Espadas", "§9⚔", "medal.espadas"),
    NEVEL("Nevel", "§b❄", "medal.neve"),
    SEMAFORO("Semáforo", "§2♻", "medal.semaforo"),
    ESTUDANTE("Estudante", "§7✍", "medal.estudante"),
    EMAIL("Email", "§f✉", "medal.email"),
    NITRO("Nitro", "§e✶", "medal.estrela"),
    DESTAQUE("Destaque", "§6✯", "medal.destaque"),
    DEFAULT("Nenhuma", "", "");

    private final String name;
    private final String suffix;
    private final String permission;

    Medal(String name, String suffix, String permission) {
        this.name = StringUtils.formatColors(name);
        this.suffix = StringUtils.formatColors(suffix);
        this.permission = permission;
    }

    public static Medal getMedalByName(String name) {
        for (Medal medal : values()) {
            if (StringUtils.stripColors(medal.getName()).equalsIgnoreCase(name)) {
                return medal;
            }
        }
        return getLastMedal();
    }

    public static Medal getMedal(Object player) {
        return getMedal(player, false);
    }

    public static Medal getMedal(Object player, boolean removeNick) {
        if (!(player instanceof Player)) {
            return getLastMedal();
        }

        Player bukkitPlayer = (Player) player;
        Profile profile = Profile.getProfile(bukkitPlayer.getName());

        if (profile != null) {
            String medalName = profile.getDataContainer("account", "medalha").getAsString();
            if (medalName != null && !medalName.isEmpty()) {
                Medal medal = getMedalByName(medalName);
                if (medal != null) return medal;
            }
        }

        return getLastMedal();
    }

    public static Medal getLastMedal() {
        Medal[] medals = values();
        return medals[medals.length - 1];
    }

    public boolean has(Player player) {
        if (player == null) return false;

        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            String medalName = profile.getDataContainer("account", "medalha").getAsString();
            if (medalName != null && !medalName.isEmpty()) {
                Medal configured = getMedalByName(medalName);
                return configured == this;
            }
        }

        return isDefault() || Manager.hasPermission(player, this.permission);
    }

    public boolean isDefault() {
        return this.permission == null || this.permission.isEmpty();
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getPermission() {
        return this.permission;
    }
}
