package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public abstract class BaseAuraNodeManipulatorTile extends BaseAuraNodeTile {

    public BaseAuraNodeManipulatorTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            findNodes();
            manipulator();
            distributeAura();
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 1) {
            transferAuraParticles();
        }
    }

    public abstract IAuraColor getAuraType();

    public void manipulator() {
        //create aura if the node is being powered
        if (this.world.isBlockPowered(this.pos)){
            auraMap.replace(getAuraType(), 100000);
        } else { //clear aura when the block is not powered
            auraMap.replace(getAuraType(), 0);
        }
    }

    @Override
    public boolean canReceive(BlockPos source, IAuraColor color) {
        return color.equals(getAuraType());
    }
}
