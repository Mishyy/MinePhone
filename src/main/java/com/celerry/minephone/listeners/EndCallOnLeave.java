package com.celerry.minephone.listeners;

import com.celerry.minephone.MinePhone;
import com.celerry.minephone.util.enums.DenyReason;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.celerry.minephone.util.CallManager.*;

public class EndCallOnLeave implements Listener {

    public EndCallOnLeave(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuitInCall(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!isInCall(player)) { return; }
        endCall(player);
    }

    @EventHandler
    public void onQuitWhileRinging(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!isInRingState(player)) { return; }
        endRing(player, DenyReason.Disconnect);
    }
}
