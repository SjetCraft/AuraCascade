package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.items.AuraCrystalWhiteItem;
import com.sjet.auracascade.common.tiles.AuraNodeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class AuraNode extends Block {
    private static final VoxelShape NODE = makeCuboidShape(4, 4, 4, 12, 12, 12);

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final AuraNode BLOCK = null;

    public AuraNode() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(1.2f)
        );

        setRegistryName(AuraCascade.MODID, "aura_node");

    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodeTile();
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return NODE;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!world.isRemote) {
            AuraNodeTile node = (AuraNodeTile) world.getTileEntity(pos);
            Item heldItem = player.inventory.getCurrentItem().getItem();

            if(heldItem instanceof AuraCrystalWhiteItem) {
                player.sendStatusMessage(new StringTextComponent("Aura:"), false);
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }
}
