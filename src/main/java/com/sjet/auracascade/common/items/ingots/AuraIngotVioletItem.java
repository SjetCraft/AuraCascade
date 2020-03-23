package com.sjet.auracascade.common.items.ingots;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraIngotVioletItem extends BaseAuraIngotItem {

    public AuraIngotVioletItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_ingot_violet");

        this.auraColor = IAuraColor.VIOLET;
    }
}
