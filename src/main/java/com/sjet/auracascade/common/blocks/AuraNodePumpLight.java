package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodePumpLightTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpLight extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_light")
    public static final AuraNodePumpLight BLOCK = null;

    public AuraNodePumpLight() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2f)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pump_light");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePumpLightTile();
    }
}
