package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodePumpBurningTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

public class AuraNodePumpBurning extends BaseAuraPump {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_burning")
    public static final AuraNodePumpBurning BLOCK = null;

    public AuraNodePumpBurning() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2f)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pump_burning");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePumpBurningTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        AuraNodePumpBurningTile node = (AuraNodePumpBurningTile) worldIn.getTileEntity(pos);

        node.findNodes();
        node.getWorld().notifyBlockUpdate(node.getPos(), node.getWorld().getBlockState(node.getPos()), node.getWorld().getBlockState(node.getPos()), 2);
        //only trigger particles on the client
        if(worldIn.isRemote) {
            node.connectParticles();
        }
    }
}
