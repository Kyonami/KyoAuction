package org.kyonami.kyoauction.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

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

    public static void sendMessage(UUID playerUUID, String message){
        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null)
            return;
        player.sendMessage("§6[KyoAuction]§f " + message);
    }
    public static void sendMessage(Player player, String message){
        player.sendMessage("§6[KyoAuction]§f " + message);
    }
}
