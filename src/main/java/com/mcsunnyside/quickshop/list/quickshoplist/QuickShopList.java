package com.mcsunnyside.quickshop.list.quickshoplist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.api.command.CommandContainer;
import org.maxgamer.quickshop.util.Util;

public final class QuickShopList extends JavaPlugin {
    private QuickShopAPI api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        reloadConfig();
        api = (QuickShopAPI) Bukkit.getPluginManager().getPlugin("QuickShop");
        Util.parseColours((YamlConfiguration) getConfig());
        getLogger().info("QuickShop List Addon loading...");
        api.getCommandManager().registerCmd(CommandContainer.builder().executor(new ListCommand(this))
                .permission("quickshop.list")
                .prefix("list")
                .description(getConfig().getString("lang.desc"))
                .build());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public QuickShopAPI getApi() {
        return api;
    }
}
