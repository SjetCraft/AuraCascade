package com.sjet.auracascade.common.items;

import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.item.Item;

public abstract class AuraCrystalItem extends Item {

    protected IAuraColor auraColor;

    public AuraCrystalItem(Item.Properties properties) {
        super(properties);
    }

    public IAuraColor getColor() {
        return auraColor;
    }

    public int getAura() {
        return 1000;
    }
}
