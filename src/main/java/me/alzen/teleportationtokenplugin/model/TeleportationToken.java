package me.alzen.teleportationtokenplugin.model;

import me.alzen.teleportationtokenplugin.TeleportationTokenPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TeleportationToken {

    private final ItemStack token;

    public TeleportationToken(TeleportationTokenPlugin pluginInstance, int quantity){

        Material baseMaterial = Material.valueOf(pluginInstance.getConfig().getString("BaseMaterial"));
        int customModelData = pluginInstance.getConfig().getInt("CustomModelData");
        token = new ItemStack(baseMaterial, quantity);
        ItemMeta itemMeta = token.getItemMeta();

        if (itemMeta != null) {
            String name = "Teleportation Token";
            itemMeta.setDisplayName(name);
            String[] lore = {
                    "Token for teleportation",
                    "Must be in your inventory to be used."
            };
            itemMeta.setLore(Arrays.asList(lore));
            itemMeta.setCustomModelData(customModelData);
        }

        token.setItemMeta(itemMeta);
    }

    public ItemStack getToken(){
        return token;
    }
}
