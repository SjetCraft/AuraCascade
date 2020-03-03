package com.sjet.auracascade.common.items;

import net.minecraft.item.Item;

public abstract class AuraCrystalItem extends Item {

    public AuraCrystalItem(Item.Properties properties) {
        super(properties);
    }

    enum Crystal {WHITE, YELLOW, ORANGE, RED, GREEN, BLUE, VIOLET, BLACK}

    private Crystal color;

    public Crystal getColor() {
        return color;
    }

    public void setColor(Crystal color) {
        this.color = color;
    }
}
