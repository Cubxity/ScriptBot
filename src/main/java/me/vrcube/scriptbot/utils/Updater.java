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

package me.vrcube.scriptbot.utils;

import me.vrcube.scriptbot.ScriptBot;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updater {
    public static void check(){
        ScriptBot.log("checking for updates..");
        Double latest = Double.parseDouble(readLine("https://github.com/VRCube/ScriptBot/raw/master/src/main/resources/version.info"));
        if(latest > ScriptBot.version){
            ScriptBot.log("new updated found, downloading...");
            try {

                download("https://github.com/VRCube/ScriptBot/raw/master/ScriptBot-builds/ScriptBot-"+latest+".jar", new File("plugins", "ScriptBot-"+latest+".jar"));
                ScriptBot.log("updated to version "+latest+", reloading the plugin..");
                PluginUtil.unload(ScriptBot.getInstance());
                ScriptBot.getJar().delete();
                PluginUtil.load(ScriptBot.getInstance().getName());
            } catch (IOException e) {
                e.printStackTrace();
                ScriptBot.log("unable to update to latest version of ScriptBot.");
            }
        }else{
            ScriptBot.log("no new versions found.");
        }
    }
    @Deprecated
    public static String read(String urls){
        try{
            URL url = new URL(urls);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            StringBuilder output = new StringBuilder();
            if((inputLine = in.readLine()) != null){
                output.append(inputLine).append("\n");
            }
            in.close();
            return output.toString();
        }catch(IOException e){
            ScriptBot.log("Error while reading response from "+urls);
            return "";
        }


    }

    private static String readLine(String urls){
        try{
            URL url = new URL(urls);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String output = in.readLine();
            in.close();
            return output;
        }catch(IOException e){
            return "";
        }


    }

    private static void download(String urlStr, File file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}
