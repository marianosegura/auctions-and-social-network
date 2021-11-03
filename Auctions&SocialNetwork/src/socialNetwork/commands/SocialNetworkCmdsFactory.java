/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import auctions.commands.AuctionCmds;
import commandsLib.CmdData;
import commandsLib.Command;
import commandsLib.ICmdFactory;
import commandsLib.NullCmd;

/**
 *
 * @author esteban
 */
public class SocialNetworkCmdsFactory implements ICmdFactory {

    @Override
    public Command getCommand(String identifier, CmdData data) {
        SocialNetworkCmds cmdEnum = SocialNetworkCmds.valueOf(identifier);
        switch(cmdEnum) {
            case NOTIFICATION:
                return new Notification(data);
            case FOLLOW_MEMBER:
                return new FollowMember(data);
            case LIKE_MESSAGE:
                return new LikeMessage(data);
            case DISLIKE_MESSAGE:
                return new DislikeMessage(data);
            case ADD_MEMBER:
                return new AddMember(data);
            case ADD_FOLLOWER:
                return new AddFollower(data);
            case WRITE_MESSAGE:
                return new WriteMessage(data);
            case UPDATE_MEMBERS:
                return new UpdateMembers(data);
            default:
                return new NullCmd();
        }
    }
    
}
