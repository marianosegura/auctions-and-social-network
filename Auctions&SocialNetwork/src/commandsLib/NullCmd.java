package commandsLib;

/**
 * Null command. Does nothing upon execution.
 * @author Luis Mariano Ramírez Segura
 */
public class NullCmd extends Command {
    /**
     * Does nothing upon execution.
     * @param context Anything. Not used.
     */
    @Override
    public void execute(Object context) {
    }
    

    @Override
    public String getIdentifier() {
        return "NULL";
    }

}
