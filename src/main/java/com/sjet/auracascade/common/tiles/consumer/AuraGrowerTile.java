package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraGrowerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_grower")
    public static final TileEntityType<AuraGrowerTile> AURA_GROWER = null;

    public AuraGrowerTile() {
        super(AURA_GROWER);
    }

    @Override
    public int getMaxProgress() {
        return 2;
    }

    @Override
    public int getPowerPerProgress() {
        return 50;
    }

    @Override
    public void onUsePower() {
        BlockPos plantPos = new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ());
        BlockState blockstate = world.getBlockState(plantPos);
        Block plantBlock = blockstate.getBlock();

        if (plantBlock instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) blockstate.getBlock();
            if (igrowable.canGrow(world, plantPos, blockstate, world.isRemote)) {
                if (world instanceof ServerWorld) {
                    if (igrowable.canUseBonemeal(world, world.rand, plantPos, blockstate)) {
                        for (int i = 0; i < 50; i++) {
                            igrowable.grow((ServerWorld) world, world.rand, plantPos, blockstate);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean validItemsNearby() {
        return false;
    }
}
