package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpCreativeTile extends BaseAuraPumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_creative")
    public static final TileEntityType<AuraNodePumpCreativeTile> TYPE_PUMP_CREATIVE = null;

    public AuraNodePumpCreativeTile() {
        super(TYPE_PUMP_CREATIVE);
    }

    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote) {
            addFuel(2, 100000);
        }
    }
}
