package com.sjet.auracascade.common.tiles.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeManipulatorBlackTile extends BaseAuraNodeManipulatorTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_manipulator_black")
    public static final TileEntityType<AuraNodeManipulatorBlackTile> TYPE_MANIPULATOR_BLACK = null;

    public AuraNodeManipulatorBlackTile() {
        super(TYPE_MANIPULATOR_BLACK);
    }

    @Override
    public IAuraColor getAuraType() {
        return IAuraColor.BLACK;
    }

}
