package me.alzen.teleportationtokenplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TPSHCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,@Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if(args.length == 1) {

                if(checkIfPlayerHasTeleportToken(player)){
                    homeList = getHomeList(playerUUID.toString());

                    if(!homeList.contains(args[0])) {

                        homeList.add(args[0]);
                        setHomeList(playerUUID.toString(), homeList);

                        Location location = player.getLocation();
                        setHome(playerUUID.toString(), args[0], location);

                        player.sendMessage(args[0] + ChatColor.GREEN + " has been added to your home list");

                    }else {
                        TextComponent wholeMessage = new TextComponent(ChatColor.GREEN + "You already have " + ChatColor.WHITE + args[0] + ChatColor.GREEN + " in you home list. ");
                        wholeMessage.addExtra(openSquareBracket);
                        wholeMessage.addExtra(showListClick);
                        wholeMessage.addExtra(closeSquareBracket);

                        showListClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tphlist"));

                        player.spigot().sendMessage(wholeMessage);

                    }
                }else{
                    player.sendMessage(ChatColor.GREEN + "You do not have a teleportation token.");
                }
                return true;
            }
            return false;
        }
        return false;
    }

}
