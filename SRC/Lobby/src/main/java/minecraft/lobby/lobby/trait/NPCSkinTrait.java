package minecraft.lobby.lobby.trait;

import minecraft.core.core.libraries.npclib.api.npc.NPC;
import minecraft.core.core.libraries.npclib.npc.skin.Skin;
import minecraft.core.core.libraries.npclib.npc.skin.SkinnableEntity;
import minecraft.core.core.libraries.npclib.trait.NPCTrait;

public class NPCSkinTrait extends NPCTrait {
  
  private final Skin skin;
  
  public NPCSkinTrait(NPC npc, String value, String signature) {
    super(npc);
    this.skin = Skin.fromData(value, signature);
  }
  
  @Override
  public void onSpawn() {
    this.skin.apply((SkinnableEntity) this.getNPC().getEntity());
  }
}
