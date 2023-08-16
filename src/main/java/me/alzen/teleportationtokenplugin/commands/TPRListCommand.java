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

public class TPRListCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            if(args.length == 0) {

                sentRequestList = new ArrayList<>();

                List<String> sentRequestNames;
                sentRequestList = getSentRequestsList(uuid.toString());
                sentRequestNames = getOfflinePlayerNamesFromUUID(sentRequestList);

                TextComponent wholeMessage = new TextComponent(ChatColor.YELLOW + "YOUR SENT TELEPORT REQUESTS\n");

                for(String name : sentRequestNames) {
                    TextComponent lineMessage = new TextComponent(name + " ");
                    lineMessage.addExtra(openSquareBracket);
                    TextComponent cancelClick = new TextComponent(ChatColor.YELLOW + "CANCEL");
                    lineMessage.addExtra(cancelClick);
                    lineMessage.addExtra(closeSquareBracket);

                    cancelClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpx " + name));
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
