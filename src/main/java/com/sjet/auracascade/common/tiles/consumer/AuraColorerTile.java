package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Random;

public class AuraColorerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_colorer")
    public static final TileEntityType<AuraColorerTile> AURA_COLORER = null;

    private static final int RANGE = 2;

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
        List<SheepEntity> nearbySheep = world.getEntitiesWithinAABB(SheepEntity.class, new AxisAlignedBB(this.pos).grow(RANGE));
        if (nearbySheep.size() > 0) {
            SheepEntity sheep = nearbySheep.get(new Random().nextInt(nearbySheep.size()));
            sheep.setSheared(false);
            sheep.setFleeceColor(DyeColor.byId(new Random().nextInt(16)));
        }
    }
}
