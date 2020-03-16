package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.common.tiles.AuraNodeTile;
import com.sjet.auracascade.common.tiles.BaseAuraPumpTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseAuraPump extends Block {
    private static final VoxelShape NODE = makeCuboidShape(4, 4, 4, 12, 12, 12);

    public BaseAuraPump(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return NODE;
    }

    public abstract void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack);

    @Override
    public boolean eventReceived(BlockState blockState, World world, BlockPos blockPos, int num1, int num2) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        BaseAuraPumpTile node = (BaseAuraPumpTile) world.getTileEntity(pos);
        ItemStack heldItem = player.getHeldItem(handIn);

        if (!heldItem.isEmpty()) {
            boolean result  = node.playerAddAura(player, heldItem);
            return result ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
}
