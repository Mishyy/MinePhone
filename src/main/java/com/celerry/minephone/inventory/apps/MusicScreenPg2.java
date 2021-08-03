package com.celerry.minephone.inventory.apps;

import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.util.enums.App;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class MusicScreenPg2 extends FastInv {

    private boolean preventClose = false;
    private Player player;

    public MusicScreenPg2(Player player, ItemStack item) {
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
        String[] names = {"Ward", "11", "Wait", "Pigstep"};

        int step = 0;
        for (int i = 0; i < 4; i++) {
            ItemStack discItem = new ItemBuilder(Material.valueOf("MUSIC_DISC_"+names[step].toUpperCase())).build();
            int finalStep = step;
            setItem(getScreen()[step], discItem, e -> {
                // Stop ALL sounds using ProtocolLib
                stopAllSounds(this.player);
                // Play music
                player.playSound(player.getLocation(), Sound.valueOf("MUSIC_DISC_"+names[finalStep].toUpperCase()), 9999, 1);
            });
            step++;
        }

        // Stop button
        ItemStack stopButton = new ItemBuilder(Material.RED_CONCRETE_POWDER).name(color("&fStop Music")).build();
        setItem(40, stopButton, e -> {
            stopAllSounds(this.player);
        });

        // Back page
        ItemStack backPage = new ItemBuilder(SkullCreator.itemFromBase64(App.BACK_BASE64.toString())).name(color("&fLast Page")).build();
        setItem(39, backPage, e -> {
            new MusicScreen(this.player, item).open(this.player);
        });

    }

    private void stopAllSounds(Player player) {
        String[] discs = {"13", "Cat", "Blocks", "Chirp", "Far", "Mall", "Mellohi", "Stal", "Strad", "Ward", "11", "Wait", "Pigstep"};
        for (String disc: discs) {
            Sound sound = Sound.valueOf("MUSIC_DISC_"+disc.toUpperCase());
            player.stopSound(sound);
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
