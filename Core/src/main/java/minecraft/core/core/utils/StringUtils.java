package minecraft.core.core.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe com utilitários relacionados a {@link String}.
 * Fornece métodos para formatação de números, cores, manipulação de strings e outras operações comuns.
 */
public final class StringUtils {
  
  // Constantes para formatação
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
  private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");
  private static final String EMPTY_STRING = "";
  
  /**
   * Construtor privado para evitar instanciação.
   */
  private StringUtils() {
    throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
  }
  
  // Métodos de formatação de números
  
  /**
   * Formata um número inteiro usando o padrão "#,###".
   *
   * @param number Número para formatar
   * @return Número formatado
   */
  public static String formatNumber(int number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  /**
   * Formata um número longo usando o padrão "#,###".
   *
   * @param number Número para formatar
   * @return Número formatado
   */
  public static String formatNumber(long number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  /**
   * Formata um número decimal usando o padrão "#,###".
   *
   * @param number Número para formatar
   * @return Número formatado
   */
  public static String formatNumber(double number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  // Métodos de manipulação de cores
  
  /**
   * Remove todas as cores de uma String.
   * Remove códigos de cor no formato §X.
   *
   * @param input String para remover as cores
   * @return String sem cores ou null se input for null
   */
  public static String stripColors(final String input) {
    if (input == null) {
      return null;
    }
    
    return COLOR_PATTERN.matcher(input).replaceAll(EMPTY_STRING);
  }
  
  /**
   * Formata os caracteres {@code &} para o código de cor {@code §}.
   *
   * @param textToFormat String para formatar as cores
   * @return String com as cores formatadas
   */
  public static String formatColors(String textToFormat) {
    return translateAlternateColorCodes('&', textToFormat);
  }
  
  /**
   * Desformata o código de cor {@code §} para {@code &}.
   *
   * @param textToDeFormat String para desformatar as cores
   * @return String com as cores desformatadas
   */
  public static String deformatColors(String textToDeFormat) {
    if (textToDeFormat == null) {
      return null;
    }
    
    Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
    while (matcher.find()) {
      String color = matcher.group();
      String replacement = "&" + color.substring(1);
      textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement(replacement));
    }
    
    return textToDeFormat;
  }
  
  /**
   * Formata caracteres alternativos para o código de cor {@code §}.
   *
   * @param altColorChar Caractere que é definido como código de cor
   * @param textToTranslate String para formatar as cores
   * @return String com as cores formatadas
   */
  public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
    if (textToTranslate == null) {
      return null;
    }
    
    Pattern pattern = Pattern.compile("(?i)(" + altColorChar + ")[0-9A-FK-OR]");
    Matcher matcher = pattern.matcher(textToTranslate);
    
    while (matcher.find()) {
      String color = matcher.group();
      String replacement = "§" + color.substring(1);
      textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement(replacement));
    }
    
    return textToTranslate;
  }
  
  /**
   * Obtém a primeira cor de uma String.
   *
   * @param input String para obter a cor
   * @return Primeira cor encontrada ou string vazia se não encontrar
   */
  public static String getFirstColor(String input) {
    if (input == null) {
      return EMPTY_STRING;
    }
    
    Matcher matcher = COLOR_PATTERN.matcher(input);
    return matcher.find() ? matcher.group() : EMPTY_STRING;
  }
  
  /**
   * Obtém a última cor de uma String.
   *
   * @param input String para obter a cor
   * @return Última cor encontrada ou string vazia se não encontrar
   */
  public static String getLastColor(String input) {
    if (input == null) {
      return EMPTY_STRING;
    }
    
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String last = EMPTY_STRING;
    
    while (matcher.find()) {
      last = matcher.group();
    }
    
    return last;
  }
  
  // Métodos de manipulação de strings
  
  /**
   * Repete uma String várias vezes.
   *
   * @param repeat String para repetir
   * @param amount Quantidade de vezes que será repetida
   * @return Resultado da repetição
   */
  public static String repeat(String repeat, int amount) {
    if (repeat == null || amount <= 0) {
      return EMPTY_STRING;
    }
    
    StringBuilder sb = new StringBuilder(repeat.length() * amount);
    for (int i = 0; i < amount; i++) {
      sb.append(repeat);
    }
    
    return sb.toString();
  }
  
