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

    List<Command> getCommands() {
        return commands;
    }
    public void loadEnableScript(){
        if(Config.getString("enable-script").equals("none"))
            return;
        if(!getScriptFile(Config.getString("enable-script")).exists()){
            ScriptBot.log("&4Failed to execute enable script: File not found.");
            return;
        }
        ScriptEngine engine = new GroovyScriptEngineImpl();
        List<String> imports = Config.getImports();
        StringBuilder script = new StringBuilder();
        for(String imp : imports){
            script.append("import ").append(imp).append(";\n");
        }
        try {
            script.append(readFile(getScriptFile(Config.getString("enable-script"))));
            engine.put("bot", ScriptBot.getBot());
            engine.eval(script.toString());
        } catch (IOException e) {
            e.printStackTrace();
            ScriptBot.log("&4Failed to execute enable script: Unable to read script");
        } catch (ScriptException e) {
            e.printStackTrace();
            ScriptBot.log("&4Error while trying to execute enable script.");
        }
    }

    public void loadDisableScript(){
        if(Config.getString("disable-script").equals("none"))
            return;
        if(!getScriptFile(Config.getString("enable-script")).exists()){
            ScriptBot.log("&4Failed to execute disable script: File not found.");
            return;
        }
        ScriptEngine engine = new GroovyScriptEngineImpl();
        List<String> imports = Config.getImports();
        StringBuilder script = new StringBuilder();
        for(String imp : imports){
            script.append("import ").append(imp).append(";\n");
        }
        try {
            script.append(readFile(getScriptFile(Config.getString("disable-script"))));
            engine.put("bot", ScriptBot.getBot());
            engine.eval(script.toString());
        } catch (IOException e) {
            e.printStackTrace();
            ScriptBot.log("&4Failed to execute disable script: Unable to read script");
        } catch (ScriptException e) {
            e.printStackTrace();
            ScriptBot.log("&4Error while trying to execute disable script.");
        }
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

    private static File getScriptFile(String name){
        File f = new File(ScriptBot.getInstance().getDataFolder(), "scripts");
        return new File(f, name);
    }

    static String readFile(File f) throws IOException{
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
        if(!getScriptFile(Config.getString("event-script")).exists()){
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
