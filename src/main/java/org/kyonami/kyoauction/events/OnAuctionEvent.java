package org.kyonami.kyoauction.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kyonami.kyoauction.auction.AuctionGUI;
import org.kyonami.kyoauction.auction.AuctionItem;
import org.kyonami.kyoauction.auction.AuctionManager;
import org.kyonami.kyoauction.utils.BukkitAPI;
import org.kyonami.kyoauction.utils.Const;
import org.kyonami.kyoauction.utils.UserConfig;
import org.kyonami.kyoeconomy.money.MoneyInfo;
import org.kyonami.kyoeconomy.money.MoneyInfos;

import java.util.logging.Level;

public class OnAuctionEvent implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith(Const.AUCTION_NAME))
            processAuction(event);
        else if (title.startsWith(Const.REGISTERED_ITEM_LIST_NAME))
            processList(event);
        else if(title.startsWith(Const.EXPIRED_ITEM_LIST_NAME))
            processExpiredList(event);
    }

    private boolean isClickedNothing(InventoryClickEvent event){
        return event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().isAir();
    }

    private int getPage(String title, String type){
        int page;
        try{
            page = Integer.parseInt(title.replace(type + " (페이지 ", "").replace(")", ""));
        } catch(Exception e) {
            Bukkit.getLogger().log(Level.WARNING, e.toString());
            return 0;
        }
        return page;
    }

    private void processAuction(InventoryClickEvent event) {
        if (isClickedNothing(event) || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int page = getPage(event.getView().getTitle(), Const.AUCTION_NAME);
        ItemStack itemStack = event.getCurrentItem();

        if (Const.ITEM_PER_PAGE <= event.getSlot()) {
            switch (itemStack.getItemMeta().getDisplayName()) {
                case "다음 페이지":
                    event.setCancelled(true);
                    new AuctionGUI(page + 1).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page + 1));
                    break;
                case "이전 페이지":
                    event.setCancelled(true);
                    if (page <= 1) break;
                    new AuctionGUI(page - 1).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page - 1));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }

        // 구매 진행
        event.setCancelled(true);
        AuctionItem item = AuctionManager.getInstance().getProduct(page, event.getSlot());
        if (item == null) {
            BukkitAPI.sendMessage(player, "판매되었거나 만료된 상품입니다.");
            return;
        }
        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(player.getUniqueId());
        long price = item.getPrice();
        if (!moneyInfo.hasEnoughMoney(price)) {
            BukkitAPI.sendMessage(player, "돈이 부족합니다.");
            return;
        }

        long charge = price / 100 * UserConfig.getInstance().auctionChargeRatio;
        MoneyInfos.getInstance().addMoney(player.getUniqueId(), -price);
        MoneyInfos.getInstance().addMoney(item.getSellerID(), price - charge);
        player.getInventory().addItem(item.getItemStack());
        AuctionManager.getInstance().removeProduct(page, event.getSlot());
        BukkitAPI.sendMessage(player, "아이템 구매 완료. 잔액: " + moneyInfo.getMoneyString());
        BukkitAPI.sendMessage(item.getSellerID(),  "아이템 판매 완료. (수수료: " + charge + ") 잔액: " + MoneyInfos.getInstance().getMoneyInfo(item.getSellerID()).getMoneyString());
        event.getView().close();
    }

    private void processList(InventoryClickEvent event) {
        if (isClickedNothing(event) || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int page = getPage(event.getView().getTitle(), Const.REGISTERED_ITEM_LIST_NAME);
        ItemStack itemStack = event.getCurrentItem();

        if( Const.ITEM_PER_PAGE <= event.getSlot()) {
            switch (itemStack.getItemMeta().getDisplayName()) {
                case "다음 페이지":
                    event.setCancelled(true);
                    new AuctionGUI(page + 1, Const.REGISTERED_ITEM_LIST_NAME).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page + 1));
                    break;
                case "이전 페이지":
                    event.setCancelled(true);
                    if (page <= 1) break;
                    new AuctionGUI(page - 1, Const.REGISTERED_ITEM_LIST_NAME).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page - 1));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }

        event.setCancelled(true);
        AuctionItem item = AuctionManager.getInstance().getProduct(player.getUniqueId(), event.getSlot());
        if (item == null) return;
        player.getInventory().addItem(item.getItemStack());
        AuctionManager.getInstance().removeProduct(page, event.getSlot());
        BukkitAPI.sendMessage(player, "아이템 판매를 취소합니다.");
        event.getView().close();
    }
    private void processExpiredList(InventoryClickEvent event){
        if(isClickedNothing(event) || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player)event.getWhoClicked();
        int page = getPage(event.getView().getTitle(), Const.EXPIRED_ITEM_LIST_NAME);
        ItemStack itemStack = event.getCurrentItem();

        if(Const.ITEM_PER_PAGE <= event.getSlot()){
            switch (itemStack.getItemMeta().getDisplayName()){
                case "다음 페이지":
                    event.setCancelled(true);
                    new AuctionGUI(page + 1, Const.EXPIRED_ITEM_LIST_NAME).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page + 1));
                    break;
                case "이전 페이지":
                    event.setCancelled(true);
                    if(page <= 1) break;
                    new AuctionGUI(page - 1, Const.EXPIRED_ITEM_LIST_NAME).showGUI(player, AuctionManager.getInstance().getProductListPerPage(page - 1));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
            return;
        }

        event.setCancelled(true);
        AuctionItem item = AuctionManager.getInstance().getExpiredItem(player.getUniqueId(), event.getSlot());
        if(item == null) return;
        player.getInventory().addItem(item.getItemStack());
        AuctionManager.getInstance().removeExpiredItem(player.getUniqueId(), event.getSlot());
        BukkitAPI.sendMessage(player, "만료 상품을 수령합니다.");
        event.getView().close();
    }
}
