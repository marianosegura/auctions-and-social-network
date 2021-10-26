package commandsLib;

/**
 * Command to name the client socket in the server. It's the only
 * exception in the CmdServer to being parsed by the ICmdFactory. 
 * @author Luis Mariano Ramírez Segura
 */
public class NameSocketCmd extends Command {
    public NameSocketCmd(CmdData data) {
        super(data);
    }
    
    
    /**
     * Constructor that receives the string to name the socket.
     * @param name Socket name
     */
    public NameSocketCmd(String name) {
        super();
        data.put("name", name);
    }
       
    /**
     * Sets the socket name. Receives the CmdSocket as context. 
     * @param context Object cast to a CmdSocket
     */
    @Override
    public void execute(Object context) {
        String name = data.get("name");
        CmdSocket socket = (CmdSocket) context;
        socket.setName(name);
    }
    

    @Override
    public String getIdentifier() {
        return "NAME_SOCKET";
    }

}
