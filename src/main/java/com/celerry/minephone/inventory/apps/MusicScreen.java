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
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class MusicScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;

    public MusicScreen(Player player, ItemStack item) {
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
        String[] names = {"13", "Cat", "Blocks", "Chirp", "Far", "Mall", "Mellohi", "Stal", "Strad"};

        int step = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack discItem = new ItemBuilder(Material.valueOf("MUSIC_DISC_"+names[step].toUpperCase())).build();
            int finalStep = step;
            setItem(getScreen()[step], discItem, e -> {
                stopAllSounds(this.player);
                // Play music
                player.playSound(player.getLocation(), Sound.valueOf("MUSIC_DISC_"+names[finalStep].toUpperCase()), SoundCategory.RECORDS, 9999, 1);
            });
            step++;
        }

        // Stop button
        ItemStack stopButton = new ItemBuilder(Material.RED_CONCRETE_POWDER).name(color("&fStop Music")).build();
        setItem(40, stopButton, e -> {
            stopAllSounds(this.player);
        });

        // Next page
        ItemStack nextPage = new ItemBuilder(SkullCreator.itemFromBase64(App.NEXT_BASE64.toString())).name(color("&fNext Page")).build();
        setItem(41, nextPage, e -> {
            new MusicScreenPg2(this.player, item).open(this.player);
        });
    }

    private void stopAllSounds(Player player) {
        String[] sounds = {"MUSIC_CREATIVE", "MUSIC_CREDITS", "MUSIC_GAME", "MUSIC_DISC_13", "MUSIC_DISC_CAT", "MUSIC_DISC_BLOCKS", "MUSIC_DISC_CHIRP", "MUSIC_DISC_FAR", "MUSIC_DISC_MALL", "MUSIC_DISC_MELLOHI", "MUSIC_DISC_STAL", "MUSIC_DISC_STRAD", "MUSIC_DISC_WARD", "MUSIC_DISC_11", "MUSIC_DISC_WAIT", "MUSIC_DISC_PIGSTEP"};
        for (String soundVal: sounds) {
            Sound sound = Sound.valueOf(soundVal.toUpperCase());
            player.stopSound(sound, SoundCategory.RECORDS);
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
