package org.kyonami.kyoauction.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIItem {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private int location;

    public GUIItem(Material material, int location) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public GUIItem(ItemStack itemStack, int location){
        this.itemStack = itemStack;
        this.location = location;
    }

    private void setLocation(int location) {
        this.location = location;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public ItemMeta getItemMeta() {
        return this.itemMeta;
    }

    public int getLocation() {
        return this.location;
    }
}
