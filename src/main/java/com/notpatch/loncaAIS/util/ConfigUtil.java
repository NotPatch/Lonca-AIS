package com.notpatch.loncaAIS.util;

import com.notpatch.loncaAIS.Lonca;
import com.notpatch.loncaAIS.model.Guild;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtil {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private static final Configuration config = Lonca.getInstance().getConfig();

    public static void sendHelpMessage(Player p){
        for(String message : Lonca.getInstance().getConfig().getStringList("messages.help")){
            p.sendMessage(getColored(message));
        }
    }

    public static void sendInfoMessage(Player p, Guild guild){
        if(guild == null){
            return;
        }
        for(String message : Lonca.getInstance().getConfig().getStringList("messages.info")){
            List<UUID> members = guild.getMembers();
            List<String> memberNames = new ArrayList<>();



            for(UUID member : members){
                if(Bukkit.getPlayer(member) == null){
                    continue;
                }
                memberNames.add(Bukkit.getPlayer(member).getName());
            }
            p.sendMessage(getColored(message).replace("%guild%", guild.getName()).replace("%rating%", String.valueOf(guild.getLevel())).replace("%members%", memberNames.toString()).replace("%leader%", Bukkit.getPlayer(guild.getLeader()).getName()));
        }
    }

    public static String getRankingMessage(Guild guild, int rank){
        if(guild != null){
            return getColored(config.getString("messages.ranking")).replace("%guild%", guild.getName()).replace("%kdr%", String.valueOf(guild.getTotalKDR())).replace("%order%", String.valueOf(rank));
        }
        return "";
    }

    public static void sendMessage(Player p, String message){
        p.sendMessage(getColored(config.getString("prefix")) + getColored(config.getString("messages." + message)));
    }

    public static String getMessage(String message, Guild guild){
        if(guild == null){
            return "";
        }
        return getColored(getColored(config.getString("prefix")) + config.getString("messages." + message)).replaceAll("%guild%", guild.getName());
    }

    public static String hexColor(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + ChatColor.of(color));
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getColored(String message) {
        return hexColor(message);
    }

}
