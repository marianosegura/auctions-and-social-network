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
public class Message implements Observable{
    private String detail;
    private ArrayList<Observer> likes;
    private ArrayList<Follower>  dislikes;
    private int notifyNumber = 2;

    public Message(String detail) {
        this.detail = detail;
        likes = new ArrayList();
        dislikes =  new ArrayList();
    }
    
    public void setNotifyNumber(int notifyNumber) {
        this.notifyNumber = notifyNumber;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<Observer> getLikes() {
        return likes;
    }
    
    public void addDislikes(Follower follower){
        if(!dislikes.contains(follower)){
            dislikes.add(follower);
        }
    }
    
    public boolean isLiker(Follower follower){
        return likes.contains(follower);
    }

    @Override
    public void addObserver(Object o) {
        Follower fan = (Follower)o;
        if(!isLiker(fan)){
            likes.add(fan);
            if(likes.size() % notifyNumber == 0){
                notifyLikers();
            }
        }
        
    }

    private void notifyLikers() {
        for(Observer follower: likes){
            String notification = "The message \"" + detail + "\" has reached " 
                    + likes.size();
            follower.notify(notification);
        }
    }

    @Override
    public String toString() {
        return detail + "likes:" + likes.size();
    }
}
