package com.sjet.auracascade.common.blocks.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.BaseAuraNode;
import com.sjet.auracascade.common.tiles.pump.AuraNodePumpProjectileTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpProjectile extends BaseAuraNode {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_projectile")
    public static final AuraNodePumpProjectile BLOCK = null;

    public AuraNodePumpProjectile() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2f)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pump_projectile");
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        /*System.out.println("Collision");
        AuraNodePumpProjectileTile pump = (AuraNodePumpProjectileTile) worldIn.getTileEntity(pos);
        pump.onEntityCollidedWithBlock(entityIn);
*/
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePumpProjectileTile();
    }
}
