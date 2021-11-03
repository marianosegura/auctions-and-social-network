/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import javax.swing.DefaultListModel;
import socialNetwork.view.Follower;

/**
 *
 * @author esteban
 */
public class UpdateMembers extends Command {

    public UpdateMembers(CmdData data) {
        super(data);
    }
    

    @Override
    public void execute(Object context) {
        DefaultListModel listModel = new DefaultListModel();
        String[] list = data.get("listMembers").split(",");
        for(String name: list){
            listModel.addElement(name);
        }
        Follower view = (Follower) context;
        view.getMembersList().setModel(listModel);
    }

    @Override
    public String getIdentifier() {
        return SocialNetworkCmds.UPDATE_MEMBERS.toString();
    }
    
}
