package me.vrcube.scriptbot;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredListener;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

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

//    @EventHandler
//    public void onJoin(PlayerJoinEvent e){ onEvent(e);}

}
