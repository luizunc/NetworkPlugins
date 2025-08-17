package dev.slickcollections.kiwizin.mysterybox.hook;

import com.comphenix.protocol.ProtocolLibrary;
import dev.slickcollections.kiwizin.mysterybox.box.Box;
import dev.slickcollections.kiwizin.mysterybox.box.action.BoxContent;
import dev.slickcollections.kiwizin.mysterybox.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.mysterybox.hook.protocollib.CameraAdapter;
import dev.slickcollections.kiwizin.mysterybox.hook.protocollib.EntityAdapter;
import dev.slickcollections.kiwizin.mysterybox.hook.protocollib.HologramAdapter;

public class MBCoreHook {
  
  public static void setupHook() {
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new EntityAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new CameraAdapter());
    
    Box.setupBoxes();
    BoxContent.setupRewards();
    
    Cosmetic.setupCosmetics();
  }
}
