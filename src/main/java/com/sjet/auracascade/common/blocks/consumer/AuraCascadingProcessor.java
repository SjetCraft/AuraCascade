package com.sjet.auracascade.common.blocks.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.consumer.AuraCascadingProcessorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;


public class AuraCascadingProcessor extends BaseAuraConsumer {

    @ObjectHolder(AuraCascade.MODID + ":aura_cascading_processor")
    public static final AuraCascadingProcessor BLOCK = null;

    public AuraCascadingProcessor() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.STONE)
                .hardnessAndResistance(2)
        );

        setRegistryName(AuraCascade.MODID, "aura_cascading_processor");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraCascadingProcessorTile();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        world.setBlockState(pos, state.with(BlockStateProperties.FACING, getPlacementHorizontalFacing(entity)), 2);
    }

    public Direction getPlacementHorizontalFacing(@Nullable LivingEntity entity) {
        return entity == null ? Direction.NORTH : entity.getHorizontalFacing().getOpposite();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }
}
