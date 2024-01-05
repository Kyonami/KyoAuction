package org.kyonami.kyoauction.auction;

import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuctionItem {
    private ItemStack itemStack;
    private long price;
    private LocalDateTime expireTime;
    private LocalDateTime registerTime;
    private UUID sellerID;

    private AuctionItem(ItemStack itemStack, long price, LocalDateTime expireTime, LocalDateTime registerTime, UUID sellerID){
        this.itemStack = itemStack;
        this.price = price;
        this.expireTime = expireTime;
        this.registerTime = registerTime;
        this.sellerID = sellerID;
    }

    public ItemStack getItemStack(){ return itemStack;}

}
