package com.sjet.auracascade.common.blocks.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.consumer.AuraPrismaticProcessorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraPrismaticProcessor extends AuraCascadingProcessor {

    @ObjectHolder(AuraCascade.MODID + ":aura_prismatic_processor")
    public static final AuraPrismaticProcessor BLOCK = null;

    public AuraPrismaticProcessor() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.STONE)
                .hardnessAndResistance(2)
        );

        setRegistryName(AuraCascade.MODID, "aura_prismatic_processor");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraPrismaticProcessorTile();
    }
}
