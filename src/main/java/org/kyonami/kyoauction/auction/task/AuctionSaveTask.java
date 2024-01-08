package org.kyonami.kyoauction.auction.task;

import org.kyonami.kyoauction.auction.AuctionManager;

public class AuctionSaveTask implements Runnable{
    @Override
    public void run() {
        AuctionManager.getInstance().save();
    }
}
