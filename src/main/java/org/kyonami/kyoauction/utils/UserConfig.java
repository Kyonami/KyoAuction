package org.kyonami.kyoauction.utils;

import org.kyonami.kyoauction.KyoAuction;

public class UserConfig {
    private static UserConfig _instance = null;
    public static UserConfig getInstance() {
        if(_instance == null)
            _instance = new UserConfig();
        return _instance;
    }

    public int expirePeriod;
    public int auctionChargeRatio;
    public long expireCheckInterval;
    public long auctionSaveInterval;

    public UserConfig(){
        expirePeriod = KyoAuction.getInstance().getConfig().getInt("EXPIRE_PERIOD", 48);
        auctionChargeRatio = KyoAuction.getInstance().getConfig().getInt("AUCTION_CHARGE", 0);
        expireCheckInterval = KyoAuction.getInstance().getConfig().getInt("EXPIRE_CHECK_INTERVAL", 20);
        auctionSaveInterval = KyoAuction.getInstance().getConfig().getInt("AUCTION_SAVE_INTERVAL", 3);
    }
}
