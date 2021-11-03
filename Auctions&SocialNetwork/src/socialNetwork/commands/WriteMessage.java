/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import java.util.ArrayList;
import socialNetwork.controller.SocialNetworkServer;
import socialNetwork.model.Member;

/**
 *
 * @author esteban
 */
public class WriteMessage extends Command {

    public WriteMessage(CmdData data) {
        super(data);
    }

    @Override
    public void execute(Object context) {
        SocialNetworkServer server = (SocialNetworkServer) context;
        int memberIndex = 
                server.getMemberIndex(data.get("member"));
        server.getMembers().get(memberIndex).addMessage(data.get("message"));
        System.out.println(server.getMembers());
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.WRITE_MESSAGE.toString();
    }
    
}
