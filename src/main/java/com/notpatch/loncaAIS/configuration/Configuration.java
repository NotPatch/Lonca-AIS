package com.notpatch.loncaAIS.configuration;

import com.notpatch.loncaAIS.Lonca;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Configuration extends YamlConfiguration {

    protected Lonca main;
    protected String name;
    protected File file;
    protected FileConfiguration configuration;

    public Configuration(Lonca main, String name){
        this.name = name;
        this.main = main;
        file = new File(main.getDataFolder(), name);
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    private void checkConfig(){
        if(!file.exists()){
            file.getParentFile().mkdirs();
            main.saveResource(name, false);
        }
    }

    public void loadConfiguration(){
        checkConfig();
        configuration = YamlConfiguration.loadConfiguration(file);
        main.getLogger().info("| Configuration " + "'" + name + "'" + " loaded successful!");
    }

    public void saveConfiguration() {
        try {
            configuration.save(file);
            main.getLogger().info("| Configuration " + "'" + name + "'" + " saved successfully!");
        } catch (IOException exception) {
            main.getLogger().severe("| Configuration " + "'" + name + "'" + " couldn't be saved!");
            exception.printStackTrace();
        }
    }

    public void reloadConfiguration() {
        loadConfiguration();
        main.getLogger().info("| Configuration " + "'" + name + "'" + " reloaded successfully!");
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}