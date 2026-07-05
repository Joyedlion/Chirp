package xyz.pgstudios.chirp;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pgstudios.chirp.commands.BroadcastCommand;
import xyz.pgstudios.chirp.commands.ChirpCommand;

import java.io.File;
import java.io.IOException;

public final class Chirp extends JavaPlugin {


    private JDA jda;
    public File discordFile;
    public FileConfiguration discordConfig;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        createDiscordFile();
        String token = getDiscordConfig().getString("bot.token");
        String status = getDiscordConfig().getString("bot.status");
        String activity = getDiscordConfig().getString("bot.activity");
        Boolean discord = getDiscordConfig().getBoolean("enable", false);
        FileConfiguration config = getConfig();
        createDiscordFile();
        getLogger().info("Chirp has successfully been ENABLED!");
        getLogger().info("Check out our Discord! https://dsc.gg/pgstudios");

        if (discord == false) {
            System.out.println("Discord is NOT enabled. Not running");
        } else {
            if (token == null || token.isEmpty()) {
                getLogger().severe("The discord feature is enabled yet the bot token is not set. Please set the bot token or disable the discord feature to use this.");
                getServer().getPluginManager().disablePlugin(this);
            } else {
                JDABuilder builder = JDABuilder.createDefault(token);
                builder.setActivity(Activity.playing(activity));
                builder.setStatus(OnlineStatus.valueOf(status.toUpperCase()));
                builder.addEventListeners(new xyz.pgstudios.chirp.discord.chat(this));
                try {
                    jda = builder.build();
                    getLogger().info("The bot has connected to Chirp!");
                } catch (Exception e) {
                    getLogger().info("The bot has failed to connect to Chirp");
                    e.printStackTrace();
                }
            }
        }
        getCommand("chirp").setTabCompleter((TabCompleter) new ChirpCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("bc").setExecutor(new BroadcastCommand(this));
        getCommand("announce").setExecutor(new BroadcastCommand(this));
        getCommand("chirp").setExecutor(new ChirpCommand(this));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (jda != null) {
            jda.shutdown();
        }
    }
    private void createDiscordFile() {
        discordFile = new File(getDataFolder(), "discord.yml");
        if (!discordFile.exists()) {
            discordFile.getParentFile().mkdirs();
            saveResource("discord.yml", false);
        }
        discordConfig = YamlConfiguration.loadConfiguration(discordFile);
    }
    public void reloadPluginConfigs() {
        reloadConfig();
        discordFile = new File(getDataFolder(), "discord.yml");
        discordConfig = YamlConfiguration.loadConfiguration(discordFile);
        getLogger().info("All Config Files has been reloaded!");
        String token = getDiscordConfig().getString("bot.token");
        String status = getDiscordConfig().getString("bot.status");
        String activity = getDiscordConfig().getString("bot.activity");
        Boolean discord = getDiscordConfig().getBoolean("enable", false);

        if (!discord) {
            if (jda != null) {
                getLogger().info("Discord Feature was disabled in config. Shutting down the bot.");
                jda.shutdown();
                jda = null;
            }
            return;
        }

        if (jda == null) {
            if (token != null && !token.isEmpty()) {
                try {
                    JDABuilder builder = JDABuilder.createDefault(token);
                    jda.getPresence().setActivity(Activity.playing(activity));
                    builder.setStatus(OnlineStatus.valueOf(status.toUpperCase()));
                    builder.addEventListeners(new xyz.pgstudios.chirp.discord.chat(this));
                    jda = builder.build();
                    getLogger().info("The bot has connected to Chirp via reload!");
                } catch (Exception e) {
                    getLogger().severe("The Bot failed to connect during the reload!");
                    e.printStackTrace();
                }
            }
        } else {
            try {
                jda.getPresence().setStatus(OnlineStatus.valueOf(status.toUpperCase()));
                jda.getPresence().setActivity(Activity.playing(activity));
                getLogger().info("Discord bot and status updated successfully!");
            } catch (IllegalArgumentException e) {
                getLogger().severe("Invalid Status Provided in Discord.yml! Use ONLINE, IDLE, DND, or INVISIBLE");
            }
        }

    }
    public FileConfiguration getDiscordConfig() {
        return this.discordConfig;
    }
    public void saveDiscordConfig() {
        try {
            getDiscordConfig().save(discordFile);
        } catch (IOException e) {
            getLogger().severe("Could not save discord.yml!");
            e.printStackTrace();
        }
    }
}

