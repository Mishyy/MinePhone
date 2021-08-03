package com.celerry.minephone.commands;

import com.celerry.minephone.MinePhone;
import com.celerry.minephone.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.celerry.minephone.util.GetPhone.getPhone;

public class MinephoneCommand implements CommandExecutor {

    private final MinePhone plugin;

    public MinephoneCommand(final MinePhone plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.msg(sender, "Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("help blah blah blah");
            return true;
        }
        if (args[0].equals("give")) {
            player.getInventory().addItem(getPhone(player));
            return true;
        }
        return false;
    }
}
