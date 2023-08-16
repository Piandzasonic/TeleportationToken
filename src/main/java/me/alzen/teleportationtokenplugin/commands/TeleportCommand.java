package me.alzen.teleportationtokenplugin.commands;

import me.alzen.teleportationtokenplugin.model.TeleportationToken;
import me.alzen.teleportationtokenplugin.TeleportationTokenPlugin;
import me.alzen.teleportationtokenplugin.utils.YamlFileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeleportCommand {
    private final TeleportationTokenPlugin pluginInstance = TeleportationTokenPlugin.getPluginInstance();
    private final String dotSeparator = ".";
    private final String requestSentKey = ".RequestsSent";
    private final String requestReceivedKey = ".RequestsReceived";
    private final String homeListKey = ".HomeList";
    private final String worldKey = ".world";
    private final String xKey = ".x";
    private final String yKey = ".y";
    private final String zKey = ".z";
    private final String pitchKey = ".pitch";
    private final String yawKey = ".yaw";

    protected List<String> sentRequestList;
    protected List<String> receivedRequestList;
    protected List<String> homeList;

    protected TextComponent openSquareBracket = new TextComponent("[");
    protected TextComponent closeSquareBracket = new TextComponent("]\n");
    protected TextComponent acceptClick = new TextComponent(ChatColor.YELLOW + "ACCEPT");
    protected TextComponent lineSeparator = new TextComponent(" | ");
    protected TextComponent declineClick = new TextComponent(ChatColor.YELLOW + "DECLINE");
    protected TextComponent showListClick = new TextComponent(ChatColor.YELLOW + "LIST");

    protected List<String> getSentRequestsList(String playerUUID){
        String path = playerUUID + requestSentKey;
        return getList(path);
    }

    protected List<String> getReceivedRequestsList(String playerUUID){
        String path = playerUUID + requestReceivedKey;
        return getList(path);
    }

    protected void setSendRequestList(String playerUUID, List<String> list) {
        YamlFileManager.setData(playerUUID + requestSentKey, list);
    }

    protected void setReceivedRequestList(String playerUUID, List<String> list) {
        YamlFileManager.setData(playerUUID + requestReceivedKey, list);
    }

    protected List<String> getHomeList(String playerUUID) {
        String path = playerUUID + homeListKey;
        return getList(path);
    }

    protected void setHomeList(String playerUUID, List<String> list) {
        YamlFileManager.setData(playerUUID + homeListKey, list);
    }

    protected void setHome(String playerUUID, String homeName, Location loc) {
        YamlFileManager.setData(playerUUID + dotSeparator + homeName, loc);
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + worldKey, Objects.requireNonNull(loc.getWorld()).getName());
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + xKey, loc.getX());
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + yKey, loc.getY());
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + zKey, loc.getZ());
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + pitchKey, loc.getPitch());
        YamlFileManager.setData(playerUUID + dotSeparator + homeName + yawKey, loc.getYaw());
    }

    protected Location getGome(String playerUUID, String homeName) {
        String basePath = playerUUID + dotSeparator + homeName;

        World world = Bukkit.getWorld(YamlFileManager.getString(basePath + worldKey));
        double x = YamlFileManager.getDouble(basePath + xKey);
        double y = YamlFileManager.getDouble(basePath + yKey);
        double z = YamlFileManager.getDouble(basePath + zKey);
        double pitch = YamlFileManager.getDouble(basePath + pitchKey);
        double yaw = YamlFileManager.getDouble(basePath + yawKey);

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    protected void removeHome(UUID id, String homeName) {
        String path = id.toString() + dotSeparator + homeName;
        YamlFileManager.setData(path, null);
    }

    @SuppressWarnings("ConstantConditions")
    private List<String> getList(String path){
        if(!YamlFileManager.getConfigurationFile().contains(path)) {
            YamlFileManager.setData(path, null);
        }

        List<String> list = YamlFileManager.getListString(path);

        if(list == null) {
            return new ArrayList<>();
        }else {
            return list;
        }
    }

    protected List<String> getOfflinePlayerNamesFromUUID(List<String> UUIDs) {
        List<String> result = new ArrayList<>();
        for(String id : UUIDs) {
            UUID uuid = UUID.fromString(id);

            Player target = Bukkit.getPlayer(uuid);
            if(target != null) {
                result.add(target.getName());
            }else {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(uuid);
                result.add(offlineTarget.getName());
            }
        }

        return result;
    }

    protected boolean checkIfPlayerHasTeleportToken(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for(ItemStack item : contents) {
            if(item != null && Objects.requireNonNull(item.getItemMeta()).getDisplayName().equalsIgnoreCase("Teleportation Token")) {
                return true;
            }
        }
        return false;
    }

    protected void TeleportCompleteInFile(UUID requestorUUID, UUID acceptorUUID) {
        sentRequestList = getSentRequestsList(requestorUUID.toString());
        sentRequestList.remove(acceptorUUID.toString());
        setSendRequestList(requestorUUID.toString(), sentRequestList);

        receivedRequestList.remove(requestorUUID.toString());
        setReceivedRequestList(acceptorUUID.toString(), receivedRequestList);
    }

    protected void NotInHomeList(Player player, String homeName) {
        TextComponent wholeMessage = new TextComponent(ChatColor.GREEN + "You don't have " + ChatColor.WHITE + homeName + ChatColor.GREEN + " in you home list. ");
        wholeMessage.addExtra(openSquareBracket);
        wholeMessage.addExtra(showListClick);
        wholeMessage.addExtra(closeSquareBracket);

        showListClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tphlist"));

        player.spigot().sendMessage(wholeMessage);
    }

    protected void TeleportPlayer(Player player, Location location) {
        player.teleport(location);
        player.getInventory().removeItem(new TeleportationToken(pluginInstance,1).getToken());
    }
}
