package org.kyonami.kyoauction.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.plugin.Plugin;
import org.kyonami.kyoauction.auction.AuctionGUI;
import org.kyonami.kyoauction.auction.AuctionManager;
import org.kyonami.kyoauction.utils.BukkitAPI;

import java.util.logging.Level;

public class AuctionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.RED + "You cant.");
            return false;
        }

        if(args.length <= 0)
        {
            new AuctionGUI().showGUI(player, AuctionManager.getInstance().getProductListPerPage(1));
            return true;
        }

        switch(args[0])
        {
            case "register":
                int count;
                long price;
                try{
                    count = Integer.parseInt(args[1]);
                    price = Long.parseLong(args[2]);
                }
                catch(Exception e){ Bukkit.getLogger().log(Level.WARNING, e.toString()); return true; }
                registerItem(player, count, price);
                break;
        }

        return true;
    }

    public void registerItem(Player player, int count, long price){
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if(mainHandItem.getType() == Material.AIR)
        {
            BukkitAPI.sendMessage(player, "아이템을 들고 다시 시도해주세요.");
            return;
        }

        if(mainHandItem.getAmount() < count){
            BukkitAPI.sendMessage(player, "개수가 부족합니다.");
            return;
        }

        if()
    }
}
