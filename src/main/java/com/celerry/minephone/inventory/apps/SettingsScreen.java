package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.inventory.apps.themechanger.ThemeScreen;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class SettingsScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public SettingsScreen(Player player, ItemStack item) {
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

        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("minephone");

        String owner = compound.getString("owner");
        long timestamp = compound.getLong("timestamp");

        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        Date date = new Date(timestamp);

        ItemStack nametag = new ItemBuilder(Material.NAME_TAG).name(color("&bPhone Owner")).addLore(color("&7"+owner)).build();
        setItem(12, nametag);

        ItemStack clock = new ItemBuilder(Material.CLOCK).name(color("&bPhone Creation Date")).addLore(color("&7"+dateFormat.format(date))).build();
        setItem(13, clock);

        String themeAsString = WordUtils.capitalize(getThemeMaterial(item).toString().replaceAll("_", " ").toLowerCase());
        ItemStack painting = new ItemBuilder(Material.PAINTING).name(color("&bPhone Theme")).addLore(color("&7"+ themeAsString)).addLore(color("&eClick to customize!")).build();
        setItem(14, painting, e -> {
            new ThemeScreen(this.player, item).open(this.player);
        });

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

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
