package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;
import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraNodePumpRedstoneTile extends BaseAuraNodePumpTile {


    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_redstone")
    public static final TileEntityType<AuraNodePumpCreativeTile> TYPE_PUMP_LIGHT = null;

    private static final int POWER = 1500;

    public AuraNodePumpRedstoneTile() {
        super(TYPE_PUMP_LIGHT);
    }

    @Override
    public void tick() {
        if (!world.isRemote && pumpTime <= 0 && world.getGameTime() % TICKS_PER_SECOND / 2 == 0) {
            findFuelAndAdd();
        }
        super.tick();
    }

    @Override
    public void findFuelAndAdd() {
        //look in all cardinal directions
        for (Direction facing : Direction.values()) {
            //only look in the direction of a redstone signal
            if( world.getRedstonePowerFromNeighbors(this.pos) > 0) {
                BlockPos blockPos = getPos().offset(facing);

                //if the first block is a redstone wire
                if (world.getBlockState(blockPos).getBlock() instanceof RedstoneWireBlock ) {
                    for (int i = 0; i <= MAX_DISTANCE; i++) {
                        //only consume redstone wire blocks
                        if (consumeBlock(blockPos, Blocks.REDSTONE_WIRE)) {
                            addFuel((int) (10 * Math.pow(1.4, i)), POWER);
                            blockPos = blockPos.offset(facing, 1);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean consumeBlock(BlockPos blockPos, Block block) {
        if (world.getBlockState(blockPos).getBlock() == block) {
            world.removeBlock(blockPos, false);
            //plays the block breaking sound and particles on the client
            world.playEvent(2001, blockPos, Block.getStateId(block.getDefaultState()));
            return true;
        }
        return false;
    }
}
