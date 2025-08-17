package dev.slickcollections.kiwizin.mysterybox.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.slickcollections.kiwizin.mysterybox.Main;
import dev.slickcollections.kiwizin.mysterybox.nms.NMS;

public class CameraAdapter extends PacketAdapter {
  
  public CameraAdapter() {
    super(params().plugin(Main.getInstance()).types(PacketType.Play.Server.CAMERA));
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
    evt.getPacket().getIntegers().write(0, NMS.BLOCK_CAMERA.getOrDefault(evt.getPlayer().getName(), evt.getPacket().getIntegers().read(0)));
  }
}
