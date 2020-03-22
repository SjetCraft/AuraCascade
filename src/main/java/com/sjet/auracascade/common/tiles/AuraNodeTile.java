package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeTile extends BaseAuraNodeTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final TileEntityType<AuraNodeTile> TYPE_NODE = null;

    public AuraNodeTile() {
        super(TYPE_NODE);
    }
}

