package com.sjet.auracascade.common.items;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalBlackItem extends BaseAuraCrystalItem {

    public AuraCrystalBlackItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_black");

        this.auraColor = IAuraColor.BLACK;
    }
}
