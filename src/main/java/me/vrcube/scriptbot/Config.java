package me.vrcube.scriptbot;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    public static String getToken(){
        return ScriptBot.getInstance().getConfig().getString("bot-token");
    }

    public static String getGame(){
        return ScriptBot.getInstance().getConfig().getString("bot-playing");
    }

    public static List<String> getImports(){
        return ScriptBot.getInstance().getConfig().getStringList("script-imports");
    }

    public static String getString(String path){
        return ScriptBot.getInstance().getConfig().getString(path);
    }

    public static FileConfiguration getConfig(){
        return ScriptBot.getInstance().getConfig();
    }
}
