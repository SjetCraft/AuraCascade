package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpLightTile extends BaseAuraNodePumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_light")
    public static final TileEntityType<AuraNodePumpLightTile> TYPE_PUMP_LIGHT = null;

    private static final int POWER = 750;

    public AuraNodePumpLightTile() {
        super(TYPE_PUMP_LIGHT);
    }

    @Override
    public void tick() {
        if (!world.isRemote && pumpTime <= 0) {
            findFuelAndAdd();
        }
        super.tick();
    }

    @Override
    public void findFuelAndAdd() {
        for (Direction facing : Direction.values()) {
            BlockPos blockPos = getPos().offset(facing);
            if (consumeBlock(blockPos, Blocks.TORCH)) {
                addFuel(30, POWER);
                break;
            } else if (consumeBlock(blockPos, Blocks.GLOWSTONE)) {
                addFuel(180, POWER);
                break;
            } else if (consumeBlock(blockPos, Blocks.SEA_LANTERN)) {
                addFuel(220, POWER);
                break;
            } else if (consumeBlock(blockPos, Blocks.LANTERN)) {
                addFuel(100, POWER);
                break;
            } else if (consumeBlock(blockPos, Blocks.REDSTONE_LAMP)) {
                addFuel(150, POWER);
                break;
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
