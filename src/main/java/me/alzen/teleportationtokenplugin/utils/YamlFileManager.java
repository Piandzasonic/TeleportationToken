package me.alzen.teleportationtokenplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlFileManager {

    private static File file;
    private static FileConfiguration playerData;

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("TeleportationToken").getDataFolder(), "PlayerData.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        playerData = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfigurationFile() {
        return playerData;
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public static void save() {
        try {
            playerData.save(file);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static void reload() {
        playerData = YamlConfiguration.loadConfiguration(file);
    }

    public static void setData(String path, Object value) {
        reload();
        playerData.set(path, value);
        save();
    }

    public static List<String> getListString(String path) {
        reload();
        return playerData.getStringList(path);
    }

    public static String getString(String path) {
        reload();
        return playerData.getString(path);
    }

    public static double getDouble(String path) {
        reload();
        return playerData.getDouble(path);
    }

    public static int getInt(String path) {
        reload();
        return playerData.getInt(path);
    }
}
