/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.VoiceChannel;

/**
 *
 * @author Jordan
 */
public class Helper {
    
    public static VoiceChannel getVoiceChannelByID(Guild guild, String id) {
        for (VoiceChannel chan : guild.getVoiceChannels()) {
            if (chan.getId().equals(id)) {
                return chan;
            }
        }
        return null;
    }

    public static VoiceChannel getVoiceChannelByName(Guild guild, String name) {
        for (VoiceChannel chan : guild.getVoiceChannels()) {
            if (chan.getName().equalsIgnoreCase(name)) {
                return chan;
            }
        }
        return null;
    }
}
