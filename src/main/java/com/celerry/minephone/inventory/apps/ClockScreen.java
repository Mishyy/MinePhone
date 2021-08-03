package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.util.enums.App;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;
import static com.celerry.minephone.util.TimeUtils.getMoonPhaseString;
import static com.celerry.minephone.util.TimeUtils.getTime;

public class ClockScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;

    public ClockScreen(Player player, ItemStack item) {
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
        ItemStack clock = new ItemBuilder(Material.CLOCK).name(color("&bTime")).addLore(color("&bCurrent Time: &f"+getTime(this.player.getWorld()))).build();
        setItem(13, clock);

        ItemStack moon = new ItemBuilder(SkullCreator.itemFromBase64(App.MOON_BASE64.toString())).name(color("&bMoon Phase")).addLore(color("&bCurrent Phase: &f"+getMoonPhaseString(this.player.getWorld()))).build();
        setItem(31, moon);

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
