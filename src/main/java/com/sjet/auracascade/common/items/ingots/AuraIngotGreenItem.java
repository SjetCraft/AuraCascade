package com.sjet.auracascade.common.items.ingots;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraIngotGreenItem extends BaseAuraIngotItem {

    public AuraIngotGreenItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_ingot_green");

        this.auraColor = IAuraColor.GREEN;
    }
}
