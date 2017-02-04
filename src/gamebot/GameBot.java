/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import java.util.Timer;

/**
 *
 * @author Noukkis
 */
public class GameBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            Timer t = new Timer();
            Bot b = new Bot("Mjc3MTI4NzcxMjg1NTQ5MDU2.C3ZPzw.q6QolopaFLfz44EoWvue3i_Ltaw");
            t.scheduleAtFixedRate(b, 0, 1000);
    }

}
