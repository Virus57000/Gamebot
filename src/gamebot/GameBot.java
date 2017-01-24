/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

/**
 *
 * @author Jordan
 */
public class GameBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Veuillez entrez le token et le nom du chan de jeu en argument");
        } else {
            Bot bot = new Bot(args[0], args[1]);
            bot.run();
        }
    }
    
}
