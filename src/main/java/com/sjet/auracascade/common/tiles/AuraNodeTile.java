package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;


public class AuraNodeTile extends BaseAuraTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final TileEntityType<AuraNodeTile> TYPE = null;

    public AuraNodeTile() {
        super(TYPE);
    }

    @Override
    public void tick() {
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            findNodes();
            distributeAura();
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        }
    }
}

