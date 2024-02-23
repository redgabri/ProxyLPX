package it.redgabri.lpxproxy.bungee.utils;

import net.md_5.bungee.api.ChatColor;

public class Utils {
    public static String format(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
