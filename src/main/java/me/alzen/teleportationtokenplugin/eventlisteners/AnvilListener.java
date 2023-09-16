package me.alzen.teleportationtokenplugin.eventlisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.util.Objects;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        String newName = event.getInventory().getRenameText();
        if(newName != null){
            if(Objects.requireNonNull(newName).equalsIgnoreCase("Teleportation Token")) {
                event.setResult(null);
                event.getInventory().setRepairCost(0);
            }
        }

    }

}
