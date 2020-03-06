package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;


public class AuraNodeTile extends AuraTile  {


    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final TileEntityType<AuraNodeTile> TYPE = null;

    public AuraNodeTile() {
        super(TYPE);
    }



    @Override
    public void onLoad() {
    }

    @Override
    public void tick() {

    }



}

