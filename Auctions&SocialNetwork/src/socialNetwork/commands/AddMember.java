/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import socialNetwork.controller.SocialNetworkServer;
import socialNetwork.model.Member;

/**
 *
 * @author esteban
 */
public class AddMember extends Command {

    public AddMember(CmdData data) {
        super(data);
    }

    @Override
    public void execute(Object context) {
        SocialNetworkServer server = (SocialNetworkServer) context;
        server.getMembers().add(new Member(data.get("nameMember")));
        server.notifyUpdateMembers();
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.ADD_MEMBER.toString();
    }
    
}
