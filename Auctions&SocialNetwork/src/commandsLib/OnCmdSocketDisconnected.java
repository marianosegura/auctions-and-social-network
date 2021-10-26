package commandsLib;

/**
 * Hook interface meant to be called when a named command socket is disconnected 
 * from the CmdServer, giving access to its name.
 * @author Luis Mariano Ramírez Segura
 */
public interface OnCmdSocketDisconnected {
    
    /**
     * Hook called when a named socket is disconnected.
     * @param socketName Name of the socket
     */
    public void onCmdSocketDisconnected(String socketName);
}
