package org.kyonami.kyoauction.auction;

import org.kyonami.kyoauction.KyoAuction;

import java.util.List;

public class AuctionManager {
    private static AuctionManager _instance = null;
    public static AuctionManager getInstance() {
        if(_instance == null)
            _instance = new AuctionManager();

        return _instance;
    }


    List<AuctionItem> productList;

    public List<AuctionItem> getProductList(){
        return productList;
    }

    public List<AuctionItem> getProductListPerPage(int page){
        return productList.subList((page - 1) * 36, page * 36 - 1);
    }
}
