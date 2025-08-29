package minecraft.core.core.utils;

import java.util.ArrayList;
import java.util.List;

public class FakeTeam {
    private static int ID = 0;
    private final List<String> members = new ArrayList<>();
    private final String name;
    private String prefix = "";
    private String suffix = "";

    public FakeTeam(String prefix, String suffix, String name) {
        this.name = name == null ? "[TEAM:" + ++ID + "]" : name + ++ID;
        this.prefix = prefix;
        this.suffix = suffix;
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
