package me.alzen.teleportationtokenplugin.commands;

import me.alzen.teleportationtokenplugin.model.TeleportationToken;
import me.alzen.teleportationtokenplugin.TeleportationTokenPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TPHCommand extends TeleportCommand implements CommandExecutor {

    private final TeleportationTokenPlugin pluginInstance = TeleportationTokenPlugin.getPluginInstance();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if(args.length == 1) {
                homeList = getHomeList(playerUUID.toString());

                if(homeList.contains(args[0])) {

                    if(checkIfPlayerHasTeleportToken(player)) {
                        Location target = getGome(playerUUID.toString(), args[0]);
                        boolean allowTP = false;
                        List<Entity> attachedEntities = getAttachedEntities(player);
                        if(attachedEntities.isEmpty()) {

                            TeleportPlayer(player, target);
                            allowTP = true;
                        }else {
                            int tokenCount = getTokenCount(player);
                            int requiredToken = attachedEntities.size() + 1;
                            if(tokenCount >= requiredToken) {
                                Entity vehicle = player.getVehicle();

                                if(vehicle != null) {
                                    player.leaveVehicle();
                                }

                                for(Entity entity : attachedEntities) {
                                    entity.teleport(target);
                                }

                                TeleportPlayer(player, target);

                                if(vehicle != null) {
                                    vehicle.addPassenger(player);
                                }

                                player.getInventory().removeItem(new TeleportationToken(pluginInstance, attachedEntities.size()).getToken());

                                allowTP = true;
                            }else {
                                player.sendMessage(ChatColor.GREEN + "You do not have enough token. You need " + ChatColor.WHITE +  requiredToken + ChatColor.GREEN + ". You currently have " + ChatColor.WHITE + tokenCount);
                            }
                        }

                        if(allowTP) {
                            player.sendMessage(ChatColor.GREEN + "You have been teleported to " + ChatColor.WHITE + args[0]);
                        }
                    }else {
                        player.sendMessage(ChatColor.GREEN + "You do not have a teleportation token.");
                    }
                }else {
                    NotInHomeList(player, args[0]);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private int getTokenCount(Player player) {
        int count = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for(ItemStack item : contents) {
            if(item != null && Objects.requireNonNull(item.getItemMeta()).getDisplayName().equalsIgnoreCase("Teleportation Token")) {
                count += item.getAmount();
            }
        }

        return count;
    }

    private List<Entity> getAttachedEntities(Player player){
        List<Entity> attachedEntities = new ArrayList<>();

        // Get mounted entities
        Entity vehicle = player.getVehicle();
        if(vehicle != null &&!(vehicle instanceof Boat)) {
            attachedEntities.add(vehicle);
        }

        // Get leashed entities
        for (Entity nearbyEntity : player.getNearbyEntities(10, 10, 10)) {
            if (nearbyEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) nearbyEntity;
                if (livingEntity.isLeashed() && livingEntity.getLeashHolder().equals(player)) {
                    attachedEntities.add(livingEntity);
                }
            }
        }

        return attachedEntities;
    }

}
