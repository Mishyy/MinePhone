package com.celerry.minephone;

import com.celerry.minephone.events.listeners.CallOpenPhoneEvent;
import com.celerry.minephone.listeners.ChatWhileInCall;
import com.celerry.minephone.listeners.EndCallOnLeave;
import com.celerry.minephone.listeners.GivePhoneRecipe;
import com.celerry.minephone.listeners.OpenPhone;
import com.raus.craftLib.CraftLib;
import com.raus.craftLib.ExactShapedRecipe;
import fr.mrmicky.fastinv.FastInvManager;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static com.celerry.minephone.util.CallManager.startRingScheduler;
import static com.celerry.minephone.util.GetPhone.getPhone;
import static com.celerry.minephone.util.Msg.color;

public final class MinePhone extends JavaPlugin {

    private static MinePhone plugin;
    private static CraftLib craftLib;

    public static MinePhone getPlugin() {
        return plugin;
    }


    @Override
    public void onLoad() {
        plugin = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        FastInvManager.register(this);
        startRingScheduler();
        // Check for CraftLib dependency, and if found, register recipes
        craftLib = getCraftLib();
        recipe();
        // Listeners
        new CallOpenPhoneEvent(this);
        new OpenPhone(this);
        new ChatWhileInCall(this);
        new EndCallOnLeave(this);
        new GivePhoneRecipe(this);
    }

    private CraftLib getCraftLib() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CraftLib");
        if(!(plugin instanceof CraftLib)) {
            getLogger().info("CraftLib not found, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return null;
        }
        return (CraftLib) plugin;
    }

    public void recipe() {
        NamespacedKey key = new NamespacedKey(this, "minephone_phone");
        ExactShapedRecipe recipe = new ExactShapedRecipe(key, new ItemBuilder(Material.IRON_INGOT).name(color("&7Phone")).build());
        recipe.setShape("CCD", "RGI", "III");
        recipe.setIngredient('C', Material.COPPER_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('I', Material.IRON_BLOCK);

        recipe.setCallback(event -> {
            if(event.getInventory().getViewers().get(0) != null && event.getInventory().getViewers().get(0) instanceof Player) {
                Player player = (Player) event.getInventory().getViewers().get(0);
                event.getInventory().setResult(getPhone(player));
            } else {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        });
        craftLib.addRecipe(recipe);
    }
}
