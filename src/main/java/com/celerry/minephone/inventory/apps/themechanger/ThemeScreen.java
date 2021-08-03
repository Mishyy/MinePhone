package com.celerry.minephone.inventory.apps.themechanger;

import com.celerry.minephone.inventory.HomeScreen;
import com.celerry.minephone.util.enums.App;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

import static com.celerry.minephone.util.Msg.color;

public class ThemeScreen extends FastInv {

    private boolean preventClose = false;
    private Player player;


    public ThemeScreen(Player player, ItemStack item) {
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

        String[] names = {"BLACK_STAINED_GLASS_PANE", "RED_STAINED_GLASS_PANE", "GREEN_STAINED_GLASS_PANE", "BROWN_STAINED_GLASS_PANE", "BLUE_STAINED_GLASS_PANE", "PURPLE_STAINED_GLASS_PANE", "CYAN_STAINED_GLASS_PANE", "LIGHT_BLUE_STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE", "PINK_STAINED_GLASS_PANE", "LIME_STAINED_GLASS_PANE", "YELLOW_STAINED_GLASS_PANE"};

        int step = 0;
        for (int i = 0; i < 12; i++) {
            String itemString = WordUtils.capitalize(names[step].replaceAll("_", " ").toLowerCase());
            ItemStack themeItem = new ItemBuilder(Material.valueOf(names[step].toUpperCase())).name(color("&f"+itemString)).build();

            int finalStep = step;
            setItem(getScreen()[step], themeItem, e -> {
                new ConfirmScreen(this.player, item, names[finalStep]).open(this.player);
            });
            step++;
        }

        // Next page
        ItemStack nextPage = new ItemBuilder(SkullCreator.itemFromBase64(App.NEXT_BASE64.toString())).name(color("&fNext Page")).build();
        setItem(42, nextPage, e -> {
            new ThemeScreenPg2(this.player, item).open(this.player);
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
