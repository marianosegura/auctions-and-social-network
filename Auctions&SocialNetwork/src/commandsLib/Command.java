package commandsLib;


/**
 * Command object that executes an action in a given context. Contains
 * the logic to parse and encode the command.
 * @author Luis Mariano Ramírez Segura
 */
public abstract class Command {
    protected CmdData data;  // data required to execute the command action
    
    
    /**
     * Default constructor. Initializes empty data.
     */
    public Command() {
        this.data = new CmdData();
    }
        
        
    /**
     * Constructor that receives data.
     * @param data Data object to set directly
     */
    public Command(CmdData data) {
        this.data = data;
    }
    
    
    /**
     * Command action. Receives the context as a generic Object
     * to cast it to any class is meant to execute upon.
     * @param context 
     */
    public abstract void execute(Object context);
    
    
    /**
     * Returns the command unique identifier.
     * @return Command unique identifier.
     */
    public abstract String getIdentifier();

    
    /**
     * Encodes the command to a string.
     * @return Command as string
     */
    @Override
    public String toString() {
        return getIdentifier() + "##" + data.toString();
    }
    
    
    /**
     * Parses the identifier from a string command.
     * @param commandString Command as string
     * @return Command identifier
     */
    public static String parseIdentifier(String commandString) {
        return commandString.split("##", 2)[0];
    }
    
    
    /**
     * Parses the data from a string command.
     * @param commandString Command as string
     * @return Command data
     */
    public static CmdData parseData(String commandString) {
        return new CmdData(commandString.split("##", 2)[1]);
    }

    
    public CmdData getData() {
        return data;
    }

    
    public void setData(CmdData data) {
        this.data = data;
    }
}
