/*
 * ScriptBot, 100% Customizable discord bot bridge plugin.
 *     Copyright (C) 2017  VRCube
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.vrcube.scriptbot.script;

import me.vrcube.scriptbot.ScriptBot;
import me.vrcube.scriptbot.script.Command;
import me.vrcube.scriptbot.utils.Config;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EventManager extends ListenerAdapter implements Listener{

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        for(Command cmd : ScriptBot.getScriptManager().getCommands()){
            try {
                if(cmd.getName().equals(event.getMessage().getRawContent().split(" ")[0])) cmd.execute(event);
            } catch (Exception e) {
                e.printStackTrace();
                ScriptBot.log("&4Error while executing command: "+cmd.getName());
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e){
        e.setCancelled(true);
        if(e.getMessage().startsWith("/discord") && Config.getConfig().getBoolean("discord-command.enabled")){
            StringBuilder message = new StringBuilder();
            for(String line : Config.getConfig().getStringList("discord-command.messages")){
                message.append(line).append("\n");
            }
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message.toString()));
        }
    }

}
