/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import java.util.ArrayList;
import net.dv8tion.jda.hooks.EventListener;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

/**
 *
 * @author Jordan
 */
public class Bot extends TimerTask implements EventListener {

    JDA jda;
    Guild guild;
    boolean stop;
    VoiceChannel chanJeux;
    ArrayList<VoiceChannel> chansDeJeu;

    public Bot(String token) {
        stop = false;
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
        } catch (IllegalArgumentException | LoginException | InterruptedException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        guild = jda.getGuilds().get(0);
        chanJeux = Helper.getVoiceChannelByName(guild, "Jeux");
        chansDeJeu = new ArrayList<>();
        jda.addEventListener(this);
    }

    @Override
    public void run() {
        verifieChanJeux();
        verifieChansDeJeu();
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

    @Override
    public void onEvent(Event event) {
        if (event instanceof PrivateMessageReceivedEvent) {
            PrivateMessageReceivedEvent p = (PrivateMessageReceivedEvent) event;
            if (p.getAuthor().getId().equals("170592618055598080")) {
                String[] c = p.getMessage().getRawContent().split(" = ");
                if (c.length == 2) {
                    switch (c[0]) {
                        case "token":
                            p.getChannel().sendMessage("Token changé en " + changeToken(c[1]));
                            break;
                        case "chan":
                            p.getChannel().sendMessage("Channel de jeu changé en " + changeChan(c[1]));
                    }
                }
            }
        }
    }

    private String changeToken(String token) {
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
        } catch (IllegalArgumentException | LoginException | InterruptedException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return token;
    }

    private String changeChan(String string) {
        chanJeux = Helper.getVoiceChannelByName(guild, string);
        return string;
    }
}
