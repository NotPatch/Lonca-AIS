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
            return Lonca.getInstance().getGuildManager().getGuild(p).getName();
        }
        if(params.equalsIgnoreCase("guild_leader")) {
            return Bukkit.getPlayer(Lonca.getInstance().getGuildManager().getGuild(p).getLeader()).getName();
        }
        if(params.equalsIgnoreCase("guild_rating")) {
            return Lonca.getInstance().getGuildManager().getGuild(p).getLevel() + "";
        }
        if(params.equalsIgnoreCase("guild_star")) {
            return ConfigUtil.hexColor(Lonca.getInstance().getGuildManager().getGuildStar(Lonca.getInstance().getGuildManager().getGuild(p)));
        }
        return null;
    }

}
