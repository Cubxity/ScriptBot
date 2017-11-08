package me.vrcube.scriptbot.utils;


import com.google.common.io.Files;
import me.vrcube.scriptbot.ScriptBot;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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

    public static boolean isMetricsEnabled(){
        return getConfig().getBoolean("metrics-enabled");
    }

    public static String getString(String path){
        return ScriptBot.getInstance().getConfig().getString(path);
    }

    public static FileConfiguration getConfig(){
        return ScriptBot.getInstance().getConfig();
    }

    public static void migrateConfig(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(ScriptBot.getInstance().getDataFolder(), "config.yml"));
        double configver = (config.get("config-ver") != null ? config.getDouble("config-ver") : 1.0);
        double pluginver = ScriptBot.version;

        if(configver < pluginver){
            ScriptBot.log("Config is outdated. Migrating..");
            File originalconfig = new File(ScriptBot.getInstance().getDataFolder(), "config.yml");
            if(new File(ScriptBot.getInstance().getDataFolder(), "config-old.yml").exists()) new File(ScriptBot.getInstance().getDataFolder(), "config-old.yml").delete();
            originalconfig.renameTo(new File(ScriptBot.getInstance().getDataFolder(), "config-old.yml"));

            

            getConfig().options().copyDefaults();
            ScriptBot.getInstance().saveDefaultConfig();
            YamlConfiguration current = YamlConfiguration.loadConfiguration(new File(ScriptBot.getInstance().getDataFolder(), "config.yml"));
            YamlConfiguration original = YamlConfiguration.loadConfiguration(new File(ScriptBot.getInstance().getDataFolder(), "config-old.yml"));
            for(String key : original.getConfigurationSection("").getKeys(true)){
                if(!key.equals("config-ver")) {
                    ScriptBot.log("&f[&aMigration&f] copying value at " + key);
                    if(original.isBoolean(key)) {
                        current.set(key, original.getBoolean(key));

                    }
                    else
                    if(original.isString(key)) {
                        current.set(key, original.getString(key));

                    }
                    else
                    if(original.isList(key)) {
                        current.set(key, original.getList(key));

                    }
                    else {
                        current.set(key, original.get(key));
                    }

                }
            }
            ScriptBot.log("saving config...");
            try {
                current.save(new File(ScriptBot.getInstance().getDataFolder(), "config.yml"));
                ScriptBot.log("migrated config from version "+configver+" to "+pluginver);
            } catch (IOException e) {
                e.printStackTrace();
                ScriptBot.log("&cError while migrating config, please report this error to the Dev");
            }
            addComments();


        }else if(configver > pluginver){
            ScriptBot.log("&f[&cWARN&f] Config had newer version than the plugin.");
        }else{
            ScriptBot.log("config is up to date!");
        }
    }
    public static void addComments(){

        addComments("config-ver", "" +
                "# ScriptBot by VRCube\n" +
                "# For info on bot, please go to https://github.com/DV8FromTheWorld/JDA/\n" +
                "# For example on script, please go to https://github.com/VRCube/ScriptBot\n" +
                "# DO NOT EDIT THIS VALUE BELOW, OR ELSE PLUGIN MIGHT CORRUPT");
        addComments("bot-token", "# Bot Info");
        addComments("script-imports",
                "# This is import for command script\n" +
                "# DO NOT REMOVE JAVA.LANG.* OR ELSE NOTHING WILL WORK!");
        addComments("commands",
                "# Use message with only type MSG\n" +
                "# Use script with only type SCRIPT\n" +
                "# Put script files in plugins/ScriptBot/scripts\n" +
                "# Put delete: true to delete command after sent\n" +
                "\n" +
                "# Example for scripts\n" +
                "#commands:\n" +
                "#  '!players':\n" +
                "#    type: SCRIPT\n" +
                "#    script: 'players.java'\n" +
                "#    delete: true\n" +
                "# in players.java you can write code there, no need methods\n" +
                "# bindings that are available:\n" +
                "# event   - MessageReceivedEvent\n" +
                "# channel - MessageChannel\n" +
                "# bot     - JDA: the bot itself\n" +
                "# author  - User: Command sender\n" +
                "# message - Message");
        addComments("event-script",
                "# Bukkit Events\n" +
                "# To get bot instance, use ScriptBot.getBot()\n" +
                "# To listen for event, use the way you did with Bukkit\n" +
                "# Example:\n" +
                "# @EventHandler\n" +
                "# public void onJoin(PLayerJoinEvent event){\n" +
                "#   System.println(\"A player joined: \"+event.getPlayer().getName());\n" +
                "#}\n" +
                "# put script in plugins/ScriptBot/scripts");
        addComments("jda-event-script",
                "# JDA Events\n" +
                "# To get bot instance, use event.getJDA()\n" +
                "# Use method overrides from ListenerAdapter class\n" +
                "# Events can be found here: https://github.com/DV8FromTheWorld/JDA/tree/master/src/main/java/net/dv8tion/jda/core/events\n" +
                "# Some event classes are not imported, if you wish to use it. Import it\n" +
                "# Example:\n" +
                "# @Override\n" +
                "# public void onMessageReceived(MessageReceivedEvent event){\n" +
                "#   Bukkit.broadcastMessage(\"Message received! \"+event.getMessage().getRawContent());\n" +
                "# }");

    }
    private static void addComments(String at, String comment){
        String config = readConfig();
        config = config.replace(at, comment+"\n"+at);
        writeConfig(config);
    }



    private static String readConfig(){
        try {
            return Files.toString(new File(ScriptBot.getInstance().getDataFolder(), "config.yml"), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void writeConfig(String s){

        new File(ScriptBot.getInstance().getDataFolder(), "config.yml").delete();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ScriptBot.getInstance().getDataFolder(), "config.yml")));
            for(String line : s.split("\n")){

                writer.write(line);
                writer.newLine();
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            ScriptBot.log("&4Error while trying to write the config");
        }
    }


}
