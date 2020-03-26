package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.entity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Collections;
import java.util.List;

public class AuraMobSpawnerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_mob_spawner")
    public static final TileEntityType<AuraMobSpawnerTile> AURA_MOB_SPAWNER = null;

    private static final int RANGE = 1;

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
        //get the list of spawnable mobs from the current biome
        Biome biome = world.getBiome(this.pos);
        List<Biome.SpawnListEntry> list = biome.getSpawns(EntityClassification.MONSTER);
        EntityType spawnEntity;
        BlockPos spawnBase = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

        //only spawn new mobs if there is not a mob on top of the spawner or right next to it
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos).grow(RANGE));
        for (Entity entity : entityList) {
            if (entity instanceof MobEntity) {
                return;
            }
        }

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
