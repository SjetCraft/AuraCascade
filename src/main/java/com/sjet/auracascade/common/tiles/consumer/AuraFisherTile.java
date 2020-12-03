package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.*;

import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuraFisherTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_fisher")
    public static final TileEntityType<AuraFisherTile> AURA_FISHER = null;

    public AuraFisherTile() {
        super(AURA_FISHER);
    }

    @Override
    public int getMaxProgress() {
        return 200;
    }

    @Override
    public int getPowerPerProgress() {
        return 200;
    }

    @Override
    public void onUsePower() {
        if(!hasWater()) {
            return;
        }

        ResourceLocation fishLootTable = new ResourceLocation("minecraft", "gameplay/fishing/fish");
        ResourceLocation junkLootTable = new ResourceLocation("minecraft", "gameplay/fishing/junk");
        ResourceLocation treasureLootTable = new ResourceLocation("minecraft", "gameplay/fishing/treasure");

        ItemStack stack;
        List<ItemStack> stacks = new ArrayList<>();
        do {
            LootContext ctx = new LootContext.Builder((ServerWorld) world).build(LootParameterSets.EMPTY);
            List<ItemStack> fish = ((ServerWorld) world).getServer().getLootTableManager().getLootTableFromLocation(fishLootTable).generate(ctx);
            stacks.addAll(fish);

            //combine all LootTables
            List<ItemStack> junk = ((ServerWorld) world).getServer().getLootTableManager().getLootTableFromLocation(junkLootTable).generate(ctx);
            stacks.addAll(junk);
            List<ItemStack> treasure = ((ServerWorld) world).getServer().getLootTableManager().getLootTableFromLocation(treasureLootTable).generate(ctx);
            stacks.addAll(treasure);

            if (stacks.isEmpty()) {
                return;
            } else {
                Collections.shuffle(stacks);
                stack = stacks.get(0);
            }
        } while ( stack.isEmpty() );

        stack.setCount(1);
        ItemEntity itementity = new ItemEntity(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5, stack);
        this.world.addEntity(itementity);
    }

    public boolean hasWater() {
        for (int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
            for (int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
                BlockPos waterPos = new BlockPos(x, pos.getY() - 1, z);
                if (world.getFluidState(waterPos).getFluid() != Fluids.WATER) {
                    System.out.println(world.getFluidState(waterPos));
                    return false;
                }
            }
        }
        return true;
    }
}
