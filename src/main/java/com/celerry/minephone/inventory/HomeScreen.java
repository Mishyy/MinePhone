package com.celerry.minephone.inventory;

import com.celerry.minephone.inventory.apps.ClockScreen;
import com.celerry.minephone.inventory.apps.ContactsScreen;
import com.celerry.minephone.inventory.apps.MusicScreen;
import com.celerry.minephone.inventory.apps.SettingsScreen;
import com.celerry.minephone.util.enums.App;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class HomeScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public HomeScreen(Player player, ItemStack item) {
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
        ItemStack settings = new ItemBuilder(SkullCreator.itemFromBase64(App.SETTINGS_BASE64.toString())).name(color(App.SETTINGS_NAME.toString())).build();
        setItem(12, settings, e -> {
            new SettingsScreen(this.player, item).open(this.player);
            // Settings app will contain:

            // Date of this phone's creation (stored in NBT as a long, converted to days)
            // Who this phone was "sold" to (who spawned it in pretty much) [saved as a name, dont bother with uuids. too much hassle for a cool lil feature]
            // Phone type [configurable, saved as nbt maybe]

            // beyond that, regular settings. themes etc.
            // all customisation options (themes and shit) are saved PER PHONE, using nbt.

        });

        ItemStack clock = new ItemBuilder(SkullCreator.itemFromBase64(App.CLOCK_BASE64.toString())).name(color(App.CLOCK_NAME.toString())).build();
        setItem(13, clock, e -> {
            new ClockScreen(this.player, item).open(this.player);
        });

        ItemStack music = new ItemBuilder(SkullCreator.itemFromBase64(App.MUSIC_BASE64.toString())).name(color(App.MUSIC_NAME.toString())).build();
        setItem(14, music, e -> {
            new MusicScreen(this.player, item).open(this.player);
        });

        ItemStack phone = new ItemBuilder(SkullCreator.itemFromBase64(App.PHONE_BASE64.toString())).name(color(App.PHONE_NAME.toString())).build();
        setItem(21, phone, e -> {
            new ContactsScreen(this.player, item, 0).open(this.player);
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
