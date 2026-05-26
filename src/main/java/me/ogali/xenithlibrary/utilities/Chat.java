package me.ogali.xenithlibrary.utilities;

import me.ogali.xenithlibrary.XenithLibrary;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static void log(String... messages) {
        for (final String message : messages)
            log(message);
    }

    public static void log(String messages) {
        tell(Bukkit.getConsoleSender(), "[" + XenithLibrary.getInstance().getName() + "] " + messages);
    }

    public static void tell(CommandSender toWhom, String... messages) {
        for (final String message : messages)
            tell(toWhom, message);
    }

    public static void tell(CommandSender toWhom, List<String> messages) {
        for (final String message : messages)
            tell(toWhom, message);
    }

    public static void tell(CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static void tellActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorize(message)));
    }

    public static void tellFormatted(CommandSender toWhom, String message, Object... args) {
        toWhom.sendMessage(colorize(String.format(message, args)));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorizeList(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (String s : list)
            temp.add(colorize(s));
        return temp;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(colorize(text));
    }

    public static List<String> strip(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (String s : colorizeList(list)) {
            temp.add(ChatColor.stripColor(s));
        }
        return temp;
    }

    public static int getLength(String text, boolean ignoreColorCodes) {
        return ignoreColorCodes ? strip(text).length() : text.length();
    }

    public static String listToString(List<String> strings) {
        String delimiter = "ꨀ";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i != strings.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static List<String> stringToList(String string) {
        String delimiter = "ꨀ";
        return new ArrayList<>(List.of(string.split(delimiter)));
    }

    public static String listToString(List<String> list, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(delimiter);
        }
        return sb.toString();
    }
}