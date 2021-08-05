package com.celerry.minephone.listeners;

import com.celerry.minephone.MinePhone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.http.WebSocket;
import java.util.stream.Stream;

import static com.celerry.minephone.util.CallManager.inCallWith;
import static com.celerry.minephone.util.CallManager.isInCall;
import static com.celerry.minephone.util.Msg.color;

public class ChatWhileInCall implements Listener {

    public ChatWhileInCall(MinePhone plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!isInCall(player)) { return; }
        event.setCancelled(true); // Cancel the message from going through
        String message = event.getMessage();

        Player other = inCallWith(player);

        Stream.of(player,other).forEach(p -> {
            p.sendMessage(color("&b&lCALL &f"+player.getName()+"&b: ")+ ChatColor.AQUA+message);
        });

        Bukkit.getLogger().info("Call Message ("+player.getName()+"->"+other.getName()+"): "+message);
    }

}
