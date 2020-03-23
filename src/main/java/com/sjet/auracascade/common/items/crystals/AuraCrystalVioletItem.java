package com.sjet.auracascade.common.items.crystals;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalVioletItem extends BaseAuraCrystalItem {

    public AuraCrystalVioletItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_violet");

        this.auraColor = IAuraColor.VIOLET;
    }
}
