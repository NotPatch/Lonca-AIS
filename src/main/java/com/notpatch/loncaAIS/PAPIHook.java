package com.notpatch.loncaAIS;

import com.notpatch.loncaAIS.util.ConfigUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "lonca";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NotPatch";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Player p = (Player) player;
        if(params.equalsIgnoreCase("guild_name")) {
            if(Lonca.getInstance().getGuildManager().getGuild(p) == null){
                return "";
            }
            String guildName = Lonca.getInstance().getGuildManager().getGuild(p).getName();
            if(guildName == null){
                return "";
            }
            return guildName;
        }
        if(params.equalsIgnoreCase("guild_leader")) {
            if(Lonca.getInstance().getGuildManager().getGuild(p) == null){
                return "";
            }
            String leader = Bukkit.getPlayer(Lonca.getInstance().getGuildManager().getGuild(p).getLeader()).getName();
            if(leader == null){
                return "";
            }
            return Bukkit.getPlayer(Lonca.getInstance().getGuildManager().getGuild(p).getLeader()).getName();
        }
        if(params.equalsIgnoreCase("guild_rating")) {
            if(Lonca.getInstance().getGuildManager().getGuild(p) == null){
                return "";
            }
            return Lonca.getInstance().getGuildManager().getGuild(p).getLevel() + "";
        }
        if(params.equalsIgnoreCase("guild_star")) {
            if(Lonca.getInstance().getGuildManager().getGuild(p) == null){
                return "";
            }
            String star = Lonca.getInstance().getGuildManager().getGuildStar(Lonca.getInstance().getGuildManager().getGuild(p));
            if(star == null){
                return "";
            }
            return ConfigUtil.hexColor(Lonca.getInstance().getGuildManager().getGuildStar(Lonca.getInstance().getGuildManager().getGuild(p)));
        }
        return null;
    }

}
