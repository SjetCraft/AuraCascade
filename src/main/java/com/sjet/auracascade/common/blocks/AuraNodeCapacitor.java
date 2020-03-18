package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodeCapacitorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

public class AuraNodeCapacitor extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_capacitor")
    public static final AuraNodeCapacitor BLOCK = null;

    public AuraNodeCapacitor() {
        super(Block.Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_capacitor");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodeCapacitorTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AuraNodeCapacitorTile node = (AuraNodeCapacitorTile) world.getTileEntity(pos);
        ItemStack heldItem = player.getHeldItem(handIn);

        if (heldItem.isEmpty() && player.isCrouching()) {
            node.updateCapacity();
            node.markDirty();
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
}
