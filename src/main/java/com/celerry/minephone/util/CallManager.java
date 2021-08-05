package com.celerry.minephone.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static com.celerry.minephone.util.Msg.color;

public class CallManager {

    private static HashMap<UUID, UUID> calls = new HashMap<>();

    public static boolean isInCall(Player player) {
        UUID uuid = player.getUniqueId();
        return calls.containsKey(uuid);
    }

    public static Player inCallWith(Player player) {
        return Bukkit.getPlayer(calls.get(player.getUniqueId()));
    }

    public static void createCall(Player sender, Player receiver) {
        UUID senderId = sender.getUniqueId();
        UUID receiverId = receiver.getUniqueId();

        // Add to calls
        calls.put(senderId, receiverId);
        calls.put(receiverId, senderId);

        // Send message
        sender.sendMessage(color("&7[&bMinePhone&7] &aCall started with "+receiver.getName()));
        receiver.sendMessage(color("&7[&bMinePhone&7] &aCall started with "+sender.getName()));
    }

    public static void endCall(Player player) {
        UUID uuid = player.getUniqueId();
        UUID with = inCallWith(player).getUniqueId();

        // Send message
        inCallWith(player).sendMessage(color("&7[&bMinePhone&7] &aCall ended with "+player.getName()));
        player.sendMessage(color("&7[&bMinePhone&7] &aCall ended with "+inCallWith(player).getName()));

        // Remove from calls
        calls.remove(uuid);
        calls.remove(with);
    }
}
