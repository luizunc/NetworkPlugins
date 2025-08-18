package minecraft.core.bukkit.plugin;

import minecraft.core.bukkit.plugin.config.FileUtils;
import minecraft.core.bukkit.plugin.config.KConfig;
import minecraft.core.bukkit.plugin.config.KWriter;
import minecraft.core.bukkit.plugin.logger.KLogger;
import minecraft.core.core.reflection.Accessors;
import minecraft.core.core.reflection.acessors.FieldAccessor;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

/**
 * Classe base abstrata para plugins do sistema Core.
 * Fornece funcionalidades comuns de configuração e logging.
 */
public abstract class KPlugin extends JavaPlugin {
  
  private static final FieldAccessor<PluginLogger> LOGGER_ACCESSOR = 
      Accessors.getField(JavaPlugin.class, "logger", PluginLogger.class);
  
  private final FileUtils fileUtils;
  
  /**
   * Construtor padrão que inicializa o plugin.
   * Configura o logger customizado e chama o método start().
   */
  public KPlugin() {
    this.fileUtils = new FileUtils(this);
    LOGGER_ACCESSOR.set(this, new KLogger(this));
    
    this.start();
  }
  
  /**
   * Método chamado durante a inicialização do plugin.
   * Deve ser implementado pelas classes filhas.
   */
  public abstract void start();
  
  /**
   * Método chamado quando o plugin é carregado.
   * Deve ser implementado pelas classes filhas.
   */
  public abstract void load();
  
  /**
   * Método chamado quando o plugin é ativado.
   * Deve ser implementado pelas classes filhas.
   */
  public abstract void enable();
  
  /**
   * Método chamado quando o plugin é desativado.
   * Deve ser implementado pelas classes filhas.
   */
  public abstract void disable();
  
  @Override
  public void onLoad() {
    this.load();
  }
  
  @Override
  public void onEnable() {
    this.enable();
  }
  
  @Override
  public void onDisable() {
    this.disable();
  }
  
  /**
   * Obtém uma configuração pelo nome.
   * 
   * @param name Nome do arquivo de configuração
   * @return Instância da configuração
   */
  public KConfig getConfig(String name) {
    return this.getConfig("", name);
  }
  
  /**
   * Obtém uma configuração pelo caminho e nome.
   * 
   * @param path Caminho do diretório
   * @param name Nome do arquivo de configuração
   * @return Instância da configuração
   */
  public KConfig getConfig(String path, String name) {
    Objects.requireNonNull(name, "Nome da configuração não pode ser nulo");
    
    String configPath = String.format("plugins/%s/%s", this.getName(), path);
    return KConfig.getConfig(this, configPath, name);
  }
  
  /**
   * Obtém um writer para um arquivo específico.
   * 
   * @param file Arquivo para escrever
   * @return Instância do writer
   */
  public KWriter getWriter(File file) {
    return this.getWriter(file, "");
  }
  
  /**
   * Obtém um writer para um arquivo específico com cabeçalho.
   * 
   * @param file Arquivo para escrever
   * @param header Cabeçalho do arquivo
   * @return Instância do writer
   */
  public KWriter getWriter(File file, String header) {
    Objects.requireNonNull(file, "Arquivo não pode ser nulo");
    
    return new KWriter((KLogger) this.getLogger(), file, header);
  }
  
  /**
   * Obtém a instância do FileUtils.
   * 
   * @return Instância do FileUtils
   */
  public FileUtils getFileUtils() {
    return this.fileUtils;
  }
}
