package me.alzen.teleportationtokenplugin;

import me.alzen.teleportationtokenplugin.commands.*;
import me.alzen.teleportationtokenplugin.eventlisteners.*;
import me.alzen.teleportationtokenplugin.model.TeleportationToken;
import me.alzen.teleportationtokenplugin.utils.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import org.bstats.bukkit.Metrics;

import java.util.Objects;

public final class TeleportationTokenPlugin extends JavaPlugin {

    private static TeleportationTokenPlugin instance;
    private final String line = "=========================";
    private final String tprCommand = "tpr";
    private final String tpxCommand = "tpx";
    private final String tprlistCommand = "tprlist";
    private final String tpalistCommand = "tpalist";
    private final String tpaCommand = "tpa";
    private final String tpqCommand = "tpq";
    private final String tpshCommand = "tpsh";
    private final String tprmCommand = "tprm";
    private final String tphCommand = "tph";
    private final String tphlistCommand = "tphlist";

    private CommandTabCompleter tabCompleter;

    private TPRCommand tpr;
    private TPXCommand tpx;
    private TPRListCommand tprlist;
    private TPAListCommand tpalist;
    private TPACommand tpa;
    private TPQCommand tpq;
    private TPSHCommand tpsh;
    private TPRMCommand tprm;
    private TPHCommand tph;
    private TPHListCommand tphlist;
    @Override
    public void onEnable() {
        // Plugin startup logic
        int pluginID = 19618;
        Metrics metrics = new Metrics(this, pluginID);

        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        YamlFileManager.setup();
        YamlFileManager.getConfigurationFile().options().copyDefaults();
        YamlFileManager.save();

        registerTpTokenRecipe();
        registerListeners();

        getLogger().info(line);
        getLogger().info("Teleportation Token Plugin Enabled!");
        getLogger().info(line);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unregisterListeners();

        getLogger().info(line);
        getLogger().info("Teleportation Token Plugin Disabled!");
        getLogger().info(line);
    }

    public static TeleportationTokenPlugin getPluginInstance(){
        return instance;
    }

    private void registerTpTokenRecipe() {
        FileConfiguration config = this.getConfig();

        int count = config.getInt("TokenCountInCrafting");
        ItemStack token = new TeleportationToken(instance, count).getToken();

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "tptoken"), token);
        recipe.shape(
                "ABC",
                "DEF",
                "GHI"
        );
        recipe.setIngredient('A', Material.valueOf(config.getString("TeleportationTokenRecipe.A")));
        recipe.setIngredient('B', Material.valueOf(config.getString("TeleportationTokenRecipe.B")));
        recipe.setIngredient('C', Material.valueOf(config.getString("TeleportationTokenRecipe.C")));
        recipe.setIngredient('D', Material.valueOf(config.getString("TeleportationTokenRecipe.D")));
        recipe.setIngredient('E', Material.valueOf(config.getString("TeleportationTokenRecipe.E")));
        recipe.setIngredient('F', Material.valueOf(config.getString("TeleportationTokenRecipe.F")));
        recipe.setIngredient('G', Material.valueOf(config.getString("TeleportationTokenRecipe.G")));
        recipe.setIngredient('H', Material.valueOf(config.getString("TeleportationTokenRecipe.H")));
        recipe.setIngredient('I', Material.valueOf(config.getString("TeleportationTokenRecipe.I")));
        Bukkit.addRecipe(recipe);
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);

        tabCompleter = new CommandTabCompleter();

        tpr = new TPRCommand();
        Objects.requireNonNull(this.getCommand(tprCommand)).setExecutor(tpr);
        Objects.requireNonNull(this.getCommand(tprCommand)).setTabCompleter(tabCompleter);

        tpx = new TPXCommand();
        Objects.requireNonNull(this.getCommand(tpxCommand)).setExecutor(tpx);
        Objects.requireNonNull(this.getCommand(tpxCommand)).setTabCompleter(tabCompleter);

        tpa = new TPACommand();
        Objects.requireNonNull(this.getCommand(tpaCommand)).setExecutor(tpa);
        Objects.requireNonNull(this.getCommand(tpaCommand)).setTabCompleter(tabCompleter);

        tpq = new TPQCommand();
        Objects.requireNonNull(this.getCommand(tpqCommand)).setExecutor(tpq);
        Objects.requireNonNull(this.getCommand(tpqCommand)).setTabCompleter(tabCompleter);

        tprm = new TPRMCommand();
        Objects.requireNonNull(this.getCommand(tprmCommand)).setExecutor(tprm);
        Objects.requireNonNull(this.getCommand(tprmCommand)).setTabCompleter(tabCompleter);

        tph = new TPHCommand();
        Objects.requireNonNull(this.getCommand(tphCommand)).setExecutor(tph);
        Objects.requireNonNull(this.getCommand(tphCommand)).setTabCompleter(tabCompleter);

        tphlist = new TPHListCommand();
        Objects.requireNonNull(this.getCommand(tphlistCommand)).setExecutor(tphlist);

        tpsh = new TPSHCommand();
        Objects.requireNonNull(this.getCommand(tpshCommand)).setExecutor(tpsh);

        tprlist = new TPRListCommand();
        Objects.requireNonNull(this.getCommand(tprlistCommand)).setExecutor(tprlist);

        tpalist = new TPAListCommand();
        Objects.requireNonNull(this.getCommand(tpalistCommand)).setExecutor(tpalist);
    }

    @SuppressWarnings("ConstantConditions")
    private void unregisterListeners(){
        HandlerList.unregisterAll(this);

        tabCompleter = null;

        tpr = null;
        this.getCommand(tprCommand).setExecutor(tpr);
        this.getCommand(tprCommand).setTabCompleter(tabCompleter);

        tpx = null;
        this.getCommand(tpxCommand).setExecutor(tpx);
        this.getCommand(tpxCommand).setTabCompleter(tabCompleter);

        tpa = null;
        this.getCommand(tpaCommand).setExecutor(tpa);
        this.getCommand(tpaCommand).setTabCompleter(tabCompleter);

        tpq = null;
        this.getCommand(tpqCommand).setExecutor(tpq);
        this.getCommand(tpqCommand).setTabCompleter(tabCompleter);

        tprm = null;
        this.getCommand(tprmCommand).setExecutor(tprm);
        this.getCommand(tprmCommand).setTabCompleter(tabCompleter);

        tph = null;
        this.getCommand(tphCommand).setExecutor(tph);
        this.getCommand(tphCommand).setTabCompleter(tabCompleter);

        tphlist = null;
        this.getCommand(tphlistCommand).setExecutor(tphlist);

        tpsh = null;
        this.getCommand(tpshCommand).setExecutor(tpsh);

        tprlist = null;
        this.getCommand(tprlistCommand).setExecutor(tprlist);

        tpalist = null;
        this.getCommand(tpalistCommand).setExecutor(tpalist);

        tabCompleter = null;
    }
}
