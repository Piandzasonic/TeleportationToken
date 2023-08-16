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

public class TPQCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {
            if(args.length == 1) {

                Player acceptor = (Player) sender;
                UUID acceptorUUID = acceptor.getUniqueId();
                receivedRequestList = getReceivedRequestsList(acceptorUUID.toString());

                List<String> requestorsNameList;
                requestorsNameList = getOfflinePlayerNamesFromUUID(receivedRequestList);

                if(requestorsNameList.contains(args[0])) {
                    Player requester = Bukkit.getPlayer(args[0]);
                    if(requester != null) {
                        UUID requestorUUID = requester.getUniqueId();
                        requester.sendMessage(acceptor.getName() + ChatColor.GREEN + " has declined your teleport request.");
                        TeleportCompleteInFile(requestorUUID, acceptorUUID);
                        acceptor.sendMessage(ChatColor.GREEN + "You declined " + ChatColor.WHITE + requester.getName() + ChatColor.GREEN + " 's teleportation request.");
                    }else {
                        acceptor.sendMessage(args[0] + ChatColor.GREEN + " is not online now.");
                    }
                }else {
                    TextComponent message = new TextComponent(args[0] + ChatColor.GREEN + " doesn't have a teleport request to you. ");
                    message.addExtra(openSquareBracket);
                    message.addExtra(showListClick);
                    message.addExtra(closeSquareBracket);

                    showListClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpalist"));

                    acceptor.spigot().sendMessage(message);
                }

                return true;
            }
            return false;
        }
        return false;
    }

}
