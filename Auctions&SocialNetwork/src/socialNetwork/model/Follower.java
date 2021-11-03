/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.model;

import commandsLib.CmdData;
import commandsLib.CmdServer;
import socialNetwork.commands.Notification;

/**
 *
 * @author Esteban Guzmán Ramírez
 */
public class Follower implements Observer {
    private String name;
    private CmdServer cmdServer;

    public Follower(String name, CmdServer cmdServer) {
        this.name = name;
        this.cmdServer = cmdServer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void notify(String notification) {
        CmdData data = new CmdData();
        data.put("notification", notification);
        cmdServer.send(new Notification(data), name);
        
    }
}
