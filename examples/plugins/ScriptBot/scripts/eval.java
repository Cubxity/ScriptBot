List<String> allowed-users = Arrays.asList(
   "Owner's ID",
   "Dev's ID",
   "Manager's ID"
   );
   
if(allowed-users.contains(author.getId())){
	String code = message.getRawContent().replaceAll("!eval ", "");
	try{
	ScriptEngine engine = new GroovyScriptEngineImpl();
	Bindings binds = engine.createBindings();
	binds.put("event", event);
	Object output = engine.eval("import java.lang.*;\nimport javax.script.*;\nimport org.codehaus.groovy.jsr223.*;\n"+code, binds);
	channel.sendMessage(":white_check_mark: Completed with output: "+String.valueOf(output)).queue();
	}catch(Exception ex){
		channel.sendMessage(":x: Error while eval, "+ex.getLocalizedMessage()).queue();
	}
}