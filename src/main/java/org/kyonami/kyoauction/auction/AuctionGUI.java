package org.kyonami.kyoauction.auction;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.kyonami.kyoauction.gui.BaseGUI;
import org.kyonami.kyoauction.gui.GUIItem;

import java.util.Arrays;
import java.util.List;

public class AuctionGUI extends BaseGUI {
    public AuctionGUI() {
        super(54, "거래소");
    }

    // objects[0] = ItemList
    @Override
    protected void onCreateGui(Player player, Object... objects) {
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 36);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 37);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 38);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 39);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 40);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 41);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 42);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 43);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 44);
        addGUIItem("이전 페이지", Material.OAK_SIGN, Arrays.asList(), 45);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 46);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 47);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 48);
        addGUIItem("검색", Material.SPYGLASS, Arrays.asList(), 49);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 50);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 51);
        addGUIItem("§0.", Material.BLACK_STAINED_GLASS_PANE, Arrays.asList(), 52);
        addGUIItem("다음 페이지", Material.OAK_SIGN, Arrays.asList(), 53);

        List<AuctionItem> itemList = (List<AuctionItem>)objects[0];

        int listSize = itemList.size();
        for(int i = 0; i < 36; i++)
        {
            if(listSize <= i)
                break;

            addGUIItem(itemList.get(i).getItemStack(), i);
        }
    }
}
