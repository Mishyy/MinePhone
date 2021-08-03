package com.celerry.minephone.util;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static com.celerry.minephone.util.Msg.color;

public class GetPhone {
    public static ItemStack getPhone(Player player) {
        ItemStack phone = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta meta = phone.getItemMeta();

        meta.setDisplayName(color("&bPhone"));
        phone.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(phone);

        NBTCompound minephoneNBT = nbtItem.addCompound("minephone");
        minephoneNBT.setUUID("no_stack", UUID.randomUUID());
        minephoneNBT.setLong("timestamp", System.currentTimeMillis());
        minephoneNBT.setString("theme", "BLACK_STAINED_GLASS_PANE");
        minephoneNBT.setString("owner", player.getName());

        return nbtItem.getItem();
    }
}
