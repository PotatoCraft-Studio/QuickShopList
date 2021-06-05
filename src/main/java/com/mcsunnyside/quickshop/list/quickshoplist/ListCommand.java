package com.mcsunnyside.quickshop.list.quickshoplist;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.command.CommandProcesser;
import org.maxgamer.quickshop.shop.Shop;
import org.maxgamer.quickshop.util.MsgUtil;
import org.maxgamer.quickshop.util.ReflectFactory;
import org.maxgamer.quickshop.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ListCommand implements CommandProcesser {
    private QuickShopList plugin;

    public ListCommand(QuickShopList plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command cannot running by Console.");
            return;
        }
        UUID playerToSee;
        if (strings.length < 1) {
            playerToSee = ((Player) commandSender).getUniqueId();
        } else {
            if (!commandSender.hasPermission("quickshop.list.others")) {
                commandSender.sendMessage(MsgUtil.getMessage("no-permission", commandSender));
                return;
            }
            playerToSee = Bukkit.getOfflinePlayer(strings[0]).getUniqueId();
        }
        Player player = (Player) commandSender;
        List<Shop> shops = QuickShopAPI.getShopAPI().getShops(playerToSee);
        player.sendMessage(plugin.getConfig().getString("lang.prefix").replace("{total}", String.valueOf(shops.size())));
        if (shops.isEmpty()) {
            player.sendMessage(plugin.getConfig().getString("lang.nothing"));
            return;
        }
        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            String message = plugin.getConfig().getString("lang.coord").replace("{num}", String.valueOf(i + 1)).replace("{name}", Util.getItemStackName(shop.getItem()));
            TextComponent messageTC = new TextComponent(message);

            List<String> lores = new ArrayList<>();
            String title = null;
            List<String> hover = plugin.getConfig().getStringList("lang.hover");

            for (int j = 0; j < hover.size(); j++) {
                if (j == 0) {
                    title = format(shop, hover.get(j));
                    continue;
                }
                lores.add(format(shop, hover.get(j)));
            }

            ItemStack stack = new ItemStack(Material.STONE);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(title);
            meta.setLore(lores);
            stack.setItemMeta(meta);
            try {
                messageTC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                        new ComponentBuilder(Objects.requireNonNull(ReflectFactory.convertBukkitItemStackToJson(stack))).create()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            player.spigot().sendMessage(messageTC);
        }
    }

    private String format(Shop shop, String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw.replace("{name}", Util.getItemStackName(shop.getItem()))
                .replace("{world}", shop.getLocation().getWorld().getName())
                .replace("{x}", String.valueOf(shop.getLocation().getBlockX()))
                .replace("{y}", String.valueOf(shop.getLocation().getBlockY()))
                .replace("{z}", String.valueOf(shop.getLocation().getBlockZ()))
                .replace("{price}", Util.format(shop.getPrice(), shop))
                .replace("{type}", shop.isSelling() ? plugin.getConfig().getString("lang.selling") : plugin.getConfig().getString("lang.buying")));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] cmdArg) {
        return null;
    }
}
