package minecraft.lobby.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import minecraft.core.core.libraries.holograms.HologramLibrary;
import minecraft.core.core.libraries.holograms.api.Hologram;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HologramAdapter extends PacketAdapter {
  
  public HologramAdapter() {
    super(params().plugin(HologramLibrary.getPlugin()).types(PacketType.Play.Server.ENTITY_METADATA));
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
    PacketContainer packet = evt.getPacket();
    
    Player player = evt.getPlayer();
    if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
      int entityId = packet.getIntegers().read(0);
      Entity entity = HologramLibrary.getHologramEntity(entityId);
      
      if (entity == null || !HologramLibrary.isHologramEntity(entity)) {
        return;
      }
      
      Hologram hologram = HologramLibrary.getHologram(entity);
      if (hologram == null) {
        return;
      }
      
      WrappedWatchableObject customName = null;
      WrappedWatchableObject visible = new WrappedWatchableObject(3, (byte) 1);
      List<WrappedWatchableObject> list = new ArrayList<>();
      for (WrappedWatchableObject watchable : packet.getWatchableCollectionModifier().read(0)) {
        if (watchable.getIndex() == 2) {
          customName = new WrappedWatchableObject(2, watchable.getValue());
        } else if (watchable.getIndex() != 3) {
          list.add(watchable);
        }
      }
      
      if (customName == null || !(customName.getValue() instanceof String)) {
        return;
      }
      
      String name = (String) customName.getValue();
      if (name.contains("{player}") || name.contains("{displayname}")) {
        name = name.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName());
      }
      
      customName.setValue(name);
      if (name.isEmpty()) {
        visible.setValue((byte) 0);
      }
      list.add(customName);
      list.add(visible);
      PacketContainer clone = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
      clone.getIntegers().write(0, entityId);
      clone.getWatchableCollectionModifier().write(0, list);
      evt.setPacket(clone);
    }
  }
  
  @Override
  public void onPacketReceiving(PacketEvent evt) {
  }
}
