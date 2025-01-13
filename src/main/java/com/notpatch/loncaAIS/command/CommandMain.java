package com.notpatch.loncaAIS.command;

import com.notpatch.loncaAIS.Lonca;
import com.notpatch.loncaAIS.model.Guild;
import com.notpatch.loncaAIS.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandMain implements TabCompleter, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length == 0) {
                ConfigUtil.sendHelpMessage(p);
                return false;
            }else{
                if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("oluştur")) {
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }
                    if(Lonca.getInstance().getGuildManager().getGuild(p) != null) {
                        ConfigUtil.sendMessage(p, "already-in-guild");
                        return false;
                    }

                    if(Lonca.getEcon().getBalance(p) < Lonca.getInstance().getConfig().getInt("create-cost")) {
                        ConfigUtil.sendMessage(p, "not-enough-money");
                        return false;
                    }

                    Lonca.getEcon().withdrawPlayer(p, Lonca.getInstance().getConfig().getInt("create-cost"));
                    Guild guild = new Guild(args[1], p.getUniqueId(), List.of(p.getUniqueId()), 0);
                    Lonca.getInstance().getGuildManager().addGuild(guild);
                    ConfigUtil.sendMessage(p, "guild-created");
                }else if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("davet")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    if(Lonca.getInstance().getGuildManager().getGuild(p) == null) {
                        ConfigUtil.sendMessage(p, "not-in-guild");
                        return false;
                    }

                    if(!Lonca.getInstance().getGuildManager().getGuild(p).getLeader().equals(p.getUniqueId())) {
                        ConfigUtil.sendMessage(p, "dont-have-permission");
                        return false;
                    }

                    Player target = Lonca.getInstance().getServer().getPlayer(args[1]);
                    if(target == null) {
                        ConfigUtil.sendMessage(p, "target-not-found");
                        return false;
                    }

                    if(Lonca.getInstance().getGuildManager().getGuild(target) != null) {
                        ConfigUtil.sendMessage(p, "player-is-in-guild");
                        return false;
                    }

                    Lonca.getInstance().getGuildManager().getInvites()
                            .computeIfAbsent(Lonca.getInstance().getGuildManager().getGuild(p), k -> new ArrayList<>())
                            .add(target);
                    ConfigUtil.sendMessage(p, "invite-send");
                    String message = ConfigUtil.getMessage("invite-received", Lonca.getInstance().getGuildManager().getGuild(p));
                    target.sendMessage(message);

                }else if(args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("at")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    if(Lonca.getInstance().getGuildManager().getGuild(p) == null) {
                        ConfigUtil.sendMessage(p, "not-in-guild");
                        return false;
                    }

                    if(!Lonca.getInstance().getGuildManager().getGuild(p).getLeader().equals(p.getUniqueId())) {
                        ConfigUtil.sendMessage(p, "dont-have-permission");
                        return false;
                    }


                    Player target = Lonca.getInstance().getServer().getPlayer(args[1]);
                    if(target == null) {
                        ConfigUtil.sendMessage(p, "target-not-found");
                        return false;
                    }

                    if(target == p){
                        ConfigUtil.sendMessage(p, "you-cant");
                        return false;
                    }

                    if(!Lonca.getInstance().getGuildManager().getGuild(p).isMember(target.getUniqueId())) {
                        ConfigUtil.sendMessage(p, "player-is-not-in-guild");
                        return false;
                    }
                    Guild guild = Lonca.getInstance().getGuildManager().getGuild(p);
                    guild.removeMember(target.getUniqueId());
                    ConfigUtil.sendMessage(p, "player-kicked");
                }else if(args[0].equalsIgnoreCase("join")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    Guild guild = Lonca.getInstance().getGuildManager().getGuild(args[1]);

                    if(guild == null) {
                        ConfigUtil.sendMessage(p, "guild-not-found");
                        return false;
                    }

                    List<Player> invites = Lonca.getInstance().getGuildManager().getInvites().computeIfAbsent(guild, k -> new ArrayList<>());

                    if(!invites.contains(p)) {
                        ConfigUtil.sendMessage(p, "no-invitation");
                        return false;
                    }

                    guild.addMember(p.getUniqueId());
                    invites.remove(p);
                    ConfigUtil.sendMessage(p, "joined-guild");
                }else if(args[0].equalsIgnoreCase("bilgi")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }
                    Guild guild = Lonca.getInstance().getGuildManager().getGuild(args[1]);
                    if(guild == null) {
                        ConfigUtil.sendMessage(p, "guild-not-found");
                        return false;
                    }

                    ConfigUtil.sendInfoMessage(p, guild);

                }else if(args[0].equalsIgnoreCase("ayrıl")){
                    if(args.length != 1) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    Guild guild = Lonca.getInstance().getGuildManager().getGuild(p);
                    if(guild == null) {
                        ConfigUtil.sendMessage(p, "not-in-guild");
                        return false;
                    }

                    if(guild.getLeader().equals(p.getUniqueId())) {
                        Lonca.getInstance().getGuildManager().removeGuild(guild);
                    }else{
                        guild.removeMember(p.getUniqueId());
                    }
                    ConfigUtil.sendMessage(p, "left-guild");
                }
                else{
                    ConfigUtil.sendHelpMessage(p);
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1) {
            return List.of("oluştur", "davet", "at", "kdr", "bilgi", "ayrıl");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("davet") || args[0].equalsIgnoreCase("invite")) {
                List<String> players = new ArrayList<>();
                Guild guild = Lonca.getInstance().getGuildManager().getGuild((Player) sender);
                for (Player player : Lonca.getInstance().getServer().getOnlinePlayers()) {
                    if (guild != null && guild.isMember(player.getUniqueId())) continue;
                    players.add(player.getName());
                }
                return players;
            } else if (args[0].equalsIgnoreCase("at") ||args[0].equalsIgnoreCase("kick")) {
                List<String> members = new ArrayList<>();
                Guild guild = Lonca.getInstance().getGuildManager().getGuild((Player) sender);
                if (guild != null) {
                    for (UUID member : guild.getMembers()) {
                        if(member.equals(guild.getLeader())) continue;
                        members.add(Bukkit.getPlayer(member).getName());
                    }
                }
                return members;
            }

        }
        return List.of();
    }


}