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

import java.util.*;

public class CommandMain implements TabCompleter, TabExecutor {

    private HashMap<Player, String> actionsConfirm = new HashMap<>();

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
                }else if(args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("katıl")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    Guild guild = Lonca.getInstance().getGuildManager().getGuild(args[1].toLowerCase());

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
                    if(args.length == 1) {
                        if(Lonca.getInstance().getGuildManager().getGuild(p) == null){
                            ConfigUtil.sendMessage(p, "guild-not-found");
                        }
                        Guild guild = Lonca.getInstance().getGuildManager().getGuild(p);
                        if(guild == null) {
                            ConfigUtil.sendMessage(p, "guild-not-found");
                        }else{
                            ConfigUtil.sendInfoMessage(p, guild);
                        }
                        return false;
                    }else{
                        if(Lonca.getInstance().getGuildManager().getGuild(args[1].toLowerCase()) == null){
                            ConfigUtil.sendMessage(p, "guild-not-found");
                            return false;
                        }

                        Guild guild = Lonca.getInstance().getGuildManager().getGuild(args[1].toLowerCase());

                        if(guild == null) {
                            ConfigUtil.sendMessage(p, "guild-not-found");
                            return false;
                        }
                        ConfigUtil.sendInfoMessage(p, guild);
                    }


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

                    if(actionsConfirm.get(p) == null){
                        actionsConfirm.put(p, "leave");
                        ConfigUtil.sendMessage(p, "confirm-action");
                        return false;
                    }

                    if(guild.getLeader().equals(p.getUniqueId())) {
                        guild.getMembers().clear();
                        Lonca.getInstance().getGuildManager().removeGuild(guild);
                        actionsConfirm.remove(p);
                    }else{
                        guild.removeMember(p.getUniqueId());
                        actionsConfirm.remove(p);
                    }

                    ConfigUtil.sendMessage(p, "left-guild");
                }else if(args[0].equalsIgnoreCase("sıralama")){
                    if(args.length != 1) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    List<Guild> guilds = Lonca.getInstance().getGuildManager().getGuilds();
                    guilds.sort(Comparator.comparingDouble(Guild::getTotalKDR).reversed());
                    List<Guild> top5Guilds = guilds.stream().limit(5).toList();
                    for (int i = 0; i < top5Guilds.size(); i++) {
                        Guild guild = top5Guilds.get(i);
                        p.sendMessage(ConfigUtil.getRankingMessage(guild, i + 1));

                    }
                }else if(args[0].equalsIgnoreCase("devret") || args[0].equalsIgnoreCase("changeleader")){
                    if(args.length != 2) {
                        ConfigUtil.sendHelpMessage(p);
                        return false;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if(target != null && !target.isOnline()){
                        Guild guild = Lonca.getInstance().getGuildManager().getGuild(p);
                        if(guild == null) {
                            ConfigUtil.sendMessage(p, "not-in-guild");
                            return false;
                        }
                        if(!guild.getLeader().equals(p.getUniqueId())) {
                            ConfigUtil.sendMessage(p, "dont-have-permission");
                            return false;
                        }
                        if(!guild.isMember(target.getUniqueId())) {
                            ConfigUtil.sendMessage(p, "player-is-not-in-guild");
                            return false;
                        }
                        guild.setLeader(target.getUniqueId());
                        ConfigUtil.sendMessage(p, "leader-changed");
                    }

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
            return List.of("oluştur", "davet", "at", "bilgi", "ayrıl", "sıralama", "devret");
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("bilgi")){
                List<String> guilds = new ArrayList<>();
                for (Guild guild : Lonca.getInstance().getGuildManager().getGuilds()) {
                    guilds.add(guild.getName());
                }
                return guilds;
            } else if (args[0].equalsIgnoreCase("davet") || args[0].equalsIgnoreCase("invite")) {
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
            }else if(args[0].equalsIgnoreCase("devret")){
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