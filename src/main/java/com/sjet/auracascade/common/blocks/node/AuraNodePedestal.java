package com.sjet.auracascade.common.blocks.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.node.AuraNodePedestalTile;
import com.sjet.auracascade.common.util.InventoryHelper;
import com.sjet.auracascade.common.util.WorldHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Optional;

public class AuraNodePedestal extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pedestal")
    public static final AuraNodePedestal BLOCK = null;

    public AuraNodePedestal() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pedestal");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePedestalTile();
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        //Don't allow pedestal node to consume Aura
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        WorldHelper.getTile(world, pos, AuraNodePedestalTile.class).ifPresent(AuraNodePedestalTile::dropPedestalInventory);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        ItemStack heldItem = player.getHeldItem(handIn);
        if (world.isRemote) {
            return (!heldItem.isEmpty() || player.isCrouching()) ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
        }

        if (!(world.getTileEntity(pos) instanceof AuraNodePedestalTile)) {
            return ActionResultType.FAIL;
        }

        Optional<AuraNodePedestalTile> pedestal = WorldHelper.getTile(world, pos, AuraNodePedestalTile.class);
        if (heldItem.isEmpty()) {
            if (player.isCrouching() && pedestal.isPresent()) {
                pedestal.get().removeAndSpawnItem();
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.FAIL;
            }
        } else {
            return pedestal.map(ped -> InventoryHelper.getItemHandlerFrom(ped)
                    .map(itemHandler -> InventoryHelper.tryAddingPlayerCurrentItem(player, itemHandler, Hand.MAIN_HAND) ? ActionResultType.SUCCESS : ActionResultType.CONSUME)
                    .orElse(ActionResultType.CONSUME)).orElse(ActionResultType.CONSUME);
        }
    }
}
