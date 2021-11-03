/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import socialNetwork.controller.SocialNetworkServer;

/**
 *
 * @author esteban
 */
public class LikeMessage extends Command {

    public LikeMessage(CmdData data) {
        super(data);
    }

    @Override
    public void execute(Object context) {
        SocialNetworkServer server = (SocialNetworkServer) context;
        int memberIndex = server.getMemberIndex(data.get("member"));
        int followerIndex = server.getFollowerIndex(data.get("follower"));
        int messageIndex = 
                server.getMessageIndex(memberIndex, data.get("detail"));
        server.getMembers().get(memberIndex) //find member
                .getMessages().get(messageIndex) //find message
                .addObserver(server.getFollowers().get(followerIndex)); //find and add follower
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.LIKE_MESSAGE.toString();
    }
    
}
