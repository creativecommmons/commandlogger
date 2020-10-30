package com.creativecommmons.serverspy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class commandlogger extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("output-file-type", "csv");
        config.options().copyDefaults(true);
        saveConfig();

        switch (config.getString("output-file-type")) {
            case "csv":
                //
                if (!(new File(this.getDataFolder(),"command-log.csv").exists())) {
                    this.saveResource("command-log.csv", false);
                }
                break;
            case "txt":
                //
                if (!(new File(this.getDataFolder(),"command-log.txt").exists())) {
                    this.saveResource("command-log.txt", false);
                }
                break;
            default:
                getLogger().warning("config.yml: 'output-file-type' value not recognised, switching to '.csv' filetype.");
                this.saveResource("command-log.csv", false);
                break;
        }



        getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) throws Exception {
        //System.out.println("["+ event.getPlayer().getName() + "] : " + event.getMessage());

        String fileType = config.getString("output-file-type");

        FileWriter writer = new FileWriter("plugins/serverspy/command-log." + fileType, true);

        String simpleDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        String playerName = event.getPlayer().getName();
        String command = event.getMessage();

        switch (fileType) {
            case "csv":
                CSVUtils.writeLine(writer, Arrays.asList(simpleDate, playerName, command));
                break;
            case "txt":
                writer.write(simpleDate + " | " + playerName + " | " + command + "\n");
                break;
            default:
                getLogger().warning("Whoops, a problem arose!");
                break;
        }

        writer.flush();
        writer.close();
    }
}
