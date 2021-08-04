package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.ErrorScreen;
import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.inventory.apps.ClockScreen;
import com.celerry.minephone.inventory.apps.MusicScreen;
import com.celerry.minephone.inventory.apps.SettingsScreen;
import com.celerry.minephone.util.enums.App;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class ContactsScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public ContactsScreen(Player player, ItemStack item, int start) {
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
        Player[] trueContacts = player.getWorld().getPlayers().toArray(new Player[0]);
        Player[] contacts = getSliceOfArray(trueContacts, start, start+12);

        int step = 0;
        for (int i = 0; i < contacts.length; i++) {
            ItemStack contact = new ItemBuilder(SkullCreator.itemFromName(contacts[step].getName())).name(color("&f"+contacts[step].getName())).build();
            int finalStep = step;
            setItem(getScreen()[step], contact, e -> {
                if(this.player.getName().equals(contacts[finalStep].getName())) {
                    new ErrorScreen(this.player, item, "Can't call yourself").open(this.player);
                }
            });
            step++;
        }

        // Up
        if(!trueContacts[0].equals(contacts[0])) {
            ItemStack backPage = new ItemBuilder(SkullCreator.itemFromBase64(App.UP_BASE64.toString())).name(color("&fScroll Up")).build();
            setItem(15, backPage, e -> {
                new ContactsScreen(this.player, item, start-3).open(this.player);
            });
        }

        // Down
        if(!trueContacts[trueContacts.length-1].equals(contacts[contacts.length-1])) {
            ItemStack nextPage = new ItemBuilder(SkullCreator.itemFromBase64(App.DOWN_BASE64.toString())).name(color("&fScroll Down")).build();
            setItem(42, nextPage, e -> {
                new ContactsScreen(this.player, item, start+3).open(this.player);
            });
        }

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

    private Player[] getSliceOfArray(Player[] arr, int start, int end) {

        // Get the slice of the Array
        Player[] slice = new Player[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
            // Prevent an out of bounds error
            if(arr[start+i].equals(arr[arr.length - 1])) {
                break;
            }
        }

        // return the slice
        return removeNull(slice);
    }

    private Player[] removeNull(Player[] a) {
        ArrayList<Player> removedNull = new ArrayList<>();
        for (Player str : a)
            if (str != null)
                removedNull.add(str);
        return removedNull.toArray(new Player[0]);
    }

    private int[] getScreen() {
        return new int[]{12,13,14,21,22,23,30,31,32,39,40,41};
    }
}
