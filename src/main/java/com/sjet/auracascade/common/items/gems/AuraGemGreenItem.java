package com.sjet.auracascade.common.items.gems;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraGemGreenItem extends BaseAuraGemItem {

    public AuraGemGreenItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_gem_green");

        this.auraColor = IAuraColor.GREEN;
    }
}
