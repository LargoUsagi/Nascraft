package me.bounser.nascraft.market;

import me.bounser.nascraft.Nascraft;
import me.bounser.nascraft.tools.Config;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketManager {

    List<Item> items = new ArrayList<>();
    List<Category> categories = new ArrayList<>();

    private static MarketManager instance;
    private static Nascraft main;

    public static MarketManager getInstance() { return instance == null ? instance = new MarketManager() : instance; }

    private MarketManager() { setupItems(); }

    public void setupItems() {

        Bukkit.broadcastMessage("Settuping items " + Config.getInstance().getCategories());

        for(String cat : Config.getInstance().getCategories()) {

            Category category = new Category(cat);
            categories.add(category);

            for(String mat : Config.getInstance().getAllMaterials(cat)) {

                Bukkit.broadcastMessage("Item " + mat + " created in category " + category.getName());
                Item item = new Item(mat, category);
                items.add(item);
                category.addItem(item);
            }
        }
    }

    public Item getItem(String material) {

        for(Item item : items) {

            if(item.getMaterial() == material){ return item; }

        }
        return null;
    }

    public List<Item> getAllItems() { return items; }

    public Category getCategoryOfIndex(int index) {
        return categories.get(index);
    }

    public int getNumOfCategories() {
        return categories.toArray().length;
    }

    public void changeCategoryOrder(boolean up) {
        if (up) {
            Collections.rotate(categories, -1);
        } else {
            Collections.rotate(categories, 1);
        }
    }

}