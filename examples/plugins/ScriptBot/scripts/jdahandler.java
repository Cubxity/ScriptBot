@Override
public void onMessageReceived(MessageReceivedEvent event){
	if(event.getAuthor().equals(ScriptBot.getBot().getSelfUser())) return;
	Bukkit.broadcastMessage("Message received: "+event.getMessage().getRawContent());
}