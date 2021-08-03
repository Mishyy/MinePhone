package com.celerry.minephone.events.listeners;

import com.celerry.minephone.MinePhone;
import com.celerry.minephone.events.PlayerOpenPhoneEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static com.celerry.minephone.util.Msg.color;

public class CallOpenPhoneEvent implements Listener {

    public CallOpenPhoneEvent(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) { return; }
        if(item.getItemMeta() == null) { return; }
        if(item.getItemMeta().getDisplayName().equals(color("&bPhone"))) {
            PlayerOpenPhoneEvent playerOpenPhoneEvent = new PlayerOpenPhoneEvent(player, item);
            Bukkit.getPluginManager().callEvent(playerOpenPhoneEvent);
        }
    }
}
