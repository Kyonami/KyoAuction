package org.kyonami.kyoauction;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class KyoAuction extends JavaPlugin {
    private static KyoAuction _instance = null;
    public static KyoAuction getInstance() { return _instance; }

    @Override
    public void onEnable(){
        _instance = this;

        Bukkit.getLogger().log(Level.INFO, "[KyoEconomy] Enable");
    }

    @Override
    public void onDisable(){
        Bukkit.getLogger().log(Level.INFO, "[simplemoney] Disable");
    }
}
