package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodeManipulatorOrangeTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodeManipulatorOrange extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_manipulator_orange")
    public static final AuraNodeManipulatorOrange BLOCK = null;

    public AuraNodeManipulatorOrange() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_manipulator_orange");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodeManipulatorOrangeTile();
    }
}
