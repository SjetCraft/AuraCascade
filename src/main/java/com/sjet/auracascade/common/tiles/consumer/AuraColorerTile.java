package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Random;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraColorerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_colorer")
    public static final TileEntityType<AuraColorerTile> AURA_COLORER = null;

    public AuraColorerTile() {
        super(AURA_COLORER);
    }

    @Override
    public int getMaxProgress() {
        return 12;
    }

    @Override
    public int getPowerPerProgress() {
        return 50;
    }

    @Override
    public void onUsePower() {
        List<SheepEntity> nearbySheep = world.getEntitiesWithinAABB(SheepEntity.class, Common.getAABB(this.pos, 2));
        if (nearbySheep.size() > 0) {
            SheepEntity sheep = nearbySheep.get(new Random().nextInt(nearbySheep.size()));
            sheep.setSheared(false);
            sheep.setFleeceColor(DyeColor.byId(new Random().nextInt(16)));
        }
    }

    @Override
    public boolean validItemsNearby() {
        List<SheepEntity> nearbySheep = world.getEntitiesWithinAABB(SheepEntity.class, Common.getAABB(this.pos, 2));
        return nearbySheep.size() > 0;
    }
}
