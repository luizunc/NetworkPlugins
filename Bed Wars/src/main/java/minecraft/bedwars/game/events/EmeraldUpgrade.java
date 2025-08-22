package minecraft.bedwars.game.events;

import minecraft.bedwars.Language;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsEvent;
import minecraft.bedwars.game.generators.Generator;
import minecraft.core.core.utils.StringUtils;

public class EmeraldUpgrade extends BedWarsEvent {
  
  @Override
  public void execute(BedWars game) {
    Generator diamond = game.listGenerators().stream().filter(collect -> collect.getType().equals(Generator.Type.EMERALD)).findAny().orElse(null);
    
    game.listGenerators().stream().filter(collect -> collect.getType().equals(Generator.Type.EMERALD)).forEach(Generator::upgrade);
    game.listPlayers(false).forEach(player -> player.sendMessage(Language.ingame$broadcast$generator_upgrade$emerald.replace("{tier}",
        StringUtils.repeat("I", diamond == null ? 1 : diamond.getTier()))));
  }
  
  @Override
  public String getName() {
    return Language.options$events$emerald;
  }
}