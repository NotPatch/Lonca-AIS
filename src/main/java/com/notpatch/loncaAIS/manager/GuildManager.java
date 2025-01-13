package com.notpatch.loncaAIS.manager;

import com.notpatch.loncaAIS.Lonca;
import com.notpatch.loncaAIS.model.Guild;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuildManager {

    public HashMap<Guild, List<Player>> invites = new HashMap<>();

    public GuildManager(Lonca main){

    }

    private List<Guild> guilds = new ArrayList<>();

    public Guild getGuild(String id) {
        for (Guild guild : guilds) {
            if (guild.getId().equals(id)) {
                return guild;
            }
        }
        return null;
    }

    public Guild getGuild(Player player) {
        for (Guild guild : guilds) {
            if (guild.isMember(player.getUniqueId())) {
                return guild;
            }
        }
        return null;
    }

    public String getGuildStar(Guild guild){
        Lonca lonca = Lonca.getInstance();
        Configuration configuration = lonca.getConfig();
        int level = guild.getLevel();
        for(String line : configuration.getConfigurationSection("stars").getKeys(false)){
            if(level >= Integer.parseInt(line)){
                return configuration.getString("stars." + line);
            }
        }
        return "";
    }

    public List<Guild> getGuilds() {
        return guilds;
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
    }

    public void removeGuild(Guild guild) {
        guilds.remove(guild);
    }

    public void saveGuilds() {
        Configuration configuration = Lonca.getInstance().getConfigurationManager().getGuildData().getConfiguration();
        for(Guild guild : guilds) {
            configuration.set("guilds." + guild.getId() + ".name", guild.getName());
            configuration.set("guilds." +  guild.getId() + ".leader", guild.getLeader().toString());
            List<String> members = new ArrayList<>();
            for(UUID member : guild.getMembers()) {
                members.add(member.toString());
            }
            configuration.set("guilds." + guild.getId() + ".members", members);
            configuration.set("guilds." + guild.getId() + ".level", guild.getLevel());
        }
        Lonca.getInstance().getConfigurationManager().getGuildData().saveConfiguration();
    }

    public void loadGuilds() {
        Configuration configuration = Lonca.getInstance().getConfigurationManager().getGuildData().getConfiguration();
        if(configuration.getConfigurationSection("guilds") == null) {
            return;
        }
        for(String id : configuration.getConfigurationSection("guilds").getKeys(false)) {
            String name = configuration.getString("guilds." + id + ".name");
            UUID leader = UUID.fromString(configuration.getString("guilds." + id + ".leader"));
            List<UUID> members = new ArrayList<>();
            for(String member : configuration.getStringList("guilds." + id + ".members")) {
                members.add(UUID.fromString(member));
            }
            int level = configuration.getInt("guilds." + id + ".level");
            Guild guild = new Guild(name, leader, members, level);
            addGuild(guild);
        }
    }

    public HashMap<Guild, List<Player>> getInvites() {
        return invites;
    }

}
