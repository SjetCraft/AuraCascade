package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuraMobSpawnerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_mob_spawner")
    public static final TileEntityType<AuraMobSpawnerTile> AURA_MOB_SPAWNER = null;

    public AuraMobSpawnerTile() {
        super(AURA_MOB_SPAWNER);
    }

    @Override
    public int getMaxProgress() {
        return 15;
    }

    @Override
    public int getPowerPerProgress() {
        return 190;
    }

    @Override
    public void onUsePower() {
        //get the current list os spawnable mobs from the current biome
        Biome biome = world.getBiome(this.pos);
        List<Biome.SpawnListEntry> list = biome.getSpawns(EntityClassification.MONSTER);
        EntityType spawnEntity;
        BlockPos spawnBase = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

        //only spawn new mobs if there is not a mob on top of the spawner
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, Common.getAABB(spawnBase, 1));
        for (Entity entity : entityList) {
            if (entity instanceof MobEntity) {
                return;
            }
        }
        /*
        for (int i = 0; i < 3; i++) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ())).getBlock() != Blocks.AIR) {
                return;
            }
        }
        */

        //if there are no mobs to spawn
        if (list.isEmpty()) {
            return;
        } else {
            Collections.shuffle(list);
            spawnEntity = list.get(0).entityType;

            //do not spawn if the mob will be suffocated
            float mobHeight = spawnEntity.getHeight();
            do {
                BlockPos headLevel = pos.up(Math.round(mobHeight));
                if (world.getBlockState(headLevel).isSuffocating(world, headLevel)) {
                    return;
                }
            } while (mobHeight-- > 1.5);

            spawnEntity.spawn(world, null, null, spawnBase, SpawnReason.SPAWNER, false, false);
        }
    }
}
