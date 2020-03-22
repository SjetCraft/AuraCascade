package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeManipulatorOrangeTile extends BaseAuraNodeManipulatorTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_manipulator_orange")
    public static final TileEntityType<AuraNodeManipulatorOrangeTile> TYPE_MANIPULATOR_BLACK = null;

    public AuraNodeManipulatorOrangeTile() {
        super(TYPE_MANIPULATOR_BLACK);
    }

    @Override
    public IAuraColor getAuraType() {
        return IAuraColor.ORANGE;
    }

}
