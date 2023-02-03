package me.bounser.nascraft.advancedgui;

import me.bounser.nascraft.market.Category;
import me.bounser.nascraft.market.Item;
import me.bounser.nascraft.market.MarketManager;
import me.bounser.nascraft.tools.Config;
import me.bounser.nascraft.tools.ImageManager;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.components.*;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;

import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class LayoutModifier implements LayoutExtension {

    @Override
    @EventHandler
    public void onLayoutLoad(LayoutLoadEvent event) {

        if (!event.getLayout().getName().equals("Nascraft")) return;

        GroupComponent cTree = event.getLayout().getTemplateComponentTree();

        // SlideComponent
        DummyComponent dct = cTree.locate("slide123", DummyComponent.class);
        dct.setComponent(new SlideComponent("slide1", null, false,
                event.getLayout().getDefaultInteraction(), 44, 55, 300, 140, Arrays.asList(1f, 1f)));

        // GraphComponent
        DummyComponent dc = cTree.locate("graph123", DummyComponent.class);
        dc.setComponent(new GraphComponent("graph1", null, false,
                event.getLayout().getDefaultInteraction(), 44, 55, 300, 140, Arrays.asList(1f, 1f)));

        // Main page
        updateMainPage(cTree);

        // Arrows
        cTree.locate("ArrowUP").setClickAction((interaction, player, primaryTrigger) -> {
            MarketManager.getInstance().changeCategoryOrder(true);
            updateMainPage(interaction.getComponentTree());
        });

        cTree.locate("ArrowDOWN").setClickAction((interaction, player, primaryTrigger) -> {
            MarketManager.getInstance().changeCategoryOrder(false);
            updateMainPage(interaction.getComponentTree());
        });

        // Time Span selectors
        for (int i = 1; i <= 5; i++) {

            int finalI = i;
            cTree.locate("timesel" + i, RectComponent.class).setClickAction((interaction, player, primaryTrigger) -> {

                interaction.getComponentTree().locate("graph1", GraphComponent.class).setTimeFrame(finalI);

            });
        }
    }

    public void updateMainPage(GroupComponent cTree) {

        for (int i = 1; i <= 3; i++) {

            Category cat = MarketManager.getInstance().getCategoryOfIndex(i - 1);
            int numOfItems = cat.getNumOfItems();

            for (int j = 1; j <= 5; j++) {

                if (j <= numOfItems) {

                    cTree.locate("t" + i + j + "1", TextComponent.class).setText(String.valueOf(cat.getItemOfIndex(j-1).getPrice()));
                    cTree.locate("t" + i + j + "2", TextComponent.class).setText(String.valueOf(cat.getItemOfIndex(j-1).getPrice()));
                    ImageComponent ic = cTree.locate("asdi" + i + "" + j, ImageComponent.class);
                    ic.setImage(ImageManager.getInstance().getImage(cat.getItemOfIndex(j - 1).getMaterial(), 32, 32, false));

                    int finalJ = j;
                    ic.setClickAction((interaction, player, primaryTrigger) -> {

                        interaction.getComponentTree().locate("nbk2fMcG", ViewComponent.class).setView("qrRtaAnd");
                        interaction.getComponentTree().locate("graph1", GraphComponent.class).changeMat(cat.getItemOfIndex(finalJ - 1).getMaterial());

                    });
                } else {
                    cTree.locate("asdi" + i + "" + j, ImageComponent.class).setHidden(true);
                    cTree.locate("t" + i + "" + j + "1", TextComponent.class).setHidden(true);
                    cTree.locate("t" + i + "" + j + "2", TextComponent.class).setHidden(true);
                }
            }
        }
    }


    @EventHandler
    public void onInteractionStart(GuiInteractionBeginEvent event) {

        if (!event.getGuiInstance().getLayout().getName().equals("Nascraft")) return;

        updateTrending(event.getInteraction());

        updateSuggestions(event.getInteraction(),event.getPlayer());

    }


    public void updateTrending(Interaction i) {
        Item max = null;

        for(Item item : MarketManager.getInstance().getAllItems()) {
            if((max == null || max.getOperations() < item.getOperations()) && item.getOperations() != 0) max = item;
        }

        GroupComponent icTree = i.getComponentTree();

        if(max != null) {
            ImageComponent ic = icTree.locate("trend", ImageComponent.class);
            ic.setHidden(false);
            ic.setImage(ImageManager.getInstance().getImage(max.getMaterial(), 33, 33, false));
            Item finalMax = max;
            ic.setClickAction((interaction, player, primaryTrigger) -> {
                interaction.getComponentTree().locate("nbk2fMcG", ViewComponent.class).setView("qrRtaAnd");
                interaction.getComponentTree().locate("graph1", GraphComponent.class).changeMat(finalMax.getMaterial());
            });
        } else {
            icTree.locate("trend1", GroupComponent.class).setHidden(true);
        }
    }

    public void updateSuggestions(Interaction inter, Player p) {
        GroupComponent icTree = inter.getComponentTree();
        HashMap<Integer, String> content = new HashMap<>();

        Inventory inv = p.getInventory();
        for(ItemStack is : inv.getContents()) {

            if(is != null)
            for(Item item : MarketManager.getInstance().getAllItems()) {

                if(is.getType().toString().equals(item.getMaterial())) {
                    content.put(is.getAmount(), item.getMaterial());
                }
            }
        }

        if(content.size() > 2)
            for(int i = 1; i <= 3; i++) {

                int maxx = 0;
                for(int amount : content.keySet()) {
                    if(amount > maxx) maxx = amount;
                }

                String mat = content.get(maxx);
                ImageComponent ic = icTree.locate("sug" + i, ImageComponent.class);

                ic.setHidden(false);
                ic.setImage(ImageManager.getInstance().getImage(mat, 33, 33, false));
                ic.setClickAction((interaction, player, primaryTrigger) -> {
                    interaction.getComponentTree().locate("nbk2fMcG", ViewComponent.class).setView("qrRtaAnd");
                    interaction.getComponentTree().locate("graph1", GraphComponent.class).changeMat(mat);
                });

                content.remove(maxx);

            }
    }

}
