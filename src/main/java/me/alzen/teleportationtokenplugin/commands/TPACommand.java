package me.alzen.teleportationtokenplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class TPACommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {
            if(args.length == 1) {

                Player player = (Player) sender;
                UUID uuid = player.getUniqueId();
                receivedRequestList = getReceivedRequestsList(uuid.toString());

                List<String> requestorsNameList;
                requestorsNameList = getOfflinePlayerNamesFromUUID(receivedRequestList);

                if(requestorsNameList.contains(args[0])) {
                    Player requester = Bukkit.getPlayer(args[0]);
                    if(requester != null) {
                        UUID requestorUUID = requester.getUniqueId();
                        if(checkIfPlayerHasTeleportToken(requester)) {

                            TeleportPlayer(requester, player.getLocation());
                            requester.sendMessage(ChatColor.GREEN + "You have been teleported to " + ChatColor.WHITE + player.getName());

                            TeleportCompleteInFile(requestorUUID, uuid);

                            player.sendMessage(ChatColor.GREEN + "You accepted " + ChatColor.WHITE + requester.getName() + ChatColor.GREEN + " 's teleportation request.");
                        }else {
                            TeleportCompleteInFile(requestorUUID, uuid);
                            player.sendMessage(args[0] + ChatColor.GREEN + " doesn't have a teleportation token at this time. Request is removed");
                        }
                    }else {
                        player.sendMessage(args[0] + ChatColor.GREEN + " is not online now.");
                    }
                }else {
                    TextComponent message = new TextComponent(args[0] + ChatColor.GREEN + " doesn't have a teleport request to you. ");
                    message.addExtra(openSquareBracket);
                    message.addExtra(showListClick);
                    message.addExtra(closeSquareBracket);

                    showListClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpalist"));

                    player.spigot().sendMessage(message);
                }

                return true;
            }
            return false;
        }
        return false;
    }

}
