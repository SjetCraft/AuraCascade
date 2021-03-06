package com.sjet.auracascade.common.blocks.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.consumer.AuraCascadingProcessorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

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

    //used for Prismatic Processor
    public AuraCascadingProcessor(Block.Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraCascadingProcessorTile();
    }
}
