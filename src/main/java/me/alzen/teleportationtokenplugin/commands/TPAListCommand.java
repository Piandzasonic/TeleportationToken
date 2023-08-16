package me.alzen.teleportationtokenplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TPAListCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            if(args.length == 0) {

                receivedRequestList = new ArrayList<>();

                List<String> receivedRequestNames;
                receivedRequestList = getReceivedRequestsList(uuid.toString());
                receivedRequestNames = getOfflinePlayerNamesFromUUID(receivedRequestList);

                TextComponent wholeMessage = new TextComponent(ChatColor.YELLOW + "YOUR RECEIVED TELEPORT REQUESTS\n");

                for(String name : receivedRequestNames) {
                    TextComponent lineMessage = new TextComponent(name + " ");
                    lineMessage.addExtra(openSquareBracket);
                    lineMessage.addExtra(acceptClick);
                    lineMessage.addExtra(lineSeparator);
                    lineMessage.addExtra(declineClick);
                    lineMessage.addExtra(closeSquareBracket);

                    acceptClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa " + name));
                    declineClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpq " + name));

                    wholeMessage.addExtra(lineMessage);
                }

                player.spigot().sendMessage(wholeMessage);

                return true;
            }
            return false;
        }
        return false;
    }

}
