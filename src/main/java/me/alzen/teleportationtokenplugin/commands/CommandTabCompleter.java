package me.alzen.teleportationtokenplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandTabCompleter extends TeleportCommand implements TabCompleter {

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        List<String> suggestions = new ArrayList<>();
        List<String> UUIDs;

        if(sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if(cmd.getName().equalsIgnoreCase("tpr")) {
                if(args.length == 1) {
                    suggestions = getOnlinePlayers();
                    suggestions.remove(player.getName());
                }
            }else if(cmd.getName().equalsIgnoreCase("tpx")) {
                if(args.length == 1) {
                    UUIDs = getSentRequestsList(playerUUID.toString());
                    suggestions = getOfflinePlayerNamesFromUUID(UUIDs);
                }
            }else if(cmd.getName().equalsIgnoreCase("tpa")) {
                if(args.length == 1) {
                    UUIDs = getReceivedRequestsList(playerUUID.toString());
                    suggestions = getOfflinePlayerNamesFromUUID(UUIDs);
                }
            }else if(cmd.getName().equalsIgnoreCase("tprm") || cmd.getName().equalsIgnoreCase("tph")) {
                if(args.length == 1) {
                    suggestions = getHomeList(playerUUID.toString());
                }
            }
        }

        return suggestions;
    }

    private List<String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
