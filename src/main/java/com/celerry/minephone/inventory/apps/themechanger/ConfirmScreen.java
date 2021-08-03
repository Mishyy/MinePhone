package com.celerry.minephone.inventory.apps.themechanger;

import com.celerry.minephone.inventory.HomeScreen;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class ConfirmScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public ConfirmScreen(Player player, ItemStack item, String newTheme) {
        super(54, color("&fMinePhone"));
        this.player = player;

        // Set the background and screen
        setItems(getBackground(), new ItemBuilder(Material.valueOf(newTheme)).name(" ").build());
        setItems(getScreen(), new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build());
        // Home button
        ItemStack homeButton = new ItemBuilder(Material.COAL_BLOCK).name(color("&fHome Button")).build();
        setItem(49, homeButton, e -> {
            new HomeScreen(this.player, item).open(this.player);
        });
        // Application Content

        String itemString = WordUtils.capitalize(newTheme.replaceAll("_", " ").toLowerCase());

        ItemStack preview = new ItemBuilder(Material.WRITABLE_BOOK).name(color("&fSet your phone's theme to")).addLore(color("&e"+itemString+"&f?")).build();
        setItem(22, preview);

        ItemStack confirm = new ItemBuilder(Material.LIME_CONCRETE).name(color("&a&lYes")).build();
        setItem(39, confirm, e -> {
            ItemStack newItem = player.getInventory().getItemInMainHand();
            NBTItem nbtItem = new NBTItem(newItem);
            NBTCompound minephoneNBT = nbtItem.addCompound("minephone");
            minephoneNBT.setString("theme", newTheme);
            this.player.getInventory().setItemInMainHand(nbtItem.getItem());
            new HomeScreen(this.player, player.getInventory().getItemInMainHand()).open(this.player);
        });

        ItemStack deny = new ItemBuilder(Material.RED_CONCRETE).name(color("&c&lNo")).build();
        setItem(41, deny, e -> {
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
