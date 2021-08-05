package com.celerry.minephone;

import com.celerry.minephone.commands.MinephoneCommand;
import com.celerry.minephone.events.listeners.CallOpenPhoneEvent;
import com.celerry.minephone.listeners.ChatWhileInCall;
import com.celerry.minephone.listeners.EndCallOnLeave;
import com.celerry.minephone.listeners.OpenPhone;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinePhone extends JavaPlugin {

    private static MinePhone plugin;
    private static ProtocolManager protocolManager;

    public static MinePhone getPlugin() {
        return plugin;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onLoad() {
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        FastInvManager.register(this);
        this.getCommand("minephone").setExecutor(new MinephoneCommand(this));
        // Listeners
        new CallOpenPhoneEvent(this);
        new OpenPhone(this);
        new ChatWhileInCall(this);
        new EndCallOnLeave(this);
    }
}
