package com.sjet.auracascade.common.blocks.consumer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;


public abstract class BaseAuraConsumer extends Block {

    public BaseAuraConsumer(Block.Properties properties) {
        super(properties);
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState blockState, World world, BlockPos blockPos, int num1, int num2) {
        return true;
    }
}
