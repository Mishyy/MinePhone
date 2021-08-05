package com.celerry.minephone.util;

import com.celerry.minephone.MinePhone;
import com.celerry.minephone.util.enums.DenyReason;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.celerry.minephone.util.Msg.color;

public class CallManager {

    /*
     * This call management class is a bit scuffed, but it seems to get the job done.
     */

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
    public static boolean startedRing(Player player) {
        UUID uuid = player.getUniqueId();
        return !rings.containsValue(uuid);
    }

    public static boolean isInRingState(Player player) {
        UUID uuid = player.getUniqueId();
        return rings.containsKey(uuid) || rings.containsValue(uuid);
    }

    public static Player inRingWith(Player player) {
        if(startedRing(player)) {
            return Bukkit.getPlayer(rings.get(player.getUniqueId()));
        }
        else {
            return Bukkit.getPlayer(rings.entrySet()
                    .stream()
                    .filter(entry -> player.getUniqueId().equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst().get());
        }
    }

    public static void createRing(Player sender, Player receiver) {
        UUID senderId = sender.getUniqueId();
        UUID receiverId = receiver.getUniqueId();

        // Add to rings
        rings.put(senderId, receiverId);

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
    }

    public static void startRingScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MinePhone.getPlugin(), new Runnable() {
            @Override
            public void run() {
                MinePhone.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
                    if(!isInRingState(player)) { return; }
                    String what = "Incoming call from ";
                    if(startedRing(player)) {
                        what= "Calling ";
                    }
                    player.sendTitle(color("&b"+what+inRingWith(player).getName()), "", 5, 20, 40);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.RECORDS, 1, 2);
                });
            }
        }, 0, 15); // Every 15 ticks
    }
    /*
     * READ ME!
     *
     * all that really needs to be done now is a ringing system and a way to turn off your phone, then get back to
     * work on the supernatural plugin :)
     */
}
