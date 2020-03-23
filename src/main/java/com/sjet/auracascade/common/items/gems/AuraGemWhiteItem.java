package com.sjet.auracascade.common.items.gems;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraGemWhiteItem extends BaseAuraGemItem {

    public AuraGemWhiteItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_gem_white");

        this.auraColor = IAuraColor.WHITE;
    }
}
