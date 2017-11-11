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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import java.io.File;
import java.util.List;

public class Command {
    private CommandType type;
    private String name;
    private boolean delete;
    private File script;
    private String response;
    public Command(CommandType type, boolean delete, String name){
        this.type = type;
        this.delete = delete;
        this.name = name;
    }

    public void loadScript(File script) {
        this.script = script;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void execute(MessageReceivedEvent e) throws Exception{
        if(type == CommandType.MSG){
            e.getChannel().sendMessage(response).queue();
        }else if (type == CommandType.SCRIPT){
            ScriptEngine engine = new GroovyScriptEngineImpl();
            List<String> imports = Config.getImports();
            StringBuilder script = new StringBuilder();
            for(String imp : imports){
                script.append("import ").append(imp).append(";\n");
            }
            script.append(ScriptManager.readFile(this.script));
            Bindings binds = engine.createBindings();
            binds.put("event", e);
            binds.put("channel", e.getChannel());
            binds.put("bot", ScriptBot.getBot());
            binds.put("author", e.getAuthor());
            binds.put("message", e.getMessage());
            engine.eval(script.toString(), binds);
        }
        if(delete)
            e.getMessage().delete().queue();
    }

    public String getName() {
        return name;
    }

    public enum CommandType{
        MSG,
        SCRIPT
    }
}
