package me.alzen.teleportationtokenplugin.eventlisteners;

import me.alzen.teleportationtokenplugin.model.TeleportationToken;
import me.alzen.teleportationtokenplugin.TeleportationTokenPlugin;
import me.alzen.teleportationtokenplugin.utils.YamlFileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    private final String keyPath = ".hasBeenGiven";
    private final TeleportationTokenPlugin pluginInstance = TeleportationTokenPlugin.getPluginInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        if(!CheckIfKeyPathExists(playerUUID)) {
            YamlFileManager.setData(playerUUID + keyPath, 0);
        }

        String valuePath = playerUUID + keyPath;

        if(YamlFileManager.getInt(valuePath) == 0) {
            if(CheckIfInventorySlotAvailable(player)) {
                GiveTokenToPlayer(player);
            }else {
                YamlFileManager.setData(valuePath, 0);
                player.sendMessage(ChatColor.GREEN + "Unable to give you Teleportation Token. Please free up a slot in your inventory and rejoin.");
            }
        }
        String url = pluginInstance.getConfig().getString("ResourcePackURL");
        player.setResourcePack(Objects.requireNonNull(url));
    }

    private void GiveTokenToPlayer(Player player) {
        int giveFreeToken = pluginInstance.getConfig().getInt("GiveNewPlayerTokens");

        if(giveFreeToken == 1) {

            int freeTokenCount = pluginInstance.getConfig().getInt("FreeTokenCount");
            YamlFileManager.setData(player.getUniqueId() + keyPath, 1);

            player.getInventory().addItem(new TeleportationToken(pluginInstance, freeTokenCount).getToken());

            player.sendMessage(ChatColor.GREEN + "You received " + freeTokenCount + " Teleportation Tokens.");
        }

    }

    private boolean CheckIfKeyPathExists(String playerUuid) {
        return YamlFileManager.getConfigurationFile().contains(playerUuid + keyPath);
    }

    private boolean CheckIfInventorySlotAvailable(Player player) {
        for(int slot = 0; slot <= 35; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if(item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }

}
