package me.vrcube.scriptbot.script;

import me.vrcube.scriptbot.utils.Config;
import me.vrcube.scriptbot.ScriptBot;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {
    private List<Command> commands = new ArrayList<>();

    public List<Command> getCommands() {
        return commands;
    }

    public void load(){
        for(String s : Config.getConfig().getConfigurationSection("commands").getKeys(false)){
          loadCommand(s);
        }
        loadBukkitEventHandler();
        loadJDAEventHandler();

    }



    private void loadCommand(String path){
        if(Config.getConfig().get("commands."+path+".type") == null) {
            ScriptBot.log("&4Failed to load command: "+path+". Type not found in config.");
            return;
        }
        Command.CommandType type;
        try{
            type = Command.CommandType.valueOf(Config.getConfig().get("commands."+path+".type").toString());
        }catch (Exception e){
            ScriptBot.log("&4Failed to load command: "+path+". Type not found: "+Config.getConfig().get("commands."+path+".type")+".");
            return;
        }
        boolean delete = (Config.getConfig().contains("commands." + path + ".delete") && Config.getConfig().getBoolean("commands." + path + ".delete"));
        if(type == Command.CommandType.SCRIPT){
            if(Config.getConfig().getString("commands."+path+".script") == null){
                ScriptBot.log("&4Failed to load command: "+path+". Script not found in config.");
                return;
            }

            if(!getScriptFile(Config.getConfig().getString("commands."+path+".script")).exists()){
                ScriptBot.log("&4Failed to load command: "+path+". Script not found in file system.");
                return;
            }


        }else{
            if(Config.getConfig().getString("commands."+path+".message") == null){
                ScriptBot.log("&4Failed to load command: "+path+". Message not found in config.");
                return;
            }
        }
        Command cmd = new Command(type, delete, path);
        if(type == Command.CommandType.SCRIPT){
            cmd.loadScript(getScriptFile(Config.getConfig().getString("commands."+path+".script")));
        }else{
            cmd.setResponse(Config.getConfig().getString("commands."+path+".message"));
        }

        ScriptBot.log("loaded command: "+cmd.getName());
        commands.add(cmd);

    }

    public static File getScriptFile(String name){
        File f = new File(ScriptBot.getInstance().getDataFolder(), "scripts");
        return new File(f, name);
    }

    public static String readFile(File f) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            builder.append(line).append("\n");
        }
        br.close();
        return builder.toString();
    }
    private void loadBukkitEventHandler(){
        if(Config.getString("event-script").equals("none"))return;
        if(!ScriptManager.getScriptFile(Config.getString("event-script")).exists()){
            ScriptBot.log("&4Unable to execute event handler script: Script not found in file system.");
        }else{
            ScriptEngine engine = new GroovyScriptEngineImpl();
            List<String> imports = Config.getImports();
            StringBuilder script = new StringBuilder();

            for(String imp : imports){
                script.append("import ").append(imp).append(";\n");
            }
            script.append("public class ScriptClazz implements Listener{\n");
            try {
                script.append(ScriptManager.readFile(ScriptManager.getScriptFile(Config.getString("event-script"))));
                script.append("\n}\nBukkit.getPluginManager().registerEvents(new ScriptClazz(), ScriptBot.getInstance());");
            } catch (IOException e1) {
                e1.printStackTrace();
                ScriptBot.log("&4Unable to load event handler script: Couldn't load the script");
            }
            Bindings binds = engine.createBindings();
            binds.put("bot", ScriptBot.getBot());

            try {
                engine.eval(script.toString(), binds);
                ScriptBot.log("loaded event handler");
            } catch (ScriptException e1) {
                e1.printStackTrace();
                //noinspection Duplicates
                ScriptBot.log("&4Error while loading event handler.");
            }

        }
    }

    private void loadJDAEventHandler(){
        if(Config.getString("jda-event-script").equals("none"))return;
        if(!ScriptManager.getScriptFile(Config.getString("jda-event-script")).exists()){
            ScriptBot.log("&4Unable to load jda event handler script: Script not found in file system.");
        }else{
            ScriptEngine engine = new GroovyScriptEngineImpl();
            List<String> imports = Config.getImports();
            StringBuilder script = new StringBuilder();

            for(String imp : imports){
                script.append("import ").append(imp).append(";\n");
            }
            script.append("public class ScriptClass extends ListenerAdapter{\n");
            try {
                script.append(ScriptManager.readFile(ScriptManager.getScriptFile(Config.getString("jda-event-script"))));
                script.append("\n}\nScriptBot.getBot().addEventListener(new ScriptClass());");
            } catch (IOException e1) {
                e1.printStackTrace();
                ScriptBot.log("&4Unable to load jda event handler script: Couldn't load the script");
            }
            Bindings binds = engine.createBindings();
            binds.put("bot", ScriptBot.getBot());

            try {
                engine.eval(script.toString(), binds);
                ScriptBot.log("loaded jda event handler");
            } catch (ScriptException e1) {
                e1.printStackTrace();
                ScriptBot.log("&4Error while loading jda event handler.");
            }

        }
    }
}
