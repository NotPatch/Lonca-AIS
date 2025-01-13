package com.notpatch.loncaAIS.listener;

import com.notpatch.loncaAIS.Lonca;
import com.notpatch.loncaAIS.model.Guild;
import com.notpatch.loncaAIS.util.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDamageListener implements Listener{

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();

            Guild guild = Lonca.getInstance().getGuildManager().getGuild(damager);
            if(guild == null) return;

            if(guild.isMember(victim.getUniqueId())){
                e.setCancelled(true);
                ConfigUtil.sendMessage(damager, "cant-attack");
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Player){

            Player p = (Player) e.getEntity();
            Player killer = p.getKiller();
            Guild guild = Lonca.getInstance().getGuildManager().getGuild(p);

            if(killer != null){
                Guild killerGuild = Lonca.getInstance().getGuildManager().getGuild(killer);
                if(killerGuild != null){
                    killerGuild.setLevel((int) killerGuild.getTotalKDR());
                    killer.sendMessage(String.valueOf(killerGuild.getLevel()));
                }
            }

            if(guild == null) return;

            guild.setLevel((int) guild.getTotalKDR());
            
        }
    }

}
