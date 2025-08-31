package minecraft.core.core.utils;

import java.util.ArrayList;
import java.util.List;

public class NickTeam {
    private static int ID = 0;
    private final String name;
    private final String prefix;
    private final String suffix;
    private final List<String> members;

    public NickTeam(String prefix, String suffix, String name) {
        this.name = name == null ? "[TEAM:" + ++ID + "]" : name + ++ID;
        this.prefix = prefix;
        this.suffix = suffix;
        this.members = new ArrayList<>();
    }

    public void addMember(String player) {
        if (!this.members.contains(player)) {
            this.members.add(player);
        }
    }

    public boolean isSimilar(String prefix, String suffix) {
        return this.prefix.equals(prefix) && this.suffix.equals(suffix);
    }

    public List<String> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
