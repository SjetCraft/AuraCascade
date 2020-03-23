package com.sjet.auracascade.common.items.gems;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;

public class AuraGemYellowItem extends BaseAuraGemItem {

    public AuraGemYellowItem() {
        super(new Properties().group(AuraCascade.MAIN_GROUP));

        this.setRegistryName(AuraCascade.MODID, "aura_gem_yellow");

        this.auraColor = IAuraColor.YELLOW;
    }
}
