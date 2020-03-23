package com.sjet.auracascade.common.items.gems;

import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraGemItem;
import net.minecraft.item.Item;


public abstract class BaseAuraGemItem extends Item implements IBaseAuraGemItem {

    protected IAuraColor auraColor;

    public BaseAuraGemItem(Properties properties) {
        super(properties);
    }

    public IAuraColor getColor() {
        return auraColor;
    }
}
