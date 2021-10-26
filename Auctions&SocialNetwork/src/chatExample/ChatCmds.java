package chatExample;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public enum ChatCmds {
    SERVER_MESSAGE("SERVER_MESSAGE"),
    CLIENT_MESSAGE("CLIENT_MESSAGE"),
    NAMED_CLIENT_MESSAGE("NAMED_CLIENT_MESSAGE"),
    NOTIFY_NAMES_CHANGE("NOTIFY_NAMES_CHANGE"),
    UPDATE_CLIENT_NAMES("UPDATE_CLIENT_NAMES");

    private final String name;
    
    ChatCmds(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
