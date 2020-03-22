package com.sjet.auracascade.common.items;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalRedItem extends BaseAuraCrystalItem {

    public AuraCrystalRedItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_red");

        this.auraColor = IAuraColor.RED;
    }
}
