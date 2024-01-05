package org.kyonami.kyoauction.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGUI {
    protected Inventory inventory;

    protected List<GUIItem> guiItemList;

    public BaseGUI(int size, String title){
        this.inventory = Bukkit.createInventory(null, size, title);
        this.guiItemList = new ArrayList<>();
    }
    protected abstract void onCreateGui(Player player, Object...objects);

    public void showGUI(Player player, Object...objects) {
        try {
            onCreateGui(player, objects);
            open(player, inventory);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    private void open(Player player, Inventory inventory) {
        for(GUIItem guiItem : guiItemList) {
            inventory.setItem(guiItem.getLocation(), guiItem.getItemStack());
        }
        player.openInventory(inventory);
    }

    protected void addGUIItem(ItemStack item, int location){
        guiItemList.add(new GUIItem(item, location));
    }
    protected void addGUIItem(String display, Material id, List<String> lore, int location) {
        GUIItem guiItem = new GUIItem(id, location);
        setDefaultItem(guiItem, display, lore);
        guiItemList.add(guiItem);
    }
    private void setDefaultItem(GUIItem guiItem, String display, List<String> lore) {
        ItemStack itemStack = guiItem.getItemStack();
        ItemMeta itemMeta = guiItem.getItemMeta();
        //아이템 설정
        itemMeta.setDisplayName(display);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
