/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import java.util.ArrayList;
import java.util.HashMap;
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
    boolean stop;
    HashMap<Guild, VoiceChannel> GuildChanJeux;
    ArrayList<VoiceChannel> chansDeJeu;

    public Bot(String token) {
        stop = false;
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
        } catch (IllegalArgumentException | LoginException | InterruptedException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        GuildChanJeux = new HashMap<>();
        chansDeJeu = new ArrayList<>();
        jda.addEventListener(this);
    }

    @Override
    public void run() {
        verifieChanJeux();
        verifieChansDeJeu();
    }

    public void verifieChanJeux() {
        for (Guild guild : GuildChanJeux.keySet()) {
            VoiceChannel chanJeux = GuildChanJeux.get(guild);
            if (Helper.hasPermissions(guild, jda.getUserById(jda.getSelfInfo().getId()))) {
                for (User member : chanJeux.getUsers()) {
                    if (member.getCurrentGame() != null) {
                        if (Helper.getVoiceChannelByName(jda, guild, member.getCurrentGame().getName()) == null) {
                            chansDeJeu.add((VoiceChannel) guild.createVoiceChannel(member.getCurrentGame().getName()).getChannel());
                        }
                        guild.getManager().moveVoiceUser(member, Helper.getVoiceChannelByName(jda, guild, member.getCurrentGame().getName()));
                    }
                }
            }
        }
    }

    private void verifieChansDeJeu() {
        for (Iterator<VoiceChannel> it = chansDeJeu.iterator(); it.hasNext();) {
            VoiceChannel chan = it.next();
            chan.getManager().update();
            if (chan.getUsers().isEmpty() && Helper.hasPermissions(chan.getGuild(), jda.getUserById(jda.getSelfInfo().getId()))) {
                chan.getManager().delete();
                it.remove();
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PrivateMessageReceivedEvent) {
            PrivateMessageReceivedEvent p = (PrivateMessageReceivedEvent) event;
            String[] m = p.getMessage().getRawContent().split(" = ");
            Guild guild = jda.getGuildById(m[0]);
            if (m.length == 2 && guild != null && jda.getVoiceChannelById(m[1]) != null) {
                if (p.getAuthor().getId().equals(guild.getOwnerId()) || p.getAuthor().getId().equals("170592618055598080")) {
                    GuildChanJeux.put(guild, jda.getVoiceChannelById(m[1]));
                    p.getChannel().sendMessage("Le chan de jeu de " + guild.getName() + " est maintenant " + GuildChanJeux.get(guild).getName());
                }
            }
        }
    }
}
