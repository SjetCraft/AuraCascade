package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuraLooterTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_looter")
    public static final TileEntityType<AuraLooterTile> AURA_LOOTER = null;

    public AuraLooterTile() {
        super(AURA_LOOTER);
    }

    @Override
    public int getMaxProgress() {
        return 100;
    }

    @Override
    public int getPowerPerProgress() {
        return 5000;
    }

    @Override
    public void onUsePower() {
        ResourceLocation fishLootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

        ItemStack stack;
        List<ItemStack> stacks = new ArrayList<>();
        do {
            LootContext ctx = new LootContext.Builder((ServerWorld) world).build(LootParameterSets.EMPTY);
            List<ItemStack> loot = ((ServerWorld) world).getServer().getLootTableManager().getLootTableFromLocation(fishLootTable).generate(ctx);
            stacks.addAll(loot);

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
}
