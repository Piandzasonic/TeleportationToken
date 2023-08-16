package me.alzen.teleportationtokenplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TPHListCommand extends TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {

                Player player = (Player) sender;
                UUID playerUUID = player.getUniqueId();

                homeList = getHomeList(playerUUID.toString());

                TextComponent wholeMessage = new TextComponent(ChatColor.YELLOW + "YOUR HOME LIST\n");
                for(String home : homeList) {
                    TextComponent lineMessage = new TextComponent(home + " ");
                    lineMessage.addExtra(openSquareBracket);
                    TextComponent teleportClick = new TextComponent(ChatColor.YELLOW + "TELEPORT");
                    lineMessage.addExtra(teleportClick);
                    lineMessage.addExtra(lineSeparator);
                    TextComponent removeClick = new TextComponent(ChatColor.YELLOW + "REMOVE");
                    lineMessage.addExtra(removeClick);
                    lineMessage.addExtra(closeSquareBracket);

                    teleportClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tph " + home));
                    removeClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tprm " + home));

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