  /**
   * Junta um array em uma String utilizando um separador.
   *
   * @param array Array para juntar
   * @param index Índice inicial da iteração (0 = toda a array)
   * @param separator Separador da junção
   * @return Resultado da junção
   */
  public static <T> String join(T[] array, int index, String separator) {
    if (array == null || array.length == 0 || index >= array.length) {
      return EMPTY_STRING;
    }
    
    Objects.requireNonNull(separator, "Separador não pode ser nulo");
    
    StringBuilder joined = new StringBuilder();
    for (int slot = index; slot < array.length; slot++) {
      if (array[slot] != null) {
        joined.append(array[slot].toString());
      }
      if (slot + 1 < array.length) {
        joined.append(separator);
      }
    }
    
    return joined.toString();
  }
  
  /**
   * Junta um array em uma String utilizando um separador.
   *
   * @param array Array para juntar
   * @param separator Separador da junção
   * @return Resultado da junção
   */
  public static <T> String join(T[] array, String separator) {
    return join(array, 0, separator);
  }
  
  /**
   * Junta uma coleção em uma String utilizando um separador.
   *
   * @param collection Coleção para juntar
   * @param separator Separador da junção
   * @return Resultado da junção
   */
  public static <T> String join(Collection<T> collection, String separator) {
    if (collection == null || collection.isEmpty()) {
      return EMPTY_STRING;
    }
    
    return join(collection.toArray(), separator);
  }
  
  /**
   * Capitaliza uma String. Exemplo: "MAXTER" se torna "Maxter".
   *
   * @param toCapitalise String para capitalizar
   * @return Resultado capitalizado
   */
  public static String capitalise(String toCapitalise) {
    if (toCapitalise == null || toCapitalise.trim().isEmpty()) {
      return toCapitalise;
    }
    
    String[] splittedString = toCapitalise.split(" ");
    StringBuilder result = new StringBuilder();
    
    for (int index = 0; index < splittedString.length; index++) {
      String word = splittedString[index];
      if (!word.isEmpty()) {
        result.append(word.substring(0, 1).toUpperCase())
              .append(word.substring(1).toLowerCase());
      }
      
      if (index + 1 < splittedString.length) {
        result.append(" ");
      }
    }
    
    return result.toString();
  }
  
  // Métodos de divisão de strings
  
  /**
   * Quebra uma String em linhas com tamanho máximo definido.
   * Variação do método split com ignoreIncompleteWords = false.
   *
   * @param toSplit String para quebrar
   * @param length Tamanho máximo das linhas
   * @return Resultado da separação
   */
  public static String[] split(String toSplit, int length) {
    return split(toSplit, length, false);
  }
  
  /**
   * Quebra uma String em linhas com tamanho máximo definido.
   *
   * @param toSplit String para quebrar
   * @param length Tamanho máximo das linhas
   * @param ignoreIncompleteWords Se deve ignorar a quebra de palavras
   * @return Resultado da separação
   */
  public static String[] split(String toSplit, int length, boolean ignoreIncompleteWords) {
    if (toSplit == null || toSplit.isEmpty() || length <= 0) {
      return new String[0];
    }
    
    StringBuilder result = new StringBuilder();
    StringBuilder current = new StringBuilder();
    
    char[] arr = toSplit.toCharArray();
    for (int charId = 0; charId < arr.length; charId++) {
      char character = arr[charId];
      
      if (current.length() == length) {
        if (!ignoreIncompleteWords) {
          handleWordBreak(result, current);
        } else {
          result.append(current).append("\n");
          current = new StringBuilder();
        }
      }
      
      // Adiciona caractere se não for espaço no início
      if (current.length() != 0 || character != ' ') {
        current.append(character);
      }
      
      // Adiciona última linha
      if (charId + 1 == arr.length) {
        result.append(current).append("\n");
      }
    }
    
    return result.toString().split("\n");
  }
  
  /**
   * Manipula a quebra de palavras para evitar cortar palavras no meio.
   *
   * @param result StringBuilder para o resultado
   * @param current StringBuilder com a linha atual
   */
  private static void handleWordBreak(StringBuilder result, StringBuilder current) {
    List<Character> removedChars = new ArrayList<>();
    
    // Procura o último espaço na linha
    for (int l = current.length() - 1; l > 0; l--) {
      if (current.charAt(l) == ' ') {
        current.deleteCharAt(l);
        result.append(current).append("\n");
        
        // Restaura caracteres removidos na próxima linha
        Collections.reverse(removedChars);
        current.setLength(0);
        for (Character c : removedChars) {
          current.append(c);
        }
        break;
      }
      
      removedChars.add(current.charAt(l));
      current.deleteCharAt(l);
    }
    
    // Se não encontrou espaço, quebra no final mesmo
    if (removedChars.isEmpty()) {
      result.append(current).append("\n");
      current.setLength(0);
    }
  }
}
