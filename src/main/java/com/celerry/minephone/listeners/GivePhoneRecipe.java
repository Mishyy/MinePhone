package com.celerry.minephone.listeners;

import com.celerry.minephone.MinePhone;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GivePhoneRecipe implements Listener {

    public GivePhoneRecipe(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NamespacedKey key = new NamespacedKey(MinePhone.getPlugin(), "minephone_phone");
        if(!player.hasDiscoveredRecipe(key)) {
            player.discoverRecipe(key);
        }
    }
}
