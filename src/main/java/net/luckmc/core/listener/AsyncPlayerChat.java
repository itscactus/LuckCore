package net.luckmc.core.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.luckmc.core.Main;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.stream.Collectors;

public class AsyncPlayerChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        final World world = player.getWorld();

        // Get a LuckPerms cached metadata for the player.
        final CachedMetaData metaData = Main.getInstance().luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
        final String group = metaData.getPrimaryGroup();

        String format = Main.getInstance().getConfig().getString(Main.getInstance().getConfig().getString("group-formats." + group) != null ? "group-formats." + group : "chat-format")
                .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                .replace("{prefixes}", metaData.getPrefixes().keySet().stream().map(key -> metaData.getPrefixes().get(key)).collect(Collectors.joining()))
                .replace("{suffixes}", metaData.getSuffixes().keySet().stream().map(key -> metaData.getSuffixes().get(key)).collect(Collectors.joining()))
                .replace("{world}", player.getWorld().getName())
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{username-color}", metaData.getMetaValue("username-color") != null ? metaData.getMetaValue("username-color") : "")
                .replace("{message-color}", metaData.getMetaValue("message-color") != null ? metaData.getMetaValue("message-color") : "");

        format = Main.getInstance().colorize(Main.getInstance().translateHexColorCodes(Main.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, format) : format));

        // Mesaj yönlendirme kısmı
        if (message.startsWith("!!")) {
            event.setCancelled(true);
            String prefix =
                    Main.getInstance().colorize(Main.getInstance().translateHexColorCodes(Main.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, Main.getInstance().getConfig().getString("global-prefix")) : Main.getInstance().getConfig().getString("global-prefix").replace("{world}", world.getName())));
            String formattedMessage = prefix + format.replace("{message}", message.substring(2)).replace("%", "%%");
            if (message.length() > 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(formattedMessage);
                }
            } else {
                player.sendMessage(Main.getInstance().colorize("&cLütfen boş mesaj gönderme."));
            }
        } else if (message.startsWith("!")) {
            event.setCancelled(true);
            String prefix =
                    Main.getInstance().colorize(Main.getInstance().translateHexColorCodes(Main.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, Main.getInstance().getConfig().getString("world-prefix")) : Main.getInstance().getConfig().getString("world-prefix").replace("{world}", world.getName())));
            String formattedMessage = prefix + format.replace("{message}", message.substring(1)).replace("%", "%%");
            if(message.length() > 1) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(formattedMessage);
                }
            } else {
                player.sendMessage(Main.getInstance().colorize("&cLütfen boş mesaj gönderme."));
            }
        } else {
            event.setCancelled(true);
            String prefix =
                    Main.getInstance().colorize(Main.getInstance().translateHexColorCodes(Main.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, Main.getInstance().getConfig().getString("radius-prefix")) : Main.getInstance().getConfig().getString("radius-prefix").replace("{world}", world.getName())));
            String formattedMessage = prefix + format.replace("{message}", message).replace("%", "%%");
            for (Player p : world.getPlayers()) {
                if (p.getLocation().distance(player.getLocation()) <= 100) {
                    p.sendMessage(formattedMessage);
                }
            }
        }
    }

}
