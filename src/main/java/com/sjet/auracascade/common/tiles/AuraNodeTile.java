package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
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

    @Override
    public void addAura(BlockPos sourcePos, IAuraColor color, int aura) {
        super.addAura(sourcePos, color, aura);

        //calculate the amount of energy to be generated based on the height
        int power = (this.pos.getY() - sourcePos.getY()) * aura;
        auraEnergy += power;
    }
}

