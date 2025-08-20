package minecraft.core.core.database.data.container;

import minecraft.core.core.database.data.DataContainer;
import minecraft.core.core.database.data.interfaces.AbstractContainer;
import minecraft.core.core.player.enums.BloodAndGore;
import minecraft.core.core.player.enums.PlayerVisibility;
import minecraft.core.core.player.enums.PrivateMessages;
import minecraft.core.core.player.enums.ProtectionLobby;
import minecraft.core.core.player.enums.ShowStatistics;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class PreferencesContainer extends AbstractContainer {
  
  public PreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }
  
  public void changePlayerVisibility() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    Object currentValue = preferences.get("pv");
    PlayerVisibility current = currentValue != null ? PlayerVisibility.getByOrdinal((long) currentValue) : PlayerVisibility.TODOS;
    preferences.put("pv", current.next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changePrivateMessages() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    Object currentValue = preferences.get("pm");
    PrivateMessages current = currentValue != null ? PrivateMessages.getByOrdinal((long) currentValue) : PrivateMessages.TODOS;
    preferences.put("pm", current.next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changeBloodAndGore() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    Object currentValue = preferences.get("bg");
    BloodAndGore current = currentValue != null ? BloodAndGore.getByOrdinal((long) currentValue) : BloodAndGore.ATIVADO;
    preferences.put("bg", current.next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changeProtectionLobby() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    Object currentValue = preferences.get("pl");
    ProtectionLobby current = currentValue != null ? ProtectionLobby.getByOrdinal((long) currentValue) : ProtectionLobby.ATIVADO;
    preferences.put("pl", current.next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public void changeShowStatistics() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    Object currentValue = preferences.get("ss");
    ShowStatistics current = currentValue != null ? ShowStatistics.getByOrdinal((long) currentValue) : ShowStatistics.ATIVADO;
    preferences.put("ss", current.next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  
  public PlayerVisibility getPlayerVisibility() {
    Object value = this.dataContainer.getAsJsonObject().get("pv");
    if (value == null) {
      return PlayerVisibility.TODOS;
    }
    PlayerVisibility result = PlayerVisibility.getByOrdinal((long) value);
    return result != null ? result : PlayerVisibility.TODOS;
  }
  
  public PrivateMessages getPrivateMessages() {
    Object value = this.dataContainer.getAsJsonObject().get("pm");
    if (value == null) {
      return PrivateMessages.TODOS;
    }
    PrivateMessages result = PrivateMessages.getByOrdinal((long) value);
    return result != null ? result : PrivateMessages.TODOS;
  }
  
  public BloodAndGore getBloodAndGore() {
    Object value = this.dataContainer.getAsJsonObject().get("bg");
    if (value == null) {
      return BloodAndGore.ATIVADO;
    }
    BloodAndGore result = BloodAndGore.getByOrdinal((long) value);
    return result != null ? result : BloodAndGore.ATIVADO;
  }
  
  public ProtectionLobby getProtectionLobby() {
    Object value = this.dataContainer.getAsJsonObject().get("pl");
    if (value == null) {
      return ProtectionLobby.ATIVADO;
    }
    ProtectionLobby result = ProtectionLobby.getByOrdinal((long) value);
    return result != null ? result : ProtectionLobby.ATIVADO;
  }
  
  public ShowStatistics getShowStatistics() {
    Object value = this.dataContainer.getAsJsonObject().get("ss");
    if (value == null) {
      return ShowStatistics.ATIVADO;
    }
    ShowStatistics result = ShowStatistics.getByOrdinal((long) value);
    return result != null ? result : ShowStatistics.ATIVADO;
  }
}
