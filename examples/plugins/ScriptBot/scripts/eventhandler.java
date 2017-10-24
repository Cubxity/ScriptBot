@EventHandler 
public void onJoin(PlayerJoinEvent e){
	ScriptBot.getBot().getTextChannelById("<A CHANNEL ID>").sendMessage("Player joined: "+e.getPlayer().getName()).queue();
}

@EventHandler
public void onDeath(PlayerDeathEvent e){
	ScriptBot.getBot().getTextChannelById("<A CHANNEL ID>").sendMessage(":skull: "+e.getEntity().getName()).queue();
}