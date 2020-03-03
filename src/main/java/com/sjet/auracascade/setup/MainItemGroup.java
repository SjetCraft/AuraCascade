package com.sjet.auracascade.setup;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.AuraNode;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MainItemGroup extends ItemGroup {
    public MainItemGroup() {
        super(AuraCascade.MODID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(AuraNode.BLOCK);
    }
}
