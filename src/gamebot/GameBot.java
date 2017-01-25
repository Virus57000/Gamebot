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
        if (args.length < 1) {
            System.out.println("Veuillez entrez le token en argument");
        } else {
            Timer t = new Timer();
            Bot b = new Bot(args[0]);
            t.scheduleAtFixedRate(b, 0, 1000);
        }
    }

}
