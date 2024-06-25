package net.luckmc.core.commands;

import net.luckmc.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LuckCoreCMD implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Main.getInstance().colorize("&aLuckCore has been reloaded."));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if(command.getName().equalsIgnoreCase("luckcore"))
            if(args.length == 1)
                return Collections.singletonList("reload");

        return new ArrayList<>();
    }

}
