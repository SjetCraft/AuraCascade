package com.sjet.auracascade.common.items;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalBlueItem extends BaseAuraCrystalItem {

    public AuraCrystalBlueItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_blue");

        this.auraColor = IAuraColor.BLUE;
    }
}
