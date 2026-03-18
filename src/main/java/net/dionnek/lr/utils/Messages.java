package net.dionnek.lr.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
}
