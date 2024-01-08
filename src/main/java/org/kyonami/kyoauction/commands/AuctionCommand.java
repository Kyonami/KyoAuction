package org.kyonami.kyoauction.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kyonami.kyoauction.auction.AuctionGUI;
import org.kyonami.kyoauction.auction.AuctionItem;
import org.kyonami.kyoauction.auction.AuctionManager;
import org.kyonami.kyoauction.utils.BukkitAPI;
import org.kyonami.kyoauction.utils.Const;

import java.util.List;
import java.util.logging.Level;

public class AuctionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.RED + "You cant.");
            return false;
        }

        if(args.length <= 0) {
            showAuctionPopup(Const.AUCTION_NAME, player, AuctionManager.getInstance().getProductListPerPage(1), 1);
            return true;
        }

        switch(args[0]) {
            case "register":
                if(args.length != 3) {
                    BukkitAPI.sendMessage(player, "/auction register [개수] [가격]");
                    break;
                }
                int count;
                long price;
                try{
                    count = Integer.parseInt(args[1]);
                    price = Long.parseLong(args[2]);
                }
                catch(Exception e){ Bukkit.getLogger().log(Level.WARNING, e.toString()); return true; }
                registerItem(player, count, price);
                break;
            case "list":
                if(args.length != 1) {
                    BukkitAPI.sendMessage(player, "/auction list");
                    break;
                }
                showAuctionPopup(Const.REGISTERED_ITEM_LIST_NAME, player, AuctionManager.getInstance().getProductList(player.getUniqueId()), 1);
                break;
            case "expired":
                if(args.length != 1) {
                    BukkitAPI.sendMessage(player, "/auction expired");
                    break;
                }
                showAuctionPopup(Const.EXPIRED_ITEM_LIST_NAME, player, AuctionManager.getInstance().getExpiredItem(player.getUniqueId()), 1);
                break;
            case "help":
            default:
                BukkitAPI.sendMessage(player,
                        "\n/auction: 거래소를 보여줍니다.\n" +
                                "/auction register [개수] [가격]: 손에 든 아이템을 거래소에 등록합니다.\n" +
                                "/auction list: 현재 나의 판매중 아이템 목록을 보여줍니다. (등록 상품 취소가 가능합니다.)\n" +
                                "/auction expired: 현재 나의 판매 만료된 아이템 목록을 보여줍니다.");
                break;
        }

        return true;
    }

    public void showAuctionPopup(String title, Player player, List<AuctionItem> productList, int page){
        new AuctionGUI(page, title).showGUI(player, productList);
    }

    public void registerItem(Player player, int count, long price){
        ItemStack mainHandItemClone = player.getInventory().getItemInMainHand().clone();
        if(mainHandItemClone.getType().isAir()) {
            BukkitAPI.sendMessage(player, "아이템을 들고 다시 시도해주세요.");
            return;
        }

        if(mainHandItemClone.getAmount() < count){
            BukkitAPI.sendMessage(player, "개수가 부족합니다.");
            return;
        }

        // 상품 리스트에 추가
        mainHandItemClone.setAmount(count);
        AuctionManager.getInstance().addProduct(mainHandItemClone, price, player);

        // 플레이어 손에서 아이템 뺏음
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        mainHandItem.setAmount(mainHandItem.getAmount() - count);

        BukkitAPI.sendMessage(player, "상품이 등록되었습니다.");
    }
}
