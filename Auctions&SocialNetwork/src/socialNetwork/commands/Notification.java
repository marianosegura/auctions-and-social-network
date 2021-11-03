/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import socialNetwork.view.Follower;

/**
 *
 * @author esteban
 */
public class Notification extends Command {

    public Notification(CmdData data) {
        super(data);
    }

    @Override
    public void execute(Object context) {
        Follower view = (Follower) context;
        view.getNotifications().add(data.get("notification"));
        view.updateNotifications();
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.NOTIFICATION.toString();
    }
    
}
