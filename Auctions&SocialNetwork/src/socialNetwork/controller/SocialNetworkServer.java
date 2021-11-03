/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.controller;

import commandsLib.CmdData;
import commandsLib.CmdServer;
import commandsLib.OnCmdSocketDisconnected;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import socialNetwork.commands.SocialNetworkCmdsFactory;
import socialNetwork.commands.UpdateMembers;
import socialNetwork.model.Follower;
import socialNetwork.model.Member;

/**
 *
 * @author esteban
 */
public class SocialNetworkServer implements OnCmdSocketDisconnected  {
    private CmdServer cmdServer;
    private ArrayList<Member> members;
    private ArrayList<Follower> followers;
    
    
    public SocialNetworkServer() {
        try {
            cmdServer = new CmdServer(8080);
            cmdServer.setLogging(true);
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }
        members = new ArrayList();
        followers = new ArrayList();
    }
    
    public void start() {
        cmdServer.listen(new SocialNetworkCmdsFactory(), this, this);  // passed as context and as hook onSocketClosed
        Thread closeAuctionsThread = new Thread() {
            public void run() {
                while (cmdServer.isRunning()) {
                    try {
                        Thread.sleep(500);  // sleep 1s
                        
                    } catch (Exception ex) { }
                }
            }
        };
        closeAuctionsThread.start();
            
        System.out.println("Input q anytime to stop the server.");  
        Scanner scanner = new Scanner(System.in);   
        while (cmdServer.isRunning()) {
            char readedChar = scanner.next().charAt(0);
            if (readedChar == 'q') {
                cmdServer.setRunning(false);  // stop sever
                System.exit(0);
            }
        }
    }

    @Override
    public void onCmdSocketDisconnected(String socketName) {
        System.out.println("Socket disconnect:"+socketName);
    }
    
    public static void main(String args[]) {
        System.out.println("-Social Network VIP Server-");
        SocialNetworkServer server = new SocialNetworkServer();
        server.start();
    }

    public CmdServer getCmdServer() {
        return cmdServer;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Follower> getFollowers() {
        return followers;
    }

    public void notifyUpdateMembers() {
        CmdData data = new CmdData();
        String listMembers = "";
        for(Member member: members){
            listMembers += member.getName() + ",";
        }
        data.put("listMembers", listMembers.substring(0, listMembers.length() - 1));
        for(Follower follower: followers){
            cmdServer.send(new UpdateMembers(data),follower.getName());
        }
        
    }
    
    public int getMemberIndex(String name){
        for(int i = 0; i < members.size(); i++){
            if(members.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    public int getFollowerIndex(String name) {
        for(int i = 0; i < followers.size(); i++){
            if(followers.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    public int getMessageIndex(int memberIndex, String detail){
        Member menber = members.get(memberIndex);
        for(int i = 0; i < menber.getMessages().size(); i++){
            if(menber.getMessages().get(i).getDetail().equals(detail))
                return i;
        }
        return -1;
    }
}
