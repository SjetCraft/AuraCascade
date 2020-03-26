package com.sjet.auracascade.common.items;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.item.Item;


public class ArcanePrismItem extends Item {

    public ArcanePrismItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "arcane_prism");
    }
}
