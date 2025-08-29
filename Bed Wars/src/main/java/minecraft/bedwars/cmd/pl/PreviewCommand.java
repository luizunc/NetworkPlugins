package minecraft.bedwars.cmd.pl;

import minecraft.bedwars.cmd.SubCommand;
import minecraft.bedwars.cosmetics.object.preview.KillEffectPreview;
import minecraft.bedwars.cosmetics.object.preview.ShopkeeperSkinPreview;
import minecraft.bedwars.cosmetics.object.preview.WinAnimationPreview;
import minecraft.bedwars.cosmetics.object.preview.BreakEffectPreview;
import minecraft.core.core.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PreviewCommand extends SubCommand {
  
  public PreviewCommand() {
    super("preview", "preview", "Setar as localizações das previsualizações.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(
          " \n§eAjuda - Preview\n \n§6/pl preview killeffect [alvo/espectador] §f- §7Setar localizações de preview do Efeito de Abate.\n§6/pl preview vendedor [npc/area] §f- §7Setar localizações de preview da Skin do Vendedor.\n§6/pl preview comemoracoes §f- §7Setar localização de preview das Comemorações.\n§6/pl preview cama §f- §7Setar localização de preview da Quebra de Cama.\n ");
      return;
    }
    
    String action = args[0];
 if (action.equalsIgnoreCase("killeffect")) {
      if (args.length < 2) {
        player.sendMessage("§cUso: /pl preview killeffect [alvo/espectador]");
        return;
      }
      
      String type = args[1];
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (type.equalsIgnoreCase("alvo")) {
        KillEffectPreview.CONFIG.set("killeffect.1", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aLocalização do alvo setada!");
      } else if (type.equalsIgnoreCase("espectador")) {
        KillEffectPreview.CONFIG.set("killeffect.2", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aLocalização do espectador setada!");
      } else {
        player.sendMessage("§cUso: /pl preview killeffect [alvo/espectador]");
      }
    } else if (action.equalsIgnoreCase("vendedor")) {
      if (args.length < 2) {
        player.sendMessage("§cUso: /pl preview vendedor [npc/area]");
        return;
      }
      
      String type = args[1];
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (type.equalsIgnoreCase("npc")) {
        ShopkeeperSkinPreview.CONFIG.set("vendedor.1", BukkitUtils.serializeLocation(location));
        ShopkeeperSkinPreview.createLocations();
        player.sendMessage("§aLocalização do NPC vendedor setada!");
      } else if (type.equalsIgnoreCase("area")) {
        ShopkeeperSkinPreview.CONFIG.set("vendedor.2", BukkitUtils.serializeLocation(location));
        ShopkeeperSkinPreview.createLocations();
        player.sendMessage("§aLocalização da área de preview setada!");
      } else {
        player.sendMessage("§cUso: /pl preview vendedor [npc/area]");
      }
    } else if (action.equalsIgnoreCase("comemoracoes")) {
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      WinAnimationPreview.CONFIG.set("comemoracoes.1", BukkitUtils.serializeLocation(location));
      WinAnimationPreview.createLocations();
      player.sendMessage("§aLocalização da área de comemorações setada!");
    } else if (action.equalsIgnoreCase("cama")) {
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      BreakEffectPreview.CONFIG.set("cama.1", BukkitUtils.serializeLocation(location));
      BreakEffectPreview.createLocations();
      player.sendMessage("§aLocalização da área de quebra de cama setada!");
          } else {
        player.sendMessage(
            " \n§eAjuda - Preview\n \n§6/pl preview killeffect [alvo/espectador] §f- §7Setar localizações de preview do Efeito de Abate.\n§6/pl preview vendedor [npc/area] §f- §7Setar localizações de preview da Skin do Vendedor.\n§6/pl preview comemoracoes §f- §7Setar localização de preview das Comemorações.\n§6/pl preview cama §f- §7Setar localização de preview da Quebra de Cama.\n ");
      }
  }
}
