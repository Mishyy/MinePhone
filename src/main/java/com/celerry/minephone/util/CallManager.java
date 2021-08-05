package com.celerry.minephone.util;

import com.celerry.minephone.util.enums.DenyReason;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static com.celerry.minephone.util.Msg.color;

public class CallManager {

    private static HashMap<UUID, UUID> calls = new HashMap<>();
    private static HashMap<UUID, UUID> rings = new HashMap<>();

    public static HashMap<UUID, UUID> getRings() {
        return rings;
    }

    public static HashMap<UUID, UUID> getCalls() {
        return calls;
    }

    /*
     * Call stuff
     */

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

        // Log
        Bukkit.getLogger().info("Call Started ("+sender.getName()+"&"+receiver.getName()+")");
    }

    public static void endCall(Player player) {
        UUID uuid = player.getUniqueId();
        UUID with = inCallWith(player).getUniqueId();

        // Send message
        inCallWith(player).sendMessage(color("&7[&bMinePhone&7] &aCall ended with "+player.getName()));
        player.sendMessage(color("&7[&bMinePhone&7] &aCall ended with "+inCallWith(player).getName()));

        // Log
        Bukkit.getLogger().info("Call Ended ("+player.getName()+"&"+inCallWith(player).getName()+")");

        // Remove from calls
        calls.remove(uuid);
        calls.remove(with);
    }

    /*
     * Ringing stuff
     */

    public static boolean isInRingState(Player player) {
        UUID uuid = player.getUniqueId();
        return rings.containsKey(uuid);
    }

    public static Player inRingWith(Player player) {
        return Bukkit.getPlayer(rings.get(player.getUniqueId()));
    }

    public static void createRing(Player sender, Player receiver) {
        UUID senderId = sender.getUniqueId();
        UUID receiverId = receiver.getUniqueId();

        // Add to rings
        rings.put(senderId, receiverId);
        rings.put(receiverId, senderId);

        // Send message
        sender.sendMessage(color("&7[&bMinePhone&7] &aTrying to call "+receiver.getName()));
        receiver.sendMessage(color("&7[&bMinePhone&7] &a"+sender.getName()+" is calling you!"));

        // Log
        Bukkit.getLogger().info("Ringing started ("+sender.getName()+"&"+receiver.getName()+")");
    }

    public static void endRing(Player player, DenyReason reason) {
        UUID uuid = player.getUniqueId();
        UUID with = inRingWith(player).getUniqueId();

        // Log
        Bukkit.getLogger().info("Ringing ended ("+player.getName()+"&"+inRingWith(player).getName()+") ["+reason.label+"]");

        // Remove from rings
        rings.remove(uuid);
        rings.remove(with);
    }

    /*
     * READ ME!
     *
     * all that really needs to be done now is a ringing system and a way to turn off your phone, then get back to
     * work on the supernatural plugin :)
     */
}
