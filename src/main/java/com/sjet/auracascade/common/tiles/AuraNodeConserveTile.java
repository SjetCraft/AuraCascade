package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraNodeConserveTile extends BaseAuraNodeTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_conserve")
    public static final TileEntityType<AuraNodeConserveTile> TYPE_CONSERVE = null;

    public AuraNodeConserveTile() {
        super(TYPE_CONSERVE);
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

    @Override
    public boolean canTransfer(BlockPos target, IAuraColor color) {
        return super.canTransfer(target, color) && target.getY() == this.pos.getY();
    }
}
