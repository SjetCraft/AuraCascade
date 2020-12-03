package com.sjet.auracascade.common.tiles.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeConserveTile extends BaseAuraNodeTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_conserve")
    public static final TileEntityType<AuraNodeConserveTile> TYPE_CONSERVE = null;

    public AuraNodeConserveTile() {
        super(TYPE_CONSERVE);
    }

    /**
     * Only Transfer to nodes on the same Y / Horizontal level
     */
    @Override
    public boolean canTransfer(BlockPos target, IAuraColor color) {
        return super.canTransfer(target, color) && target.getY() == this.pos.getY();
    }
}
