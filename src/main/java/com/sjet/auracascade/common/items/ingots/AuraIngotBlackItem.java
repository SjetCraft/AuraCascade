package com.sjet.auracascade.common.items.ingots;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraIngotBlackItem extends BaseAuraIngotItem {

    public AuraIngotBlackItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_ingot_black");

        this.auraColor = IAuraColor.BLACK;
    }
}
