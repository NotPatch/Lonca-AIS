package com.notpatch.loncaAIS.configuration.impl;

import com.notpatch.loncaAIS.Lonca;
import com.notpatch.loncaAIS.configuration.Configuration;

public class GuildData extends Configuration {

    public GuildData() {
        super(Lonca.getInstance(), "guilds.yml");
    }
}
