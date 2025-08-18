package minecraft.core.bungee.cmd;

import minecraft.core.Manager;
import minecraft.core.bungee.Bungee;
import minecraft.core.core.player.role.Role;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static minecraft.core.bungee.Bungee.ALEX;
import static minecraft.core.bungee.Bungee.STEVE;

public class FakeCommand extends Commands {
  
  public FakeCommand() {
    super("fake");
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }
    
    ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!player.hasPermission("core.cmd.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }
    
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUso: /fake <nick>"));
      return;
    }
    
    String fakeName = args[0];
    if (!Bungee.isUsable(fakeName)) {
      player.sendMessage(TextComponent.fromLegacyText("§cO nickname '" + fakeName + "' não está disponível para uso."));
      return;
    }
    
    // Cargo sempre "Membro" e skin do nick escolhido
    String finalRoleName = "Membro";
    String finalSkin = Manager.getSkin(fakeName, "value") + ":" + Manager.getSkin(fakeName, "signature");
    
    Bungee.applyFake(player, fakeName, finalRoleName, finalSkin);
  }
}
