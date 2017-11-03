package me.vrcube.scriptbot.script;

import me.vrcube.scriptbot.ScriptBot;
import me.vrcube.scriptbot.script.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.event.Listener;

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
