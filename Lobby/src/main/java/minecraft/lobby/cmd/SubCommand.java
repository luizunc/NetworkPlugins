package minecraft.lobby.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Classe base abstrata para subcomandos.
 * Define a estrutura comum para todos os subcomandos do plugin.
 */
public abstract class SubCommand {
    
    private final String name;
    private final String usage;
    private final String description;
    private final boolean onlyForPlayer;
    
    /**
     * Constrói um subcomando.
     * @param name nome do subcomando
     * @param usage sintaxe de uso do comando
     * @param description descrição do comando
     * @param onlyForPlayer true se o comando é apenas para jogadores
     */
    public SubCommand(String name, String usage, String description, boolean onlyForPlayer) {
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.onlyForPlayer = onlyForPlayer;
    }
    
    /**
     * Executa o subcomando para qualquer tipo de executor.
     * Implementação padrão vazia - deve ser sobrescrita se necessário.
     * @param sender executor do comando
     * @param args argumentos do comando
     */
    public void perform(CommandSender sender, String[] args) {
        // Implementação padrão vazia
    }
    
    /**
     * Executa o subcomando especificamente para jogadores.
     * Implementação padrão vazia - deve ser sobrescrita se necessário.
     * @param player jogador executor do comando
     * @param args argumentos do comando
     */
    public void perform(Player player, String[] args) {
        // Implementação padrão vazia
    }
    
    /**
     * Obtém o nome do subcomando.
     * @return nome do subcomando
     */
    public String getName() {
        return name;
    }
    
    /**
     * Obtém a sintaxe de uso do comando.
     * @return sintaxe de uso
     */
    public String getUsage() {
        return usage;
    }
    
    /**
     * Obtém a descrição do comando.
     * @return descrição do comando
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Verifica se o comando é apenas para jogadores.
     * @return true se apenas para jogadores, false caso contrário
     */
    public boolean onlyForPlayer() {
        return onlyForPlayer;
    }
    
    @Override
    public String toString() {
        return String.format("SubCommand{name='%s', usage='%s', description='%s', onlyForPlayer=%s}", 
                name, usage, description, onlyForPlayer);
    }
}
