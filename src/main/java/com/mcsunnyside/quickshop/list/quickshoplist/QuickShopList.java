package com.mcsunnyside.quickshop.list.quickshoplist;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.command.CommandContainer;
import org.maxgamer.quickshop.util.Util;

import java.util.Objects;

public final class QuickShopList extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        reloadConfig();
        Util.parseColours((YamlConfiguration) getConfig());
        getLogger().info("QuickShop List Addon loading...");
        Objects.requireNonNull(QuickShopAPI.getCommandManager()).registerCmd(CommandContainer.builder().executor(new ListCommand(this))
                .permission("quickshop.list")
                .prefix("list")
                .build());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
