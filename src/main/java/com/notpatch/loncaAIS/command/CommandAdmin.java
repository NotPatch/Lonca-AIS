package com.notpatch.loncaAIS.command;

import com.notpatch.loncaAIS.Lonca;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!p.isOp()){
                return false;
            }

            if(args.length == 0) {
                return false;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                Lonca.getInstance().reloadConfig();
                Lonca.getInstance().saveDefaultConfig();
                Lonca.getInstance().saveConfig();
                p.sendMessage("Reloading...");
                return true;
            }


        }
        return false;
    }
}
