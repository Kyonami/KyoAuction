package org.kyonami.kyoauction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.kyonami.kyoauction.auction.AuctionManager;
import org.kyonami.kyoauction.auction.task.AuctionExpiredItemCheckTask;
import org.kyonami.kyoauction.auction.task.AuctionSaveTask;
import org.kyonami.kyoauction.commands.AuctionCommand;
import org.kyonami.kyoauction.events.OnAuctionEvent;
import org.kyonami.kyoauction.utils.Config;
import org.kyonami.kyoauction.utils.Const;
import org.kyonami.kyoauction.utils.UserConfig;

import java.io.File;
import java.util.logging.Level;

public final class KyoAuction extends JavaPlugin {
    private static KyoAuction _instance = null;
    public static KyoAuction getInstance() {
        return _instance;
    }

    @Override
    public void onEnable(){
        _instance = this;

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new OnAuctionEvent(), this);
        getCommand("auction").setExecutor(new AuctionCommand());

        AuctionManager.getInstance().load();
        Bukkit.getScheduler().runTaskTimer(this, new AuctionExpiredItemCheckTask(), 0, Const.TICKS_PER_SEC * UserConfig.getInstance().expireCheckInterval);
        Bukkit.getScheduler().runTaskTimer(this, new AuctionSaveTask(), 0, Const.TICKS_PER_SEC *  UserConfig.getInstance().auctionSaveInterval);
        Bukkit.getLogger().log(Level.INFO, "[KyoAuction] Enable");
    }

    @Override
    public void onDisable(){
        AuctionManager.getInstance().cleanExpiredItem();
        AuctionManager.getInstance().save();
        Bukkit.getLogger().log(Level.INFO, "[KyoAuction] Disable");
    }
}
