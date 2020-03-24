package com.sjet.auracascade.common.blocks.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.consumer.AuraGrowerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;


public class AuraGrower extends BaseAuraConsumer {

    @ObjectHolder(AuraCascade.MODID + ":aura_grower")
    public static final AuraGrower BLOCK = null;

    public AuraGrower() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.STONE)
                .hardnessAndResistance(2)
        );

        setRegistryName(AuraCascade.MODID, "aura_grower");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraGrowerTile();
    }
}
