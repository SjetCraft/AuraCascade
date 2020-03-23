package com.sjet.auracascade.common.items.crystals;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalOrangeItem extends BaseAuraCrystalItem {

    public AuraCrystalOrangeItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_orange");

        this.auraColor = IAuraColor.ORANGE;
    }
}
