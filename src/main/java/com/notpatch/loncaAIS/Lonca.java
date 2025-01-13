package com.notpatch.loncaAIS;

import com.notpatch.loncaAIS.command.CommandAdmin;
import com.notpatch.loncaAIS.command.CommandMain;
import com.notpatch.loncaAIS.configuration.ConfigurationManager;
import com.notpatch.loncaAIS.listener.EntityDamageListener;
import com.notpatch.loncaAIS.manager.GuildManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lonca extends JavaPlugin {

    private static Lonca instance;

    private static Economy econ = null;

    private GuildManager guildManager;
    private ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveConfig();

        configurationManager = new ConfigurationManager();
        configurationManager.loadConfigurations();
        guildManager = new GuildManager(this);
        guildManager.loadGuilds();

        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        getCommand("lonca").setExecutor(new CommandMain());
        getCommand("loncaadmin").setExecutor(new CommandAdmin());

        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            getLogger().info("PlaceholderAPI found! Enabling placeholders...");
            new PAPIHook().register();
        }

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        configurationManager.saveConfigurations();
        guildManager.saveGuilds();
    }

    public static Lonca getInstance() {
        return instance;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public static Economy getEcon() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
