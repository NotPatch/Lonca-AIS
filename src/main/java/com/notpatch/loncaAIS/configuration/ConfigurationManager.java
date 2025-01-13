package com.notpatch.loncaAIS.configuration;

import com.notpatch.loncaAIS.configuration.impl.GuildData;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {

    private final List<Configuration> configurations = new ArrayList<>();

    private final GuildData guildData;

    public ConfigurationManager() {
        configurations.add(guildData = new GuildData());
    }

    public void loadConfigurations() {
        for (Configuration configuration : configurations) {
            configuration.loadConfiguration();
        }
    }

    public void saveConfigurations() {
        for (Configuration configuration : configurations) {
            configuration.saveConfiguration();
        }
    }

    public GuildData getGuildData() {
        return guildData;
    }
}
