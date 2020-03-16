package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraNodeTile extends BaseAuraTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final TileEntityType<AuraNodeTile> TYPE_NODE = null;

    public AuraNodeTile() {
        super(TYPE_NODE);
    }

    @Override
    public void tick() {
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            findNodes();
            distributeAura();
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 1) {
            transferAuraParticles();
        }
    }
}

