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

package me.vrcube.scriptbot.commands;

import me.vrcube.scriptbot.ScriptBot;
import me.vrcube.scriptbot.utils.Color;
import me.vrcube.scriptbot.utils.PluginUtil;
import me.vrcube.scriptbot.utils.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Color.of("&f[&cScriptBot&f] running version &1"+ ScriptBot.version));
            if (sender.hasPermission("scriptbot.commands.help"))
                sender.sendMessage(help);

            return true;
        }
        switch(args[0]){
            case "reload":
                if(!sender.hasPermission("scriptbot.commands.reload"))return true;
                sender.sendMessage(Color.of("&aReloading..."));
                PluginUtil.reload(ScriptBot.getInstance());
                sender.sendMessage(Color.of("&aReloaded!"));
                break;
            case "forceupdate":
                if(!sender.hasPermission("scriptbot.commands.forceupdate"))return true;
                sender.sendMessage(Color.of("&aChecking for updates.. If there is new update, plugin will reload itself."));
                Updater.check();
                break;
            case "download":
                if(!sender.hasPermission("scriptbot.commands.download"))return true;
                sender.sendMessage(Color.of("&eYou can download plugin here: &ahttps://www.spigotmc.org/resources/scriptbot.48768/"));
                break;
            default:
                sender.sendMessage(Color.of("&f[&cScriptBot&f] running version &1"+ ScriptBot.version));
                if(sender.hasPermission("scriptbot.commands.help"))
                    sender.sendMessage(help);
        }
        return false;
    }
    private String help = Color.of(
            "&c/scriptbot &ereload      &7- reloads the plugin\n"+
               "&c/scriptbot &eforceupdate &7- force check update\n"+
               "&c/scriptbot &edownload    &7- link to spigotmc page"
    );
}
