/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;

/**
 *
 * @author Jordan
 */
public class Bot {

    JDA jda;
    Guild guild;
    boolean stop;
    VoiceChannel chanJeux;
    ArrayList<VoiceChannel> chansDeJeu;

    public Bot(String token, String jeuxName) {
        stop = false;
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
        } catch (IllegalArgumentException | LoginException | InterruptedException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        guild = jda.getGuilds().get(0);
        chanJeux = Helper.getVoiceChannelByName(guild, jeuxName);
        chansDeJeu = new ArrayList<>();
    }

    public void run() {
        while (!stop) {
            verifieChanJeux();
            verifieChansDeJeu();
        }
    }

    public void verifieChanJeux() {
        for (User member : chanJeux.getUsers()) {
            if (member.getCurrentGame() != null) {
                if (Helper.getVoiceChannelByName(guild, member.getCurrentGame().getName()) == null) {
                    chansDeJeu.add((VoiceChannel) guild.createVoiceChannel(member.getCurrentGame().getName()).getChannel());
                }
                guild.getManager().moveVoiceUser(member, Helper.getVoiceChannelByName(guild, member.getCurrentGame().getName()));
            }
        }
    }

    private void verifieChansDeJeu() {
        for (Iterator<VoiceChannel> it = chansDeJeu.iterator(); it.hasNext();) {
            VoiceChannel chan = it.next();
            chan.getManager().update();
            if (chan.getUsers().isEmpty()) {
                chan.getManager().delete();
                it.remove();
            }
        }
    }
}
