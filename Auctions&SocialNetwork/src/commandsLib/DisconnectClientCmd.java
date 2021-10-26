package commandsLib;

/**
 * Used to disconnect a client from the server.
 * @author Luis Mariano Ramírez Segura
 */
public class DisconnectClientCmd extends Command {

    /**
     * Does nothing upon execution.
     * @param context Object not used
     */
    @Override
    public void execute(Object context) {
    }

    @Override
    public String getIdentifier() {
        return "DISCONNECT_CLIENT";
    }

}
