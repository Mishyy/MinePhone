package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.ErrorScreen;
import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.util.enums.DenyReason;
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

public class RingManageScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public RingManageScreen(Player player, ItemStack item) {
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

        ItemStack otherPersonHead = new ItemBuilder(SkullCreator.itemFromUuid(inRingWith(this.player).getUniqueId())).name(color("&f"+inRingWith(this.player).getName())).build();
        setItem(22, otherPersonHead);

        if(startedRing(this.player)) {
            // If player started the call only show them a way to cancel
            ItemStack denyButton = new ItemBuilder(Material.RED_CONCRETE_POWDER).name(color("&cCancel Call")).build();
            setItem(40, denyButton, e -> {
                if(!isInRingState(this.player)) {
                    new ErrorScreen(this.player, item, "You're not receiving a call").open(this.player);
                    return;
                }
                endRing(this.player, DenyReason.Cancelled);
                new HomeScreen(this.player, item).open(this.player);
            });
        } else {
            // If player is RECEIVING the call, show them deny AND accept
            ItemStack denyButton = new ItemBuilder(Material.RED_CONCRETE_POWDER).name(color("&cDeny Call")).build();
            setItem(41, denyButton, e -> {
                if(!isInRingState(this.player)) {
                    new ErrorScreen(this.player, item, "You're not receiving a call").open(this.player);
                    return;
                }
                endRing(inRingWith(this.player), DenyReason.Denied);
                new HomeScreen(this.player, item).open(this.player);
            });

            ItemStack acceptButton = new ItemBuilder(Material.LIME_CONCRETE_POWDER).name(color("&aAccept Call")).build();
            setItem(39, acceptButton, e -> {
                Player otherPlayer = inRingWith(this.player);
                if (!isInRingState(this.player) || !isInRingState(this.player)) {
                    new ErrorScreen(this.player, item, "You're not receiving a call").open(this.player);
                    return;
                }

                // End ring, start call
                endRing(inRingWith(this.player), DenyReason.Started);
                createCall(this.player, otherPlayer);
                new PhoneManageScreen(this.player, item).open(this.player);
            });
        }
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
