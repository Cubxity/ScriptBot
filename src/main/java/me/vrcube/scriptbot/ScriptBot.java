/*
*ScriptBot, 100% Customizable discord bot bridge plugin.
*   Copyright (C) 2017  VRCube
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package me.vrcube.scriptbot;

import me.vrcube.scriptbot.commands.MainCommand;
import me.vrcube.scriptbot.script.EventManager;
import me.vrcube.scriptbot.script.ScriptManager;
import me.vrcube.scriptbot.utils.Config;
import me.vrcube.scriptbot.utils.Metrics;
import me.vrcube.scriptbot.utils.UpdateScheduler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class ScriptBot extends JavaPlugin {
    private static ScriptBot instance;
    private static File jar;
    public static ScriptBot getInstance() {
        return instance;

    }

    private static JDA bot;

    public static JDA getBot() {
        return bot;
    }

    private static ScriptManager scriptManager = new ScriptManager();

    public static ScriptManager getScriptManager() {
        return scriptManager;
    }

    public static double version = 1.2;

    private static Metrics metrics;
    private void initConfig(){
        if(!new File(getDataFolder(), "config.yml").exists()) {
            this.getConfig().options().copyDefaults();
            saveResource("config.yml", false);
            Config.addComments();

        }
        Config.migrateConfig();
    }
    @Override
    public void onEnable() {
        instance = this;
        jar = new File(getFile().getAbsolutePath());
        initConfig();
        startup();
    }

    @Override
    public void onDisable() {
        getScriptManager().loadDisableScript();
        if(bot != null)
            bot.shutdownNow();
        ScriptBot.log("Shutting down...");

    }


    public static void log(String l){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&7S&8B&f] "+l));

    }

    private void startup(){
        System.out.println("\n  ____            _       _   ____        _   \n" +
                " / ___|  ___ _ __(_)_ __ | |_| __ )  ___ | |_ \n" +
                " \\___ \\ / __| '__| | '_ \\| __|  _ \\ / _ \\| __|\n" +
                "  ___) | (__| |  | | |_) | |_| |_) | (_) | |_ \n" +
                " |____/ \\___|_|  |_| .__/ \\__|____/ \\___/ \\__|\n" +
                "                   |_|                        \n" +
                "\nMade by VRCube, Any bug please report on Github\n" +
                "\nJDA:                "+ JDAInfo.VERSION+
                "\nJava:               "+System.getProperty("java.version")+
                "\nScriptBot:          "+version+
                "\nBukkit:             "+ Bukkit.getBukkitVersion());
        try{
            bot = new JDABuilder(AccountType.BOT).setToken(Config.getToken()).addEventListener(new EventManager()).setAudioEnabled(false).setGame(Game.of(Config.getGame())).buildBlocking();
        }catch (Exception e){
            log("&4&lFailed to login! Please check your token and try again. Plugin will now disable.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        log("&aLogged in as "+bot.getSelfUser().getName());
        new File(ScriptBot.getInstance().getDataFolder(), "scripts").mkdirs();
        scriptManager.load();
        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
        if(Config.isMetricsEnabled()){
            metrics = new Metrics(this);
            log("metrics loaded.");
        }
        getScriptManager().loadEnableScript();
        getCommand("scriptbot").setExecutor(new MainCommand());
        log("&aFinished Loading!");
        UpdateScheduler.start();
        schedulePresenceUpdate();
    }
    public static File getJar(){
        return jar;
    }
    private void schedulePresenceUpdate(){
        new BukkitRunnable(){
            @Override
            public void run() {
                bot.getPresence().setGame(Game.of(Config.getGame()));
            }
        }.runTaskTimer(this, 0, 200);
    }

}
