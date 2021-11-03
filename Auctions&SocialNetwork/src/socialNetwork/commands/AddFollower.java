/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import socialNetwork.controller.SocialNetworkServer;
import socialNetwork.model.Follower;


/**
 *
 * @author esteban
 */
public class AddFollower extends Command {

    public AddFollower(CmdData data) {
        super(data);
    }

    @Override
    public void execute(Object context) {
        SocialNetworkServer server = (SocialNetworkServer) context;
        server.getFollowers().add(new Follower(data.get("nameFollower"),server.getCmdServer()));
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.ADD_FOLLOWER.toString();
    }
    
}
