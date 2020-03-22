package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodeManipulatorBlackTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeManipulatorBlack extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_manipulator_black")
    public static final AuraNodeManipulatorBlack BLOCK = null;

    public AuraNodeManipulatorBlack() {
        super(Block.Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_manipulator_black");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodeManipulatorBlackTile();
    }
}
