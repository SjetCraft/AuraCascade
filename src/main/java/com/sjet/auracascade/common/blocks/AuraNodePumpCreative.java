package com.sjet.auracascade.common.blocks;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodePumpCreativeTile;
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

public class AuraNodePumpCreative extends BaseAuraPump {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_creative")
    public static final AuraNodePumpCreative BLOCK = null;

    public AuraNodePumpCreative() {
        super(Properties.create(Material.GLASS)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2f)
                .notSolid()
        );

        setRegistryName(AuraCascade.MODID, "aura_node_pump_creative");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraNodePumpCreativeTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        AuraNodePumpCreativeTile node = (AuraNodePumpCreativeTile) worldIn.getTileEntity(pos);

        node.findNodes();
        node.getWorld().notifyBlockUpdate(node.getPos(), node.getWorld().getBlockState(node.getPos()), node.getWorld().getBlockState(node.getPos()), 2);
        //only trigger particles on the client
        if(worldIn.isRemote) {
            node.connectParticles();
        }
    }
}
