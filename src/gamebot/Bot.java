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
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.exceptions.GuildUnavailableException;

/**
 *
 * @author Noukkis
 */
public class Bot extends TimerTask implements EventListener {

    JDA jda;
    HashMap<Guild, VoiceChannel> GuildChanJeux;
    ArrayList<VoiceChannel> chansDeJeu;
    User botOwner;

    public Bot(String token) {
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
        } catch (IllegalArgumentException | LoginException | InterruptedException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        GuildChanJeux = new HashMap<>();
        chansDeJeu = new ArrayList<>();
        jda.addEventListener(this);
        botOwner = jda.getUserById("203616169519742977");
    }

    @Override
    public void run() {
        verifieChanJeux();
        verifieChansDeJeu();
        String sd = "215873323857477632 = 277130403914186752";
        String[] m = sd.split(" = ");
        Guild guild = jda.getGuildById(m[0]);
        GuildChanJeux.put(guild, jda.getVoiceChannelById(m[1]));
    }

    public void verifieChanJeux() {
        for (Guild guild : GuildChanJeux.keySet()) {
            VoiceChannel chanJeux = GuildChanJeux.get(guild);
            if (Helper.hasPermissions(guild, jda.getUserById(jda.getSelfInfo().getId()), Permission.MANAGE_CHANNEL) && Helper.hasPermissions(guild, jda.getUserById(jda.getSelfInfo().getId()), Permission.VOICE_MOVE_OTHERS)) {
                for (User member : chanJeux.getUsers()) {
                    if (member.getCurrentGame() != null) {
                        String game = member.getCurrentGame().getName();
                        if (Helper.getVoiceChannelByName(guild, game) == null) {
                            try {
                                chansDeJeu.add((VoiceChannel) guild.createVoiceChannel(game).getChannel());
                            } catch (GuildUnavailableException e) {
                                sendBotOwner(e.toString());
                            }
                        }
                        try {
                            VoiceChannel chan = Helper.getVoiceChannelByName(guild, game);
                            if (chan.checkPermission(member, Permission.VOICE_CONNECT)) {
                                guild.getManager().moveVoiceUser(member, chan);
                            }
                        } catch (Exception e) {
                            sendBotOwner(Helper.getStackTrace(e));
                        }
                    }
                }
            }
        }
    }

    private void verifieChansDeJeu() {
        for (Iterator<VoiceChannel> it = chansDeJeu.iterator(); it.hasNext();) {
            VoiceChannel chan = it.next();
            chan.getManager().update();
            if (chan.getUsers().isEmpty() && Helper.hasPermissions(chan.getGuild(), jda.getUserById(jda.getSelfInfo().getId()), Permission.MANAGE_CHANNEL) && Helper.hasPermissions(chan.getGuild(), jda.getUserById(jda.getSelfInfo().getId()), Permission.VOICE_MOVE_OTHERS)) {
                chan.getManager().delete();
                it.remove();
            }
        }
    }

    private void sendBotOwner(String s) {
        String message = s;
        if (message.length() > 1900) {
            message = message.substring(0, 1850) + "\n etc.";
        }
        botOwner.getPrivateChannel().sendMessage(message);
    }

    @Override
    public void onEvent(Event arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
