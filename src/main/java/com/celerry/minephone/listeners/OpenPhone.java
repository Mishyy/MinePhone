package com.celerry.minephone.listeners;

import com.celerry.minephone.MinePhone;
import com.celerry.minephone.events.PlayerOpenPhoneEvent;
import com.celerry.minephone.inventory.HomeScreen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;


public class OpenPhone implements Listener {

    public OpenPhone(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void openPhone(PlayerOpenPhoneEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        new HomeScreen(player, item).open(player);
    }
}
