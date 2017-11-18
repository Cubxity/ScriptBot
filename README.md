[download]: https://img.shields.io/badge/download-Spigot-orange.svg
[license]: https://img.shields.io/badge/license-GNU%20General%20public%20license%20v3-lightgrey.svg
[discord-invite]: https://discord.gg/zNmpa8K
[ ![download][] ](https://www.spigotmc.org/resources/scriptbot.48768/)
[ ![license][] ](https://github.com/VRCube/ScriptBot/tree/master/LICENSE)
[ ![Discord](https://discordapp.com/api/guilds/372605909685501963/widget.png) ][discord-invite]
[Discord Bots](https://discordbots.org/api/widget/status/364678314108911616.png)(https://discordbots.org/bot/364678314108911616
<img align="right" src="https://github.com/VRCube/ScriptBot/raw/master/scriptbot-logo.png" height="200" width="200">
# ScriptBot
A Discord bridge bot for spigot and discord, 100% Customizable, Custom script for JDA Events and Bukkit.
Java 8+ is **REQUIRED** for this plugin to work properly.
## Permissions
/scriptbot              - `scriptbot.commands.help`

/scriptbot reload       - `scriptbot.commands.reload`

/scriptbot forceupdate  - `scriptbot.commands.forceupdate`

/scriptbot download     - `scriptbot.commands.download`
## Configuration
```yaml
# ScriptBot by VRCube
# For info on bot, please go to https://github.com/DV8FromTheWorld/JDA/
# For example on script, please go to https://github.com/VRCube/ScriptBot
# Bot info
bot-token: 'Insert token here'
bot-playing: 'Minecraft'
# This is import for command script
# DO NOT REMOVE JAVA.LANG.* OR ELSE NOTHING WILL WORK!
script-imports:
  - java.lang.*
  - java.utils.*
  - org.bukkit.*
  - org.bukkit.block.*
  - org.bukkit.entity.*
  - org.bukkit.inventory.*
  - org.bukkit.material.*
  - org.bukkit.map.*
  - org.bukkit.util.*
  - org.bukkit.scheduler.*
  - org.bukkit.plugin.*
  - org.bukkit.scoreboard.*
  - net.dv8tion.jda.core.*
  - net.dv8tion.jda.core.hooks.*
  - net.dv8tion.jda.core.utils.*
  - net.dv8tion.jda.core.events.message.*
  - net.dv8tion.jda.core.events.guild.*
  - net.dv8tion.jda.core.events.channel.*
  - net.dv8tion.jda.core.events.*
  - net.dv8tion.jda.core.entities.*
  - net.dv8tion.jda.bot.*
  - net.dv8tion.jda.client.*
  - org.bukkit.event.*
  - org.bukkit.entity.*
  - org.bukkit.event.player.*
  - org.bukkit.event.block.*
  - org.bukkit.event.entity.*
  - org.bukkit.event.enchantment.*
  - org.bukkit.event.hanging.*
  - org.bukkit.event.inventory.*
  - org.bukkit.event.server.*
  - org.bukkit.event.vehicle.*
  - org.bukkit.event.world.*
  - me.vrcube.scriptbot.*


# Use message with only type MSG
# Use script with only type SCRIPT
# Put script files in plugins/ScriptBot/scripts
# Put delete: true to delete command after sent

# Example for scripts
#commands:
#  '!players':
#    type: SCRIPT
#    script: 'players.java'
#    delete: true
# in players.java you can write code there, no need methods
# bindings that are available:
# event   - MessageReceivedEvent
# channel - MessageChannel
# bot     - JDA: the bot itself
# author  - User: Command sender
# message - Message

commands:
  '!ping':
    type: MSG
    message: 'hello'

# Bukkit Events
# To get bot instance, use ScriptBot.getBot()
# To listen for event, use the way you did with Bukkit
# Example:
# @EventHandler
# public void onJoin(PLayerJoinEvent event){
#   System.println("A player joined: "+event.getPlayer().getName());
#}
# put script in plugins/ScriptBot/scripts
event-script: 'none'

# JDA Events
# To get bot instance, use event.getJDA()
# Use method overrides from ListenerAdapter class
# Events can be found here: https://github.com/DV8FromTheWorld/JDA/tree/master/src/main/java/net/dv8tion/jda/core/events
# Some event classes are not imported, if you wish to use it. Import it
# Example:
# @Override
# public void onMessageReceived(MessageReceivedEvent event){
#   Bukkit.broadcastMessage("Message received! "+event.getMessage().getRawContent());
# }

jda-event-script: 'none'

metrics-enabled: true

# Enable and Disable script, runs when plugin enable and disable
# available bindings: 
# bot - bot instance
enable-script: 'none'
disable-script: 'none'
discord-command:
  messages:
  - '&c-------------------------------------------------'
  - '&eJoin our discord now: https://discord.gg/example'
  - '&ePowered by ScriptBot'
  - '&c-------------------------------------------------'
  enabled: true
```
## Scripts
Bot is using JDA libraries, so if you are making command script, please visit JDA's GitHub [here](https://github.com/DV8FromTheWorld/JDA)
Instructions for using the script is in config.yml.

Command Script Example:
```java
channel.sendMessage("Hi! This is a command").queue()
```

Bukkit Event Script Example [DOCS](https://hub.spigotmc.org/javadocs/bukkit/)
```java
@EventHandler
public void onDeath(PlayerDeathEvent e){
  bot.getTextChannelById("YOU CHANNEL ID").sendMessage(e.getEntity().getName()+" just died").queue();
}
```

JDA Event Script Example [DOCS](https://github.com/DV8FromTheWorld/JDA)
```java
@Override
public void onMessageReceived(MessageReceivedEvent e){
  Bukkit.broadcastMessage(e.getAuthor().getName()+" > "+e.getMessage().getRawContent());
}
```

## More Examples
Examples can be found [here](https://github.com/VRCube/ScriptBot/tree/master/examples)
