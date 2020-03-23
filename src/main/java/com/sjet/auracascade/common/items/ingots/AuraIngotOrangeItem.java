package com.sjet.auracascade.common.items.ingots;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraIngotOrangeItem extends BaseAuraIngotItem {

    public AuraIngotOrangeItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_ingot_orange");

        this.auraColor = IAuraColor.ORANGE;
    }
}
