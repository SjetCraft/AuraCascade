package com.sjet.auracascade.common.blocks.node;

import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IBaseAuraCrystalItem;
import com.sjet.auracascade.common.api.IBaseAuraNodeTile;
import com.sjet.auracascade.common.tiles.node.BaseAuraNodeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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

public abstract class BaseAuraNode extends Block {
    private static final VoxelShape NODE = makeCuboidShape(4, 4, 4, 12, 12, 12);

    public BaseAuraNode(Block.Properties properties) {
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState blockState, World world, BlockPos blockPos, int num1, int num2) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BaseAuraNodeTile node = (BaseAuraNodeTile) worldIn.getTileEntity(pos);

        node.findNodes();
        node.getWorld().notifyBlockUpdate(node.getPos(), node.getWorld().getBlockState(node.getPos()), node.getWorld().getBlockState(node.getPos()), 2);
        //only trigger particles on the client
        if (worldIn.isRemote) {
            node.connectParticles();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        BaseAuraNodeTile node = (BaseAuraNodeTile) world.getTileEntity(pos);
        ItemStack heldItem = player.getHeldItem(handIn);

        if (!heldItem.isEmpty()) {
            boolean result  = node.playerAddAura(player, heldItem);
            return result ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if(entityIn instanceof ItemEntity) {
            IBaseAuraNodeTile node = (IBaseAuraNodeTile) worldIn.getTileEntity(pos);
            Item item = ((ItemEntity) entityIn).getItem().getItem();

            if (item instanceof IBaseAuraCrystalItem) {
                int auraToAdd = ((ItemEntity) entityIn).getItem().getStack().getCount();
                node.addAura(pos, ((IBaseAuraCrystalItem) item).getColor(), ((IBaseAuraCrystalItem) item).getAura() * auraToAdd);
                ((ItemEntity) entityIn).getItem().shrink(auraToAdd);

                if (worldIn.isRemote) {
                    ParticleHelper.itemBurningParticles(worldIn, entityIn.getPositionVector());
                }
            }
            else {
                super.onEntityCollision(state, worldIn, pos, entityIn);
            }
        }
    }
}
