package me.vrcube.scriptbot.utils;



import me.vrcube.scriptbot.ScriptBot;

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

        }else{
            ScriptBot.log("no new versions found.");
        }
    }

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
