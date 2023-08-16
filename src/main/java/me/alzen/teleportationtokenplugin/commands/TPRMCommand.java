package me.alzen.teleportationtokenplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TPRMCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if(args.length == 1) {

                homeList = getHomeList(playerUUID.toString());

                if(homeList.contains(args[0])) {
                    homeList.remove(args[0]);
                    setHomeList(playerUUID.toString(), homeList);
                    removeHome(playerUUID, args[0]);
                    player.sendMessage(args[0] + ChatColor.GREEN + " has been removed from your home list");
                }else {
                    NotInHomeList(player, args[0]);
                }

                return true;
            }
            return false;
        }
        return false;
    }

}
