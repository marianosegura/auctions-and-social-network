/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.controller;

import commandsLib.CmdServer;
import java.util.ArrayList;
import socialNetwork.model.Follower;
import socialNetwork.model.Member;

/**
 *
 * @author Esteban Guzmán Ramírez
 */
public class Controller {
    
    private ArrayList<Follower> followers;
    private ArrayList<Member> members;

    public Controller() {
        this.followers = new ArrayList();
        this.members = new ArrayList();
    }

    public ArrayList<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Follower> followers) {
        this.followers = followers;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
    
    public boolean memberExist(String name){
        for(int i = 0; i < members.size(); i++){
            if(members.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    
    public void addMember(String name) {
        members.add(new Member(name));
    }
    
    public Member getMember(String name) {
        for(int i = 0; i < members.size(); i++){
            if(members.get(i).getName().equals(name)){
                return members.get(i);
            }
        }
        return null;
    }
    
    public boolean followerExist(String name){
        for(int i = 0; i < followers.size(); i++){
            if(followers.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    
    public void addFollower(String name, CmdServer cmdServer) {
        followers.add(new Follower(name, cmdServer));
    } 
    
    public Follower getFollower(String name) {
        for(int i = 0; i < followers.size(); i++){
            if(followers.get(i).getName().equals(name)){
                return followers.get(i);
            }
        }
        return null;
    }
    
    public void addFollowerToMember(String follower, String member){
        getMember(member).addObserver(getFollower(follower));
    }
    
    public void writeMessage(String member, String detail){
        getMember(member).addMessage(detail);
    }
    
    public int indexMessage(String member, String detail){
        for(int i = 0; i < getMember(member).getMessages().size(); i++){
            if(getMember(member).getMessages().get(i).getDetail().equals(detail))
                return i;
        }
        return -1;
    }
    
    public void likeMessage(String follower, String member, String detail){
        getMember(member).getMessages().get(indexMessage(member, detail))
                .addObserver(getFollower(follower));
    }
    
    public void disLikeMessage(String follower, String member, int indexMessage){
        getMember(member).getMessages()
                .get(indexMessage).addDislikes(getFollower(follower));
    }
             
    
}
