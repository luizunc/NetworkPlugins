package tk.kiwicollections.kiwizin.utils.roles.object;

import tk.kiwicollections.kiwizin.utils.Main;
import tk.slicecollections.maxteer.plugin.config.MConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Role {

    private String prefix, icon, cmd;

    public static void setupRoles() {
        if (!new File("plugins/" + Main.getInstance().getDescription().getName() + "/roles.yml").exists()) {
            Main.getInstance().saveResource("roles.yml", false);
        }
        MConfig config = Main.getInstance().getConfig("roles");
        for (String key : config.getSection("roles").getKeys(false)) {
            Role.listRoles().add(new Role(config.getString("roles." + key + ".icon"), config.getString("roles." + key + ".prefix"), config.getString("roles." + key + ".cmd")));
        }
    }

    public Role(String icon, String prefix, String cmd) {
        this.icon = icon;
        this.prefix = prefix;
        this.cmd = cmd;
    }

    public tk.slicecollections.maxteer.player.role.Role getRoleByPrefix() {
        for (tk.slicecollections.maxteer.player.role.Role role : tk.slicecollections.maxteer.player.role.Role.listRoles()) {
            if (role.getPrefix().equals(prefix)) {
                return role;
            }
        }
        return null;
    }

    private static final List<Role> ROLES = new ArrayList<>();

    public String getCmd() {
        return cmd;
    }

    public String getIcon() {
        return icon;
    }

    public tk.slicecollections.maxteer.player.role.Role getRole() {
        return getRoleByPrefix();
    }

    public static List<Role> listRoles() {
        return ROLES;
    }
}
