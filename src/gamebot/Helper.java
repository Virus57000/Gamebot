/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebot;

import java.io.PrintWriter;
import java.io.StringWriter;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;

/**
 *
 * @author Noukkis
 */
public class Helper {

    static VoiceChannel getVoiceChannelByName(Guild guild, String name) {
        for (VoiceChannel chan : guild.getVoiceChannels()) {
            if (chan.getName().equals(name)) {
                return chan;
            }
        }
        return null;
    }

    static boolean hasPermissions(Guild guild, User user, Permission perm) {
        for (Role role : guild.getRolesForUser(user)) {
            for (Permission permission : role.getPermissions()) {
                if (permission.equals(perm)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
