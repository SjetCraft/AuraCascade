package com.sjet.auracascade.common.items.crystals;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraCrystalYellowItem extends BaseAuraCrystalItem {

    public AuraCrystalYellowItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_crystal_yellow");

        this.auraColor = IAuraColor.YELLOW;
    }
}
