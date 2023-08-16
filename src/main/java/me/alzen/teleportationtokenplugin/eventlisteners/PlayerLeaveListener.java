package me.alzen.teleportationtokenplugin.eventlisteners;

import me.alzen.teleportationtokenplugin.commands.TeleportCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener extends TeleportCommand implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        sentRequestList = getSentRequestsList(playerUUID);

        for(String targetUUID : sentRequestList) {
            receivedRequestList = getReceivedRequestsList(targetUUID);
            receivedRequestList.remove(playerUUID);
            setReceivedRequestList(targetUUID, receivedRequestList);
            receivedRequestList.clear();
        }

        sentRequestList.clear();
        setSendRequestList(playerUUID, sentRequestList);

    }

}
