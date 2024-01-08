package org.kyonami.kyoauction.auction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.kyonami.kyoauction.utils.BukkitAPI;
import org.kyonami.kyoauction.utils.Const;
import org.kyonami.kyoauction.utils.Config;
import org.kyonami.kyoauction.utils.UserConfig;

import javax.annotation.Nullable;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;

public class AuctionManager {
    private static AuctionManager _instance = null;
    public static AuctionManager getInstance() {
        if(_instance == null)
            _instance = new AuctionManager();

        return _instance;
    }

    private int getIndex(int page, int location) { return (page - 1) * Const.ITEM_PER_PAGE + location; }
    List<AuctionItem> productList = new ArrayList<>();
    Map<UUID, List<AuctionItem>> expiredItemMap = new HashMap<>();

    private final Config config = new Config("plugins/KyoAuction/Auction.yml");
    private boolean hasRevisionInProductList = false;
    private boolean hasRevisionInExpiredList = false;


    @Nullable
    public AuctionItem getProduct(int page, int location){
        int index = getIndex(page, location);
        if(productList.size() <= index)
            return null;
        return productList.get(index);
    }

    @Nullable
    public AuctionItem getProduct(UUID playerID, int location){
        int counter = 0;
        for(AuctionItem item : productList) {
            if(item.getSellerID() == playerID)
                if(counter == location)
                    return item;
                else
                    counter++;
        }

        return null;
    }

    public void addProduct(ItemStack itemStack, long price, Player seller){
        AuctionItem auctionItem = new AuctionItem(itemStack, price, LocalDateTime.now().plusHours(UserConfig.getInstance().expirePeriod), seller.getUniqueId(), seller.getName());
        productList.add(auctionItem);
        hasRevisionInProductList = true;
    }

    public void removeProduct(int page, int location) {
        productList.remove(getIndex(page, location));
        hasRevisionInProductList = true;
    }

    public List<AuctionItem> getProductListPerPage(int page){
        int startIndex = getIndex(page, 0);
        int endIndex = Math.min(productList.size(), getIndex(page, Const.ITEM_PER_PAGE));

        if(productList.size() <= startIndex)
            return new ArrayList<>();

        return productList.subList(startIndex, endIndex);
    }
    // 플레이어 아이디로 필터링
    public List<AuctionItem> getProductList(UUID playerID){
        List<AuctionItem> filteredList = new ArrayList<>();

        for(AuctionItem item : productList)
            if(item.getSellerID() == playerID)
                filteredList.add(item);

        return filteredList;
    }


    public List<AuctionItem> getExpiredItem(UUID playerID){
        if(!expiredItemMap.containsKey(playerID))
            expiredItemMap.put(playerID, new ArrayList<>());
        return expiredItemMap.get(playerID);
    }
    public AuctionItem getExpiredItem(UUID playerID, int location){
        if(!expiredItemMap.containsKey(playerID) ||expiredItemMap.get(playerID).size() <= location)
            return null;

        return expiredItemMap.get(playerID).get(location);
    }
    public void removeExpiredItem(UUID playerID, int location){
        if(!expiredItemMap.containsKey(playerID) ||expiredItemMap.get(playerID).size() <= location)
            return;

        expiredItemMap.get(playerID).remove(location);
        hasRevisionInExpiredList = true;
    }

    public void cleanExpiredItem(){
        for(int i = productList.size() - 1; 0 <= i; i--)
        {
            if(productList.get(i).isExpired(LocalDateTime.now())) {
                AuctionItem auctionItem = productList.get(i);

                if(!expiredItemMap.containsKey(auctionItem.getSellerID()))
                    expiredItemMap.put(auctionItem.getSellerID(), new ArrayList<>());

                expiredItemMap.get(auctionItem.getSellerID()).add(auctionItem);

                BukkitAPI.sendMessage(auctionItem.getSellerID(), "만료된 품목이 있습니다.");
                productList.remove(i);
                hasRevisionInProductList = true;
                hasRevisionInExpiredList = true;
            }
        }
    }

    private void setAuctionItemSection(ConfigurationSection section, String prefix, AuctionItem auctionItem){
        section.set(prefix + ".itemStack", auctionItem.getItemStack().serialize());
        section.set(prefix + ".displayItemStack", auctionItem.getDisplayItemStack().serialize());
        section.set(prefix + ".price", auctionItem.getPrice());
        section.set(prefix + ".expireTime", auctionItem.getExpireTime().toString());
        section.set(prefix + ".sellerID", auctionItem.getSellerID().toString());
    }
    public void save() {
        ConfigurationSection section;
        if(hasRevisionInProductList) {
            section = config.createSection("productList");

            for (int i = 0; i < productList.size(); i++)
                setAuctionItemSection(section, String.valueOf(i), productList.get(i));

            try {
                config.set("productList", section);
                config.save();
                hasRevisionInProductList = false;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, e.toString());
            }
        }
        if(hasRevisionInExpiredList) {
            section = config.createSection("expiredList");

            for (UUID id : expiredItemMap.keySet()) {
                List<AuctionItem> expiredItemList = expiredItemMap.get(id);
                for(int i = 0; i < expiredItemList.size(); i++)
                    setAuctionItemSection(section, String.valueOf(i), expiredItemList.get(i));
            }

            try {
                config.set("expiredList", section);
                config.save();
                hasRevisionInExpiredList = false;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, e.toString());
            }
        }
    }

    private AuctionItem getAuctionItem(ConfigurationSection section, String prefix){
        ItemStack itemStack = ItemStack.deserialize(section.getConfigurationSection(prefix + ".itemStack").getValues(false));
        ItemStack displayItemStack = ItemStack.deserialize(section.getConfigurationSection(prefix + ".displayItemStack").getValues(false));
        long price = section.getLong(prefix + ".price");
        LocalDateTime expireTime = LocalDateTime.parse(section.getString(prefix + ".expireTime"));
        UUID sellerID = UUID.fromString(section.getString(prefix + ".sellerID"));
        return new AuctionItem(itemStack, displayItemStack, price, expireTime, sellerID);
    }
    public void load() {
        ConfigurationSection section = config.getConfigurationSection("productList");
        productList.clear();
        if(section != null) // no save data
            for (String key : section.getKeys(false))
                productList.add(getAuctionItem(section, key));

        section = config.getConfigurationSection("expiredList");
        expiredItemMap.clear();
        if(section != null) // no save data
            for (String key : section.getKeys(false)) {
                AuctionItem auctionItem = getAuctionItem(section, key);

                if(!expiredItemMap.containsKey(auctionItem.getSellerID()))
                    expiredItemMap.put(auctionItem.getSellerID(), new ArrayList<>());

                expiredItemMap.get(auctionItem.getSellerID()).add((auctionItem));
            }
    }
}
