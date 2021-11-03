/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.model;

import java.util.ArrayList;

/**
 *
 * @author Esteban Guzmán Ramírez
 */
public class Member implements Observable{
    private String name;
    private ArrayList<Message> messages;
    private ArrayList<Observer> followers;
    private int notifyNumber = 2;
    

    public Member(String name) {
        this.name = name;
        this.messages = new ArrayList();
        this.followers = new ArrayList();
        this.toString();
    }

    public void setNotifyNumber(int notifyNumber) {
        this.notifyNumber = notifyNumber;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Observer> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Observer> followers) {
        this.followers = followers;
    }
    
    public boolean followed(Observer follower){
        return followers.contains(follower);
    }
    
    public void addMessage(String message){
        messages.add(new Message(message));
        notifyFollowers(false);
    }
    
    @Override
    public void addObserver(Object o) {
        Follower fan = (Follower) o;
        if(!followed(fan)){
            followers.add(fan); 
            if(followers.size() % notifyNumber == 0){
                notifyFollowers(true);
                System.out.println("Followers:"+followers);
            }
        }
        
    }
    
    public void notifyFollowers(boolean islevel){
        String notification;
        if(islevel){
            int level = followers.size() / notifyNumber;
            notification = name + " has reached level " + level;
        } else {
            notification = name + ":" 
                + messages.get(messages.size() - 1).getDetail();
        }
        for(Observer follower: followers){
            follower.notify(notification);
        }
    }

    @Override
    public String toString() {
        return name + "\nmessages:\n" + messages + "\nfollowers:" + followers.size();
    }
    
    
    
    
}
