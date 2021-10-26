package commandsLib;

/**
 * Factory method class to return a command for a given identifier.
 * @author Luis Mariano Ramírez Segura
 */
public interface ICmdFactory {
    
    /**
     * Factory method to return a command based on an identifier.
     * @param identifier Command identifier
     * @param data Command data
     * @return Command corresponding to the identifier
     */
    public Command getCommand(String identifier, CmdData data);
}
