package com.sjet.auracascade.common.items.crystals;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalGreenItem extends BaseAuraCrystalItem {

    public AuraCrystalGreenItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_green");

        this.auraColor = IAuraColor.GREEN;
    }
}
