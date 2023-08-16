package me.alzen.teleportationtokenplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TPXCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {

        sentRequestList = new ArrayList<>();
        receivedRequestList = new ArrayList<>();

        if(sender instanceof Player) {

            Player requester = (Player) sender;
            UUID requestorUUID = requester.getUniqueId();

            if(args.length == 1) {
                sentRequestList = getSentRequestsList(requestorUUID.toString());
                List<String> list = getOfflinePlayerNamesFromUUID(sentRequestList);

                if(list.contains(args[0])) {
                    Player target = Bukkit.getPlayer(args[0]);
                    UUID targetID;

                    if(target != null) {
                        targetID = target.getUniqueId();
                    }else {
                        @SuppressWarnings("deprecation")
                        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
                        targetID = offlineTarget.getUniqueId();
                    }

                    sentRequestList.remove(targetID.toString());
                    setSendRequestList(requestorUUID.toString(), sentRequestList);

                    requester.sendMessage(ChatColor.GREEN + "You cancelled your teleport request to " + ChatColor.WHITE + args[0]);

                    receivedRequestList = getReceivedRequestsList(targetID.toString());
                    receivedRequestList.remove(requestorUUID.toString());
                    setReceivedRequestList(targetID.toString(), receivedRequestList);

                    if(target != null) {
                        target.sendMessage(requester.getName() + ChatColor.GREEN + " cancelled their teleport request.");
                    }

                }else {
                    TextComponent message = new TextComponent(ChatColor.GREEN + "You do not have a request to " + ChatColor.WHITE + args[0] + " ");
                    message.addExtra(openSquareBracket);
                    message.addExtra(showListClick);
                    message.addExtra(closeSquareBracket);

                    showListClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tprlist"));

                    requester.spigot().sendMessage(message);
                }
            }
            return true;
        }
        return false;
    }

}
