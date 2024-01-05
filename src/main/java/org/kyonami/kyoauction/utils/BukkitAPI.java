package org.kyonami.kyoauction.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BukkitAPI {
    public static Player findOnlinePlayer(String playerName){
        for(Player player : Bukkit.getOnlinePlayers())
            if(player.getName().equals(playerName))
                return player;

        return null;
    }

    public static OfflinePlayer findOfflinePlayer(String playerName){
        for(OfflinePlayer player : Bukkit.getOfflinePlayers())
            if(player.getName().equals(playerName))
                return player;

        return null;
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage("[KyoAuction] " + message);
    }
}
