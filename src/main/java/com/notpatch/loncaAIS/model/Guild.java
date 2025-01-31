package com.notpatch.loncaAIS.model;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Guild {

    private String name;
    private UUID leader;
    private List<UUID> members = new ArrayList<>();
    private String id;
    private int level;

    public Guild(String name, UUID leader, List<UUID> members, int level) {
        this.name = name;
        this.level = level;
        this.id = name.toLowerCase();
        this.leader = leader;
        this.members = new ArrayList<>(members);
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    public void setMembers(List<UUID> members) {
        this.members = new ArrayList<>(members);
    }

    public void addMember(UUID player) {
        members.add(player);
    }

    public void removeMember(UUID player) {
        members.remove(player);
    }

    public int getLevel() {
        return level;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public boolean isMember(UUID player) {
        return members.contains(player) || leader.equals(player);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getTotalKDR(){
        int kills = 0;
        int deaths = 0;
        for(UUID p : getMembers()){
            if(p == null){
                continue;
            }
            if(Bukkit.getPlayer(p) == null){
                continue;
            }
            kills += Bukkit.getPlayer(p).getStatistic(Statistic.PLAYER_KILLS);
            deaths += Bukkit.getPlayer(p).getStatistic(Statistic.DEATHS);
        }
        if(deaths == 0){
            return kills;
        }
        double kdr = (double) kills / deaths;
        kdr = Math.round(kdr * 100.0) / 100.0;
        return kdr;
    }
}

