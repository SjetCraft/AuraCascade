package com.sjet.auracascade.common.blocks.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.node.AuraNodeTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNode extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final AuraNode BLOCK = null;

    public AuraNode() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodeTile();
    }
}
