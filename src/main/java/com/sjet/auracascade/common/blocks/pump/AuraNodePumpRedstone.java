package com.sjet.auracascade.common.blocks.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.node.BaseAuraNode;
import com.sjet.auracascade.common.tiles.pump.AuraNodePumpRedstoneTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpRedstone extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_redstone")
    public static final AuraNodePumpRedstone BLOCK = null;

    public AuraNodePumpRedstone() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2f)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pump_redstone");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePumpRedstoneTile();
    }
}
