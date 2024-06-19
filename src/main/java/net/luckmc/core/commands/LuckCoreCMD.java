package net.luckmc.core.commands;

import net.luckmc.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class LuckCoreCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Main.getInstance().colorize("&aLuckCore has been reloaded."));
            return true;
        }
        return false;
    }
}
