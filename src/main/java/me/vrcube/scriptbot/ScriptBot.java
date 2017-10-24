package me.vrcube.scriptbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ScriptBot extends JavaPlugin {
    private static ScriptBot instance;

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

    public static double version = 1.0;
    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        instance = this;
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
    }

    @Override
    public void onDisable() {
        if(bot != null)
            bot.shutdownNow();
    }

    public static void log(String l){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&7S&8B&f] "+l));

    }


}
