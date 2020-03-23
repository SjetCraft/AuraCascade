package com.sjet.auracascade.common.items.crystals;

import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraCrystalItem;
import net.minecraft.item.Item;

import static com.sjet.auracascade.AuraCascade.AURA_IN_ITEM;

public abstract class BaseAuraCrystalItem extends Item implements IBaseAuraCrystalItem {

    protected IAuraColor auraColor;

    public BaseAuraCrystalItem(Item.Properties properties) {
        super(properties);
    }

    public IAuraColor getColor() {
        return auraColor;
    }

    public int getAura() {
        return AURA_IN_ITEM;
    }
}
