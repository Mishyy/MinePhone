package com.celerry.minephone.listeners;

import com.celerry.minephone.MinePhone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Stream;

import static com.celerry.minephone.util.CallManager.*;
import static com.celerry.minephone.util.Msg.color;

public class EndCallOnLeave implements Listener {

    public EndCallOnLeave(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!isInCall(player)) { return; }
        endCall(player);
    }
}
