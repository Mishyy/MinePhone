package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.HomeScreen;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.CallManager.*;
import static com.celerry.minephone.util.Msg.color;

public class PhoneManageScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public PhoneManageScreen(Player player, ItemStack item) {
        super(54, color("&fMinePhone"));
        this.player = player;

        // Set the background and screen
        setItems(getBackground(), new ItemBuilder(getThemeMaterial(item)).name(" ").build());
        setItems(getScreen(), new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build());
        // Home button
        ItemStack homeButton = new ItemBuilder(Material.COAL_BLOCK).name(color("&fHome Button")).build();
        setItem(49, homeButton, e -> {
            new HomeScreen(this.player, item).open(this.player);
        });
        // Application Content

        ItemStack otherPersonHead = new ItemBuilder(SkullCreator.itemFromUuid(inCallWith(this.player).getUniqueId())).name(color("&f"+inCallWith(this.player).getName())).build();
        setItem(22, otherPersonHead);

        ItemStack endButton = new ItemBuilder(Material.RED_CONCRETE_POWDER).name(color("&cEnd Call")).build();
        setItem(40, endButton, e -> {
            if(!isInCall(this.player)) {
                new HomeScreen(this.player, item).open(this.player);
                return;
            }
            endCall(player);
            new HomeScreen(this.player, item).open(this.player);
        });

    }

    private Material getThemeMaterial(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("minephone");
        String theme = compound.getString("theme");
        return Material.valueOf(theme);
    }

    private int[] getBackground() {
        int size = this.getInventory().getSize();
        return IntStream.range(0,size).toArray();
    }

    private int[] getScreen() {
        return new int[]{12,13,14,21,22,23,30,31,32,39,40,41};
    }
}
