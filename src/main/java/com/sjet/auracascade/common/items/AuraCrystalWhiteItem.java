package com.sjet.auracascade.common.items;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.item.Item;

public class AuraCrystalWhiteItem extends BaseAuraCrystalItem {

    public AuraCrystalWhiteItem() {
        super(new Item.Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_white");

        this.auraColor = IAuraColor.WHITE;
    }
}
