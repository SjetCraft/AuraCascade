package com.sjet.auracascade.common.items.ingots;

import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraIngotItem;
import net.minecraft.item.Item;


public abstract class BaseAuraIngotItem extends Item implements IBaseAuraIngotItem {

    protected IAuraColor auraColor;

    public BaseAuraIngotItem(Properties properties) {
        super(properties);
    }

    public IAuraColor getColor() {
        return auraColor;
    }
}
