/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialNetwork.controller;

import commandsLib.CmdData;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author Esteban Guzmán Ramírez
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CmdData data = new CmdData();
        ArrayList<String> list = new ArrayList();
        list.add("hola");
        list.add("adios");
        list.add("Test");
        data.put("list", list.toString().substring(1, list.toString().length() - 1).replace(" ", ""));
        String[] list2 = data.get("list").split(",");
        System.out.println(data.toString());
        for(String e: list2){
            System.out.println(e);
        }
        
    }
    
}
