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
import java.util.ArrayList;
import java.util.UUID;

public class TPRCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {

        if(sender instanceof Player) {
            if(args.length == 1) {

                Player requester = (Player) sender;
                Player target = Bukkit.getPlayer(args[0]);

                if(target != null && target.isOnline()) {

                    UUID requesterID = requester.getUniqueId();
                    UUID targetID = target.getUniqueId();

                    String requesterName = requester.getName();

                    if(!requesterID.equals(targetID)) {

                        if(checkIfPlayerHasTeleportToken(requester)) {
                            sentRequestList = new ArrayList<>();
                            receivedRequestList = new ArrayList<>();

                            sentRequestList = getSentRequestsList(requesterID.toString());
                            if(!sentRequestList.contains(targetID.toString())) {

                                sentRequestList.add(targetID.toString());
                                setSendRequestList(requesterID.toString(), sentRequestList);

                                TextComponent requesterMessage = new TextComponent(ChatColor.GREEN + "You sent a request to " + ChatColor.WHITE + args[0] + " ");
                                requesterMessage.addExtra(openSquareBracket);
                                TextComponent cancelRequestMessage = new TextComponent(ChatColor.YELLOW + "CANCEL");
                                requesterMessage.addExtra(cancelRequestMessage);
                                requesterMessage.addExtra(closeSquareBracket);
                                cancelRequestMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpx " + target.getName()));

                                requester.spigot().sendMessage(requesterMessage);

                                receivedRequestList = getReceivedRequestsList(targetID.toString());
                                receivedRequestList.add(requesterID.toString());
                                setReceivedRequestList(targetID.toString(), receivedRequestList);

                                TextComponent targetMessage = new TextComponent(requester.getName() + ChatColor.GREEN + " requested to teleport to you. " + ChatColor.WHITE);

                                acceptClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa " + requesterName));
                                declineClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpq " + requesterName));

                                targetMessage.addExtra(openSquareBracket);
                                targetMessage.addExtra(acceptClick);
                                targetMessage.addExtra(lineSeparator);
                                targetMessage.addExtra(declineClick);
                                targetMessage.addExtra(closeSquareBracket);
                                target.spigot().sendMessage(targetMessage);

                            }else {
                                requester.sendMessage(ChatColor.GREEN + "You already sent a request to " + ChatColor.WHITE + args[0]);
                            }
                        }else {
                            requester.sendMessage(ChatColor.GREEN + "You do not have a teleportation token.");
                        }


                    }else {
                        requester.sendMessage(ChatColor.GREEN + "You can't send a request to yourself.");
                    }
                }else {
                    requester.sendMessage(args[0] + ChatColor.GREEN + " player not found.");
                }

                return true;
            }
            return false;
        }
        return false;
    }

}
