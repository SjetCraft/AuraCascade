package com.sjet.auracascade.common.items.gems;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraGemBlueItem extends BaseAuraGemItem {

    public AuraGemBlueItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_gem_blue");

        this.auraColor = IAuraColor.BLUE;
    }
}
