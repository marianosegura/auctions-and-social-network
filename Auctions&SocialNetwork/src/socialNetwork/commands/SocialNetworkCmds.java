/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.commands;

/**
 *
 * @author esteban
 */
public enum SocialNetworkCmds {
    WRITE_MESSAGE("WRITE_MESSAGE"),
    NOTIFICATION("NOTIFICATION"),
    ADD_FOLLOWER("ADD_FOLLOWER"),
    ADD_MEMBER("ADD_MEMBER"),
    LIKE_MESSAGE("LIKE_MESSAGE"),
    DISLIKE_MESSAGE("DISLIKE_MESSAGE"),
    FOLLOW_MEMBER("FOLLOW_MEMBER"),
    UPDATE_MEMBERS("UPDATE_MEMBERS");
    
    private final String name;
    
    SocialNetworkCmds(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
