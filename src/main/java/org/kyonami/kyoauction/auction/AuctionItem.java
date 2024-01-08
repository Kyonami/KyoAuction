package org.kyonami.kyoauction.auction;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuctionItem {
    private ItemStack itemStack;
    private ItemStack displayItemStack;
    private long price;
    private LocalDateTime expireTime;
    private UUID sellerID;

    public AuctionItem(ItemStack itemStack, ItemStack displayItemStack, long price, LocalDateTime expireTime, UUID sellerID)
    {
        this.itemStack = itemStack;
        this.displayItemStack = displayItemStack;
        this.price = price;
        this.expireTime = expireTime;
        this.sellerID = sellerID;
    }
    public AuctionItem(ItemStack itemStack, long price, LocalDateTime expireTime, UUID sellerID, String sellerName){
        this.itemStack = itemStack;
        this.price = price;
        this.expireTime = expireTime;
        this.sellerID = sellerID;
        this.displayItemStack = itemStack.clone();

        ItemMeta itemMeta = this.displayItemStack.getItemMeta();
        List<String> lores = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lores.addAll(Arrays.asList("가격: "+ price, "판매자: " + sellerName,
                expireTime.getMonthValue() + "월 " + expireTime.getDayOfMonth() + "일 " + expireTime.getHour() + "시 " + expireTime.getMinute() + "분에 만료."));
        itemMeta.setLore(lores);
        this.displayItemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack(){ return this.itemStack;}
    public ItemStack getDisplayItemStack() { return this.displayItemStack; }
    public UUID getSellerID(){ return this.sellerID; }
    public long getPrice() { return this.price;}
    public LocalDateTime getExpireTime() { return this.expireTime; }
    public boolean isExpired(LocalDateTime compareTime){ return this.expireTime.isBefore(compareTime); }
}
