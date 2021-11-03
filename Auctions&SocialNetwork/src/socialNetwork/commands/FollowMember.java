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
import socialNetwork.model.Follower;
import socialNetwork.model.Member;

/**
 *
 * @author esteban
 */
public class FollowMember extends Command {

    public FollowMember(CmdData data) {
        super(data);
    }
    
    @Override
    public void execute(Object context) {
        SocialNetworkServer server = (SocialNetworkServer) context;
        int memberIndex = server.getMemberIndex(data.get("member"));
        int followerIndex = server.getFollowerIndex(data.get("follower"));
        server.getMembers().get(memberIndex)
                .addObserver(server.getFollowers().get(followerIndex));
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.FOLLOW_MEMBER.toString();
    }

    
    
}
