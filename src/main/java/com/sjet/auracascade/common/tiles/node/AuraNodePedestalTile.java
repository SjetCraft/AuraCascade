package com.sjet.auracascade.common.tiles.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.HUDHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class AuraNodePedestalTile extends BaseAuraNodeInventoryTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pedestal")
    public static final TileEntityType<AuraNodePedestalTile> TYPE_NODE = null;

    public AuraNodePedestalTile() {
        super(TYPE_NODE);
    }

    /**
     * Used to render the amount of Aura on the screen when the cursor is on this node
     *
     * @param minecraft
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft minecraft) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Aura Stored");

        for (IAuraColor color : IAuraColor.values()) {
            int auraAmount = auraMap.get(color);
            if (auraAmount > 0) {
                list.add("    " + color.capitalizedName() + ": " + auraAmount);
            }
        }
        if (list.size() == 1) {
            list.set(0, "No Aura");
        }

        if (isEmpty()) {
            list.add("No Item Stored");
        } else {
            list.add(getItem().getDisplayName().getFormattedText());
        }

        HUDHelper.printTextOnScreen(minecraft, list);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return (index > 0) ? false : isEmpty();
    }
}

