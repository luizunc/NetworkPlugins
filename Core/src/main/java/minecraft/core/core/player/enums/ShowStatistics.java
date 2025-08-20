package minecraft.core.core.player.enums;

public enum ShowStatistics {
  ATIVADO,
  DESATIVADO;
  
  private static final ShowStatistics[] VALUES = values();
  
  public static ShowStatistics getByOrdinal(long ordinal) {
    if (ordinal < 2 && ordinal > -1) {
      return VALUES[(int) ordinal];
    }
    
    return null;
  }
  
  public String getInkSack() {
    if (this == ATIVADO) {
      return "10";
    }
    
    return "8";
  }
  
  public String getName() {
    if (this == ATIVADO) {
      return "§aAtivado";
    }
    
    return "§cDesativado";
  }
  
  public ShowStatistics next() {
    if (this == DESATIVADO) {
      return ATIVADO;
    }
    
    return DESATIVADO;
  }
}
